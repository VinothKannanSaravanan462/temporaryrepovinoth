package com.cts.project.dto;

import com.cts.project.entity.Products;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter // Generating getter and setter methods for fields

//Transfer only the required data, reducing the amount of data sent over the network
public class ProductDTO {
	private int productid;
	private String name;
	private String description;
	private String imageUrl;
	
	// Constructor to initialize ProductDTO from Products entity
	public ProductDTO(Products product) {
		
		this.productid = product.getProductid();
		this.name=product.getName();
		this.description = product.getDescription();
		//Constructs the imageUrl based on the productid
		this.imageUrl = "/OMP/product/image/" + product.getProductid(); 
	}
}
