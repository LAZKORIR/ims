package com.ims.mslimitservice.utils;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LogHelper {
    private final Logger logger;
//    private final Consumer<LogBuilder> initializer;
    private final List<Consumer<LogBuilder>> initializers = new ArrayList<>();

    public LogHelper(Logger logger, Consumer<LogBuilder> initializer){
        this.logger = logger;
//        this.initializer = initializer;
        this.initializers.add(initializer);
    }

    public LogBuilder build() {
        var logBuilder = new LogBuilder(logger);
//        initializer.accept(logBuilder);
        initializers.forEach(initializer -> initializer.accept(logBuilder));
        return logBuilder;
    }

    public static LogBuilder builder(Logger logger) {
        return new LogBuilder(logger);
    }

    public static LogHelper withInitializer(Logger logger, Consumer<LogBuilder> initializer) {
        return new LogHelper(logger, initializer);
    }

    public LogHelper addInitializer(Consumer<LogBuilder> initializer){
        initializers.add(initializer);
        return this;
    }

    public LogHelper transactionId(String transactionId){
        initializers.add((builder) -> builder.transactionID(transactionId));
        return this;
    }

    @Accessors(fluent = true)
    public static class LogBuilder {

        private final Logger logger;
        private StringBuilder stringBuilder;

        @Setter
        private String sourceSystem;
        @Setter
        private String sourceIP;
        @Setter
        private String operationName;
        @Setter
        private String targetSystem;
        @Setter
        private String targetEndpoint;
        @Setter
        private Supplier<String> targetEndpointFn;
        @Setter
        private String responseCode;
        @Setter
        private String logMsgType;
        @Setter
        private String logMsg;
        @Setter
        private String logDetailedMsg;
        @Setter
        private String logStatus;
        @Setter
        private String transactionID;

        private long startTime = System.currentTimeMillis();

        public LogBuilder(Logger logger) {
            this.logger = logger;
            try {
                Properties properties = PropertiesLoaderUtils
                        .loadProperties(new ClassPathResource("application.properties"));

                final DatagramSocket socket = new DatagramSocket();
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                sourceIP = socket.getLocalAddress().getHostAddress() + ":" + properties.getProperty("server.port");
                sourceSystem = properties.getProperty("spring.application.name");

            } catch (IOException e) {
                logger.error("LogMsg=Failed to load required properties for logger.");
            }
        }

        private void build() {
            stringBuilder = new StringBuilder();
            stringBuilder
                    .append("SourceSystem=").append(sourceSystem)
                    .append(" | SourceIP=").append(sourceIP);
            if (transactionID != null) stringBuilder.append(" | TransactionID=").append(transactionID);
            if (operationName != null) stringBuilder.append(" | OperationName=").append(operationName);
            if (targetSystem != null) stringBuilder.append(" | TargetSystem=").append(targetSystem);
            if (targetEndpoint != null) stringBuilder.append(" | TargetEndpoint=").append(targetEndpoint);
            if (targetEndpointFn != null) stringBuilder.append(" | TargetEndpoint=").append(targetEndpointFn.get());
            if (responseCode != null) stringBuilder.append(" | ResponseCode=").append(responseCode);
            if (logMsgType != null) stringBuilder.append(" | LogMsgtype=").append(logMsgType);
            if (logMsg != null) stringBuilder.append(" | LogMsg=").append(logMsg);
            if (logDetailedMsg != null) stringBuilder.append(" | LogDetailedMsg=").append(logDetailedMsg);
            if (logStatus != null) stringBuilder.append(" | LogStatus=").append(logStatus);
            stringBuilder.append(" | TransactionCost=").append(System.currentTimeMillis() - startTime);
        }

        public LogBuilder resetTime() {
            startTime = System.currentTimeMillis();
            return this;
        }

        public void error() {
            build();
            logger.error(StringEscapeUtils.escapeJava(stringBuilder.toString()));
            stringBuilder = null;
        }

        public void info() {
            build();
            logger.info(StringEscapeUtils.escapeJava(stringBuilder.toString()));
            stringBuilder = null;
        }

        public void warn() {
            build();
            logger.warn(StringEscapeUtils.escapeJava(stringBuilder.toString()));
            stringBuilder = null;
        }
    }

}

