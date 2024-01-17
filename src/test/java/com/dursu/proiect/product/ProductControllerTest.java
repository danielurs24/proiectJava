package com.dursu.proiect.product;

import com.dursu.proiect.controller.ProductController;
import com.dursu.proiect.dto.ProductDto;
import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
	
	@InjectMocks
	private ProductController productController;
	
	@Mock
	private ProductService productService;
	
	@Test
	public void testAddProduct() {
		// Arrange
		ProductDto request = new ProductDto();
		ResponseDto mockResponse = new ResponseDto(true, "Product Inserted successfully");
		when(productService.addProduct(request)).thenReturn(mockResponse);
		
		// Act
		ResponseEntity<ResponseDto> response = productController.addProduct(request);
		
		// Assert
		verify(productService, times(1)).addProduct(request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals("Product Inserted successfully", response.getBody().getMessage());
	}
	
	@Test
	public void testSearchProductsByName() {
		// Arrange
		String searchName = "TestProduct";
		List<ProductDto> mockProducts = Arrays.asList(new ProductDto(), new ProductDto());
		when(productService.searchProductsByName(searchName)).thenReturn(mockProducts);
		
		// Act
		ResponseEntity<ResponseDto> response = productController.searchProductsByName(searchName);
		
		// Assert
		verify(productService, times(1)).searchProductsByName(searchName);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals(mockProducts, response.getBody().getMessage());
	}
	
	@Test
	public void testSearchProductByName() {
		// Arrange
		String productName = "TestProduct";
		Optional<ProductDto> mockProduct = Optional.of(new ProductDto());
		when(productService.searchProductByName(productName)).thenReturn(mockProduct);
		
		// Act
		ResponseEntity<ResponseDto> response = productController.searchProductByName(productName);
		
		// Assert
		verify(productService, times(1)).searchProductByName(productName);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals(mockProduct, response.getBody().getMessage());
	}
	
	@Test
	public void testEditProduct() {
		// Arrange
		ProductDto updatedProductDto = new ProductDto();
		ResponseDto mockResponse = new ResponseDto(true, "Product updated successfully");
		when(productService.editProduct(updatedProductDto)).thenReturn(mockResponse);
		
		// Act
		ResponseEntity<ResponseDto> response = productController.editProduct(updatedProductDto);
		
		// Assert
		verify(productService, times(1)).editProduct(updatedProductDto);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals("Product updated successfully", response.getBody().getMessage());
	}
	
	@Test
	public void testDeleteProduct() {
		// Arrange
		Long productId = 1L;
		ResponseDto mockResponse = new ResponseDto(true, "Product deleted successfully");
		when(productService.deleteProduct(productId)).thenReturn(mockResponse);
		
		// Act
		ResponseEntity<ResponseDto> response = productController.deleteProduct(productId);
		
		// Assert
		verify(productService, times(1)).deleteProduct(productId);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals("Product deleted successfully", response.getBody().getMessage());
	}
	
	@Test
	public void testGetAllProducts() {
		// Arrange
		List<ProductDto> mockProducts = Arrays.asList(new ProductDto(), new ProductDto());
		when(productService.getAllProducts()).thenReturn(mockProducts);
		
		// Act
		ResponseEntity<ResponseDto> response = productController.getAllProducts();
		
		// Assert
		verify(productService, times(1)).getAllProducts();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals(mockProducts, response.getBody().getMessage());
	}
	
	@Test
	public void testBuyProducts() {
		// Arrange
		List<Long> productIds = Arrays.asList(1L, 2L);
		ResponseDto mockResponse = new ResponseDto(true, "Products bought successfully");
		when(productService.buyProducts(productIds)).thenReturn(mockResponse);
		
		// Act
		ResponseEntity<ResponseDto> response = productController.buyProducts(productIds);
		
		// Assert
		verify(productService, times(1)).buyProducts(productIds);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals("Products bought successfully", response.getBody().getMessage());
	}
	
	@Test
	public void testGetProductsByCategory() {
		// Arrange
		String categoryName = "Electronics";
		ResponseDto mockResponse = new ResponseDto(true, Collections.singletonList(new ProductDto()));
		when(productService.getProductsByCategory(categoryName)).thenReturn(mockResponse);
		
		// Act
		ResponseEntity<ResponseDto> response = productController.getProductsByCategory(categoryName);
		
		// Assert
		verify(productService, times(1)).getProductsByCategory(categoryName);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals(mockResponse, response.getBody());
	}
}

