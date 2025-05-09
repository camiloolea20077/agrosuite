package com.erp.backend_erp.repositories.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SupplierQueryRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    public boolean existsByIdAndCompanyId(Long supplierId, Long companyId) {
    String sql = "SELECT COUNT(*) FROM suppliers WHERE id = :supplierId AND company_id = :companyId";
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("supplierId", supplierId);
    params.addValue("companyId", companyId);
    Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    return count != null && count > 0;
}
}
