package com.dursu.proiect.repository;

import com.dursu.proiect.entity.FavoriteProduct;
import com.dursu.proiect.entity.Product;
import com.dursu.proiect.entity.User;
import com.fasterxml.jackson.databind.introspect.AnnotationCollector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
	Optional<FavoriteProduct> findByUserAndProduct(User user, Product product);
	
	List<FavoriteProduct> findByUser(User user);
}
