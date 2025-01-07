package com.legmod.ddd.inventory.interfaces.event.mmaper;

import com.legmod.ddd.inventory.config.MapstructConfig;
import com.legmod.ddd.inventory.domain.model.commands.RemoveStockCommand;
import com.myshop.shareddomain.model.order.OrderLine;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapstructConfig.class)
public interface OrderEventMapper {
    RemoveStockCommand toRemoveStockCommand(String orderNumber, String ordererId, String ordererName, List<OrderLine> orderLines, String orderDate);
}
