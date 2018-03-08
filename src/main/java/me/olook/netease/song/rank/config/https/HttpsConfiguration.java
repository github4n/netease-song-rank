package me.olook.netease.song.rank.config.https;

import io.undertow.Undertow;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
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
@EnableConfigurationProperties(HttpProperties.class)
public class HttpsConfiguration {

    private Logger log = LoggerFactory.getLogger(HttpsConfiguration.class);

    @Autowired
    private HttpProperties httpProperties;

    @Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory undertow = new UndertowEmbeddedServletContainerFactory();
        undertow.addBuilderCustomizers((Undertow.Builder builder) -> {
            builder.addHttpListener(httpProperties.getPort(), "0.0.0.0");
        });
        log.info("\n*** Undertow http setting successful." + httpProperties.getPort());
        undertow.addDeploymentInfoCustomizers(deploymentInfo -> {
            deploymentInfo.addSecurityConstraint(new SecurityConstraint()
                    .addWebResourceCollection(new WebResourceCollection()
                            .addUrlPattern("/*")) .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                    .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
                    .setConfidentialPortManager(exchange -> 443);
        });
        return undertow;
    }
}