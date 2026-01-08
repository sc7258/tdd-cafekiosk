package com.sc7258.tddcafekiosk.api;

import com.sc7258.tddcafekiosk.api.models.OrderCreateRequest;
import com.sc7258.tddcafekiosk.api.models.OrderResponse;
import com.sc7258.tddcafekiosk.api.models.ProductResponse;
import com.sc7258.tddcafekiosk.api.models.SellingStatus;
import com.sc7258.tddcafekiosk.api.models.OrderStatus;
import com.sc7258.tddcafekiosk.api.models.OrderStatusUpdateRequest; // OrderStatusUpdateRequest import 추가
import com.sc7258.tddcafekiosk.domain.order.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq; // eq import 추가
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
class OrderApiDelegateImplTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public OrderService orderService() {
            return mock(OrderService.class);
        }
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @DisplayName("신규 주문을 생성한다.")
    @Test
    void createOrder() throws Exception {
        // given
        OrderCreateRequest request = new OrderCreateRequest();
        request.setProductNumbers(Collections.singletonList("001"));

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setProductNumber("001");
        productResponse.setName("아메리카노");
        productResponse.setPrice(4000);
        productResponse.setSellingStatus(SellingStatus.SELLING);

        OrderResponse response = new OrderResponse();
        response.setId(1L);
        response.setTotalPrice(4000);
        response.setRegisteredDateTime(OffsetDateTime.now());
        response.setOrderStatus(OrderStatus.INIT);

        List<ProductResponse> products = new ArrayList<>();
        products.add(productResponse);
        response.setProducts(products);

        when(orderService.createOrder(any(OrderCreateRequest.class)))
                .thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(4000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products").isArray());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void updateOrderStatus() throws Exception {
        // given
        Long orderId = 1L;
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setOrderStatus(OrderStatus.COMPLETED);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setProductNumber("001");
        productResponse.setName("아메리카노");
        productResponse.setPrice(4000);
        productResponse.setSellingStatus(SellingStatus.SELLING);

        OrderResponse response = new OrderResponse();
        response.setId(orderId);
        response.setTotalPrice(4000);
        response.setRegisteredDateTime(OffsetDateTime.now());
        response.setOrderStatus(OrderStatus.COMPLETED); // 변경된 상태
        response.setProducts(Collections.singletonList(productResponse));

        when(orderService.updateOrderStatus(eq(orderId), any(OrderStatusUpdateRequest.class)))
                .thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/orders/{orderId}/status", orderId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(orderId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderStatus").value(OrderStatus.COMPLETED.name()));
    }
}
