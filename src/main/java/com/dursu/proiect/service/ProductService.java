package com.dursu.proiect.service;

import com.dursu.proiect.dto.ProductDto;
import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.entity.Category;
import com.dursu.proiect.entity.Product;
import com.dursu.proiect.entity.Role;
import com.dursu.proiect.repository.CategoryRepository;
import com.dursu.proiect.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
	
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	
	public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}
	
	public Object addProduct(ProductDto product) {
		
		try {
			if (productRepository.findByName(product.getName()).isPresent()) {
				return new ResponseDto(false, "Product already exists");
			}
			
			Optional<Category> category = categoryRepository.findByName(product.getCategoryName());
			
			if (!category.isPresent()) {
				return new ResponseDto(false, "Category not found " + product.getCategoryName());
			}
			
			Product newProduct = new Product();
			newProduct.setName(product.getName());
			newProduct.setCategory(category.get());
			newProduct.setDescription(product.getDescription());
			newProduct.setStock(product.getStock());
			
			productRepository.save(newProduct);
		} catch (Exception e) {
			return new ResponseDto(false, e);
		}
		return new ResponseDto(true, "Product Inserted succesfully");
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
	
	public List<ProductDto> searchProductsByName(String searchName) {
		
		List<Product> product = this.productRepository.findByNameContainingIgnoreCase(searchName);
		
		return product.stream()
				.map(this::convertToProductDto)
				.collect(Collectors.toList());
	}
	
	public Optional<ProductDto> searchProductByName(String name) {
		ProductDto productDto = new ProductDto();
		Optional<Product> product = this.productRepository.findByName(name);
		if (!product.isEmpty()) {
			productDto = convertToProductDto(product.get());
		}
		
		return Optional.ofNullable(productDto);
	}
	
	public List<ProductDto> getAllProducts() {
		List<Product> product = productRepository.findAll();
		
		return product.stream()
				.map(this::convertToProductDto)
				.collect(Collectors.toList());
	}
	
	public Object editProduct(ProductDto updatedProductDto) {
		Optional<Product> optionalProduct = productRepository.findById(updatedProductDto.getId());
		
		if (optionalProduct.isPresent()) {
			Optional<Category> category = categoryRepository.findByName(updatedProductDto.getCategoryName());
			
			if (!category.isPresent()) {
				return new ResponseDto(false, "Category not found " + updatedProductDto.getCategoryName());
			}
			
			Product existingProduct = optionalProduct.get();
			
			// Update the fields you want to allow editing
			existingProduct.setName(updatedProductDto.getName());
			existingProduct.setDescription(updatedProductDto.getDescription());
			existingProduct.setCategory(category.get());
			existingProduct.setStock(updatedProductDto.getStock());
			
			productRepository.save(existingProduct);
			
			return new ResponseDto(true, "Product updated successfully");
		} else {
			return new ResponseDto(false, "Product not found");
		}
	}
	
	public Object deleteProduct(Long productId) {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		
		if (optionalProduct.isPresent()) {
			productRepository.delete(optionalProduct.get());
			return new ResponseDto(true, "Product deleted successfully");
		} else {
			return new ResponseDto(false, "Product not found");
		}
	}
	
	public ResponseDto buyProducts(List<Long> productIds) {
		for (Long productId : productIds) {
			Optional<Product> optionalProduct = productRepository.findById(productId);
			
			if (optionalProduct.isPresent()) {
				Product product = optionalProduct.get();
				
				if (product.getStock() > 0) {
					product.setStock(product.getStock() - 1);
					productRepository.save(product);
				} else {
					return new ResponseDto(false, "Product with ID " + productId + " is out of stock");
				}
			} else {
				return new ResponseDto(false, "Product with ID " + productId + " not found");
			}
		}
		
		return new ResponseDto(true, "Products bought successfully");
	}
	
	public ResponseDto getProductsByCategory(String categoryName) {
		Optional<Category> category = categoryRepository.findByName(categoryName);
		
		if (category.isPresent()) {
			List<Product> productList = productRepository.findByCategory(category.get());
			
			if (productList.isEmpty()) {
				return new ResponseDto(false, "No products found");
			}
			return new ResponseDto(true,
					productList.stream()
					.map(this::convertToProductDto)
					.collect(Collectors.toList()));
		} else {
			// Handle the case where the category is not found
			return new ResponseDto(false, "Category not found: " + categoryName);
		}
	}
}
