package uk.co.huntersix.spring.rest.controller;

import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    void testTimeout() {
        ServletWebServerApplicationContext context = (ServletWebServerApplicationContext) this.ctx;
        TomcatWebServer embeddedServletContainer = (TomcatWebServer) context.getWebServer();
        ProtocolHandler protocolHandler = embeddedServletContainer.getTomcat().getConnector().getProtocolHandler();
        int timeout = ((AbstractProtocol<?>) protocolHandler).getConnectionTimeout();
        assertThat(timeout).isEqualTo(5000);
        //default value is 60000 updated is as 5000 in properteis file
    }
}