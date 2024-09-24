package com.hackafun.foodliner.models;

import java.io.Serializable;
import java.util.List;

public class GenerateFoodResponse implements Serializable {
    private List<String> items;

    public GenerateFoodResponse(List<String> items) {
        this.items = items;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
