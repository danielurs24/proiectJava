package com.dursu.proiect.controller;

import com.dursu.proiect.config.JwtUtil;
import com.dursu.proiect.dto.FavoriteProductDto;
import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.service.FavoriteProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Favorite Products", description = "Favorite Products management APIs")
@RestController
@RequestMapping("/user/favorite-products")
public class FavoriteProductController {
	
	private final FavoriteProductService favoriteProductService;
	
	@Autowired
	public FavoriteProductController(FavoriteProductService favoriteProductService) {
		this.favoriteProductService = favoriteProductService;
	}
	
	
	@Operation(
			summary = "Add favorite product",
			description = "Add favorite product by Username and productId",
			tags = { "post" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@PostMapping("/add")
	public ResponseEntity<ResponseDto> addFavoriteProduct(@RequestBody FavoriteProductDto favoriteProductDto) {
		ResponseDto response = favoriteProductService.addFavoriteProduct(favoriteProductDto.getUsername(),
				favoriteProductDto.getProductId());
		return ResponseEntity.ok(response);
	}
	
	
	@Operation(
			summary = "Remove favorite product",
			description = "Remove favorite product by Username and productId",
			tags = { "post" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@PostMapping("/remove")
	public ResponseEntity<ResponseDto> removeFavoriteProduct(@RequestBody FavoriteProductDto favoriteProductDto) {
		ResponseDto response = favoriteProductService.removeFavoriteProduct(favoriteProductDto.getUsername(),
				favoriteProductDto.getProductId());
		return ResponseEntity.ok(response);
	}
	
	@Operation(
			summary = "Get all favorite products",
			description = "Get all favorite products by Username",
			tags = { "get" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@GetMapping("/all/{username}")
	public ResponseEntity<ResponseDto> getAllFavoriteProducts(@PathVariable String username) {
		ResponseDto favoriteProducts = favoriteProductService.getAllFavoriteProducts(username);
		if (!favoriteProducts.isSuccess()) {
			return ResponseEntity.ok(new ResponseDto(false, "No favorite products found"));
		}
		
		return ResponseEntity.ok(new ResponseDto(true, favoriteProducts));
	}
	
}
