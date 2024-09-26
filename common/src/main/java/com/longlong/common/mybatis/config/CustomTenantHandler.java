package com.longlong.common.mybatis.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.longlong.common.secure.utils.SecureUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomTenantHandler implements TenantLineHandler {
    @Override
    public Expression getTenantId() {
        return new StringValue(SecureUtil.getTenantId());
    }

    @Override
    public String getTenantIdColumn() {
        return "tenant_id";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 根据需要返回是否忽略该表
        return false;
    }
}
