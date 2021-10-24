package com.lpg.pmapp.vo;

import com.lpg.pmapp.model.Product;


/**
 *
 * @author olatundeonigbinde
 */

public class GetProductResponse extends ServiceResponse {
    private Product product;

    public GetProductResponse(int code) {
        super(code);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
