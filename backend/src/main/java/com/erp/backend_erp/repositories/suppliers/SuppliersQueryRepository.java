package com.erp.backend_erp.repositories.suppliers;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.suppliers.SuppliersTableDto;
import com.erp.backend_erp.util.MapperRepository;
import com.erp.backend_erp.util.PageableDto;

@Repository
public class SuppliersQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SuppliersQueryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean existsByCodigo(String codigo) {
        String sql = "SELECT COUNT(1) > 0 FROM suppliers WHERE LOWER(codigo) = :codigo";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("codigo", codigo);
        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    public PageImpl<SuppliersTableDto> listSuppliers(PageableDto<Object> pageableDto) {
        int pageNumber = pageableDto.getPage() != null ? pageableDto.getPage().intValue() : 0;
        int pageSize = pageableDto.getRows() != null ? pageableDto.getRows().intValue() : 10;
        String search = pageableDto.getSearch() != null ? pageableDto.getSearch().trim() : null;

        String sql = """
                SELECT 
                    o.id,
                    o.nit AS nit,
                    o.nombre AS nombre,
                    o.contacto AS contacto,
                    o.telefono AS telefono,
                    o.email AS email,
                    o.activo as activo,
                    COUNT(*) OVER() AS total_rows
                FROM 
                    suppliers o
                WHERE 
                    o.deleted_at IS NULL
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", pageableDto.getFarmId());
        if (search != null && !search.isEmpty()) {
            sql += "AND (LOWER((o.nit)) ILIKE (:search) " +
                    "OR LOWER((o.nombre)) ILIKE (:search) " +
                    "OR LOWER((o.contacto)) ILIKE (:search) " +
                    "OR LOWER((o.telefono)) ILIKE (:search) " +
                    "OR LOWER((o.email)) ILIKE (:search) " ;
            params.addValue("search", "%" + search.toLowerCase() + "%");
        }

        if (pageableDto.getOrder_by() != null && !pageableDto.getOrder_by().isEmpty()) {
            sql += "ORDER BY " + pageableDto.getOrder_by() + " " + pageableDto.getOrder() + " ";
        }

        sql += "OFFSET :offset LIMIT :limit";

        long offset = pageNumber * pageSize;
        params.addValue("offset", offset);
        params.addValue("limit", pageSize);

        List<Map<String, Object>> resultList = jdbcTemplate.query(sql, params, new ColumnMapRowMapper());
        List<SuppliersTableDto> result = MapperRepository.mapListToDtoList(resultList, SuppliersTableDto.class);

        long count = resultList.isEmpty() ? 0 : (long) resultList.get(0).get("total_rows");
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(result, pageable, count);
    }
}