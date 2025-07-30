package com.erp.backend_erp.repositories.terceros;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.terceros.AutoCompleteDto;
import com.erp.backend_erp.dto.terceros.AutoCompleteModelDto;
import com.erp.backend_erp.util.MapperRepository;

@Repository
public class TercerosQueryRepository {
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public Boolean existsTerceroByNumeroIdentificacion(String numeroIdentificacion) {
        String sql = "SELECT COUNT(*) FROM terceros WHERE numero_identificacion = :numeroIdentificacion";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("numeroIdentificacion", numeroIdentificacion);

        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }
    public List<AutoCompleteModelDto> autoCompleteTerceros(AutoCompleteDto<Object> autoCompleteDto, Long farmId) {
        String search = autoCompleteDto.getSearch() != null ? autoCompleteDto.getSearch().trim() : "";
        String likeSearch = "%" + search + "%";

        String sql = """
            SELECT
                id,
                numero_identificacion AS "numeroIdentificacion",
                tipo_identificacion AS "tipoIdentificacion",
                nombre_razon_social AS "nombreRazonSocial",
                telefono,
                direccion
            FROM
                terceros
            WHERE
                farm_id = :farmId
                AND numero_identificacion ILIKE :search
            LIMIT 10
        """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("search", likeSearch);
        params.addValue("farmId", farmId);

        List<Map<String, Object>> resultList = namedParameterJdbcTemplate.query(sql, params, new ColumnMapRowMapper());

        return MapperRepository.mapListToDtoList(resultList, AutoCompleteModelDto.class);
    }



}
