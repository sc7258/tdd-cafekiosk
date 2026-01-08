package com.sc7258.tddcafekiosk.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc7258.tddcafekiosk.api.models.ProductCreateRequest;
import com.sc7258.tddcafekiosk.api.models.SellingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductApiDelegateImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest()
                .productNumber("001")
                .name("아메리카노")
                .price(4000)
                .sellingStatus(SellingStatus.SELLING);

        // when // then
        mockMvc.perform(
                        post("/api/v1/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productNumber").value("001"))
                .andExpect(jsonPath("$.name").value("아메리카노"))
                .andExpect(jsonPath("$.price").value(4000))
                .andExpect(jsonPath("$.sellingStatus").value("SELLING"));
    }

    @DisplayName("판매중인 상품 목록을 조회한다.")
    @Test
    void getSellingProducts() throws Exception {
        // given
        // Note: For now, we only check if the response format is an array.
        // Data setup and verification will be done in later stages.

        // when // then
        mockMvc.perform(
                        get("/api/v1/products/selling")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
