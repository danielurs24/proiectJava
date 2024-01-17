package com.dursu.proiect.product;

import com.dursu.proiect.dto.ProductDto;
import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.entity.Category;
import com.dursu.proiect.entity.Product;
import com.dursu.proiect.repository.CategoryRepository;
import com.dursu.proiect.repository.ProductRepository;
import com.dursu.proiect.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
class ProductServiceUnitTest {
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private ProductService productService;
	
	@Test
	void testAddProduct_Success() {
		// Mock data
		ProductDto productDto = new ProductDto();
		productDto.setName("TestProduct");
		productDto.setCategoryName("TestCategory");
		productDto.setDescription("TestDescription");
		productDto.setStock(10);
		
		Category category = new Category();
		category.setName("TestCategory");
		
		// Mock repository behavior
		when(categoryRepository.findByName("TestCategory")).thenReturn(Optional.of(category));
		when(productRepository.findByName("TestProduct")).thenReturn(Optional.empty());
		
		// Test the addProduct method
		ResponseDto response = (ResponseDto) productService.addProduct(productDto);
		
		// Assertions
		assertTrue(response.isSuccess());
		assertEquals("Product Inserted succesfully", response.getMessage());
	}
	
	@Test
	public void testAddProduct_ProductAlreadyExists() {
		// Mock data
		ProductDto productDto = new ProductDto();
		productDto.setName("ExistingProduct");
		productDto.setCategoryName("TestCategory");
		productDto.setDescription("TestDescription");
		productDto.setStock(10);
		
		Category category = new Category();
		category.setName("TestCategory");
		
		// Mock repository behavior
		when(categoryRepository.findByName("TestCategory")).thenReturn(Optional.of(category));
		when(productRepository.findByName("ExistingProduct")).thenReturn(Optional.of(new Product()));
		
		// Test the addProduct method when the product already exists
		ResponseDto response = (ResponseDto) productService.addProduct(productDto);
		
		// Assertions
		assertFalse(response.isSuccess());
		assertEquals("Product already exists", response.getMessage());
	}
	
	@Test
	public void testAddProduct_CategoryNotFound() {
		// Mock data
		ProductDto productDto = new ProductDto();
		productDto.setName("TestProduct");
		productDto.setCategoryName("NonexistentCategory");
		productDto.setDescription("TestDescription");
		productDto.setStock(10);
		
		// Mock repository behavior
		when(categoryRepository.findByName("NonexistentCategory")).thenReturn(Optional.empty());
		
		// Test the addProduct method when the category is not found
		ResponseDto response = (ResponseDto) productService.addProduct(productDto);
		
		// Assertions
		assertFalse(response.isSuccess());
		assertEquals("Category not found NonexistentCategory", response.getMessage());
	}
	
