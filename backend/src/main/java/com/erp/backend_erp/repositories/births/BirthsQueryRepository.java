package com.erp.backend_erp.repositories.births;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.dto.births.BirthsListDto;
import com.erp.backend_erp.dto.births.BirthsTableDto;
import com.erp.backend_erp.dto.births.DashboardBirthDto;
import com.erp.backend_erp.dto.births.DesteteTableDto;
import com.erp.backend_erp.dto.births.ResultadoMigracionDto;
import com.erp.backend_erp.util.MapperRepository;
import com.erp.backend_erp.util.PageableDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class BirthsQueryRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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
                        b.peso_cria as peso_cria,
                        b.color_cria as color_cria,
                        b.observaciones as observaciones,
                        COUNT(*) OVER() AS total_rows
                    FROM births b
                    LEFT JOIN cattle vaca ON vaca.id = b.id_vaca
                    LEFT JOIN cattle toro ON toro.id = b.id_toro
                    WHERE b.deleted_at IS NULL
                    AND b.farm_id = :farmId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", pageableDto.getFarmId());
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

    public List<DashboardBirthDto> getBirthsByMonth(Long farmId) {
        String sql = """
            SELECT 
                EXTRACT(MONTH FROM TO_DATE(b.fecha_nacimiento, 'YYYY-MM-DD')) AS month, 
                SUM(CASE WHEN b.sexo = 'Macho' THEN 1 ELSE 0 END) AS maleCount, 
                SUM(CASE WHEN b.sexo = 'Hembra' THEN 1 ELSE 0 END) AS femaleCount
            FROM births b
            WHERE b.deleted_at IS NULL AND b.farm_id = :farmId
            GROUP BY EXTRACT(MONTH FROM TO_DATE(b.fecha_nacimiento, 'YYYY-MM-DD'))
            ORDER BY month
        """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", farmId);

        List<Map<String, Object>> resultList = namedParameterJdbcTemplate.query(sql, params, new ColumnMapRowMapper());

        return resultList.stream()
            .map(row -> {
                Integer month = ((Number) row.get("month")).intValue();
                Long maleCount = ((Number) row.get("maleCount")).longValue();
                Long femaleCount = ((Number) row.get("femaleCount")).longValue();
                return new DashboardBirthDto(month, maleCount, femaleCount);
            }).collect(Collectors.toList());
    }

    public long getTotalCattleCount(Long farmId) {
        String sql = "SELECT COUNT(*) FROM cattle WHERE deleted_at IS NULL AND activo = 1 AND farm_id = :farmId";
        MapSqlParameterSource params = new MapSqlParameterSource("farmId", farmId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public long getTotalBirths(Long farmId) {
        String sql = "SELECT COUNT(*) FROM births WHERE deleted_at IS NULL AND farm_id = :farmId";
        MapSqlParameterSource params = new MapSqlParameterSource("farmId", farmId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public long getTotalEmployees(Long farmId) {
        String sql = """
            SELECT COUNT(*) 
            FROM employees 
            WHERE deleted_at IS NULL 
            AND activo = 1 
            AND farm_id = :farmId
        """;
        MapSqlParameterSource params = new MapSqlParameterSource("farmId", farmId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public void marcarNacimientosComoVendidos(List<Long> cattleIds) {
        String sql = """
            UPDATE births
            SET 
                deleted_at = CURRENT_TIMESTAMP
            WHERE id IN (:ids)
        """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", cattleIds);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void updateFarmId(Long birthId, Long newFarmId) {
        String sql = "UPDATE births SET farm_id = :farmId WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", newFarmId);
        params.addValue("id", birthId);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public List<DesteteTableDto> listDesteteDashboard(Long farmId) {
        String sql = """
            SELECT id,
                numero_cria,
                fecha_nacimiento::date,
                (fecha_nacimiento::date + INTERVAL '8 months')::date AS fecha_proxima_destete,
                GREATEST(0, EXTRACT(days FROM (fecha_nacimiento::date + INTERVAL '8 months') - CURRENT_DATE)::integer) AS dias_restantes
            FROM births
            WHERE deleted_at IS NULL
            AND farm_id = :farmId
            ORDER BY fecha_proxima_destete
            LIMIT 10;
        """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", farmId);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            DesteteTableDto dto = new DesteteTableDto();
            dto.setId(rs.getLong("id"));
            dto.setNumero_cria(rs.getString("numero_cria"));
            dto.setFecha_nacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
            dto.setFecha_proxima_destete(rs.getDate("fecha_proxima_destete").toLocalDate());
            dto.setDias_restantes(Long.valueOf(rs.getInt("dias_restantes")));
            return dto;
        });
    }

    public List<DesteteTableDto> obtenerTernerosListosParaDestetar(Long farmId) {
        String sql = """
            SELECT id,
                   numero_cria,
                   fecha_nacimiento::date,
                   (fecha_nacimiento::date + INTERVAL '8 months')::date AS fecha_proxima_destete,
                   GREATEST(0, EXTRACT(days FROM (fecha_nacimiento::date + INTERVAL '8 months') - CURRENT_DATE)::integer) AS dias_restantes
            FROM births
            WHERE deleted_at IS NULL
              AND COALESCE(migrado_cattle, FALSE) = FALSE
              AND farm_id = :farmId
              AND (fecha_nacimiento::date + INTERVAL '8 months') <= CURRENT_DATE
            ORDER BY fecha_proxima_destete;
        """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", farmId);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            DesteteTableDto dto = new DesteteTableDto();
            dto.setId(rs.getLong("id"));
            dto.setNumero_cria(rs.getString("numero_cria"));
            dto.setFecha_nacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
            dto.setFecha_proxima_destete(rs.getDate("fecha_proxima_destete").toLocalDate());
            dto.setDias_restantes(Long.valueOf(rs.getInt("dias_restantes")));
            return dto;
        });
    }

    public List<DesteteTableDto> obtenerProximosDestetes(Long farmId, Integer limit) {
        String sql = """
            SELECT id,
                   numero_cria,
                   fecha_nacimiento::date,
                   (fecha_nacimiento::date + INTERVAL '8 months')::date AS fecha_proxima_destete,
                   EXTRACT(days FROM (fecha_nacimiento::date + INTERVAL '8 months') - CURRENT_DATE)::integer AS dias_restantes
            FROM births
            WHERE deleted_at IS NULL
              AND COALESCE(migrado_cattle, FALSE) = FALSE
              AND farm_id = :farmId
            ORDER BY fecha_proxima_destete
            LIMIT :limit;
        """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", farmId);
        params.addValue("limit", limit != null ? limit : 10);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            DesteteTableDto dto = new DesteteTableDto();
            dto.setId(rs.getLong("id"));
            dto.setNumero_cria(rs.getString("numero_cria"));
            dto.setFecha_nacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
            dto.setFecha_proxima_destete(rs.getDate("fecha_proxima_destete").toLocalDate());
            dto.setDias_restantes(Long.valueOf(rs.getInt("dias_restantes")));
            return dto;
        });
    }

    public ResultadoMigracionDto migrarTerneroACattle(Long birthId, String decision) {
        // Usar JdbcTemplate simple para la función
        String sql = "SELECT migrar_ternero_a_cattle(?, ?)";
        
        try {
            String jsonResult = jdbcTemplate.queryForObject(sql, String.class, birthId, decision);
            
            if (jsonResult == null) {
                return new ResultadoMigracionDto(
                    false, 
                    "No se recibió respuesta de la función", 
                    birthId, 
                    null, 
                    decision
                );
            }

            // Parsear el JSON usando Jackson ObjectMapper
            JsonNode jsonNode = objectMapper.readTree(jsonResult);
            
            boolean success = jsonNode.get("success").asBoolean();
            String message = jsonNode.get("message").asText();
            Long cattleId = null;
            
            if (jsonNode.has("cattle_id") && !jsonNode.get("cattle_id").isNull()) {
                cattleId = jsonNode.get("cattle_id").asLong();
            }
            
            return new ResultadoMigracionDto(success, message, birthId, cattleId, decision);
            
        } catch (Exception e) {
            // Log del error completo
            System.err.println("Error al migrar ternero: " + e.getMessage());
            e.printStackTrace();
            
            return new ResultadoMigracionDto(
                false, 
                "Error al migrar ternero: " + e.getMessage(), 
                birthId, 
                null, 
                decision
            );
        }
    }

    public List<Map<String, Object>> obtenerHistorialDestetes(Long farmId) {
        String sql = "SELECT * FROM obtener_historial_destetes(?)";
        
        try {
            return jdbcTemplate.query(sql, new ColumnMapRowMapper(), farmId);
        } catch (Exception e) {
            System.err.println("Error al obtener historial de destetes: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Retornar lista vacía en caso de error
        }
    }

    // Método para verificar si un ternero puede ser migrado
    public boolean puedeSerMigrado(Long birthId, Long farmId) {
        String sql = """
            SELECT CASE 
                WHEN (fecha_nacimiento::date + INTERVAL '8 months') <= CURRENT_DATE 
                     AND COALESCE(migrado_cattle, FALSE) = FALSE 
                     AND deleted_at IS NULL 
                THEN TRUE 
                ELSE FALSE 
            END as puede_migrar
            FROM births 
            WHERE id = ? AND farm_id = ?;
        """;
        
        try {
            Boolean resultado = jdbcTemplate.queryForObject(sql, Boolean.class, birthId, farmId);
            return resultado != null && resultado;
        } catch (Exception e) {
            System.err.println("Error verificando si puede ser migrado: " + e.getMessage());
            return false;
        }
    }

    // Método para obtener estadísticas de destetes
    public Map<String, Object> obtenerEstadisticasDestetes(Long farmId) {
        String sql = """
            SELECT 
                COUNT(*) FILTER (WHERE COALESCE(migrado_cattle, FALSE) = FALSE AND (fecha_nacimiento::date + INTERVAL '8 months') <= CURRENT_DATE) as listos_para_destetar,
                COUNT(*) FILTER (WHERE COALESCE(migrado_cattle, FALSE) = FALSE AND (fecha_nacimiento::date + INTERVAL '8 months') > CURRENT_DATE) as proximos_destetes,
                COUNT(*) FILTER (WHERE COALESCE(migrado_cattle, FALSE) = TRUE) as ya_destetados,
                COUNT(*) FILTER (WHERE COALESCE(migrado_cattle, FALSE) = TRUE AND decision_destete = 'cria') as enviados_a_cria,
                COUNT(*) FILTER (WHERE COALESCE(migrado_cattle, FALSE) = TRUE AND decision_destete = 'venta') as enviados_a_venta
            FROM births 
            WHERE farm_id = ? AND deleted_at IS NULL;
        """;
        
        try {
            return jdbcTemplate.queryForMap(sql, farmId);
        } catch (Exception e) {
            System.err.println("Error obteniendo estadísticas: " + e.getMessage());
            return Map.of(
                "listos_para_destetar", 0,
                "proximos_destetes", 0, 
                "ya_destetados", 0,
                "enviados_a_cria", 0,
                "enviados_a_venta", 0
            );
        }
    }

    // Método auxiliar mejorado para parsear intervalos (ya no se necesita)
    @Deprecated
    private Long extractDaysFromPostgreSQLInterval(String interval) {
        if (interval == null || interval.isEmpty()) {
            return 0L;
        }
        
        try {
            // Buscar patrón "X days" en el string
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\s+days");
            java.util.regex.Matcher matcher = pattern.matcher(interval);
            
            if (matcher.find()) {
                return Long.parseLong(matcher.group(1));
            }
            
            // Buscar patrón "-X days" para números negativos
            java.util.regex.Pattern negPattern = java.util.regex.Pattern.compile("-(\\d+)\\s+days");
            java.util.regex.Matcher negMatcher = negPattern.matcher(interval);
            
            if (negMatcher.find()) {
                return -Long.parseLong(negMatcher.group(1));
            }
            
        } catch (Exception e) {
            System.err.println("Error parsing interval: " + interval);
        }
        
        return 0L;
    }

    // Métodos auxiliares obsoletos - reemplazados por Jackson ObjectMapper
    @Deprecated
    private Long extractCattleIdFromJson(String json) {
        // Este método es obsoleto, se reemplazó por Jackson ObjectMapper
        try {
            String[] parts = json.split("\"cattle_id\":");
            if (parts.length > 1) {
                String idPart = parts[1].split(",")[0].trim();
                return Long.parseLong(idPart);
            }
        } catch (Exception e) {
            System.err.println("Error extracting cattle_id: " + e.getMessage());
        }
        return null;
    }

    @Deprecated
    private String extractMessageFromJson(String json) {
        // Este método es obsoleto, se reemplazó por Jackson ObjectMapper
        try {
            String[] parts = json.split("\"message\":\"");
            if (parts.length > 1) {
                return parts[1].split("\"")[0];
            }
        } catch (Exception e) {
            System.err.println("Error extracting message: " + e.getMessage());
        }
        return "Error desconocido";
    }
    public List<BirthsListDto> getBirhts(Long farmId) {
        String sql = """
                SELECT 
                    u.id,
                    u.numero_cria AS nombre
                FROM births u
                WHERE u.deleted_at IS NULL 
                AND u.farm_id = :farmId
        """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("farmId", farmId);
        return namedParameterJdbcTemplate.query(sql, params, new RowMapper<BirthsListDto>() {
            @Override
            public BirthsListDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                BirthsListDto dto = new BirthsListDto();
                dto.setId(rs.getLong("id"));
                dto.setNumero(rs.getString("nombre"));
                return dto;
            }
        });
    }

}