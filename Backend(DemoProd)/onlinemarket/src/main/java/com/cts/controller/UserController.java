package com.cts.controller;
 
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
 
import com.cts.dto.ProductViewDTO;
import com.cts.dto.RequestDTO;
import com.cts.dto.ResetPasswordDTO;
import com.cts.dto.ResponseDTO;
import com.cts.dto.SignInRequest;
import com.cts.dto.SignInResponse;
import com.cts.entity.User;
import com.cts.exception.UserNotFoundException;
import com.cts.service.ProductService;
import com.cts.service.UserService;
import com.cts.exception.PasswordsMismatchException;


import jakarta.validation.Valid;

import com.cts.service.UserValidationService;

 
@RestController
@RequestMapping("/OMP")
@CrossOrigin(origins = "http://127.0.0.1:3000")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    UserValidationService userValidationService;
    @Autowired
    ProductService productService;
 
    // Create User API
    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String nickName,
            @RequestParam(required = false) String addressLine1,
            @RequestParam(required = false) String addressLine2,
            @RequestParam(required = false) String postalCode, // Changed to String
            @RequestParam(required = false) String contactNumber,
            @RequestParam(required = false) String dateOfBirth, // Changed to String
            @RequestParam(required = false) MultipartFile imageFile
    ) throws IOException, ParseException {
 
        List<String> missingFields = new ArrayList<>();
        if (firstName == null || firstName.isEmpty()) missingFields.add("firstName");
        if (lastName == null || lastName.isEmpty()) missingFields.add("lastName");
        if (email == null || email.isEmpty()) missingFields.add("email");
        if (password == null || password.isEmpty()) missingFields.add("password");
        if (contactNumber == null || contactNumber.isEmpty()) missingFields.add("contactNumber");
        if (dateOfBirth == null || dateOfBirth.isEmpty()) missingFields.add("dateOfBirth");
 
        if (!missingFields.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Missing required fields: " + String.join(", ", missingFields));
        }
 
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setFirstName(firstName);
        requestDTO.setLastName(lastName);
        requestDTO.setEmail(email);
        requestDTO.setPassword(password);
        requestDTO.setNickName(nickName);
        requestDTO.setAddressLine1(addressLine1);
        requestDTO.setAddressLine2(addressLine2);
        requestDTO.setPostalCode(postalCode); // Pass String
        requestDTO.setContactNumber(contactNumber);
        requestDTO.setDateOfBirth(dateOfBirth); // Pass String
 
        ResponseDTO responseDTO = userService.createUser(requestDTO, imageFile);
        return ResponseEntity.ok(responseDTO);
    }
 
    // Get All Users API
    @GetMapping("/getAllUser")
    public List<ResponseDTO> getAllUser() {
        return userService.getAllUsers();
    }
 
 // Get User ID by Email API
    @GetMapping("/getUserIdByEmail")
    public Integer getUserIdByEmail(
//    		@RequestHeader("Authorization") String authHeaders,
    		@RequestParam String emailId) {
    	
//    	this.checkAuthorizationHeaders(authHeaders);
    	
        return userService.getUserIdByEmail(emailId);
    }
    
    @GetMapping("/getUserEmailById")
    public String getUserEmailbyId(
//    		@RequestHeader("Authorization") String authHeaders,
    		@RequestParam int id) {
    	
//    	this.checkAuthorizationHeaders(authHeaders);
    	
        return userService.getUserEmailById(id);
    }
 
    // Get User Image by ID API
    @GetMapping("user/image/{userId}")
    public ResponseEntity<byte[]> getImage(@PathVariable int userId) {
        byte[] image = userService.getUserImage(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }
 
//    @GetMapping("user/image/{userId}")
//    public ResponseEntity<Resource> getImage(@PathVariable int userId) {
//    	byte[] imageData = userService.getProductImage(userId);
//        if (imageData != null && imageData.length > 0) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_JPEG)
//                    .body(new ByteArrayResource(imageData));
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
    
    // Get User Details by ID API
    @GetMapping("/myDetails")
    public ResponseEntity<ResponseDTO> getUserDetailsById(
    		@RequestHeader("Authorization") String authHeaders,
    		@RequestParam(required = false) Integer userId) {
        if (userId == null) {
            throw new UserNotFoundException("user not found");
        }
        ResponseDTO user = userService.getUserDetailsById(userId);
        
        this.checkAuthorizationHeaders(authHeaders);
        
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/myName")
    public ResponseEntity<Map<String, String>> getUsername(
    	@RequestParam(required=false) Integer userId){
    	if (userId == null) {
            throw new UserNotFoundException("user not found");
        }
    	String userName = userService.getUserName(userId);
        Map<String, String> response = new HashMap<>();
        response.put("firstName", userName);
        return ResponseEntity.ok(response);
    }
 
    // Update User API
    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<User> updateUserDetails(
    		@RequestHeader("Authorization") String authHeaders,
            @PathVariable int userId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) MultipartFile photo,
            @RequestParam(required = false) String nickName,
            @RequestParam(required = false) String addressLine1,
            @RequestParam(required = false) String addressLine2,
            @RequestParam(required = false) String postalCode, // Changed to String
            @RequestParam(required = false) String contactNumber,
            @RequestParam(required = false) String dateOfBirth, // Changed to String
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String password
    ) throws IOException {
 
        User updatedUser = userService.updateUserDetails(
                userId, firstName, lastName, email, photo, nickName,
                addressLine1, addressLine2, postalCode, contactNumber, dateOfBirth, isActive, password
        );
 
        this.checkAuthorizationHeaders(authHeaders);
        
        return ResponseEntity.ok(updatedUser);
    }



    
    @PostMapping("/login")
    public ResponseEntity<SignInResponse> loginUser(@Valid @RequestBody SignInRequest signInRequest) {
        SignInResponse response = userService.validateUser(signInRequest);
        return ResponseEntity.ok(response);
     }
 
    // Email Verification API
    @PostMapping("/verify-email")
    public String verifyEmail(@RequestParam String email) {
        return userService.verifyEmail(email);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String email = ex.getParameterName();
        return new ResponseEntity<>("Validation Error: '" + email + "' field is required.", HttpStatus.BAD_REQUEST);
    }
 
   
    @PostMapping("/reset-password")

    public ResponseEntity<String> resetPassword(
            @RequestParam("email") String email,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword) {
        try {
            userService.resetPassword(email, newPassword, confirmPassword);
            return ResponseEntity.ok("Password updated successfully!");      
        } 
        catch (UserNotFoundException e) 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (PasswordsMismatchException e) 
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during password reset.");
        }
    }
 
    // Generate Reset Link API
    @PostMapping("/generate-reset-link")
    public ResponseEntity<String> generateResetLink(@RequestParam String email) {
        String resetLink = userService.generateResetLink(email);
        if (resetLink.equals("User not found")) {
            return ResponseEntity.status(404).body(resetLink);
        }
        return ResponseEntity.ok(resetLink);
    }
    
     //get subscription list
    @GetMapping("/getProductSubscriptionList")
    public List<ProductViewDTO> getProductSubscriptionList(
    		@RequestHeader("Authorization") String authHeaders,
    		@RequestParam int userId){
    	this.checkAuthorizationHeaders(authHeaders);
    	return productService.getProductSubscriptionList(userId);
    }
    
    public void checkAuthorizationHeaders(String authHeaders) {
    	if (authHeaders != null && authHeaders.startsWith("Basic ")) {
            String base64Credentials = authHeaders.substring("Basic ".length());
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String decodedString = new String(decodedBytes);
 
            // Split username and password
            String[] credentials = decodedString.split(":", 2);
            String username = credentials[0];
            String password = credentials[1];
            	
            System.out.println(username);
            System.out.println(password);
        } else {
        	System.out.println("Invalid Authorization headers");
        }
    }
    
}
 