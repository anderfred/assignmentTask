package com.anderfred.assignmenttask.controller.generic;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class BaseController {
    @SuppressWarnings("ConstantConditions")
    protected String fetchClientIpAddr() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        Assert.isTrue(ip.chars().filter($ -> $ == '.').count() == 3, "Illegal IP: " + ip);
        return ip;
    }

    protected void logDebug(Logger log, String message) {
        MDC.put("request_ip", fetchClientIpAddr());
        log.debug(message);
        MDC.clear();
    }
    protected void logError(Logger log, String message) {
        MDC.put("request_ip", fetchClientIpAddr());
        log.error(message);
        MDC.clear();
    }
}
