package com.dursu.proiect.favoriteProduct;
import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.entity.FavoriteProduct;
import com.dursu.proiect.entity.Product;
import com.dursu.proiect.entity.User;
import com.dursu.proiect.repository.FavoriteProductRepository;
import com.dursu.proiect.repository.ProductRepository;
import com.dursu.proiect.repository.UserRepository;
import com.dursu.proiect.service.FavoriteProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FavoriteProductUnitTest {
	
	@Mock
	private FavoriteProductRepository favoriteProductRepository;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private FavoriteProductService favoriteProductService;
	
	@Test
	public void testAddFavoriteProduct_Success() {
		// Mocking
		when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));
		when(productRepository.findById(any())).thenReturn(Optional.of(new Product()));
		when(favoriteProductRepository.save(any())).thenReturn(new FavoriteProduct());
		
		// Test
		ResponseDto response = favoriteProductService.addFavoriteProduct("username", 1L);
		
		// Verify
		verify(favoriteProductRepository, times(1)).save(any());
		assertEquals(true, response.isSuccess());
		assertEquals("Product added to favorites successfully", response.getMessage());
	}
	
	@Test
	public void testAddFavoriteProduct_UserNotFound() {
		// Mocking
		when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
		
		// Test
		ResponseDto response = favoriteProductService.addFavoriteProduct("nonexistentUser", 1L);
		
		// Verify
		verify(favoriteProductRepository, never()).save(any());
		assertEquals(false, response.isSuccess());
		assertEquals("User not found!", response.getMessage());
	}
	
	@Test
	public void testAddFavoriteProduct_ProductNotFound() {
		// Mocking
		when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));
		when(productRepository.findById(any())).thenReturn(Optional.empty());
		
		// Test
		ResponseDto response = favoriteProductService.addFavoriteProduct("username", 1L);
		
		// Verify
		verify(favoriteProductRepository, never()).save(any());
		assertEquals(false, response.isSuccess());
		assertEquals("Product not found!", response.getMessage());
	}
	
	@Test
	public void testAddFavoriteProduct_ProductAlreadyInFavorites() {
		User user = new User();
		Product product = new Product();
		when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
		when(productRepository.findById(any())).thenReturn(Optional.of(product));
		when(favoriteProductRepository.findByUserAndProduct(any(), any())).thenReturn(Optional.of(new FavoriteProduct()));
		
		// Test
		ResponseDto response = favoriteProductService.addFavoriteProduct("username", 1L);
		
		// Verify
		verify(favoriteProductRepository, never()).save(any());
		assertEquals(false, response.isSuccess());
		assertEquals("Product is already a favorite for the user", response.getMessage());
	}
	
	@Test
	public void testRemoveFavoriteProduct_Success() {
		// Mocking
		User user = new User();
		Product product = new Product();
		when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
		when(productRepository.findById(any())).thenReturn(Optional.of(product));
		when(favoriteProductRepository.findByUserAndProduct(any(), any())).thenReturn(Optional.of(new FavoriteProduct()));
		
		// Test
		ResponseDto response = favoriteProductService.removeFavoriteProduct("username", 1L);
		
		// Verify
		verify(favoriteProductRepository, times(1)).delete(any());
		assertEquals(true, response.isSuccess());
		assertEquals("Product removed from favorites successfully", response.getMessage());
	}
	
	@Test
	public void testRemoveFavoriteProduct_UserNotFound() {
		// Mocking
		when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
		
		// Test
		ResponseDto response = favoriteProductService.removeFavoriteProduct("nonexistentUser", 1L);
		
		// Verify
		verify(favoriteProductRepository, never()).delete(any());
		assertEquals(false, response.isSuccess());
		assertEquals("User not found!", response.getMessage());
	}
	
	@Test
	public void testRemoveFavoriteProduct_ProductNotFound() {
		// Mocking
		User user = new User();
		when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
		when(productRepository.findById(any())).thenReturn(Optional.empty());
		
		// Test
		ResponseDto response = favoriteProductService.removeFavoriteProduct("username", 1L);
		
		// Verify
		verify(favoriteProductRepository, never()).delete(any());
		assertEquals(false, response.isSuccess());
		assertEquals("Product not found!", response.getMessage());
	}
	
	@Test
	public void testRemoveFavoriteProduct_ProductNotInFavorites() {
		// Mocking
		User user = new User();
		Product product = new Product();
		when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
		when(productRepository.findById(any())).thenReturn(Optional.of(product));
		when(favoriteProductRepository.findByUserAndProduct(any(), any())).thenReturn(Optional.empty());
		
		// Test
		ResponseDto response = favoriteProductService.removeFavoriteProduct("username", 1L);
		
		// Verify
		verify(favoriteProductRepository, never()).delete(any());
		assertEquals(false, response.isSuccess());
		assertEquals("Product is not in favorites!", response.getMessage());
	}
	
	@Test
	public void testGetAllFavoriteProducts_Success() {
		// Mocking
		User user = new User();
		user.setId(1L);
		when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
		
		Product product1 = new Product();
		product1.setId(1L);
		product1.setName("Product 1");
		
		Product product2 = new Product();
		product2.setId(2L);
		product2.setName("Product 2");
		
		List<FavoriteProduct> favoriteProducts = List.of(
				createFavoriteProduct(user, product1),
				createFavoriteProduct(user, product2)
		);
		
		when(favoriteProductRepository.findByUser(user)).thenReturn(favoriteProducts);
		
		// Test
		ResponseDto response = favoriteProductService.getAllFavoriteProducts("username");
		
		// Verify
		verify(favoriteProductRepository, times(1)).findByUser(user);
		assertEquals(true, response.isSuccess());
		assertEquals(2, ((List<?>) response.getMessage()).size());
	}
	
	// Helper method to create a FavoriteProduct
	private FavoriteProduct createFavoriteProduct(User user, Product product) {
		FavoriteProduct favoriteProduct = new FavoriteProduct();
		favoriteProduct.setUser(user);
		favoriteProduct.setProduct(product);
		return favoriteProduct;
	}
	
	@Test
	public void testGetAllFavoriteProducts_UserNotFound() {
		// Mocking
		when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
		
		// Test
		ResponseDto response = favoriteProductService.getAllFavoriteProducts("nonexistentUser");
		
		// Verify
		assertEquals(false, response.isSuccess());
		assertEquals("User not found!", response.getMessage());
	}
	
	@Test
	public void testGetAllFavoriteProducts_NoFavorites() {
		// Mocking
		User user = new User();
		when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
		when(favoriteProductRepository.findByUser(any())).thenReturn(Collections.emptyList());
		
		// Test
		ResponseDto response = favoriteProductService.getAllFavoriteProducts("username");
		
		// Verify
		assertEquals(false, response.isSuccess());
		assertEquals("No favorite products found!", response.getMessage());
	}
}
