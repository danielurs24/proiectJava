package com.dursu.proiect.favoriteProduct;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import com.dursu.proiect.controller.FavoriteProductController;
import com.dursu.proiect.dto.FavoriteProductDto;
import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.service.FavoriteProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class FavoriteProductControllerTest {
	
	@InjectMocks
	private FavoriteProductController favoriteProductController;
	
	@Mock
	private FavoriteProductService favoriteProductService;
	
	@Test
	public void testAddFavoriteProduct() {
		FavoriteProductDto favoriteProductDto = new FavoriteProductDto();
		favoriteProductDto.setUsername("testUser");
		favoriteProductDto.setProductId(123L);
		
		ResponseDto mockResponse = new ResponseDto(true, "Favorite product added successfully");
		when(favoriteProductService.addFavoriteProduct(anyString(), anyLong())).thenReturn(mockResponse);
		
		ResponseEntity<ResponseDto> response = favoriteProductController.addFavoriteProduct(favoriteProductDto);
		
		verify(favoriteProductService, times(1)).addFavoriteProduct("testUser", 123L);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals("Favorite product added successfully", response.getBody().getMessage());
	}
	
	@Test
	public void testRemoveFavoriteProduct() {
		FavoriteProductDto favoriteProductDto = new FavoriteProductDto();
		favoriteProductDto.setUsername("testUser");
		favoriteProductDto.setProductId(123L);
		
		ResponseDto mockResponse = new ResponseDto(true, "Favorite product removed successfully");
		when(favoriteProductService.removeFavoriteProduct(anyString(), anyLong())).thenReturn(mockResponse);
		
		ResponseEntity<ResponseDto> response = favoriteProductController.removeFavoriteProduct(favoriteProductDto);
		
		verify(favoriteProductService, times(1)).removeFavoriteProduct("testUser", 123L);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals("Favorite product removed successfully", response.getBody().getMessage());
	}
	
	@Test
	public void testGetAllFavoriteProducts() {
		String username = "testUser";
		
		ResponseDto mockResponse = new ResponseDto(true, Collections.emptyList());
		when(favoriteProductService.getAllFavoriteProducts(username)).thenReturn(mockResponse);

		ResponseEntity<ResponseDto> response = favoriteProductController.getAllFavoriteProducts(username);
		
		verify(favoriteProductService, times(1)).getAllFavoriteProducts(username);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals(mockResponse, response.getBody().getMessage());
	}
}
