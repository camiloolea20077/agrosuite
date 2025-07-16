package com.erp.backend_erp.repositories.employees;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.employees.EmployeesTableDto;
import com.erp.backend_erp.util.MapperRepository;
import com.erp.backend_erp.util.PageableDto;

@Repository
public class EmployeesQueryRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Boolean existsByDocument(String identificacion) {
        String sql = "SELECT COUNT(*) FROM employees WHERE identificacion = :identificacion";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("identificacion", identificacion);

        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }
    public PageImpl<EmployeesTableDto> pageEmployees(PageableDto<Object> pageableDto) {
        int pageNumber = pageableDto.getPage() != null ? pageableDto.getPage().intValue() : 0;
        int pageSize = pageableDto.getRows() != null ? pageableDto.getRows().intValue() : 10;
        String search = pageableDto.getSearch() != null ? pageableDto.getSearch().trim() : null;

        String sql = """
                SELECT 
                    o.id,
                    o.nombre AS nombre,
                    o.identificacion AS identificacion,
                    o.cargo AS cargo,
                    o.fecha_ingreso AS fecha_ingreso,
                    o.activo as activo,
                    COUNT(*) OVER() AS total_rows
                FROM 
                    employees o
                WHERE 
                    o.deleted_at IS NULL
					and o.activo=1
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (search != null && !search.isEmpty()) {
            sql += "AND (LOWER((o.nombre)) ILIKE (:search) " +
                    "OR LOWER((o.identificacion)) ILIKE (:search) " +
                    "OR LOWER((o.cargo)) ILIKE (:search) " +
                    "OR LOWER((o.fecha_ingreso)) ILIKE (:search)) ";
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
        List<EmployeesTableDto> result = MapperRepository.mapListToDtoList(resultList, EmployeesTableDto.class);

        long count = resultList.isEmpty() ? 0 : (long) resultList.get(0).get("total_rows");
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(result, pageable, count);
    }
}
