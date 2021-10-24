package com.lpg.pmapp.model.repo;

import com.lpg.pmapp.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId ORDER BY p.creationDate DESC")
    List<Product> findByCategoryIdOrderByCreationDateDesc(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Product p ORDER BY p.name DESC")
    List<Product> findAllOrderByNameDesc();

    @Query("SELECT p FROM Product p WHERE lower(p.name) = :productName AND p.category.id = :categoryId ORDER BY p.creationDate DESC")
    List<Product> findByNameAndCategory(@Param("productName") String productName, @Param("categoryId") Long categoryId);
}
