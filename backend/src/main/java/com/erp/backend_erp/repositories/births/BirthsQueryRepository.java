package com.erp.backend_erp.repositories.births;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.births.BirthsTableDto;
import com.erp.backend_erp.dto.births.DashboardBirthDto;
import com.erp.backend_erp.util.MapperRepository;
import com.erp.backend_erp.util.PageableDto;

@Repository
public class BirthsQueryRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Boolean existCattle(String numeroCria) {
        String sql = "SELECT COUNT(*) FROM births WHERE numero_cria = :numeroCria";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("numeroCria", numeroCria);
        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }
    public PageImpl<BirthsTableDto> listBirths(PageableDto<Object> pageableDto) {
        int pageNumber = pageableDto.getPage() != null ? pageableDto.getPage().intValue() : 0;
        int pageSize = pageableDto.getRows() != null ? pageableDto.getRows().intValue() : 10;
        String search = pageableDto.getSearch() != null ? pageableDto.getSearch().trim() : null;

        String sql = """
                    SELECT 
                        b.id,
                        vaca.numero_ganado AS numero_ganado,
                        toro.numero_ganado AS numero_toro,
                        b.fecha_nacimiento as fecha_nacimiento,
                        b.numero_cria as numero_cria,
                        b.sexo as sexo,
                        b.color_cria as color_cria,
                        b.observaciones as observaciones,
                        COUNT(*) OVER() AS total_rows
                    FROM births b
                    LEFT JOIN cattle vaca ON vaca.id = b.id_vaca
                    LEFT JOIN cattle toro ON toro.id = b.id_toro
                    WHERE b.deleted_at IS NULL
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (search != null && !search.isEmpty()) {
            sql += "AND (LOWER((vaca.numero_ganado)) ILIKE (:search) " +
                    "OR LOWER((toro.numero_ganado)) ILIKE (:search) " +
                    "OR LOWER((b.fecha_nacimiento)) ILIKE (:search) " +
                    "OR LOWER((b.numero_cria)) ILIKE (:search) " +
                    "OR LOWER((b.sexo)) ILIKE (:search) " +
                    "OR LOWER((b.color_cria)) ILIKE (:search) " +
                    "OR LOWER((b.observaciones)) ILIKE (:search)) ";
            params.addValue("search", "%" + search.toLowerCase() + "%");
        }
        if (pageableDto.getOrder_by() != null && !pageableDto.getOrder_by().isEmpty()) {
            String orderBy = pageableDto.getOrder_by();
            if ("id".equalsIgnoreCase(orderBy)) {
                orderBy = "b.id";
            }
            sql += "ORDER BY " + orderBy + " " + pageableDto.getOrder() + " ";
        }
        sql += "OFFSET :offset LIMIT :limit";

        long offset = pageNumber * pageSize;
        params.addValue("offset", offset);
        params.addValue("limit", pageSize);

        List<Map<String, Object>> resultList = namedParameterJdbcTemplate.query(sql, params, new ColumnMapRowMapper());
        List<BirthsTableDto> result = MapperRepository.mapListToDtoList(resultList, BirthsTableDto.class);

        long count = resultList.isEmpty() ? 0 : (long) resultList.get(0).get("total_rows");
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(result, pageable, count);
    }
    public List<DashboardBirthDto> getBirthsByMonth() {
        String sql = """
            SELECT 
                EXTRACT(MONTH FROM TO_DATE(b.fecha_nacimiento, 'YYYY-MM-DD')) AS month, 
                SUM(CASE WHEN b.sexo = 'Macho' THEN 1 ELSE 0 END) AS maleCount, 
                SUM(CASE WHEN b.sexo = 'Hembra' THEN 1 ELSE 0 END) AS femaleCount
            FROM births b
            WHERE b.deleted_at IS NULL
            GROUP BY EXTRACT(MONTH FROM TO_DATE(b.fecha_nacimiento, 'YYYY-MM-DD'))
            ORDER BY month
        """;

        List<Map<String, Object>> resultList = namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(), new ColumnMapRowMapper());

        List<DashboardBirthDto> birthDataList = resultList.stream()
                .map(row -> {
                    // Manejar el mes
                    Number monthNumber = (Number) row.get("month");
                    Integer month = monthNumber != null ? monthNumber.intValue() : null;
                    
                    // Manejar los conteos como Long
                    Number maleCountNumber = (Number) row.get("maleCount");
                    Long maleCount = maleCountNumber != null ? maleCountNumber.longValue() : 0L;
                    
                    Number femaleCountNumber = (Number) row.get("femaleCount");
                    Long femaleCount = femaleCountNumber != null ? femaleCountNumber.longValue() : 0L;

                    return new DashboardBirthDto(month, maleCount, femaleCount);
                })
                .collect(Collectors.toList());

        return birthDataList;
    }

    public long getTotalCattleCount() {
        String sql = "SELECT COUNT(*) FROM cattle WHERE deleted_at IS NULL AND activo = 1";
        return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Long.class);
    }
    public long getTotalBirths() {
        String sql = """
            SELECT 
                CAST(COUNT(*) AS INTEGER) AS totalBirths
            FROM births b
            WHERE b.deleted_at IS NULL;
        """;

        Integer totalBirths = namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);

        return totalBirths != null ? totalBirths : 0;
    }
    public long getTotalEmployees() {
        String sql = """
            SELECT 
                CAST(COUNT(*) AS INTEGER) AS totalBirths
            FROM employees b
            WHERE b.deleted_at IS NULL
            AND b.activo = 1
        """;

        Integer totalEmployees = namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);

        return totalEmployees != null ? totalEmployees : 0;
    }
}
