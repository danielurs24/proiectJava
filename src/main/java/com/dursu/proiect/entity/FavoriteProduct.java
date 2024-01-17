package com.dursu.proiect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "favorite_products")
public class FavoriteProduct {
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@ManyToOne
		@JoinColumn(name = "user_id", nullable = false)
		@NotNull
		private User user;
		
		@ManyToOne
		@JoinColumn(name = "product_id", nullable = false)
		@NotNull
		private Product product;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
}
