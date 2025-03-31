package com.cts.project.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cts.project.dto.ProductDTO;
import com.cts.project.entity.Products;
import com.cts.project.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired // Automatically injecting the ProductRepository bean
	ProductRepository productRepository;
	
	@Override
	public List<ProductDTO> viewAllProducts() {
		
		    List<Products> products = productRepository.findAll();
		    List<ProductDTO> productDTOs = new ArrayList<>();
		    
		    for (Products product : products) {
		        productDTOs.add(new ProductDTO(product));
		    }
		    
		    return productDTOs;
	}

	@Override
	public void removeProduct(int id) {
		// Removing product by ID from the repository
		productRepository.deleteById(id); 
	}

	@Override
	public Products viewProductById(int id) {
		// Fetching product by ID from the repository
		return productRepository.findById(id).get(); 
	}

	
	@Override
	public Products addProduct(String name, String description, MultipartFile imageFile) throws IOException {
		
		// Creating a new Product
		Products product = new Products(); 
		product.setName(name);
		product.setDescription(description);
		product.setImages(imageFile.getBytes());
		return productRepository.save(product);
	}



	@Override
	public byte[] getProductImage(int id) {
		
		// Fetching product by ID or throwing exception if not found
		Products product = productRepository.findById(id).orElseThrow(()->
									new RuntimeException("Product not Found"));
		// Returning product images
		return product.getImages();
	}


}
