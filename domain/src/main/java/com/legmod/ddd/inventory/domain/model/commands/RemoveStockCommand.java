package com.legmod.ddd.inventory.domain.model.commands;

import com.myshop.shareddomain.model.order.OrderLine;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RemoveStockCommand {
    String orderNumber;
    String ordererId;
    String ordererName;
    List<OrderLine> orderLines;
    String orderDate;
}
