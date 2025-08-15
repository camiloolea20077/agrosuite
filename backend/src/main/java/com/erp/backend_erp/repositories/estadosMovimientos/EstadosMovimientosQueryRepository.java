package com.erp.backend_erp.repositories.estadosMovimientos;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EstadosMovimientosQueryRepository {
    private final NamedParameterJdbcTemplate jdbc;
    public EstadosMovimientosQueryRepository(NamedParameterJdbcTemplate jdbc) { this.jdbc = jdbc; }

    public Boolean existsByCodigo(String codigoLower) {
        String sql = "SELECT COUNT(1) > 0 FROM estados_movimientos WHERE LOWER(codigo) = :codigo";
        MapSqlParameterSource p = new MapSqlParameterSource().addValue("codigo", codigoLower);
        return jdbc.queryForObject(sql, p, Boolean.class);
    }
}