package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.dto.ReviewAndRatingDTO;
import com.cts.entity.ReviewsAndRatings;
import com.cts.entity.User;

import jakarta.transaction.Transactional;

public interface ReviewAndRatingRepository extends JpaRepository<ReviewsAndRatings, Long> {
//	List<ReviewsAndRatings> findByUserId(int userId);
//	List<ReviewsAndRatings> findByProductId(int productId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE reviewsandratings " +
            "SET isactive = false, updatedon = CURRENT_TIMESTAMP " +
            "WHERE isactive = true AND userid = :userId AND productid = :productId",
    nativeQuery = true)
	void deactivateExistingReview(@Param("userId") int userId,
	                              @Param("productId") int productId);
	
	@Query(value = "SELECT productid, MAX(rating) as max_rating FROM reviewsandratings " +
            "WHERE isactive = 1 " +
            "GROUP BY productid " +
            "ORDER BY max_rating DESC " +
            "LIMIT 2", nativeQuery = true)
	List<Object[]> findTopRatedProducts();
	List<ReviewsAndRatings> findByUser(User user);
	List<ReviewsAndRatings> findByProductsProductidOrderByRatingDesc(int productid);
	
	@Query("SELECT r FROM ReviewsAndRatings r WHERE r.products.productid = :productId AND r.reviewActiveStatus = TRUE ORDER BY r.rating DESC, r.reviewCreatedOn DESC")
	List<ReviewsAndRatings> findTopByProductidAndReviewActiveStatusOrderByRatingDescAndReviewCreatedOnDesc(@Param("productId") int productId);
	
	List<ReviewsAndRatings> findByProductsProductidAndReviewActiveStatusOrderByRatingDesc(int productid, boolean reviewActiveStatus);

//	@Query(value="SELECT r FROM ReviewsAndRatings r WHERE r.products.productid = :productId ORDER BY r.rating DESC, r.reviewCreatedOn DESC")
//	List<ReviewsAndRatings> findTopByProductidOrderByRatingDescAndReviewCreatedOnDesc(int productId);
	
	@Query(value="SELECT r FROM ReviewsAndRatings r WHERE r.products.productid = :productId ORDER BY r.rating DESC, r.reviewCreatedOn DESC")
    List<ReviewsAndRatings> findTopByProductidOrderByRatingDescAndReviewCreatedOnDesc(int productId);
}
