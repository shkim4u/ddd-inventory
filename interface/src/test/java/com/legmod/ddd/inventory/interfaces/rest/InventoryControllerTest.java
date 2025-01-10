package com.legmod.ddd.inventory.interfaces.rest;

import com.legmod.ddd.inventory.application.queryservices.InventoryQueryService;
import com.legmod.ddd.inventory.domain.model.view.InventoryView;
import com.legmod.ddd.inventory.interfaces.rest.dto.AppObject;
import com.legmod.ddd.inventory.interfaces.rest.dto.InventoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@Import(InventoryControllerTest.TestConfig.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryQueryService inventoryQueryService;

    @Configuration
    static class TestConfig {
        @Bean
        public InventoryQueryService inventoryQueryService() {
            return Mockito.mock(InventoryQueryService.class);
        }

        @Bean
        public InventoryController inventoryController(InventoryQueryService inventoryQueryService) {
            return new InventoryController(inventoryQueryService);
        }
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(inventoryQueryService);
    }

    @DisplayName("재고 조회")
    @Test
    void getInventory() throws Exception {
        // Given.
        String productId = "prod-001";
        InventoryView inventoryView = InventoryView.builder()
                .id("inv-001")
                .productId(productId)
                .quantity(2)
                .price(50000)
                .build();
        InventoryDto inventoryDto = InventoryDto.builder()
                .id(inventoryView.getId())
                .productId(inventoryView.getProductId())
                .quantity(inventoryView.getQuantity())
                .price(inventoryView.getPrice())
                .build();
        AppObject<InventoryDto> appObject = AppObject.appObject(inventoryDto);

        // When.
        Mockito.when(inventoryQueryService.getInventory(productId)).thenReturn(inventoryView);

        // Then.
//        mockMvc.perform(
//                get("/inventory/v1/{productId}", productId)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json("{\"app\":{\"id\":\"inv-001\",\"productId\":\"prod-001\",\"quantity\":2,\"price\":50000}}"));

        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/inventory/v1/{productId}", productId)
                    .param("productId", productId)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"app\":{\"id\":\"inv-001\",\"productId\":\"prod-001\",\"quantity\":2,\"price\":50000}}"))
                .andDo(document("get-inventory",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productId").description("상품 ID")
                        ),
//                        queryParameters(
//                                parameterWithName("productId").description("상품 ID")
//                        ),
                        responseFields(
                                fieldWithPath("app").description("응답 객체"),
                                fieldWithPath("app.id").description("재고 ID"),
                                fieldWithPath("app.productId").description("상품 ID"),
                                fieldWithPath("app.quantity").description("수량"),
                                fieldWithPath("app.price").description("가격")
                        )
                ));

    }
}
