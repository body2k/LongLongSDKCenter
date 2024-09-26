
package com.longlong.common.secure.provider;

import lombok.AllArgsConstructor;
import com.longlong.common.secure.constant.SecureConstant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 获取客户端详情
 */
@AllArgsConstructor
public class ClientDetailsServiceImpl implements IClientDetailsService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public IClientDetails loadClientByClientId(String clientId) {
        try {
            return jdbcTemplate.queryForObject(SecureConstant.DEFAULT_SELECT_STATEMENT, new String[]{clientId}, new BeanPropertyRowMapper<>(ClientDetails.class));
        } catch (Exception ex) {

            throw new RuntimeException("查询失败" + SecureConstant.DEFAULT_SELECT_STATEMENT + "条件" + clientId + "不存在");
        }
    }

    @Override
    public IClientDetails loadClientByClientId(String clientId, String tenantId) {
        try {
            return jdbcTemplate.queryForObject(SecureConstant.DEFAULT_SELECT_STATEMENT_tenantId, new String[]{clientId,tenantId}, new BeanPropertyRowMapper<>(ClientDetails.class));
        } catch (Exception ex) {

            throw new RuntimeException("查询失败" + SecureConstant.DEFAULT_SELECT_STATEMENT_tenantId + "条件" + clientId +"和"+tenantId+ "不存在");
        }
    }

}
