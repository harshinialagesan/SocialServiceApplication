package com.ssa.config.Logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
//@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private String logFilePath;
    private static final String START_TIME_ATTRIBUTE = "startTime";
    private File logFile;
    private static final ThreadLocal<Exception> exceptionThreadLocal = new ThreadLocal<>();

    public static void setException(Exception exception) {
        exceptionThreadLocal.set(exception);
    }

    LoggingInterceptor(String logFilePath) {
        this.logFilePath = logFilePath;
        this.logFile = new File(this.logFilePath);
        if (!this.logFile.exists()) {
            this.createLogFile();
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Skip logging for SpringBoot default endpoints
        String requestPath = request.getRequestURI();
        if (requestPath.startsWith("/actuator") || "/error".equals(requestPath) || "/info".equals(requestPath)) {
            return false;
        }
        logRequestDetails(request);
//        log.info("Log file path : {}\n \n ", logFilePaths);
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logResponseDetails(request, response, ex);
    }

    private void logRequestDetails(HttpServletRequest request) {
        JSONObject logDetails = generalLogData(request);
        String requestPath = request.getRequestURI();
        String requestMethod = request.getMethod();
        String message = String.format("Started %s %s at %s", requestPath, requestMethod, getReadableTimeStamp());
        logDetails.put("message", message);
        writeLog(logDetails);
    }

    private void logResponseDetails(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        BigDecimal startTime = new BigDecimal((Long) request.getAttribute(START_TIME_ATTRIBUTE));
        BigDecimal endTime = new BigDecimal(System.currentTimeMillis());
        String responseTime = endTime.subtract(startTime).toString();
        int status = response.getStatus();
        boolean isErrorResponse = status >= 400 && status < 600;
        JSONObject logDetails = generalLogData(request);
        String requestPath = request.getRequestURI();
        String requestMethod = request.getMethod();
        String message = String.format("%s %s %s at %s", isErrorResponse ? "Error" : "Completed", requestPath, requestMethod, getReadableTimeStamp());
        logDetails.put("message", message);
        logDetails.put("responseTime", responseTime);
        logDetails.put("http.response.status_code", status);
        JSONObject payload = new JSONObject();

        try {
            String requestBodyString = getRequestBody(request);
            if (!requestBodyString.isBlank()) {
                // logging payload if error happens
                payload = new JSONObject(requestBodyString);
            }
            logDetails.put("payload", payload);

        } catch (IOException ex) {
            logDetails.put("payload", payload);
        }
        if (isErrorResponse) {
            logDetails.put("log.level", "error");
            Exception storedException = exceptionThreadLocal.get();
            logDetails.put("error.message", storedException.getLocalizedMessage());
            logDetails.put("error.stack_trace", storedException.getStackTrace());
            exceptionThreadLocal.remove();
        }
        writeLog(logDetails);
    }

    private JSONObject generalLogData(HttpServletRequest request) {
        JSONObject logDetails = new JSONObject();
        String userAgent = request.getHeader("User-Agent");
        logDetails.put("@timestamp", Instant.now().toString());
        logDetails.put("log.level", "info");
        logDetails.put("service", "Master");
        logDetails.put("agent", userAgent);
        logDetails.put("url.path", request.getRequestURI());
        logDetails.put("url.full", request.getRequestURL().toString());
        logDetails.put("url.domain", request.getServerName());
        logDetails.put("url.query", request.getQueryString());
        logDetails.put("url.scheme", request.getScheme());
        logDetails.put("http.request.method", request.getMethod());
        return logDetails;
    }


    private void createLogFile() {
        try {
            // Create a new file
            if (logFile.createNewFile()) {
                log.info("Log file created successfully: {}", logFilePath);
            } else {
                log.warn("Failed to create the Log file.");
            }
        } catch (Exception e) {
            log.error("An error occurred while creating the Log file :{}", e.getMessage(), e.getCause());
        }
    }

    private void writeLog(JSONObject logData) {
        try {
            try (FileWriter file = new FileWriter(logFilePath, true)) {
                file.write(logData.toString() + "\n");
            }
        } catch (Exception e) {
            log.error("An error occurred while inserting log data :{}", e.getMessage(), e.getCause());
        }
    }

    private String getReadableTimeStamp() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        // Create a DateTimeFormatter instance with the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'XXX (z)");
        return zonedDateTime.format(formatter);
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            char[] charBuffer = new char[128];
            int bytesRead;
            while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        }
        return stringBuilder.toString();
    }
}