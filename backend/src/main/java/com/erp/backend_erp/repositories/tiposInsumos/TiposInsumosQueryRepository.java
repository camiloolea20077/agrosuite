package com.erp.backend_erp.repositories.tiposInsumos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.insumos.TiposInsumosElementsDto;

@Repository
public class TiposInsumosQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TiposInsumosQueryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean existsByCodigo(String codigo) {
        String sql = "SELECT COUNT(1) > 0 FROM tipos_insumos WHERE LOWER(codigo) = :codigo";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("codigo", codigo);
        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    public List<TiposInsumosElementsDto> getTiposInsumos() {
        String sql = "SELECT id, codigo, nombre FROM tipos_insumos WHERE activo = 1 ORDER BY nombre";
        
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), new RowMapper<TiposInsumosElementsDto>() {
            public TiposInsumosElementsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                TiposInsumosElementsDto dto = new TiposInsumosElementsDto();
                dto.setId(rs.getLong("id"));
                dto.setCodigo(rs.getString("codigo"));
                dto.setNombre(rs.getString("nombre"));
                return dto;
            }
        });
    }
}