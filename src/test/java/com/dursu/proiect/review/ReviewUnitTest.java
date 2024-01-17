package com.dursu.proiect.review;

import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.dto.ReviewDto;
import com.dursu.proiect.entity.Category;
import com.dursu.proiect.entity.Product;
import com.dursu.proiect.entity.Review;
import com.dursu.proiect.entity.User;
import com.dursu.proiect.repository.ProductRepository;
import com.dursu.proiect.repository.ReviewRepository;
import com.dursu.proiect.repository.UserRepository;
import com.dursu.proiect.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReviewUnitTest {
	
	@Mock
	private ReviewRepository reviewRepository;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private ReviewService reviewService;
	
	@Test
	public void testAddReview_Success() {
		// Mocking
		User user = new User();
		user.setId(1L);
		
		Product product = new Product();
		product.setId(1L);
		
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setProductId(product.getId());
		reviewDto.setRating(5);
		reviewDto.setDescription("Great product!");
		
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
		when(reviewRepository.save(any())).thenReturn(new Review());
		
		// Test
		ResponseDto response = reviewService.addReview(reviewDto);
		
		// Verify
		assertTrue(response.isSuccess());
		assertEquals("Review added successfully", response.getMessage());
	}
	
	@Test
	public void testAddReview_ProductNotFound() {
		// Mocking
		long productId = 1L;
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setProductId(productId);
		reviewDto.setRating(5);
		reviewDto.setDescription("ProductNotFound!");
		
		when(productRepository.findById(reviewDto.getProductId())).thenReturn(Optional.empty());
		
		// Test
		ResponseDto response = reviewService.addReview(reviewDto);
		
		// Verify
		assertFalse(response.isSuccess());
		assertEquals("Product not found with ID: " + productId, response.getMessage());
	}
	
	@Test
	public void testAddReview_InvalidRating() {
		// Mocking
		User user = new User();
		user.setId(1L);
		
		Product product = new Product();
		product.setId(1L);
		
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setProductId(product.getId());
		reviewDto.setRating(6); // Assuming rating should be between 1 and 5
		reviewDto.setDescription("InvalidRating!");
		
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
		
		// Test
		ResponseDto response = reviewService.addReview(reviewDto);
		
		// Verify
		assertFalse(response.isSuccess());
		assertEquals("Invalid rating. Please provide a rating between 1 and 5.", response.getMessage());
	}
	
	@Test
	public void testGetReviewsByProductId_Success() {
		// Mocking
		Long productId = 1L;
		
		Category category = new Category();
		category.setName("categ");
		
		Product product = new Product();
		product.setId(productId);
		product.setName("Test");
		product.setStock(5);
		product.setDescription("test");
		product.setCategory(category);
		
		Review review1 = new Review();
		review1.setId(1L);
		review1.setRating(5);
		review1.setDescription("Great product!");
		review1.setProduct(product);
		
		when(reviewRepository.findByProductId(anyLong())).thenReturn(Collections.singletonList(review1));
		
		List<ReviewDto> reviews = reviewService.getReviewsByProductId(productId);
		// Verify
		assertEquals(1, reviews.size());
	}
	
	@Test
	public void testGetReviewsByProductId_ProductNotFound() {
		// Mocking
		Long productId = 1L;
		
		when(productRepository.findById(productId)).thenReturn(Optional.empty());
		
		// Test
		List<ReviewDto> reviews = reviewService.getReviewsByProductId(productId);
		
		// Verify
		assertTrue(reviews.isEmpty());
	}
	
	@Test
	public void testGetReviewsByProductId_NoReviews() {
		// Mocking
		Long productId = 1L;
		
		Product product = new Product();
		product.setId(productId);
		
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));
		when(reviewRepository.findByProductId(productId)).thenReturn(Collections.emptyList());
		
		// Test
		List<ReviewDto> reviews = reviewService.getReviewsByProductId(productId);
		
		// Verify
		assertTrue(reviews.isEmpty());
	}
	
	@Test
	public void testGetReviewsByProductId_EmptyProductId() {
		// Test
		List<ReviewDto> reviews = reviewService.getReviewsByProductId(null);
		
		// Verify
		assertTrue(reviews.isEmpty());
	}
	
	@Test
	public void testGetReviewsByProductId_InvalidProductId() {
		// Test
		List<ReviewDto> reviews = reviewService.getReviewsByProductId(-1L);
		
		// Verify
		assertTrue(reviews.isEmpty());
	}
	
	@Test
	public void testGetReviewsByProductId_ProductIdNotFound() {
		// Mocking
		Long productId = 1L;
		
		when(productRepository.findById(productId)).thenReturn(Optional.empty());
		
		// Test
		List<ReviewDto> reviews = reviewService.getReviewsByProductId(productId);
		
		// Verify
		assertTrue(reviews.isEmpty());
	}
	
	@Test
	public void testGetReviewsByProductId_ProductIdNull() {
		// Test
		List<ReviewDto> reviews = reviewService.getReviewsByProductId(null);
		
		// Verify
		assertTrue(reviews.isEmpty());
	}
	
}

