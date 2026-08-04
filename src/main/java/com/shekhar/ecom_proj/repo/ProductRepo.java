package com.shekhar.ecom_proj.repo;

import com.shekhar.ecom_proj.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    @Query("""
SELECT p FROM Product p WHERE
LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<Product> searchProducts(String keyword);

    @Modifying
    @Query("""
        UPDATE Product p
        SET p.quantity = p.quantity - :qty
        WHERE p.id = :id
        AND p.quantity >= :qty
        """)
    int decreaseStock(@Param("id") Integer id,
                      @Param("qty") int qty);

    @Query("""
    SELECT p.quantity from Product p WHERE p.id = :id
""")
    int quantityOfProduct(@Param("id") Integer id);


}
