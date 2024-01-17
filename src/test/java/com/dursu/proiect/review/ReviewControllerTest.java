package com.dursu.proiect.review;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.dursu.proiect.controller.ReviewController;
import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.dto.ReviewDto;
import com.dursu.proiect.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {
	
	@InjectMocks
	private ReviewController reviewController;
	
	@Mock
	private ReviewService reviewService;
	
	@Test
	public void testAddReview() {
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setProductId(123L);
		reviewDto.setRating(5);
		reviewDto.setDescription("Great product!");
		
		ResponseDto mockResponse = new ResponseDto(true, "Review added successfully");
		when(reviewService.addReview(any(ReviewDto.class))).thenReturn(mockResponse);
		
		ResponseEntity<ResponseDto> response = reviewController.addReview(reviewDto);
		
		verify(reviewService, times(1)).addReview(reviewDto);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals("Review added successfully", response.getBody().getMessage());
	}
	
	@Test
	public void testGetReviewsByProductIdWithReviews() {
		Long productId = 123L;
		
		ReviewDto review1 = new ReviewDto();
		review1.setProductId(productId);
		review1.setRating(4);
		review1.setDescription("Good product!");
		
		ReviewDto review2 = new ReviewDto();
		review1.setProductId(productId);
		review2.setRating(5);
		review2.setDescription("Excellent!");
		
		List<ReviewDto> mockReviews = Arrays.asList(review1, review2);
		when(reviewService.getReviewsByProductId(productId)).thenReturn(mockReviews);
		
		ResponseEntity<ResponseDto> response = reviewController.getReviewsByProductId(productId);
		
		verify(reviewService, times(1)).getReviewsByProductId(productId);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals(mockReviews, response.getBody().getMessage());
	}
	
	@Test
	public void testGetReviewsByProductIdNoReviews() {
		Long productId = 456L;
		
		when(reviewService.getReviewsByProductId(productId)).thenReturn(Collections.emptyList());
		
		ResponseEntity<ResponseDto> response = reviewController.getReviewsByProductId(productId);
		
		verify(reviewService, times(1)).getReviewsByProductId(productId);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertFalse(response.getBody().isSuccess());
		assertEquals("No reviews for this product!", response.getBody().getMessage());
	}
}
