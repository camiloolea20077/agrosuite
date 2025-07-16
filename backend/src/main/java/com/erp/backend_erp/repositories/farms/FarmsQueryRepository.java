package com.erp.backend_erp.repositories.farms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.listElements.FarmsElementsDto;

@Repository
public class FarmsQueryRepository {
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Boolean existsFarmById(Long farmId) {
        String sql = "SELECT COUNT(*) FROM farms WHERE id = :id AND deleted_at IS NULL";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", farmId);

        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }

    public List<FarmsElementsDto> getFarms() {
    String sql = "SELECT id, nombre FROM farms";

    // Usamos un RowMapper para mapear los resultados a FarmsElementsDto
    return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(), new RowMapper<FarmsElementsDto>() {
        @Override
        public FarmsElementsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            FarmsElementsDto dto = new FarmsElementsDto();
            dto.setId(rs.getLong("id"));
            dto.setNombre(rs.getString("nombre"));
            return dto;
        }
    });
}


}
