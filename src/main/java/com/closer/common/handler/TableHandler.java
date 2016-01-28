package com.closer.common.handler;

import com.closer.tenant.event.TenantCreateEvent;
import com.closer.tenant.service.TenantSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 表处理帮助类
 * Created by closer on 2016/1/5.
 */
@Component
public class TableHandler {

    @Autowired
    private List<TenantSupport> tenantSupports;

    @Autowired
    private TableCreateHandler tableCreateHandler;

    private Set<Class> entityClasses;

    @PostConstruct
    public void postConstruct() {
        entityClasses = new HashSet<>();
        for (TenantSupport tenantSupport : tenantSupports) {
            entityClasses.addAll(tenantSupport.getEntities());
        }
    }

    /**
     * 处理公司新增事件，该处理将于公司新增的保存提交后执行
     *
     * @param event 事件
     */

    @TransactionalEventListener
    public void handleCompanyCreate(TenantCreateEvent event) {
        tableCreateHandler.createTable(entityClasses, event.getTenant(),
                TableProvider.PREFIX, " IF NOT EXISTS " + TableProvider.getTablePrefix());
    }

}
