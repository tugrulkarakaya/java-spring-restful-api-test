package uk.co.huntersix.spring.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SmokeTest {

    @Autowired
    private PersonController controller;

    @Autowired
    ApplicationContext ctx;

    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("Contex Loaded")
    public void checkPersonController(){
        assertThat(this.ctx).isNotNull();
        assertThat(this.ctx.containsBean("personDataService")).isTrue();
        assertThat(this.ctx.containsBean("personController")).isTrue();
        assertThat(this.ctx.containsBean("person")).isTrue();
    }
}