package com.dursu.proiect.repository;

import com.dursu.proiect.entity.Product;
import com.dursu.proiect.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByProductId(Long productId);
}
