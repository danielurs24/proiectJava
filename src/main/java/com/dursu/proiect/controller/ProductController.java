package com.dursu.proiect.controller;

import com.dursu.proiect.dto.ProductDto;
import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.dto.UserDto;
import com.dursu.proiect.entity.Product;
import com.dursu.proiect.entity.User;
import com.dursu.proiect.repository.ProductRepository;
import com.dursu.proiect.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Product", description = "Product management APIs")
@RestController
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Operation(
			summary = "Create a new Product",
			description = "Create a new Product if you have admin role",
			tags = { "post" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application" +
					"/json") })})
	@PostMapping("/admin/create-product")
	public ResponseEntity<ResponseDto> addProduct(@RequestBody ProductDto request) {
		Object response = productService.addProduct(request);
		return ResponseEntity.ok((ResponseDto) response);
	}
	
	@Operation(
			summary = "Search a product",
			description = "Search a product using equals",
			tags = { "get" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") })})
	@GetMapping("/product/search-products/{name}")
	public ResponseEntity<ResponseDto> searchProductsByName(@PathVariable String name) {
		List<ProductDto> products = productService.searchProductsByName(name);
		if (products.isEmpty()) {
			return ResponseEntity.ok(new ResponseDto(false, "No product found!"));
		}
		return ResponseEntity.ok(new ResponseDto(true, products));
	}
	
	@Operation(
			summary = "Search a product",
			description = "Search a product using contains",
			tags = { "get" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") })})
	@GetMapping("/product/search-product/{name}")
	public ResponseEntity<ResponseDto> searchProductByName(@PathVariable String name) {
		Optional<ProductDto> product = productService.searchProductByName(name);
		if (product.isEmpty()) {
			return ResponseEntity.ok(new ResponseDto(false, "No product found!"));
		}
		return ResponseEntity.ok(new ResponseDto(true, product));
	}
	
	@Operation(
			summary = "Edit a product",
			description = "Edit a product if you have Admin role",
			tags = { "post" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") })})
	@PostMapping("/admin/edit-product")
	public ResponseEntity<ResponseDto> editProduct(@RequestBody ProductDto updatedProductDto) {
		Object response = productService.editProduct(updatedProductDto);
		return ResponseEntity.ok((ResponseDto) response);
	}
	
	@Operation(
			summary = "Delete a product",
			description = "Delete a product if you have Admin role",
			tags = { "post" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") })})
	@PostMapping("/admin/delete-product/{id}")
	public ResponseEntity<ResponseDto> deleteProduct(@PathVariable Long id) {
		Object response = productService.deleteProduct(id);
		return ResponseEntity.ok((ResponseDto) response);
	}
	
	@Operation(
			summary = "Get all products",
			description = "Get a list of all products",
			tags = { "get" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") })})
	@GetMapping("/product/all-products")
	public ResponseEntity<ResponseDto>getAllProducts() {
		List<ProductDto> productDtoList = productService.getAllProducts();
		
		if (productDtoList.isEmpty()) {
			return ResponseEntity.ok(new ResponseDto(false, "No products found!"));
		}
		return ResponseEntity.ok(new ResponseDto(true, productDtoList));
	}
	
	@Operation(
			summary = "Buy a product",
			description = "Buy a product if you have User role",
			tags = { "post" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") })})
	@PostMapping("/user/buy-products")
	public ResponseEntity<ResponseDto> buyProducts(@RequestBody List<Long> productIds) {
		ResponseDto response = productService.buyProducts(productIds);
		return ResponseEntity.ok(response);
	}
	
	@Operation(
			summary = "Filter the products",
			description = "Filter the products using existing categories from Database",
			tags = { "get" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") })})
	@GetMapping("/product/products-by-category/{categoryName}")
	public ResponseEntity<ResponseDto> getProductsByCategory(@PathVariable String categoryName) {
		ResponseDto productDtoList = productService.getProductsByCategory(categoryName);
		return ResponseEntity.ok(productDtoList);
	}
}
