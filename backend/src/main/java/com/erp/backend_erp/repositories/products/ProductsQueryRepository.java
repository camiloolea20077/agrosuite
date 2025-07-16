package com.erp.backend_erp.repositories.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductsQueryRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        public Boolean existsByName(String code) {
        String sql = "SELECT COUNT(*) FROM products WHERE code = :code";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("code", code);

        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }
}
