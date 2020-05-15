package net.chensee.exception.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "sys")
@PropertySource(value = "classpath:sysExceptionConfig.yml", encoding = "UTF-8")
public class SysExceptionEntity {

    private String otherException;

    private Map<String, String> customException = new HashMap<>();
}
