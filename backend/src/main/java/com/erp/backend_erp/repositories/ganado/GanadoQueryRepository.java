package com.erp.backend_erp.repositories.ganado;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.ganado.GanadoTableDto;
import com.erp.backend_erp.dto.listElements.CattleElementsDto;
import com.erp.backend_erp.util.MapperRepository;
import com.erp.backend_erp.util.PageableDto;

@Repository
public class GanadoQueryRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Boolean existCattle(String numeroGanado) {
        String sql = "SELECT COUNT(*) FROM cattle WHERE numero_ganado = :numeroGanado";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("numeroGanado", numeroGanado);
        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }
    public PageImpl<GanadoTableDto> listCattle(PageableDto<Object> pageableDto) {
        int pageNumber = pageableDto.getPage() != null ? pageableDto.getPage().intValue() : 0;
        int pageSize = pageableDto.getRows() != null ? pageableDto.getRows().intValue() : 10;
        String search = pageableDto.getSearch() != null ? pageableDto.getSearch().trim() : null;

        String sql = """
                SELECT 
                    o.id,
                    o.tipo_ganado AS tipo_ganado,
                    o.numero_ganado AS numero_ganado,
                    o.sexo AS sexo,
                    o.color AS color,
                    o.peso AS peso,
                    o.fecha_nacimiento as fecha_nacimiento,
                    o.lote_ganado as lote_ganado,
                    o.observaciones as observaciones,
                    o.activo as activo,
                    COUNT(*) OVER() AS total_rows
                FROM 
                    cattle o
                WHERE 
                    o.deleted_at IS NULL
                    AND o.farm_id = :farmId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", pageableDto.getFarmId());
        if (search != null && !search.isEmpty()) {
            sql += "AND (LOWER((o.tipo_ganado)) ILIKE (:search) " +
                    "OR LOWER((o.numero_ganado)) ILIKE (:search) " +
                    "OR LOWER((o.sexo)) ILIKE (:search) " +
                    "OR LOWER((o.peso)) ILIKE (:search) " +
                    "OR LOWER((o.fecha_nacimiento)) ILIKE (:search) " +
                    "OR LOWER((o.lote_ganado)) ILIKE (:search) " +
                    "OR LOWER((o.observaciones)) ILIKE (:search) " +
                    "OR LOWER((o.color)) ILIKE (:search)) ";
            params.addValue("search", "%" + search.toLowerCase() + "%");
        }

        if (pageableDto.getOrder_by() != null && !pageableDto.getOrder_by().isEmpty()) {
            sql += "ORDER BY " + pageableDto.getOrder_by() + " " + pageableDto.getOrder() + " ";
        }

        sql += "OFFSET :offset LIMIT :limit";

        long offset = pageNumber * pageSize;
        params.addValue("offset", offset);
        params.addValue("limit", pageSize);

        List<Map<String, Object>> resultList = namedParameterJdbcTemplate.query(sql, params, new ColumnMapRowMapper());
        List<GanadoTableDto> result = MapperRepository.mapListToDtoList(resultList, GanadoTableDto.class);

        long count = resultList.isEmpty() ? 0 : (long) resultList.get(0).get("total_rows");
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(result, pageable, count);
    }

	public List<CattleElementsDto> findListForId(Long farmId) {

		String sql = """
                SELECT id, numero_ganado 
                    FROM cattle 
                    WHERE sexo = 'Macho' 
                    AND deleted_at IS NULL
                    AND activo = 1
                    AND farm_id = ?;
                """;
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, farmId);
		List<CattleElementsDto> list = MapperRepository.mapListToDtoList(resultList, CattleElementsDto.class);
		return list;
	}

	public List<CattleElementsDto> findListForIdFemale(Long farmId) {

		String sql = """
                SELECT id, numero_ganado 
                    FROM cattle 
                    WHERE sexo = 'Hembra' 
                    AND deleted_at IS NULL
                    AND farm_id = ?;
                """;

		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, farmId);
		List<CattleElementsDto> list = MapperRepository.mapListToDtoList(resultList, CattleElementsDto.class);
		return list;
	}
    public void marcarGanadoComoVendido(List<Long> cattleIds) {
        String sql = """
            UPDATE cattle
            SET activo = 0,
                deleted_at = CURRENT_TIMESTAMP
            WHERE id IN (:ids)
        """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", cattleIds);
        namedParameterJdbcTemplate.update(sql, params);
    }

}
