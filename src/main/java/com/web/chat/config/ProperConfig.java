package com.web.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProperConfig {
    @Value("${test.name}")
    public String properName;
}
