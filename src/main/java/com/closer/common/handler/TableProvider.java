package com.closer.common.handler;

import com.closer.tenant.domain.Tenant;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
public class TableProvider {

    private static final String DEFAULT_PREFIX = "default";
    public static final String PREFIX = "#org#";
    public static final String PREFIX_ = PREFIX + "_";
    private static ThreadLocal<Tenant> tenantThreadLocal = new ThreadLocal<>();

    public static void setTenant(Tenant tenant) {
        tenantThreadLocal.set(tenant);
    }

    public static String getTablePrefix() {
        Tenant tenant = tenantThreadLocal.get();
        if (tenant == null) {
            return "";
        }
        return StringUtils.defaultString("T"+String.valueOf(getTableNum()), DEFAULT_PREFIX);
    }

    public static long getTableNum() {
        return getTenantId() & 7;
    }

    public static long getTenantId() {
        Tenant tenant = tenantThreadLocal.get();
        if (tenant == null) {
            throw new RuntimeException("未标识您请求的是租户id");
        }
        return tenant.getId();
    }

    public static String getDataSoureName() {
        Tenant tenant = tenantThreadLocal.get();
        if (tenant == null) {
            return "";
        }
        return tenant.getDataSourceName();
    }

    public static void clear() {
        tenantThreadLocal.remove();
    }
}
