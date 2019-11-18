package com.jokls.jok.db.core.dynamic;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 12:52
 */
public class RelaxedPropertyResolver implements PropertyResolver {

    private final PropertyResolver resolver;
    private final String prefix;

    public RelaxedPropertyResolver(PropertyResolver resolver) {
        this(resolver, null);
    }

    public RelaxedPropertyResolver(PropertyResolver resolver, String prefix) {
        this.resolver = resolver;
        this.prefix = prefix != null ? prefix : "";
    }

    public String getRequiredProperty(String key) throws IllegalStateException {
        return this.getRequiredProperty(key, String.class);
    }

    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        T value = this.getProperty(key, targetType);
        Assert.state(value != null, String.format("required key [%s] not found", key));
        return value;
    }

    public String getProperty(String key) {
        return this.getProperty(key, String.class, null);
    }

    public String getProperty(String key, String defaultValue) {
        return this.getProperty(key, String.class, defaultValue);
    }

    public <T> T getProperty(String key, Class<T> targetType) {
        return this.getProperty(key, targetType, null);
    }

    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        RelaxedNames prefixes = new RelaxedNames(this.prefix);
        RelaxedNames keys = new RelaxedNames(key);

        for (String prefix : prefixes) {
            for (String relaxedKey : keys) {
                if (this.resolver.containsProperty(prefix + relaxedKey)) {
                    return this.resolver.getProperty(prefix + relaxedKey, targetType);
                }
            }
        }

        return defaultValue;
    }

 

    public boolean containsProperty(String key) {
        RelaxedNames prefixes = new RelaxedNames(this.prefix);
        RelaxedNames keys = new RelaxedNames(key);

        for (String prefix : prefixes) {

            for (String relaxedKey : keys) {
                if (this.resolver.containsProperty(prefix + relaxedKey)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String resolvePlaceholders(String text) {
        throw new UnsupportedOperationException("Unable to resolve placeholders with relaxed properties");
    }

    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Unable to resolve placeholders with relaxed properties");
    }

    public Map<String, Object> getSubProperties(String keyPrefix) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, this.resolver, "SubProperties not available.");
        ConfigurableEnvironment env = (ConfigurableEnvironment)this.resolver;
        return PropertySourceUtils.getSubProperties(env.getPropertySources(), this.prefix, keyPrefix);
    }

    public static RelaxedPropertyResolver ignoringUnresolvableNestedPlaceholders(Environment environment, String prefix) {
        Assert.notNull(environment, "Environment must not be null");
        PropertyResolver resolver = environment;
        if (environment instanceof ConfigurableEnvironment) {
            resolver = new PropertySourcesPropertyResolver(((ConfigurableEnvironment)environment).getPropertySources());
            ((PropertySourcesPropertyResolver)resolver).setIgnoreUnresolvableNestedPlaceholders(true);
        }

        return new RelaxedPropertyResolver(resolver, prefix);
    }
}
