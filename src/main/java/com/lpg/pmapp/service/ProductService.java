package com.lpg.pmapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lpg.pmapp.model.Category;
import com.lpg.pmapp.model.Product;
import com.lpg.pmapp.model.User;
import com.lpg.pmapp.model.repo.CategoryRepository;
import com.lpg.pmapp.model.repo.ProductRepository;
import com.lpg.pmapp.model.repo.UserRepository;
import com.lpg.pmapp.vo.GetCategoriesResponse;
import com.lpg.pmapp.vo.GetProductResponse;
import com.lpg.pmapp.vo.GetProductsResponse;
import com.lpg.pmapp.vo.ServiceResponse;
import jodd.util.Base64;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductService {
    private static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());

    @Autowired
    CategoryRepository categoryRepo;
    @Autowired
    ProductRepository productRepo;
    @Autowired
    UserRepository userRepo;

    public ServiceResponse createCategory(String sessionToken, String categoryName){
        if (StringUtil.isBlank(sessionToken)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Session token is required");
        }
        Long userId = userIdInSessionToken(sessionToken);
        if (userId == null || userId <= 0l) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }
        // check if this user data exists
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }
        if (StringUtil.isBlank(categoryName)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Category name is required");
        }

        // check if this category exists
        Optional<Category> categoryOptional = categoryRepo.findByName(categoryName.toLowerCase());
        if (categoryOptional.isPresent()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Category already exists");
        }

        Category category = new Category();
        category.setCreateDate(new Date());
        category.setName(categoryName);
        try {
            categoryRepo.save(category);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "", ex);
            return new ServiceResponse(ServiceResponse.FAILED_CODE, ServiceResponse.FAILED_MESSAGE);
        }
        return new ServiceResponse(ServiceResponse.SUCCESS_CODE, ServiceResponse.SUCCESS_MESSAGE + ". Category created successfully");
    }

    public ServiceResponse getCategories(String sessionToken){
        if (StringUtil.isBlank(sessionToken)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Session token is required");
        }
        Long userId = userIdInSessionToken(sessionToken);
        if (userId == null || userId <= 0l) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }
        // check if this user data exists
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }

        GetCategoriesResponse response = new GetCategoriesResponse(ServiceResponse.FAILED_CODE);
        List<Category> categories = categoryRepo.findAllOrderByNameDesc();
        response.setCategoryList(categories);
        if(categories != null && !categories.isEmpty()) {
            response.setCode(ServiceResponse.SUCCESS_CODE);
            response.setMessage(categories.size() + " record(s) found");
        } else {
            response.setMessage("No record found");
        }
        return response;
    }

    public ServiceResponse getProductsByCategory(String sessionToken, Long categoryId) throws JsonProcessingException {
        if (StringUtil.isBlank(sessionToken)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Session token is required");
        }
        Long userId = userIdInSessionToken(sessionToken);
        if (userId == null || userId <= 0l) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }
        // check if this user data exists
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }
        if (categoryId == null || categoryId <= 0l) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Please supply a product category for this search");
        }


        // check if this category exists
        Optional<Category> categoryOptional = categoryRepo.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Category ID supplied does not exist");
        }

        GetProductsResponse response = new GetProductsResponse(ServiceResponse.FAILED_CODE);
        List<Product> products = productRepo.findByCategoryIdOrderByCreationDateDesc(categoryId);
        response.setProductsList(products);
        if(products != null && !products.isEmpty()) {
            response.setCode(ServiceResponse.SUCCESS_CODE);
            response.setMessage(products.size() + " record(s) found");
        } else {
            response.setMessage("No record found");
        }
        return response;
    }

    public ServiceResponse getProductById(String sessionToken, Long productId) throws JsonProcessingException {
        if (StringUtil.isBlank(sessionToken)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Session token is required");
        }
        Long userId = userIdInSessionToken(sessionToken);
        if (userId == null || userId <= 0l) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }
        // check if this user data exists
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }

        if (productId == null || productId <= 0l) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Product ID is required");
        }


        GetProductResponse response = new GetProductResponse(ServiceResponse.SUCCESS_CODE);
        Optional<Product> product = productRepo.findById(productId);
        if(product.isPresent()){
            response.setProduct(product.get());
            response.setMessage("product found");
        }
        else {
            response.setCode(ServiceResponse.FAILED_CODE);
            response.setMessage("No record found");
        }
        return response;
    }

    public Long userIdInSessionToken(String session_token) {
        Long id = null;
        try {
            session_token = Base64.decodeToString(session_token);
            if (StringUtil.isBlank(session_token)) {
                return null;
            }

            String[] session_token_fields = session_token.split("-");
            id = Long.parseLong(session_token_fields[0]);
        } catch (Exception var3) {

        }
        return id;
    }

    public ServiceResponse getProducts(String sessionToken){
        if (StringUtil.isBlank(sessionToken)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Session token is required");
        }
        Long userId = userIdInSessionToken(sessionToken);
        if (userId == null || userId <= 0l) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }
        // check if this user data exists
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }

        GetProductsResponse response = new GetProductsResponse(ServiceResponse.FAILED_CODE);
        List<Product> products = productRepo.findAllOrderByNameDesc();
        response.setProductsList(products);
        if(products != null && !products.isEmpty()) {
            response.setCode(ServiceResponse.SUCCESS_CODE);
            response.setMessage(products.size() + " record(s) found");
        } else {
            response.setMessage("No record found");
        }
        return response;
    }

    public ServiceResponse createProduct(String sessionToken, String productName, String description, Long categoryId,
                                         String lastPurchasedDateStr){
        if (StringUtil.isBlank(sessionToken)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Session token is required");
        }
        Long userId = userIdInSessionToken(sessionToken);
        if (userId == null || userId <= 0l) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }
        // check if this user data exists
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }

        if (StringUtil.isBlank(productName)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Product name is required");
        }
        if (StringUtil.isBlank(description)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Product description is required");
        }
        if (categoryId == null || categoryId <= 0l) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Product category is required");
        }
        // check if this category exists
        Optional<Category> categoryOptional = categoryRepo.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Category ID supplied does not exist");
        }

        List<Product> duplicateProduct = productRepo.findByNameAndCategory(productName.toLowerCase(), categoryId);
        if(duplicateProduct != null && duplicateProduct.size() >0){
            return new ServiceResponse(ServiceResponse.FAILED_CODE, String.format("A product with name %s already exists under category %s", productName, categoryOptional.get().getName()));
        }

        if (StringUtil.isBlank(lastPurchasedDateStr)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Last purchased date is required");
        }
        Product product = new Product();
        product.setCreationDate(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date lastPurchasedDate = null;

        try {
            lastPurchasedDate = sdf.parse(lastPurchasedDateStr);
        } catch (Exception ex) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Last purchased date entered is not in valid format. yyyy-MM-dd");
        }
        if(lastPurchasedDate.after(product.getCreationDate())){
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Last purchased date cannot be a future date");
        }

        product.setName(productName);
        product.setCategory(categoryOptional.get());
        product.setDescription(description);
        product.setLastPurchasedDate(lastPurchasedDate);

        try {
            productRepo.save(product);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "", ex);
            return new ServiceResponse(ServiceResponse.FAILED_CODE, ServiceResponse.FAILED_MESSAGE);
        }
        return new ServiceResponse(ServiceResponse.SUCCESS_CODE, ServiceResponse.SUCCESS_MESSAGE + ". Product created successfully");
    }

    public ServiceResponse editProduct(String sessionToken, Long productId, String productName, String description, Long categoryId,
                                         String lastPurchasedDateStr){
        if (StringUtil.isBlank(sessionToken)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Session token is required");
        }
        Long userId = userIdInSessionToken(sessionToken);
        if (userId == null || userId <= 0l) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }
        // check if this user data exists
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }
        Optional<Product> productOptional = productRepo.findById(productId);
        if(productOptional.isEmpty()){
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Product with ID supplied does not exist");
        }
        Product productToUpdate = productOptional.get();
        productToUpdate.setUpdateDate(new Date());

        if (StringUtil.isNotBlank(productName)) {
            productToUpdate.setName(productName);
        }
        if (StringUtil.isNotBlank(description)) {
            productToUpdate.setDescription(description);
        }
        if (categoryId != null && categoryId >= 0l) {
            // check if this category exists
            Optional<Category> categoryOptional = categoryRepo.findById(categoryId);
            if (categoryOptional.isEmpty()) {
                return new ServiceResponse(ServiceResponse.FAILED_CODE, "Category ID supplied does not exist");
            }
            productToUpdate.setCategory(categoryOptional.get());
        }
        if (StringUtil.isNotBlank(lastPurchasedDateStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date lastPurchasedDate;

            try {
                lastPurchasedDate = sdf.parse(lastPurchasedDateStr);
            } catch (Exception ex) {
                return new ServiceResponse(ServiceResponse.FAILED_CODE, "Last purchased date entered is not in valid format. yyyy-MM-dd");
            }
            if(lastPurchasedDate.after(productToUpdate.getCreationDate())){
                return new ServiceResponse(ServiceResponse.FAILED_CODE, String.format("Last purchased date cannot be after the creation date %s",productToUpdate.getCreationDate()));
            }
            productToUpdate.setLastPurchasedDate(lastPurchasedDate);
        }
        List<Product> existingProduct = productRepo.findByNameAndCategory(productToUpdate.getName().toLowerCase(), productToUpdate.getCategory().getId());
        if((!existingProduct.isEmpty()) && existingProduct.stream().findFirst().get().getId().compareTo(productToUpdate.getId()) != 0){
            return new ServiceResponse(ServiceResponse.FAILED_CODE, String.format("A product with name %s already exists under category %s", productToUpdate.getName(), productToUpdate.getCategory().getName()));
        }

        try {
            productRepo.save(productToUpdate);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "", ex);
            return new ServiceResponse(ServiceResponse.FAILED_CODE, ServiceResponse.FAILED_MESSAGE);
        }
        return new ServiceResponse(ServiceResponse.SUCCESS_CODE, ServiceResponse.SUCCESS_MESSAGE + ". Product updated successfully");
    }

    public ServiceResponse deleteProduct(String sessionToken, Long productId){
        if (StringUtil.isBlank(sessionToken)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Session token is required");
        }
        Long userId = userIdInSessionToken(sessionToken);
        if (userId == null || userId <= 0l) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }
        // check if this user data exists
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Invalid session token supplied, please login and try again");
        }
        if (productId == null || productId <= 0l) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Product ID is required");
        }

        // check if this product exists
        Optional<Product> productOptional = productRepo.findById(productId);
        if (productOptional.isEmpty()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Product ID supplied does not exist");
        }
        try {
            productRepo.delete(productOptional.get());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "", ex);
            return new ServiceResponse(ServiceResponse.FAILED_CODE, ServiceResponse.FAILED_MESSAGE);
        }
        return new ServiceResponse(ServiceResponse.SUCCESS_CODE, ServiceResponse.SUCCESS_MESSAGE + ". Product deleted successfully");
    }
}
