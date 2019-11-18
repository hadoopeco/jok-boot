package com.jokls.jok.common.trace;

import com.jokls.jok.common.util.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 15:46
 */
public class TraceInfo {
        private String traceId;
        private String spanId;
        private String depotId;
        private String moduleType;
        private String timestamp;
        private String name;
        private String serverName;
        private String appType;
        private String ip;
        private String responseStatus;
        private String version;
        private String spanType;
        private String charset;
        private String myPackage;
        private AtomicInteger spanNumber = new AtomicInteger(0);

        public TraceInfo() {
        }

        public String getTraceId() {
            return this.traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getSpanId() {
            return this.spanId;
        }

        public void setSpanId(String spanId) {
            this.spanId = spanId;
        }

        public String getDepotId() {
            return this.depotId;
        }

        public void setDepotId(String depotId) {
            this.depotId = depotId;
        }

        public String getModuleType() {
            return this.moduleType;
        }

        public void setModuleType(String moduleType) {
            this.moduleType = moduleType;
        }

        public String getTimestamp() {
            return this.timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getServerName() {
            return this.serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getAppType() {
            return this.appType;
        }

        public void setAppType(String appType) {
            this.appType = appType;
        }

        public String getIp() {
            return this.ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getResponseStatus() {
            return this.responseStatus;
        }

        public void setResponseStatus(String responseStatus) {
            this.responseStatus = responseStatus;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getSpanType() {
            return this.spanType;
        }

        public void setSpanType(String spanType) {
            this.spanType = spanType;
        }

        public String getCharset() {
            return this.charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public String getMyPackage() {
            return this.myPackage;
        }

        public void setMyPackage(String myPackage) {
            this.myPackage = myPackage;
        }

        public AtomicInteger getSpanNumber() {
            return this.spanNumber;
        }

        public void setSpanNumber(AtomicInteger spanNumber) {
            this.spanNumber = spanNumber;
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("{");
            buffer.append("\"info\":[");
            buffer.append("\"");
            buffer.append(null == this.traceId ? "" : this.traceId);
            buffer.append("\",");
            buffer.append("\"");
            buffer.append(null == this.spanId ? "" : this.spanId);
            buffer.append("\",");
            buffer.append("\"");
            buffer.append(null == this.depotId ? "" : this.depotId);
            buffer.append("\",");
            buffer.append("\"");
            buffer.append(null == this.moduleType ? "" : this.moduleType);
            buffer.append("\",");
            buffer.append("\"");
            buffer.append(null == this.timestamp ? "" : this.timestamp);
            buffer.append("\",");
            buffer.append("\"");
            buffer.append(null == this.name ? "" : this.name);
            buffer.append("\",");
            buffer.append("\"");
            buffer.append(null == this.ip ? "" : this.ip);
            buffer.append("\",");
            buffer.append("\"");
            buffer.append(null == this.responseStatus ? "" : this.responseStatus);
            buffer.append("\",");
            buffer.append("\"");
            buffer.append(null == this.version ? "" : this.version);
            buffer.append("\",");
            buffer.append("\"");
            buffer.append(null == this.spanType ? "" : this.spanType);
            buffer.append("\",");
            buffer.append("\"");
            buffer.append(null == this.serverName ? "" : this.serverName);
            buffer.append("\",");
            buffer.append("\"");
            buffer.append(null == this.appType ? "" : this.appType);
            buffer.append("\",");
            buffer.append("\"");
            buffer.append(null == this.charset ? "" : this.charset);
            buffer.append("\"");
            buffer.append("]");
            if (!StringUtils.isEmpty(this.myPackage)) {
                buffer.append(",\"myPackage\":");
                buffer.append(this.myPackage);
            }

            buffer.append("}");
            return buffer.toString();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof TraceInfo)) {
                return false;
            } else {
                TraceInfo that = (TraceInfo)o;
                return null != this.traceId && null != that.traceId && this.traceId.equals(that.traceId) && null != this.spanId && null != that.spanId && this.spanId.equals(that.spanId) && null != this.depotId && null != that.depotId && this.depotId.equals(that.depotId);
            }
        }

        public int atomicSpan() {
            return this.spanNumber.addAndGet(1);
        }
}
