package com.cts.project.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cts.project.dto.ProductDTO;
import com.cts.project.entity.Products;

public interface ProductService {
	
	//Returns a list of all products as ProductDTO objects
	public List<ProductDTO> viewAllProducts();
	
	//Returns a Products entity by its ID.
	public Products viewProductById(int id);
	
	//This method handles file uploads and may throw an IOException if there are issues with file handling
	public Products addProduct(String name, String description, MultipartFile imageFile) throws IOException;
	
	//Removes a product by its ID
	public void removeProduct(int id);

	//Returns the image of a product as a byte array
	public byte[] getProductImage(int id);
	
}
