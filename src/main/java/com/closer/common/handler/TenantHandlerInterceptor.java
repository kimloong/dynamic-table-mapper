package com.closer.common.handler;

import com.closer.tenant.domain.Tenant;
import com.closer.tenant.service.TenantService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by closer on 2016/1/27.
 */
public class TenantHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private TenantService tenantService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().startsWith("/tenants")) {
            return true;
        }
        String tenantIdStr = request.getHeader("tenant");
        if (StringUtils.isBlank(tenantIdStr) || !NumberUtils.isNumber(tenantIdStr)) {
            throw new RuntimeException("请求头部需要包含有租户的id，类型为整型");
        }
        long tenantId = NumberUtils.toLong(tenantIdStr);
        Tenant tenant = tenantService.findOne(tenantId);
        if (tenant == null) {
            throw new RuntimeException("指定的租户不存在");
        }
        TableProvider.setTenant(tenant);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TableProvider.clear();
    }
}