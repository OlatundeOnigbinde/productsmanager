package com.lpg.pmapp.vo;

import com.lpg.pmapp.model.Category;

import java.util.List;

/**
 *
 * @author olatundeonigbinde
 */
public class GetCategoriesResponse extends ServiceResponse {
    private List<Category> categoryList;

    public GetCategoriesResponse(int code) {
        super(code);
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
