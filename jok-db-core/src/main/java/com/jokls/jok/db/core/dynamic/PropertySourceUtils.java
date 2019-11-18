package com.jokls.jok.db.core.dynamic;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 12:58
 */
public class PropertySourceUtils {
    public PropertySourceUtils() {
    }

    public static Map<String, Object> getSubProperties(PropertySources propertySources, String keyPrefix) {
        return getSubProperties(propertySources, null, keyPrefix);
    }

    public static Map<String, Object> getSubProperties(PropertySources propertySources, String rootPrefix, String keyPrefix) {
        RelaxedNames keyPrefixes = new RelaxedNames(keyPrefix);
        Map<String, Object> subProperties = new LinkedHashMap<>();
        Iterator propertySource = propertySources.iterator();

        while(true) {
            PropertySource source;
            do {
                if (!propertySource.hasNext()) {
                    return Collections.unmodifiableMap(subProperties);
                }

                source = (PropertySource)propertySource.next();
            } while(!(source instanceof EnumerablePropertySource));

            String[] propertyNames = ((EnumerablePropertySource)source).getPropertyNames();

            for (String name : propertyNames) {
                String key = getSubKey(name, rootPrefix, keyPrefixes);
                if (key != null && !subProperties.containsKey(key)) {
                    subProperties.put(key, source.getProperty(name));
                }
            }
        }
    }

    private static String getSubKey(String name, String rootPrefixes, RelaxedNames keyPrefix) {
        rootPrefixes = rootPrefixes != null ? rootPrefixes : "";

        for (String rootPrefix : new RelaxedNames(rootPrefixes)) {

            for (String candidateKeyPrefix : keyPrefix) {
                if (name.startsWith(rootPrefix + candidateKeyPrefix)) {
                    return name.substring((rootPrefix + candidateKeyPrefix).length());
                }
            }
        }

        return null;
    }
}
