package com.erp.backend_erp.repositories.products;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.inventory.InventoryTableDto;
import com.erp.backend_erp.util.MapperRepository;
import com.erp.backend_erp.util.PageableDto;

@Repository
public class InventoryQueryRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        public Boolean existsByName(String nombre_insumo) {
        String sql = "SELECT COUNT(*) FROM inventory WHERE nombre_insumo = :nombre_insumo";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("nombre_insumo", nombre_insumo);

        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }
    public PageImpl<InventoryTableDto> pageInventory(PageableDto<Object> pageableDto) {
        int pageNumber = pageableDto.getPage() != null ? pageableDto.getPage().intValue() : 0;
        int pageSize = pageableDto.getRows() != null ? pageableDto.getRows().intValue() : 10;
        String search = pageableDto.getSearch() != null ? pageableDto.getSearch().trim() : null;

        String sql = """
                SELECT 
                    o.id,
                    o.nombre_insumo AS nombre_insumo,
                    o.unidad AS unidad,
                    o.cantidad_total AS cantidad_total,
                    o.descripcion AS descripcion,
                    COUNT(*) OVER() AS total_rows
                FROM 
                    inventory o
                WHERE 
                    o.deleted_at IS NULL
                    AND o.farm_id = :farmId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", pageableDto.getFarmId());
        if (search != null && !search.isEmpty()) {
            sql += "AND (LOWER((o.nombre_insumo)) ILIKE (:search) " +
                    "OR LOWER((o.unidad)) ILIKE (:search) " +
                    "OR LOWER((o.cantidad_total)) ILIKE (:search) " +
                    "OR LOWER((o.descripcion)) ILIKE (:search)) ";
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
        List<InventoryTableDto> result = MapperRepository.mapListToDtoList(resultList, InventoryTableDto.class);

        long count = resultList.isEmpty() ? 0 : (long) resultList.get(0).get("total_rows");
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(result, pageable, count);
    }
}
