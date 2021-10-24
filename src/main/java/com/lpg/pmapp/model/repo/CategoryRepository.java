package com.lpg.pmapp.model.repo;

import com.lpg.pmapp.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository  extends CrudRepository<Category, Long> {

    @Query("SELECT c FROM Category c ORDER BY c.name DESC")
    List<Category> findAllOrderByNameDesc();

    @Query("SELECT c FROM Category c where lower(c.name) = :categoryName")
    Optional<Category> findByName(@Param("categoryName") String categoryName);
}
