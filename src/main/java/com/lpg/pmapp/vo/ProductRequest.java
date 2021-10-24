package com.lpg.pmapp.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author olatundeonigbinde
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ProductRequest {
    private Long productId;
    private String productName;
    private String description;
    private Long categoryId;
    private String lastPurchasedDateStr;
}
