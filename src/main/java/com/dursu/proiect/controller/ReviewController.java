package com.dursu.proiect.controller;

import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.dto.ReviewDto;
import com.dursu.proiect.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Review", description = "Review management APIs")
@RestController
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Operation(
			summary = "Add a review",
			description = "Add review to a product if you are logged in",
			tags = { "post" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") })})
	@PostMapping("/user/add-review")
	public ResponseEntity<ResponseDto> addReview(@RequestBody ReviewDto reviewDto) {
		ResponseDto response = reviewService.addReview(reviewDto);
		return ResponseEntity.ok(response);
	}
	
	@Operation(
			summary = "Check all the reviews",
			description = "Check all the stored review of a product",
			tags = { "get" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") })})
	@GetMapping("/product/get-reviews/{productId}")
	public ResponseEntity<ResponseDto> getReviewsByProductId(@PathVariable Long productId) {
		List<ReviewDto> reviews = reviewService.getReviewsByProductId(productId);
		
		if (reviews.isEmpty()) {
			return ResponseEntity.ok(new ResponseDto(false, "No reviews for this product!"));
		}
		return ResponseEntity.ok(new ResponseDto(true, reviews));
	}
}
