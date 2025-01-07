package com.legmod.ddd.inventory.interfaces.event;

import com.legmod.ddd.inventory.application.commandservices.InventoryCommandService;
import com.legmod.ddd.inventory.domain.model.commands.RemoveStockCommand;
import com.legmod.ddd.inventory.interfaces.event.mmaper.OrderEventMapper;
import com.myshop.shareddomain.event.order.OrderPlacedEvent;
import com.myshop.shareddomain.model.order.OrderLine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OrderEventsConsumer {
    private final OrderEventMapper orderEventMapper;
    private final InventoryCommandService inventoryCommandService;

    @Bean
    public Consumer<Message<OrderPlacedEvent>> orderEventsHandler() {  // application.yaml의 Function Definition에 정의된 이름과 일치해야 함
        return message -> {
            OrderPlacedEvent orderPlacedEvent = message.getPayload();
            log.info("Received order placed event: {}", orderPlacedEvent);
            processOrderEvent(orderPlacedEvent);
        };
    }

    private void processOrderEvent(OrderPlacedEvent orderPlacedEvent) {
        // 이벤트를 명령으로 문맥 변환한다.
        String orderNumber = orderPlacedEvent.getNumber();
        String ordererId = orderPlacedEvent.getOrderer().getMemberId().getId();
        String ordererName = orderPlacedEvent.getOrderer().getName();
        List<OrderLine> orderLines = orderPlacedEvent.getOrderLines().stream()
                .map(line -> OrderLine.builder().productId(line.getProductId()).price(line.getPrice()).quantity(line.getQuantity()).build())
                .toList();
        String orderDate = orderPlacedEvent.getOrderDate().toString();

        RemoveStockCommand removeStockCommand = orderEventMapper.toRemoveStockCommand(orderNumber, ordererId, ordererName, orderLines, orderDate);

        // 명령을 처리한다.
        inventoryCommandService.removeStock(removeStockCommand);
    }
}
