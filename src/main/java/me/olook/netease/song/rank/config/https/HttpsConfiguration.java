package me.olook.netease.song.rank.config.https;

import io.undertow.Undertow;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaohw
 * @date 2018-03-05 0:10
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(HttpsProperties.class)
public class HttpsConfiguration {

    private Logger log = LoggerFactory.getLogger(HttpsConfiguration.class);

    @Autowired
    private HttpsProperties properties;

    @Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory undertow = new UndertowEmbeddedServletContainerFactory();
        undertow.addBuilderCustomizers((Undertow.Builder builder) -> {
            builder.addHttpListener(properties.getPort(), "0.0.0.0");
        });
        log.info("\n*** Undertow http setting successful." + properties.getPort());
        return undertow;
    }
}