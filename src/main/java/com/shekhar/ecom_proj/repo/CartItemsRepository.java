package com.shekhar.ecom_proj.repo;

import com.shekhar.ecom_proj.dto.CartItemDto;
import com.shekhar.ecom_proj.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {
    @Query("""
        SELECT new com.shekhar.ecom_proj.dto.CartItemDto (
          c.productId,c.quantity
          )
        FROM CartItems c WHERE c.user.userId = :id
""")
    List<CartItemDto> getCartItembyId(@Param("id") Integer id);

    void deleteByUserUserId(Integer userId);
}