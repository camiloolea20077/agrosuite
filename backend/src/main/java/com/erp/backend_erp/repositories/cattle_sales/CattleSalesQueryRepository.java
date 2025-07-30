package com.erp.backend_erp.repositories.cattle_sales;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.cattleSales.SalesTableDto;
import com.erp.backend_erp.entity.cattleSales.CattleSaleItemEntity;
import com.erp.backend_erp.util.MapperRepository;
import com.erp.backend_erp.util.PageableDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class CattleSalesQueryRepository {


    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @PersistenceContext
    private EntityManager entityManager;
    public boolean existsByOrigen(String tipoOrigen, Long idOrigen, Long farmId) {
        String sql = "";
        if ("GANADO".equalsIgnoreCase(tipoOrigen)) {
            sql = "SELECT COUNT(*) FROM cattle WHERE id = :idOrigen AND farm_id = :farmId AND deleted_at IS NULL";
        } else if ("TERNERO".equalsIgnoreCase(tipoOrigen)) {
            sql = "SELECT COUNT(*) FROM births WHERE id = :idOrigen AND farm_id = :farmId AND deleted_at IS NULL";
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("idOrigen", idOrigen);
        params.addValue("farmId", farmId);

        Long count = jdbc.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }
    public PageImpl<SalesTableDto> listSales(PageableDto<Object> pageableDto) {
        int pageNumber = pageableDto.getPage() != null ? pageableDto.getPage().intValue() : 0;
        int pageSize = pageableDto.getRows() != null ? pageableDto.getRows().intValue() : 10;
        String search = pageableDto.getSearch() != null ? pageableDto.getSearch().trim() : null;

        String sql = """
            SELECT 
                s.id AS id,
                s.tipo_venta,
                s.fecha_venta,
                s.observaciones,
                s.destino,
                s.precio_kilo,
                s.peso_total,
                s.subtotal,
                s.iva,
                s.descuentos,
                s.total AS total_venta,
                COUNT(si.id) AS total_animales,
                si.tipo_origen,
                COUNT(*) OVER() AS total_rows
            FROM 
                cattle_sales s
            LEFT JOIN 
                cattle_sale_items si ON s.id = si.sale_id
            WHERE 
                s.deleted_at IS NULL
                AND s.farm_id = :farmId
            GROUP BY 
                s.id, s.tipo_venta, s.fecha_venta, s.observaciones, s.destino,
                s.precio_kilo, s.peso_total, s.subtotal, s.iva, s.descuentos, s.total,
                si.tipo_origen
            """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", pageableDto.getFarmId());

        if (search != null && !search.isEmpty()) {
            sql += """
                AND (
                    LOWER(s.tipo_venta) ILIKE :search OR
                    LOWER(s.comprador) ILIKE :search OR
                    LOWER(s.observaciones) ILIKE :search
                )
            """;
            params.addValue("search", "%" + search.toLowerCase() + "%");
        }

        if (pageableDto.getOrder_by() != null && !pageableDto.getOrder_by().isEmpty()) {
            sql += " ORDER BY " + pageableDto.getOrder_by() + " " + pageableDto.getOrder() + " ";
        }

        sql += " OFFSET :offset LIMIT :limit ";

        long offset = pageNumber * pageSize;
        params.addValue("offset", offset);
        params.addValue("limit", pageSize);

        List<Map<String, Object>> resultList = jdbc.query(sql, params, new ColumnMapRowMapper());
        List<SalesTableDto> result = MapperRepository.mapListToDtoList(resultList, SalesTableDto.class);


        long count = resultList.isEmpty() ? 0 : ((Number) resultList.get(0).get("total_rows")).longValue();
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(result, pageable, count);
    }
    public List<CattleSaleItemEntity> findItemsBySaleId(Long saleId) {
        String jpql = "SELECT i FROM CattleSaleItemEntity i WHERE i.sale.id = :saleId";
        return entityManager.createQuery(jpql, CattleSaleItemEntity.class)
            .setParameter("saleId", saleId)
            .getResultList();
    }


}
