package com.lpg.pmapp.controller;

import com.lpg.pmapp.service.ProductService;
import com.lpg.pmapp.vo.ProductRequest;
import com.lpg.pmapp.vo.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {
    private static final Logger LOGGER = Logger.getLogger(ProductController.class.getName());

    @Autowired
    ProductService ps;

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<ServiceResponse> getProducts(@RequestHeader("sessionToken") String sessionToken) {
        try {
            ServiceResponse response = ps.getProducts(sessionToken);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().body(new ServiceResponse(ServiceResponse.FAILED_CODE, "Error occurred. Reason: " + ex.getMessage()));
        }
    }

    @GetMapping(value = "/{productId}", produces = {"application/json"})
    public ResponseEntity<ServiceResponse> getProductById(@RequestHeader("sessionToken") String sessionToken,
                                                                 @PathVariable("productId") Long productId) {
        try {
            ServiceResponse response = ps.getProductById(sessionToken, productId);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().body(new ServiceResponse(ServiceResponse.FAILED_CODE, "Error occurred. Reason: " + ex.getMessage()));
        }
    }

    @GetMapping(value = "/getproducts/{categoryId}", produces = {"application/json"})
    public ResponseEntity<ServiceResponse> getProductsByCategory(@RequestHeader("sessionToken") String sessionToken,
                                                                 @PathVariable("categoryId") Long categoryId) {
        try {
            ServiceResponse response = ps.getProductsByCategory(sessionToken, categoryId);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().body(new ServiceResponse(ServiceResponse.FAILED_CODE, "Error occurred. Reason: " + ex.getMessage()));
        }
    }

    @GetMapping(value = "/categories", produces = {"application/json"})
    public ResponseEntity<ServiceResponse> getProductsCategories(@RequestHeader("sessionToken") String sessionToken) {
        try {
            ServiceResponse response = ps.getCategories(sessionToken);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().body(new ServiceResponse(ServiceResponse.FAILED_CODE, "Bad request was sent. Reason: " + ex.getMessage()));
        }
    }

    @PostMapping(value = "/categories",consumes = {"application/json", "text/plain;charset=UTF-8"}, produces = {"application/json"})
    public ResponseEntity<ServiceResponse> createCategory(
            @RequestHeader("sessionToken") String sessionToken,
            @RequestParam String categoryName) {
        try {
            ServiceResponse response = ps.createCategory(sessionToken, categoryName);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().body(new ServiceResponse(ServiceResponse.FAILED_CODE, "Bad request was sent. Reason: " + ex.getMessage()));
        }
    }

    @PostMapping(consumes = {"application/json", "text/plain;charset=UTF-8"}, produces = {"application/json"})
    public ResponseEntity<ServiceResponse> createProduct(
            @RequestHeader("sessionToken") String sessionToken,
            @RequestBody ProductRequest req) {
        try {
            ServiceResponse response = ps.createProduct(sessionToken, req.getProductName(), req.getDescription(), req.getCategoryId(),
                    req.getLastPurchasedDateStr());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().body(new ServiceResponse(ServiceResponse.FAILED_CODE, "Bad request was sent. Reason: " + ex.getMessage()));
        }
    }

    @PostMapping(value = "/edit/{productId}", consumes = {"application/json", "text/plain;charset=UTF-8"}, produces = {"application/json"})
    public ResponseEntity<ServiceResponse> editProduct(
            @RequestHeader("sessionToken") String sessionToken,
            @RequestBody ProductRequest req,
            @PathVariable("productId") Long productId) {
        try {
            ServiceResponse response = ps.editProduct(sessionToken, productId, req.getProductName(), req.getDescription(), req.getCategoryId(),
                    req.getLastPurchasedDateStr());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().body(new ServiceResponse(ServiceResponse.FAILED_CODE, "Bad request was sent. Reason: " + ex.getMessage()));
        }
    }

    @DeleteMapping(value = "/delete/{productId}", produces = {"application/json"})
    public ResponseEntity<ServiceResponse> deleteProduct(
            @RequestHeader("sessionToken") String sessionToken,
            @PathVariable("productId") Long productId) {
        try {
            ServiceResponse response = ps.deleteProduct(sessionToken, productId);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().body(new ServiceResponse(ServiceResponse.FAILED_CODE, "Bad request was sent. Reason: " + ex.getMessage()));
        }
    }
}
