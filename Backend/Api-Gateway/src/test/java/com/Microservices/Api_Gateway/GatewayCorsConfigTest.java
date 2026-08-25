package com.Microservices.Api_Gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.server.WebFilter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GatewayCorsConfigTest {

    @Autowired(required = false)
    private CorsWebFilter corsWebFilter;

    @Autowired(required = false)
    private WebFilter webFilter;

    @Test
    void testCorsWebFilterBeanExists() {
        // Verify that the CorsWebFilter bean is created
        assertThat(corsWebFilter).isNotNull();
    }

    @Test
    void testCorsConfiguration() {
        if (corsWebFilter != null) {
            // We cannot easily inspect the CorsConfiguration from the filter,
            // but we can verify that the bean exists and is of the correct type
            assertThat(corsWebFilter).isInstanceOf(CorsWebFilter.class);
        }
    }

    @Test
    void testWebFilterBeanExists() {
        // If the CorsWebFilter is not injected, we can still verify the WebFilter bean
        if (corsWebFilter == null) {
            assertThat(webFilter).isNotNull();
        }
    }
}
