package com.cts.project.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cts.project.dto.ProductDTO;
import com.cts.project.entity.Products;
import com.cts.project.repository.ProductRepository;
import com.cts.project.service.ProductService;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")//Allows cross-origin requests specifically from http://127.0.0.1:5500/
@RequestMapping("/OMP") //Sets the base URL for all endpoints in this controller to /OMP

public class ProductController {
	
	@Autowired // Automatically injecting the ProductService bean
	ProductService productService;
	
	// Endpoint to add a new product
	@PostMapping("/addProduct")
	
	//Represents the entire HTTP response, including the status code, headers, and body
	public ResponseEntity<Products>  createNewProduct(
			
			//bind the request parameters to the method parameters.
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam MultipartFile imageFile) throws IOException
	{
	
	 // Calling service method to add product and returning response
	 return ResponseEntity.ok(productService.addProduct(name, description, imageFile));
	
	}
	
	// Endpoint to view all products
	@GetMapping("/viewAllProducts")
	public ResponseEntity<List<ProductDTO>> viewAllProducts(){
		return ResponseEntity.ok(productService.viewAllProducts());
	}
	
	// Endpoint to view details of a specific product by ID
	@GetMapping("/viewProductDetails/{productId}")
	public Products viewProductDetails(@PathVariable int productId){
		return productService.viewProductById(productId);
	}
	
	// Endpoint to remove a product by ID
	@DeleteMapping("/removeProduct/{productId}")
	public String removeProduct(@PathVariable int productId) {
		productService.removeProduct(productId);
		return "Product deleted successfully";
	}
	
	// Endpoint to get the image of a product by ID
	@GetMapping("product/image/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable int id){
		byte[] image = productService.getProductImage(id);
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_PNG) // Setting content type to PNG
				.body(image); // Returning image in response body
	}
	
}
