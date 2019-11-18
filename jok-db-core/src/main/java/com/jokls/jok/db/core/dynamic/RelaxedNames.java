package com.jokls.jok.db.core.dynamic;

import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 12:53
 */
public final class RelaxedNames implements Iterable<String> {
    private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("([^A-Z-])([A-Z])");
    private static final Pattern SEPARATED_TO_CAMEL_CASE_PATTERN = Pattern.compile("[_\\-.]");
    private final String name;
    private final Set<String> values = new LinkedHashSet();

    public RelaxedNames(String name) {
        this.name = name != null ? name : "";
        this.initialize(this.name, this.values);
    }

    public Iterator<String> iterator() {
        return this.values.iterator();
    }

    private void initialize(String name, Set<String> values) {
        if (!values.contains(name)) {
            RelaxedNames.Variation[] variations = RelaxedNames.Variation.values();

            for (Variation variation : variations) {
                Manipulation[] manipulations = Manipulation.values();

                for (Manipulation manipulation : manipulations) {
                    String result = manipulation.apply(name);
                    result = variation.apply(result);
                    values.add(result);
                    this.initialize(result, values);
                }
            }

        }
    }

    public static RelaxedNames forCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        char[] chars = name.toCharArray();

        for (char c : chars) {
            result.append(Character.isUpperCase(c) && result.length() > 0 && result.charAt(result.length() - 1) != '-' ? "-" + Character.toLowerCase(c) : c);
        }

        return new RelaxedNames(result.toString());
    }

    static enum Manipulation {
        NONE {
            public String apply(String value) {
                return value;
            }
        },
        HYPHEN_TO_UNDERSCORE {
            public String apply(String value) {
                return value.indexOf(45) != -1 ? value.replace('-', '_') : value;
            }
        },
        UNDERSCORE_TO_PERIOD {
            public String apply(String value) {
                return value.indexOf(95) != -1 ? value.replace('_', '.') : value;
            }
        },
        PERIOD_TO_UNDERSCORE {
            public String apply(String value) {
                return value.indexOf(46) != -1 ? value.replace('.', '_') : value;
            }
        },
        CAMELCASE_TO_UNDERSCORE {
            public String apply(String value) {
                if (value.isEmpty()) {
                    return value;
                } else {
                    Matcher matcher = RelaxedNames.CAMEL_CASE_PATTERN.matcher(value);
                    if (!matcher.find()) {
                        return value;
                    } else {
                        matcher = matcher.reset();
                        StringBuffer result = new StringBuffer();

                        while (matcher.find()) {
                            matcher.appendReplacement(result, matcher.group(1) + '_' + StringUtils.uncapitalize(matcher.group(2)));
                        }

                        matcher.appendTail(result);
                        return result.toString();
                    }
                }
            }
        },
        CAMELCASE_TO_HYPHEN {
            public String apply(String value) {
                if (value.isEmpty()) {
                    return value;
                } else {
                    Matcher matcher = RelaxedNames.CAMEL_CASE_PATTERN.matcher(value);
                    if (!matcher.find()) {
                        return value;
                    } else {
                        matcher = matcher.reset();
                        StringBuffer result = new StringBuffer();

                        while (matcher.find()) {
                            matcher.appendReplacement(result, matcher.group(1) + '-' + StringUtils.uncapitalize(matcher.group(2)));
                        }

                        matcher.appendTail(result);
                        return result.toString();
                    }
                }
            }
        },
        SEPARATED_TO_CAMELCASE {
            public String apply(String value) {
                return RelaxedNames.Manipulation.separatedToCamelCase(value, false);
            }
        },
        CASE_INSENSITIVE_SEPARATED_TO_CAMELCASE {
            public String apply(String value) {
                return RelaxedNames.Manipulation.separatedToCamelCase(value, true);
            }
        };

        private static final char[] SUFFIXES = new char[]{'_', '-', '.'};

        private Manipulation() {
        }

        public abstract String apply(String value);

        private static String separatedToCamelCase(String value, boolean caseInsensitive) {
            if (value.isEmpty()) {
                return value;
            } else {
                StringBuilder builder = new StringBuilder();
                String[] relaxNames = RelaxedNames.SEPARATED_TO_CAMEL_CASE_PATTERN.split(value);

                for (String relaxName : relaxNames) {
                    String field = relaxName;
                    field = caseInsensitive ? field.toLowerCase(Locale.ENGLISH) : field;
                    builder.append(builder.length() != 0 ? StringUtils.capitalize(field) : field);
                }

                char lastChar = value.charAt(value.length() - 1);
                for (char suffix : SUFFIXES) {
                    if (lastChar == suffix) {
                        builder.append(suffix);
                        break;
                    }
                }

                return builder.toString();
            }
        }
    }

    static enum Variation {
        NONE {
            public String apply(String value) {
                return value;
            }
        },
        LOWERCASE {
            public String apply(String value) {
                return value.isEmpty() ? value : value.toLowerCase(Locale.ENGLISH);
            }
        },
        UPPERCASE {
            public String apply(String value) {
                return value.isEmpty() ? value : value.toUpperCase(Locale.ENGLISH);
            }
        };

        private Variation() {
        }

        public abstract String apply(String value);
    }
}