	@Test
	public void testSearchProductsByName_Success() {
		// Mock data
		String searchName = "TestProduct";
		List<Product> productList = Arrays.asList(
				new Product(1L, "TestProduct1", 10, "Description1", new Category()),
				new Product(2L, "TestProduct2", 15, "Description2", new Category())
		);
		
		// Mock repository behavior
		when(productRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(productList);
		
		// Test the searchProductsByName method
		List<ProductDto> result = productService.searchProductsByName(searchName);
		
		// Assertions
		assertEquals(2, result.size());
		assertEquals("TestProduct1", result.get(0).getName());
		assertEquals("TestProduct2", result.get(1).getName());
	}
	
	@Test
	public void testSearchProductByName_Success() {
		// Mock data
		String productName = "TestProduct";
		Product product = new Product(1L, productName, 10, "Description", new Category());
		
		// Mock repository behavior
		when(productRepository.findByName(productName)).thenReturn(Optional.of(product));
		
		// Test the searchProductByName method
		Optional<ProductDto> result = productService.searchProductByName(productName);
		
		// Assertions
		assertTrue(result.isPresent());
		assertEquals(productName, result.get().getName());
	}
	
	@Test
	public void testEditProduct_Success() {
		// Mock data
		ProductDto updatedProductDto = new ProductDto();
		updatedProductDto.setId(1L);
		updatedProductDto.setName("UpdatedProduct");
		updatedProductDto.setCategoryName("TestCategory");
		updatedProductDto.setDescription("UpdatedDescription");
		updatedProductDto.setStock(20);
		
		Product existingProduct = new Product(1L, "OriginalProduct", 10, "OriginalDescription", new Category());
		
		Category category = new Category();
		category.setName("TestCategory");
		
		// Mock repository behavior
		when(productRepository.findById(updatedProductDto.getId())).thenReturn(Optional.of(existingProduct));
		when(categoryRepository.findByName("TestCategory")).thenReturn(Optional.of(category));
		
		// Test the editProduct method
		ResponseDto response = (ResponseDto) productService.editProduct(updatedProductDto);
		
		// Assertions
		assertTrue(response.isSuccess());
		assertEquals("Product updated successfully", response.getMessage());
		assertEquals(updatedProductDto.getName(), existingProduct.getName());
		assertEquals(updatedProductDto.getDescription(), existingProduct.getDescription());
		assertEquals(updatedProductDto.getStock(), existingProduct.getStock());
		assertEquals(category, existingProduct.getCategory());
	}
	
	@Test
	public void testEditProduct_ProductNotFound() {
		// Mock data
		ProductDto updatedProductDto = new ProductDto();
		updatedProductDto.setId(1L);
		
		// Mock repository behavior
		when(productRepository.findById(updatedProductDto.getId())).thenReturn(Optional.empty());
		
		// Test the editProduct method when the product is not found
		ResponseDto response = (ResponseDto) productService.editProduct(updatedProductDto);
		
		// Assertions
		assertFalse(response.isSuccess());
		assertEquals("Product not found", response.getMessage());
	}
	
	@Test
	public void testEditProduct_CategoryNotFound() {
		// Mock data
		ProductDto updatedProductDto = new ProductDto();
		updatedProductDto.setId(1L);
		updatedProductDto.setCategoryName("NonexistentCategory");
		
		Product existingProduct = new Product(1L, "OriginalProduct", 10, "OriginalDescription", new Category());
		
		// Mock repository behavior
		when(productRepository.findById(updatedProductDto.getId())).thenReturn(Optional.of(existingProduct));
		when(categoryRepository.findByName("NonexistentCategory")).thenReturn(Optional.empty());
		
		// Test the editProduct method when the category is not found
		ResponseDto response = (ResponseDto) productService.editProduct(updatedProductDto);
		
		// Assertions
		assertFalse(response.isSuccess());
		assertEquals("Category not found NonexistentCategory", response.getMessage());
	}
	
	@Test
	public void testDeleteProduct_Success() {
		// Mock data
		Long productId = 1L;
		Product existingProduct = new Product(productId, "TestProduct", 10, "Description", new Category());
		
		// Mock repository behavior
		when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
		
		// Test the deleteProduct method
		ResponseDto response = (ResponseDto) productService.deleteProduct(productId);
		
		// Assertions
		assertTrue(response.isSuccess());
		assertEquals("Product deleted successfully", response.getMessage());
	}
	
	@Test
	public void testDeleteProduct_ProductNotFound() {
		// Mock data
		Long productId = 1L;
		
		// Mock repository behavior
		when(productRepository.findById(productId)).thenReturn(Optional.empty());
		
		// Test the deleteProduct method when the product is not found
		ResponseDto response = (ResponseDto) productService.deleteProduct(productId);
		
		// Assertions
		assertFalse(response.isSuccess());
		assertEquals("Product not found", response.getMessage());
	}
	
	@Test
	public void testBuyProducts_Success() {
		// Mock data
		List<Long> productIds = Arrays.asList(1L, 2L);
		
		Product product1 = new Product(1L, "Product1", 10, "Description1", new Category());
		Product product2 = new Product(2L, "Product2", 5, "Description2", new Category());
		
		// Mock repository behavior
		when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
		when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
		
		// Test the buyProducts method
		ResponseDto response = productService.buyProducts(productIds);
		
		// Assertions
		assertTrue(response.isSuccess());
		assertEquals("Products bought successfully", response.getMessage());
		assertEquals(9, product1.getStock());
		assertEquals(4, product2.getStock());
	}
	
	@Test
	public void testBuyProducts_OutOfStock() {
		// Mock data
		List<Long> productIds = Collections.singletonList(1L);
		
		Product product = new Product(1L, "Product1", 0, "Description1", new Category());
		
		// Mock repository behavior
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		
		// Test the buyProducts method when a product is out of stock
		ResponseDto response = productService.buyProducts(productIds);
		
		// Assertions
		assertFalse(response.isSuccess());
		assertEquals("Product with ID 1 is out of stock", response.getMessage());
		assertEquals(0, product.getStock());  // Stock should not be modified
	}
	
	@Test
	public void testBuyProducts_ProductNotFound() {
		// Mock data
		List<Long> productIds = Collections.singletonList(1L);
		
		// Mock repository behavior
		when(productRepository.findById(1L)).thenReturn(Optional.empty());
		
		// Test the buyProducts method when a product is not found
		ResponseDto response = productService.buyProducts(productIds);
		
		// Assertions
		assertFalse(response.isSuccess());
		assertEquals("Product with ID 1 not found", response.getMessage());
	}
	
	@Test
	public void testGetProductsByCategory_Success() {
		// Mock data
		String categoryName = "TestCategory";
		Category category = new Category(1L, categoryName);
		
		List<Product> productList = Arrays.asList(
				new Product(1L, "Product1", 10, "Description1", category),
				new Product(2L, "Product2", 15, "Description2", category)
		);
		
		// Mock repository behavior
		when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(category));
		when(productRepository.findByCategory(category)).thenReturn(productList);
		
		// Test the getProductsByCategory method
		ResponseDto response = productService.getProductsByCategory(categoryName);
		
		// Assertions
		assertTrue(response.isSuccess());
		List<ProductDto> productDtoList = (List<ProductDto>) response.getMessage();
		assertEquals(2, productDtoList.size());
		assertEquals("Product1", productDtoList.get(0).getName());
		assertEquals("Product2", productDtoList.get(1).getName());
	}
	
	@Test
	public void testGetProductsByCategory_CategoryNotFound() {
		// Mock data
		String categoryName = "NonexistentCategory";
		
		// Mock repository behavior
		when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());
		
		// Test the getProductsByCategory method when the category is not found
		ResponseDto response = productService.getProductsByCategory(categoryName);
		
		// Assertions
		assertFalse(response.isSuccess());
		assertEquals("Category not found: NonexistentCategory", response.getMessage());
	}
}
