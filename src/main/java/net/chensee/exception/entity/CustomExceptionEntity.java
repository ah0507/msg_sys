package net.chensee.exception.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "custom")
@PropertySource(value = "classpath:customExceptionConfig.yml", encoding = "UTF-8")
public class CustomExceptionEntity {

    private Map<String, String> exception = new HashMap<>();
}
