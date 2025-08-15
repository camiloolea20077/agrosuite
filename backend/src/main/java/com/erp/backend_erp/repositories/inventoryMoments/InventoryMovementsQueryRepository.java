package com.erp.backend_erp.repositories.inventoryMoments;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.InventoryMovements.InventoryMovementsTableDto;
import com.erp.backend_erp.util.MapperRepository;
import com.erp.backend_erp.util.PageableDto;

@Repository
public class InventoryMovementsQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public InventoryMovementsQueryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean existsByCodigoMovimiento(String codigo) {
        String sql = "SELECT COUNT(1) > 0 FROM inventory_movements WHERE LOWER(codigo_movimiento) = :codigo";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("codigo", codigo);
        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    public PageImpl<InventoryMovementsTableDto> pageMovements(PageableDto<Object> pageableDto) {
        int pageNumber = pageableDto.getPage() != null ? pageableDto.getPage().intValue() : 0;
        int pageSize = pageableDto.getRows() != null ? pageableDto.getRows().intValue() : 10;
        String search = pageableDto.getSearch() != null ? pageableDto.getSearch().trim() : null;

        String sql = """
            SELECT 
                im.id,
                im.fecha_movimiento as fecha,
                i.nombre_insumo as insumo,
                tm.nombre as "tipoMovimiento",
                im.cantidad,
                im.numero_documento as documento,
                em.nombre as estado,
                im.observaciones,
                im.inventory_id AS "inventoryId",
                im.tipo_movimiento_id AS "tipoMovimientoId",
                im.estado_id AS "estadoId",
                COUNT(*) OVER() AS total_rows
            FROM inventory_movements im
            INNER JOIN inventory i ON im.inventory_id = i.id
            LEFT JOIN tipos_movimientos tm ON im.tipo_movimiento_id = tm.id
            LEFT JOIN estados_movimientos em ON im.estado_id = em.id
            WHERE im.farm_id = :farmId
            AND im.deleted_at IS NULL
                """;
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", pageableDto.getFarmId());
        
        if (search != null && !search.isEmpty()) {
            sql += """
                    AND (LOWER(i.nombre_insumo) ILIKE :search 
                    OR LOWER(tm.nombre) ILIKE :search 
                    OR LOWER(em.nombre) ILIKE :search 
                    OR LOWER(im.numero_documento) ILIKE :search
                    OR LOWER(im.observaciones) ILIKE :search) 
                    """;
            params.addValue("search", "%" + search.toLowerCase() + "%");
        }

        if (pageableDto.getOrder_by() != null && !pageableDto.getOrder_by().isEmpty()) {
            sql += "ORDER BY " + pageableDto.getOrder_by() + " " + pageableDto.getOrder() + " ";
        } else {
            // Ordenamiento por defecto
            sql += "ORDER BY im.fecha_movimiento DESC ";
        }

        sql += "OFFSET :offset LIMIT :limit";

        long offset = pageNumber * pageSize;
        params.addValue("offset", offset);
        params.addValue("limit", pageSize);

        List<Map<String, Object>> resultList = jdbcTemplate.query(sql, params, new ColumnMapRowMapper());
        List<InventoryMovementsTableDto> result = MapperRepository.mapListToDtoList(resultList, InventoryMovementsTableDto.class);

        long count = resultList.isEmpty() ? 0 : (long) resultList.get(0).get("total_rows");
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(result, pageable, count);
    }
}