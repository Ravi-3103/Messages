package com.app.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackageClasses = SecurityCommonAutoConfiguration.class)
public class SecurityCommonAutoConfiguration {
}
