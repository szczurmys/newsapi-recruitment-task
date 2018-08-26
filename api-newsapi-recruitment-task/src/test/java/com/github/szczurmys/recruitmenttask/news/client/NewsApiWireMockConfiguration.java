package com.github.szczurmys.recruitmenttask.news.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Configuration
public class NewsApiWireMockConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer createMockServer() {
        return new WireMockServer(options().dynamicPort());
    }

    @Bean
    @Primary
    public NewsApiConfiguration createConfiguration(WireMockServer server) {
        NewsApiConfiguration conf = new NewsApiConfiguration();
        conf.setNewsApiUrl("http://localhost:" + server.port());
        conf.setAuthToken("test-token");
        return conf;
    }
}
