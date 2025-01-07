package com.myshop.shareddomain.model.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderLine {
    private String productId;
    private int price;
    private int quantity;
}
