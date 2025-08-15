package com.erp.backend_erp.repositories.products;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.inventory.InventoryTableDto;
import com.erp.backend_erp.dto.inventory.ListInventoryDto;
import com.erp.backend_erp.util.MapperRepository;
import com.erp.backend_erp.util.PageableDto;

@Repository
public class InventoryQueryRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Boolean existsByCodigoInterno(String codigoInterno) {
        String sql = "SELECT COUNT(1) > 0 FROM inventory WHERE LOWER(codigo_interno) = :codigo";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("codigo", codigoInterno);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Boolean.class);
    }
    public PageImpl<InventoryTableDto> pageInventory(PageableDto<Object> pageableDto) {
        int pageNumber = pageableDto.getPage() != null ? pageableDto.getPage().intValue() : 0;
        int pageSize = pageableDto.getRows() != null ? pageableDto.getRows().intValue() : 10;
        String search = pageableDto.getSearch() != null ? pageableDto.getSearch().trim() : null;

        String sql = """
                SELECT
                    o.id,
                    o.nombre_insumo AS nombre_insumo,
                    o.unidad_medida AS unidad,
                    o.descripcion as descripcion,
                    o.cantidad_actual AS stock_actual,
                    e.nombre AS estado_stock,
                    COUNT(*) OVER() AS total_rows
                FROM
                    inventory o
                JOIN estados_inventario e ON e.id = o.estado_id
                WHERE
                    o.deleted_at IS NULL
                    AND o.farm_id = :farmId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", pageableDto.getFarmId());
        if (search != null && !search.isEmpty()) {
            sql += "AND (LOWER((o.nombre_insumo)) ILIKE (:search) " +
                    "OR LOWER((o.unidad_medida)) ILIKE (:search) " +
                    "OR LOWER((o.cantidad_actual)) ILIKE (:search) " +
                    "OR LOWER((o.nombre)) ILIKE (:search)) ";
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
    public List<ListInventoryDto> getInventory(Long farmId) {
        String sql = """
                SELECT 
                    u.id,
                    u.nombre_insumo AS nombre,
                    u.unidad_medida AS unidadMedida
                FROM inventory u
                WHERE u.deleted_at IS NULL 
                AND u.farm_id = :farmId
        """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", farmId);
        return namedParameterJdbcTemplate.query(sql, params, new RowMapper<ListInventoryDto>() {
            @Override
            public ListInventoryDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                ListInventoryDto dto = new ListInventoryDto();
                dto.setId(rs.getLong("id"));
                dto.setNombre(rs.getString("nombre"));
                dto.setUnidadMedida(rs.getString("unidadMedida"));
                return dto;
            }
        });
    }

}
