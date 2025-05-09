package com.erp.backend_erp.repositories.clients;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.clients.ClientsTableDto;
import com.erp.backend_erp.util.MapperRepository;
import com.erp.backend_erp.util.PageableDto;

@Repository
public class ClientsQueryRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public Boolean findByDocument(String document) {
        String sql = "SELECT COUNT(*) FROM customers WHERE document = :document";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("document", document);

        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }

    public PageImpl<ClientsTableDto> listConventions(PageableDto<Object> pageableDto) {
    int pageNumber = pageableDto.getPage() != null ? pageableDto.getPage().intValue() : 0;
    int pageSize = pageableDto.getRows() != null ? pageableDto.getRows().intValue() : 10;
    String search = pageableDto.getSearch() != null ? pageableDto.getSearch().trim() : null;

    String sql = "SELECT " +
            "o.id, " +
            "cs.name AS company_name, " +
            "o.name AS name, " +
            "o.email AS email, " +
            "o.phone AS phone, " +
            "o.document AS document, " +
            "o.document_type AS document_type, " +
            "COUNT(*) OVER() AS total_rows " +
            "FROM customers o " +
            "LEFT JOIN companies cs ON cs.id = o.company_id " +
            "WHERE o.deleted_at IS NULL ";

    MapSqlParameterSource params = new MapSqlParameterSource();

    if (search != null && !search.isEmpty()) {
        sql += "AND (LOWER(unaccent(o.name)) ILIKE unaccent(:search) " +
                "OR LOWER(unaccent(o.email)) ILIKE unaccent(:search) " +
                "OR LOWER(unaccent(o.phone)) ILIKE unaccent(:search) " +
                "OR LOWER(unaccent(o.document)) ILIKE unaccent(:search)) ";
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
    List<ClientsTableDto> result = MapperRepository.mapListToDtoList(resultList, ClientsTableDto.class);

    long count = resultList.isEmpty() ? 0 : (long) resultList.get(0).get("total_rows");
    PageRequest pageable = PageRequest.of(pageNumber, pageSize);
    return new PageImpl<>(result, pageable, count);
}

}
