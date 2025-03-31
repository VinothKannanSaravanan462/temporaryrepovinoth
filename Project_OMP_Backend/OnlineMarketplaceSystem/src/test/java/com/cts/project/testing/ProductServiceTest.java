package com.cts.project.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
 
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import com.cts.project.dto.ProductDTO;
import com.cts.project.entity.Products;
import com.cts.project.repository.ProductRepository;
import com.cts.project.service.ProductServiceImpl;
 
//Extends the test class with Mockito's extension to enable Mockito annotations.
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
	
	//Creates a mock instance of ProductRepository.
	@Mock
	private ProductRepository productRepository;
	
	//Injects the mock ProductRepository into the ProductServiceImpl instance
	@InjectMocks
	private ProductServiceImpl productService;
	
	Products product1 = new Products();
	Products product2 = new Products();
	
	//This method runs before each test, setting up the test data
	@BeforeEach
	void setup() {
		
		product1.setProductid(1);
		product1.setName("Nestle");
		
		
		product2.setProductid(2);
		product2.setName("DiaryMilk");
	}
	
	//Marks this method as a test case
	@Test
	void testViewAllProducts() {
		
		//Mocks the findAll method of productRepository to return a list containing product1 and product2
		when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
		
		// Calling the service method
		List<ProductDTO> productDTO = productService.viewAllProducts();
		
		//Asserts that the size of the returned list is 2
		assertEquals(2, productDTO.size());
		assertEquals("Nestle", productDTO.get(0).getName());
		assertEquals("DiaryMilk", productDTO.get(1).getName());
		
		//Verifies that the findAll method of productRepository was called exactly once
		verify(productRepository, times(1)).findAll();
	}
	
	//Marks this method as a test case
	@Test
	void testViewProductsById() {
		
		//Mocks the findById method of productRepository to return Optional.of(product1) when called with ID 1
		when(productRepository.findById(1)).thenReturn(Optional.of(product1));
		
		// Calling the service method
		Products product = productService.viewProductById(1);
		
		assertEquals(1, product.getProductid());
		assertEquals("Nestle", product.getName());
		
		verify(productRepository, times(1)).findById(1);
	}

}
