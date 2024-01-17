package com.dursu.proiect.service;
import com.dursu.proiect.dto.ProductDto;
import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.entity.FavoriteProduct;
import com.dursu.proiect.entity.Product;
import com.dursu.proiect.entity.User;
import com.dursu.proiect.repository.FavoriteProductRepository;
import com.dursu.proiect.repository.ProductRepository;
import com.dursu.proiect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteProductService {
	
	private final FavoriteProductRepository favoriteProductRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	
	@Autowired
	public FavoriteProductService(FavoriteProductRepository favoriteProductRepository,
			UserRepository userRepository,
			ProductRepository productRepository) {
		this.favoriteProductRepository = favoriteProductRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}
	
	private ProductDto convertToProductDto(Product product) {
		ProductDto productDto = new ProductDto();
		productDto.setId(product.getId());
		productDto.setName(product.getName());
		productDto.setStock(product.getStock());
		productDto.setDescription(product.getDescription());
		
		// Assuming Category has a getName() method
		if (product.getCategory() != null) {
			productDto.setCategoryName(product.getCategory().getName());
		}
		
		return productDto;
	}
	
	public ResponseDto addFavoriteProduct(String username, Long productId) {
		
		Optional<User> user = userRepository.findByUsername(username);
		if (userRepository.findByUsername(username).isEmpty()) {
			return new ResponseDto(false, "User not found!");
		}
		
		Optional<Product> product = productRepository.findById(productId);
		if (productRepository.findById(productId).isEmpty()) {
			return new ResponseDto(false, "Product not found!");
		}
		
		if (favoriteProductRepository.findByUserAndProduct(user.get(), product.get()).isPresent()) {
			return new ResponseDto(false, "Product is already a favorite for the user");
		}
		
		FavoriteProduct favoriteProduct = new FavoriteProduct();
		favoriteProduct.setUser(user.get());
		favoriteProduct.setProduct(product.get());
		favoriteProductRepository.save(favoriteProduct);
		
		return new ResponseDto(true, "Product added to favorites successfully");
	}
	
	public ResponseDto removeFavoriteProduct(String username, Long productId) {
		// Find user by username
		Optional<User> user = userRepository.findByUsername(username);
		if (userRepository.findByUsername(username).isEmpty()) {
			return new ResponseDto(false, "User not found!");
		}
		
		Optional<Product> product = productRepository.findById(productId);
		if (productRepository.findById(productId).isEmpty()) {
			return new ResponseDto(false, "Product not found!");
		}
		
		if (!favoriteProductRepository.findByUserAndProduct(user.get(), product.get()).isPresent()) {
			return new ResponseDto(false, "Product is not in favorites!");
		}
		
		favoriteProductRepository.findByUserAndProduct(user.get(), product.get())
				.ifPresent(favoriteProductRepository::delete);
		
		return new ResponseDto(true, "Product removed from favorites successfully");
	}
	
	public ResponseDto getAllFavoriteProducts(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (userRepository.findByUsername(username).isEmpty()) {
			return new ResponseDto(false, "User not found!");
		}
		
		List<FavoriteProduct> favoriteProducts = favoriteProductRepository.findByUser(user.get());
		
		if(favoriteProducts.isEmpty()) {
			return new ResponseDto(false, "No favorite products found!");
		}

		return new ResponseDto(true, favoriteProducts.stream()
				.map(fp -> convertToProductDto(fp.getProduct()))
				.collect(Collectors.toList()));
	}
}
