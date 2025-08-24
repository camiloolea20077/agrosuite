package com.erp.backend_erp.repositories.cattleDeath;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.cattleDeath.DeathsTableDto;
import com.erp.backend_erp.dto.cattleDeath.ViewCattleDeathDto;
import com.erp.backend_erp.util.MapperRepository;
import com.erp.backend_erp.util.PageableDto;

@Repository
public class CattleDeathQueryRepository {
   
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PageImpl<DeathsTableDto> listDeaths(PageableDto<Object> pageableDto) {
        int pageNumber = pageableDto.getPage() != null ? pageableDto.getPage().intValue() : 0;
        int pageSize = pageableDto.getRows() != null ? pageableDto.getRows().intValue() : 10;
        String search = pageableDto.getSearch() != null ? pageableDto.getSearch().trim() : null;

        String sql = """
            SELECT 
                cd.id,
                CASE 
                    WHEN cd.cattle_id IS NOT NULL THEN 'GANADO'
                    WHEN cd.birth_id IS NOT NULL THEN 'TERNERO'
                END as tipo_animal,
                CASE 
                    WHEN cd.cattle_id IS NOT NULL THEN c.numero_ganado
                    WHEN cd.birth_id IS NOT NULL THEN b.numero_cria
                END as numero_animal,
                CASE 
                    WHEN cd.cattle_id IS NOT NULL THEN c.sexo
                    WHEN cd.birth_id IS NOT NULL THEN b.sexo
                END as sexo_animal,
                cd.fecha_muerte,
                cd.motivo_muerte,
                cd.causa_categoria,
                cd.peso_muerte,
                u.nombre_completo as nombre,
                COUNT(*) OVER() AS total_rows
            FROM cattle_deaths cd
            LEFT JOIN cattle c ON cd.cattle_id = c.id
            LEFT JOIN births b ON cd.birth_id = b.id
            LEFT JOIN users u ON cd.usuario_registro = u.id
            WHERE cd.deleted_at IS NULL
            AND cd.farm_id = :farmId
            """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", pageableDto.getFarmId());

        if (search != null && !search.isEmpty()) {
            sql += """
                AND (LOWER(c.numero_ganado) ILIKE (:search) 
                OR LOWER(b.numero_cria) ILIKE (:search) 
                OR LOWER(cd.fecha_muerte) ILIKE (:search) 
                OR LOWER(cd.motivo_muerte) ILIKE (:search) 
                OR LOWER(cd.causa_categoria) ILIKE (:search) 
                OR LOWER(cd.peso_muerte) ILIKE (:search)) 
                """;
            params.addValue("search", "%" + search.toLowerCase() + "%");
        }

        if (pageableDto.getOrder_by() != null && !pageableDto.getOrder_by().isEmpty()) {
            String orderBy = pageableDto.getOrder_by();
            if ("id".equalsIgnoreCase(orderBy)) {
                orderBy = "cd.id";
            } else if ("fecha_muerte".equalsIgnoreCase(orderBy)) {
                orderBy = "cd.fecha_muerte";
            } else if ("motivo_muerte".equalsIgnoreCase(orderBy)) {
                orderBy = "cd.motivo_muerte";
            }
            sql += "ORDER BY " + orderBy + " " + pageableDto.getOrder() + " ";
        } else {
            sql += "ORDER BY cd.fecha_muerte DESC, cd.id DESC ";
        }

        sql += "OFFSET :offset LIMIT :limit";

        long offset = pageNumber * pageSize;
        params.addValue("offset", offset);
        params.addValue("limit", pageSize);

        List<Map<String, Object>> resultList = namedParameterJdbcTemplate.query(sql, params, new ColumnMapRowMapper());
        List<DeathsTableDto> result = MapperRepository.mapListToDtoList(resultList, DeathsTableDto.class);

        long count = resultList.isEmpty() ? 0 : (long) resultList.get(0).get("total_rows");
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(result, pageable, count);
    }

