package com.dursu.proiect.repository;

import com.dursu.proiect.entity.Category;
import com.dursu.proiect.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	Optional<Category> findByName(String name);
}
