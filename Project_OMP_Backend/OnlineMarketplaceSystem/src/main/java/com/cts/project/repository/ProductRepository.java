package com.cts.project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.project.entity.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {
	
	// No additional methods required, JpaRepository provides basic CRUD operations
}
