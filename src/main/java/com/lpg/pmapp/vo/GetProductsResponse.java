package com.lpg.pmapp.vo;

import com.lpg.pmapp.model.Product;

import java.util.List;

/**
 *
 * @author olatundeonigbinde
 */

public class GetProductsResponse extends ServiceResponse {
    private List<Product> productList;

    public GetProductsResponse(int code) {
        super(code);
    }

    public List<Product> getProductsList() {
        return productList;
    }

    public void setProductsList(List<Product> productList) {
        this.productList = productList;
    }
}