    public Optional<ViewCattleDeathDto> findDetailedById(Integer id, Long farmId) {
        String sql = """
            SELECT 
                cd.id,
                cd.cattle_id,
                cd.birth_id,
                CASE 
                    WHEN cd.cattle_id IS NOT NULL THEN 'GANADO'
                    WHEN cd.birth_id IS NOT NULL THEN 'TERNERO'
                END as tipo_animal,
                CASE 
                    WHEN cd.cattle_id IS NOT NULL THEN c.numero_ganado
                    WHEN cd.birth_id IS NOT NULL THEN b.numero_cria
                END as numero_animal,
                CASE 
                    WHEN cd.cattle_id IS NOT NULL THEN c.sexo
                    WHEN cd.birth_id IS NOT NULL THEN b.sexo
                END as sexo_animal,
                CASE 
                    WHEN cd.cattle_id IS NOT NULL THEN c.color
                    WHEN cd.birth_id IS NOT NULL THEN b.color_cria
                END as color_animal,
                CASE 
                    WHEN cd.cattle_id IS NOT NULL THEN c.tipo_animal
                    WHEN cd.birth_id IS NOT NULL THEN 'ternero'
                END as tipo_animal_detalle,
                cd.fecha_muerte,
                cd.motivo_muerte,
                cd.descripcion_detallada,
                cd.peso_muerte,
                cd.causa_categoria,
                u.nombre_completo as usuario_registro_nombre,
                cd.created_at,
                cd.updated_at
            FROM cattle_deaths cd
            LEFT JOIN cattle c ON cd.cattle_id = c.id
            LEFT JOIN births b ON cd.birth_id = b.id
            LEFT JOIN users u ON cd.usuario_registro = u.id
            WHERE cd.id = :id AND cd.farm_id = :farmId AND cd.deleted_at IS NULL
            """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("farmId", farmId);

        try {
            List<Map<String, Object>> resultList = namedParameterJdbcTemplate.query(sql, params, new ColumnMapRowMapper());
            if (!resultList.isEmpty()) {
                List<ViewCattleDeathDto> result = MapperRepository.mapListToDtoList(resultList, ViewCattleDeathDto.class);
                return Optional.of(result.get(0));
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Método para marcar ganado como muerto
    public void marcarGanadoComoMuerto(List<Integer> idsGanado) {
        if (idsGanado.isEmpty()) return;
        
        String sql = """
                UPDATE cattle
                SET activo = 0,
                    deleted_at = CURRENT_TIMESTAMP
                WHERE id IN (:ids)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", idsGanado);
        namedParameterJdbcTemplate.update(sql, params);
    }

    // Método para marcar nacimientos como muertos
    public void marcarNacimientosComoMuertos(List<Integer> idsNacimientos) {
        if (idsNacimientos.isEmpty()) return;
        
        String sql = """
                UPDATE births
                SET migrado_cattle = true
                WHERE id IN (:ids)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", idsNacimientos);
        namedParameterJdbcTemplate.update(sql, params);
    }

    // Métodos para reactivar (si se elimina el registro de muerte)
    public void reactivarGanado(List<Integer> idsGanado) {
        if (idsGanado.isEmpty()) return;
        
        String sql = "UPDATE cattle SET activo = 1 WHERE id IN (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", idsGanado);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void reactivarNacimientos(List<Integer> idsNacimientos) {
        if (idsNacimientos.isEmpty()) return;
        
        String sql = "UPDATE births SET migrado_cattle = false WHERE id IN (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", idsNacimientos);
        namedParameterJdbcTemplate.update(sql, params);
    }

    // Método para validar existencia de ganado
    public boolean existsGanadoById(Integer cattleId, Long farmId) {
        String sql = "SELECT COUNT(*) FROM cattle WHERE id = :cattleId AND farm_id = :farmId AND deleted_at IS NULL";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("cattleId", cattleId);
        params.addValue("farmId", farmId);
        
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    // Método para validar existencia de ternero
    public boolean existsBirthById(Integer birthId, Long farmId) {
        String sql = "SELECT COUNT(*) FROM births WHERE id = :birthId AND farm_id = :farmId AND deleted_at IS NULL";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("birthId", birthId);
        params.addValue("farmId", farmId);
        
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}