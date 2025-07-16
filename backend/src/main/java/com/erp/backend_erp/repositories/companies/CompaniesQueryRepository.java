package com.erp.backend_erp.repositories.companies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CompaniesQueryRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public Boolean existsByNit(String nit) {
        String sql = "SELECT COUNT(*) FROM companies WHERE nit = :nit";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("nit", nit);

        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }
}
