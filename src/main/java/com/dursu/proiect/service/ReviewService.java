package com.dursu.proiect.service;

import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.dto.ReviewDto;
import com.dursu.proiect.entity.Product;
import com.dursu.proiect.entity.Review;
import com.dursu.proiect.repository.ProductRepository;
import com.dursu.proiect.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
	
	private final ReviewRepository reviewRepository;
	private final ProductRepository productRepository;
	
	public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
		this.reviewRepository = reviewRepository;
		this.productRepository = productRepository;
	}
	
	public ResponseDto addReview(ReviewDto reviewDto) {
		try {
			Optional<Product> optionalProduct = productRepository.findById(reviewDto.getProductId());
			
			if (optionalProduct.isPresent()) {
				Product product = optionalProduct.get();
				
				if (reviewDto.getRating() < 1 || reviewDto.getRating() > 5) {
					return new ResponseDto(false, "Invalid rating. Please provide a rating between 1 and 5.");
				}
				
				Review newReview = new Review();
				newReview.setRating(reviewDto.getRating());
				newReview.setDescription(reviewDto.getDescription());
				newReview.setProduct(product);
				
				reviewRepository.save(newReview);
				
				return new ResponseDto(true, "Review added successfully");
			} else {
				return new ResponseDto(false, "Product not found with ID: " + reviewDto.getProductId());
			}
		} catch (NumberFormatException e) {
			return new ResponseDto(false, "Invalid product ID format");
		} catch (Exception e) {
			return new ResponseDto(false, e.getMessage());
		}
	}
	
	public List<ReviewDto> getReviewsByProductId(Long productId) {
		List<Review> reviews = reviewRepository.findByProductId(productId);
		return reviews.stream()
				.map(this::convertToReviewDto)
				.collect(Collectors.toList());
	}
	
	private ReviewDto convertToReviewDto(Review review) {
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setId(review.getId());
		reviewDto.setRating(review.getRating());
		reviewDto.setDescription(review.getDescription());
		reviewDto.setProductId(review.getProduct().getId());
		
		return reviewDto;
	}
}
