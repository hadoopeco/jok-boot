package com.jokls.jok.common.code;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 16:40
 */
public class ErrorCode {
    public static final int UNKNOWN_EXCEPTION = 2147483647;
    public static final int BIZ_EXCEPTION = -1;
    public static final int NETWORK_EXCEPTION = -2;
    public static final int TIMEOUT_EXCEPTION = -3;
    public static final int FORBIDDEN_EXCEPTION = -4;
    public static final int SERIALIZATION_EXCEPTION = -5;
    public static final int CONFIG_EXCEPTION = -6;

    public ErrorCode() {
    }

    public class FILE {
        public static final int DEF_EXCEPTION = 2700;
        public static final int FILE_ALREADY_EXISTS = 2701;
        public static final int FILE_NOT_EXISTS = 2702;

        public FILE() {
        }
    }

    public class RPC {
        public static final int DEF_EXCEPTION = 2600;
        public static final int RESPONSE_ISNULL = 2601;
        public static final int RESPONSE_ISERROR = 2602;
        public static final int CALLBACK_INSTANCE_ERROR = 2603;
        public static final int FILTER_INSTANCE_ERROR = 2604;
        public static final int FALLBACK_INSTANCE_ERROR = 2605;
        public static final int BAGGAGE_KEY_ISNULL = 2606;
        public static final int BAGGAGE_KEY_ERROR = 2607;
        public static final int CHANNAL_EXCEPTION = 2608;
        public static final int CONTEXT_SEND_EXCEPTION = 2609;
        public static final int PARAMETER_VALIDATION_EXCEPTION = 2610;

        public RPC() {
        }
    }

    public class MQ {
        public static final int DEF_EXCEPTION = 2400;
        public static final int CONFIG_EXCEPTION = 2401;
        public static final int NETWORK_EXCEPTION = 2402;
        public static final int PRODUCER_UNKNOWN_EXCEPTION = 2403;
        public static final int FORBIDDEN_EXCEPTION = 2404;
        public static final int SERIALIZATION_EXCEPTION = 2405;
        public static final int CONSUMER_UNKNOWN_EXCEPTION = 2406;
        public static final int PARARM_ERROR_EXCEPTION = 2407;
        public static final int INSTANCEID_UNKNOWN_EXCEPTION = 2408;
        public static final int INTERCEPTOR_EXCEPTION = 2409;

        public MQ() {
        }
    }

    public class SCHEDULER {
        public static final int DEF_EXCEPTION = 2300;
        public static final int TARGET_ERROR = 2301;
        public static final int EXECUTE_FAILURE = 2302;

        public SCHEDULER() {
        }
    }

    public class CACHE {
        public static final int DEF_EXCEPTION = 2200;
        public static final int KEY_ALREADY_EXISTS = 2201;
        public static final int KEY_NOT_EXISTS = 2202;

        public CACHE() {
        }
    }

    public class DB {
        public static final int DEF_EXCEPTION = 2000;
        public static final int DS_NOT_FOUND = 2001;
        public static final int DS_ACCESS_ERROR = 2003;
        public static final int DS_ACCESS_ERROR_URL_NULL = 2004;
        public static final int DS_ACCESS_ERROR_USERNAME_NULL = 2005;
        public static final int DS_ACCESS_ERROR_PASSWORD_NULL = 2006;

        public DB() {
        }
    }
}
