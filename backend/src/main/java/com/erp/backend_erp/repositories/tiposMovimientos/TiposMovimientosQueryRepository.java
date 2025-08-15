package com.erp.backend_erp.repositories.tiposMovimientos;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TiposMovimientosQueryRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public TiposMovimientosQueryRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Boolean existsByCodigo(String codigo) {
        String sql = "SELECT COUNT(1) > 0 FROM tipos_movimientos WHERE LOWER(codigo) = :codigo";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("codigo", codigo);
        return jdbc.queryForObject(sql, params, Boolean.class);
    }

    // Si más adelante agregas paginación:
    // public PageImpl<TiposMovimientosTableDto> listTiposMovimientos(PageableDto<Object> pageableDto) { ... }
}