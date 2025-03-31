package com.cts.project.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // Declaring this class as an entity to be mapped as a table
@Data // Generating getter, setter, toString,etc
@Getter @Setter // Generating getter and setter methods for fields
@NoArgsConstructor // Generating no-argument constructor
@AllArgsConstructor //Generates a constructor with one parameter for each field in the class.
@Table(name="products") // Mapping this entity to the "products" table

public class Products {
	@Id // Declaring this field as the primary key
	@Column(name="productid")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Specifies that the primary key value will be generated automatically by the database.
	private int productid;
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@Lob // Declaring this field as a large object - binary large object or character large object
	@Column(columnDefinition = "LONGBLOB")
	private byte[] images; //Stores images as a byte array.
	
	@Column(name="isactive")
	private Boolean isActive = true;
	
	@Column(name="addedon")
	// Field for product added date, default is current date and time
	private LocalDateTime addedOn = LocalDateTime.now();
	
	@Column(name="updatedon")
	// Field for product updated date, default is current date and time
	private LocalDateTime updatedOn = LocalDateTime.now();
}
