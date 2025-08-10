package com.erp.backend_erp.repositories.cattleTransfer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.cattleTransfer.CattleTransferTableDto;
import com.erp.backend_erp.util.MapperRepository;
import com.erp.backend_erp.util.PageableDto;

@Repository
public class CattleTransferQueryRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    public PageImpl<CattleTransferTableDto> pageTransfers(PageableDto<Object> dto) {
        int pageNumber = dto.getPage() != null ? dto.getPage().intValue() : 0;
        int pageSize = dto.getRows() != null ? dto.getRows().intValue() : 10;
        String search = dto.getSearch() != null ? dto.getSearch().trim() : null;

        String sql = """
            SELECT
                t.id,
                t.transfer_type,
                f1.nombre AS origin_farm,
                f2.nombre AS destination_farm,
                t.transfer_date,
                t.observations,
                u.nombre AS created_by,
                COUNT(*) OVER() AS total_rows
            FROM
                cattle_transfer t
            INNER JOIN farms f1 ON f1.id = t.origin_farm_id
            INNER JOIN farms f2 ON f2.id = t.destination_farm_id
            INNER JOIN users u ON u.id = t.created_by
            WHERE t.deleted_at IS NULL
            AND t.origin_farm_id = :farmId
        """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", dto.getFarmId());

        if (search != null && !search.isEmpty()) {
            sql += """
                AND (LOWER(f1.nombre) ILIKE :search OR LOWER(f2.nombre) ILIKE :search OR LOWER(u.nombre) ILIKE :search)
            """;
            params.addValue("search", "%" + search.toLowerCase() + "%");
        }

        if (dto.getOrder_by() != null && !dto.getOrder_by().isEmpty()) {
            sql += " ORDER BY " + dto.getOrder_by() + " " + dto.getOrder() + " ";
        }

        sql += " OFFSET :offset LIMIT :limit ";
        params.addValue("offset", pageNumber * pageSize);
        params.addValue("limit", pageSize);

        List<Map<String, Object>> rows = jdbc.query(sql, params, new ColumnMapRowMapper());
        List<CattleTransferTableDto> result = MapperRepository.mapListToDtoList(rows, CattleTransferTableDto.class);
        long total = rows.isEmpty() ? 0 : (long) rows.get(0).get("total_rows");

        return new PageImpl<>(result, PageRequest.of(pageNumber, pageSize), total);
    }
}
