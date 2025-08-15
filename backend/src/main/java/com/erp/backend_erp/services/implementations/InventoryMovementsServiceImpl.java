package com.erp.backend_erp.services.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.InventoryMovements.CreateInventoryMovementsDto;
import com.erp.backend_erp.dto.InventoryMovements.InventoryMovementsDto;
import com.erp.backend_erp.dto.InventoryMovements.InventoryMovementsTableDto;
import com.erp.backend_erp.dto.InventoryMovements.UpdateInventoryMovementsDto;
import com.erp.backend_erp.entity.inventory.InventoryEntity;
import com.erp.backend_erp.entity.inventory.InventoryMovementsEntity;
import com.erp.backend_erp.mappers.tiposMovimientos.InventoryMovementsMapper;
import com.erp.backend_erp.repositories.inventoryMoments.InventoryMovementsJPARepository;
import com.erp.backend_erp.repositories.inventoryMoments.InventoryMovementsQueryRepository;
import com.erp.backend_erp.repositories.products.InventoryJPARepository;
import com.erp.backend_erp.services.InventoryMovementsService;
import com.erp.backend_erp.services.TiposMovimientosService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

import jakarta.transaction.Transactional;

@Service
public class InventoryMovementsServiceImpl implements InventoryMovementsService {
    
    private final InventoryMovementsJPARepository inventoryMovementsJPARepository;
    private final InventoryMovementsQueryRepository inventoryMovementsQueryRepository;
    private final InventoryMovementsMapper inventoryMovementsMapper;
    private final InventoryJPARepository inventoryJPARepository;
    private final TiposMovimientosService tiposMovimientosService;
    public InventoryMovementsServiceImpl(InventoryMovementsJPARepository inventoryMovementsJPARepository,
                                    TiposMovimientosService tiposMovimientosService,
                                    InventoryMovementsQueryRepository inventoryMovementsQueryRepository,
                                    InventoryMovementsMapper inventoryMovementsMapper,
                                    InventoryJPARepository inventoryJPARepository) {
        this.inventoryMovementsJPARepository = inventoryMovementsJPARepository;
        this.tiposMovimientosService = tiposMovimientosService;
        this.inventoryMovementsQueryRepository = inventoryMovementsQueryRepository;
        this.inventoryMovementsMapper = inventoryMovementsMapper;
        this.inventoryJPARepository = inventoryJPARepository;
    }
    private BigDecimal nvl(BigDecimal v) { return v != null ? v : BigDecimal.ZERO; }
    private BigDecimal abs(BigDecimal v) { return v.signum() < 0 ? v.negate() : v; }

    @Override
    public InventoryMovementsDto create(CreateInventoryMovementsDto createDto) {
        try {
            // Verificar que el inventario existe y pertenece a la finca
            InventoryEntity inventory = inventoryJPARepository.findByIdAndFarmId(createDto.getInventoryId(), createDto.getFarmId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Inventario no encontrado"));

            InventoryMovementsEntity entity = inventoryMovementsMapper.createToEntity(createDto);
            entity.setFarmId(createDto.getFarmId());
            entity.setCantidadAnterior(inventory.getCantidadActual());
            
            InventoryMovementsEntity savedEntity = inventoryMovementsJPARepository.save(entity);
            return inventoryMovementsMapper.toDto(savedEntity);
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<InventoryMovementsDto> createAll(List<CreateInventoryMovementsDto> dtoList) {
        List<InventoryMovementsDto> result = new ArrayList<>();
        for (CreateInventoryMovementsDto dto : dtoList) {
            try {
                InventoryMovementsDto saved = this.create(dto);
                result.add(saved);
            } catch (Exception e) {
                System.out.println("No se pudo guardar movimiento: " + e.getMessage());
            }
        }
        return result;
    }

    @Transactional
    @Override
    public Boolean update(UpdateInventoryMovementsDto dto) {
        var movPrev = inventoryMovementsJPARepository.findById(dto.getId())
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Movimiento no encontrado"));

        // Si no afectó stock (pendiente de aprobación), solo actualiza y retorna
        boolean afectoStock = !Boolean.TRUE.equals(movPrev.getRequiereAprobacion());
        if (!afectoStock) {
            inventoryMovementsMapper.updateEntityFromDto(dto, movPrev);
            inventoryMovementsJPARepository.save(movPrev);
            return true;
        }

        // --- 1) Resolver inventarios (puede cambiar inventoryId) ---
        var invOld = inventoryJPARepository.findByIdAndFarmId(movPrev.getInventoryId(), movPrev.getFarmId())
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Inventario previo no encontrado"));

        var invNew = dto.getInventoryId() != null && !dto.getInventoryId().equals(movPrev.getInventoryId())
            ? inventoryJPARepository.findByIdAndFarmId(dto.getInventoryId(), movPrev.getFarmId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Inventario nuevo no encontrado"))
            : invOld;

        // --- 2) Signos por tipo (flags), no por signo de cantidad ---
        var tipoPrev = tiposMovimientosService.findById(movPrev.getTipoMovimientoId());
        var tipoNew  = tiposMovimientosService.findById(dto.getTipoMovimientoId());

        int signPrev = Long.valueOf(1L).equals(tipoPrev.getEsEntrada()) ? +1
                    : Long.valueOf(1L).equals(tipoPrev.getEsSalida())  ? -1 : 0;

        int signNew  = Long.valueOf(1L).equals(tipoNew.getEsEntrada())  ? +1
                    : Long.valueOf(1L).equals(tipoNew.getEsSalida())   ? -1 : 0;

        var deltaPrev = abs(nvl(movPrev.getCantidad())).multiply(BigDecimal.valueOf(signPrev)); // efecto aplicado
        var deltaNew  = abs(nvl(dto.getCantidad())).multiply(BigDecimal.valueOf(signNew));      // nuevo efecto

        // --- 3) Revertir efecto anterior ---
        invOld.setCantidadActual(nvl(invOld.getCantidadActual()).subtract(deltaPrev));
        if (invOld.getCantidadActual().signum() < 0) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Reverso deja inventario negativo");
        }

        // --- 4) Aplicar nuevo efecto ---
        var anteriorNew = nvl(invNew.getCantidadActual());
        var nuevaNew    = anteriorNew.add(deltaNew);
        if (nuevaNew.signum() < 0) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Nuevo ajuste deja inventario negativo");
        }
        invNew.setCantidadActual(nuevaNew);

        inventoryJPARepository.save(invOld);
        if (invNew.getId() != invOld.getId()) {
            inventoryJPARepository.save(invNew);
        }

        // --- 5) Actualizar movimiento ---
        inventoryMovementsMapper.updateEntityFromDto(dto, movPrev);
        movPrev.setCantidadAnterior(anteriorNew);
        movPrev.setCantidadNueva(nuevaNew);
        inventoryMovementsJPARepository.save(movPrev);

        return true;
    }

    @Override
    public Boolean delete(Long id) {
        inventoryMovementsJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        inventoryMovementsJPARepository.deleteById(id);
        return true;
    }
    @Transactional
    @Override
    public Boolean approve(Long id, Long userId) {
        var movement = inventoryMovementsJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Movimiento no encontrado"));

        if (!Boolean.TRUE.equals(movement.getRequiereAprobacion())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Este movimiento no requiere aprobación");
        }

        var inv = inventoryJPARepository.findById(movement.getInventoryId())
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Inventario no encontrado"));

        // Usa los FLAGS del tipo, no el signo
        var tipo = tiposMovimientosService.findById(movement.getTipoMovimientoId());
        boolean esEntrada = Long.valueOf(1L).equals(tipo.getEsEntrada());
        boolean esSalida  = Long.valueOf(1L).equals(tipo.getEsSalida());

        BigDecimal anterior = nvl(inv.getCantidadActual());
        BigDecimal delta    = abs(nvl(movement.getCantidad()));
        BigDecimal nueva;

        if (esEntrada) {
            nueva = anterior.add(delta);
        } else if (esSalida) {
            if (anterior.compareTo(delta) < 0) {
                throw new GlobalException(HttpStatus.BAD_REQUEST, "Stock insuficiente para aprobar este movimiento");
            }
            nueva = anterior.subtract(delta);
        } else {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Tipo de movimiento no soportado para aprobación");
        }

        // Afecta stock ahora (solo en aprobación)
        inv.setCantidadActual(nueva);
        inventoryJPARepository.save(inv);

        // Marca aprobado y actualiza track de cantidades
        movement.setAprobadoPor(userId);
        movement.setFechaAprobacion(LocalDateTime.now());
        movement.setCantidadAnterior(anterior);
        movement.setCantidadNueva(nueva);
        movement.setRequiereAprobacion(false);

        inventoryMovementsJPARepository.save(movement);
        return true;
    }

        @Override
        public Boolean close(Long id, Long userId) {
            InventoryMovementsEntity entity = inventoryMovementsJPARepository.findById(id)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Movimiento no encontrado"));
            
            entity.setEstaCerrado(true);
            entity.setUpdatedBy(userId);
            inventoryMovementsJPARepository.save(entity);
            return true;
        }

    @Override
    public InventoryMovementsDto findById(Long id, Long farmId) {
        InventoryMovementsEntity entity = inventoryMovementsJPARepository.findByIdAndFarmId(id, farmId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        return inventoryMovementsMapper.toDto(entity);
    }

    @Override
    public List<InventoryMovementsDto> findByInventory(Long inventoryId, Long farmId) {
        List<InventoryMovementsEntity> entities = inventoryMovementsJPARepository
            .findByInventoryIdAndFarmIdOrderByFechaMovimientoDesc(inventoryId, farmId);
        return entities.stream().map(inventoryMovementsMapper::toDto).toList();
    }

    @Override
    public List<InventoryMovementsDto> findByDateRange(Long farmId, LocalDateTime startDate, LocalDateTime endDate) {
        List<InventoryMovementsEntity> entities = inventoryMovementsJPARepository
            .findByFarmIdAndFechaMovimientoBetweenAndDeletedAtIsNullOrderByFechaMovimientoDesc(farmId, startDate, endDate);
        return entities.stream().map(inventoryMovementsMapper::toDto).toList();
    }

    @Override
    public List<InventoryMovementsDto> findPendingApprovals(Long farmId) {
        List<InventoryMovementsEntity> entities = inventoryMovementsJPARepository
            .findByFarmIdAndRequiereAprobacionAndAprobadoPorIsNull(farmId, true);
        return entities.stream().map(inventoryMovementsMapper::toDto).toList();
    }

    // @Override
    // public PageImpl<InventoryMovementsTableDto> getPage(PageableDto<Object> pageableDto) {
    //     return inventoryMovementsQueryRepository.listInventoryMovements(pageableDto);
    // }

    @Override
    public PageImpl<InventoryMovementsTableDto> getPage(PageableDto<Object> pageableDto) {
        return inventoryMovementsQueryRepository.pageMovements(pageableDto);
    }

    @Transactional
    @Override
    public InventoryMovementsDto processEntry(CreateInventoryMovementsDto dto) {
        var inv = inventoryJPARepository.findByIdAndFarmId(dto.getInventoryId(), dto.getFarmId())
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Inventario no encontrado"));

        BigDecimal anterior = nvl(inv.getCantidadActual());
        BigDecimal delta    = abs(nvl(dto.getCantidad()));   // <-- ABS SIEMPRE
        BigDecimal nueva    = anterior.add(delta);           // <-- ENTRADA = SUMA

        // setea previas en el dto (si tu mapper las copia a la entity)
        dto.setCantidadAnterior(anterior);
        dto.setCantidadNueva(nueva);

        var entity = inventoryMovementsMapper.createToEntity(dto);
        entity.setFarmId(dto.getFarmId());

        // Si NO requiere aprobación, afecta stock y marca aprobado en el acto
        if (Boolean.FALSE.equals(dto.getRequiereAprobacion())) {
            inv.setCantidadActual(nueva);
            inventoryJPARepository.save(inv);

            entity.setAprobadoPor(dto.getCreatedBy());
            entity.setFechaAprobacion(LocalDateTime.now());
            entity.setRequiereAprobacion(false);
        }

        var saved = inventoryMovementsJPARepository.save(entity);
        return inventoryMovementsMapper.toDto(saved);
    }
    @Transactional
    @Override
    public InventoryMovementsDto processExit(CreateInventoryMovementsDto dto) {
        var inv = inventoryJPARepository.findByIdAndFarmId(dto.getInventoryId(), dto.getFarmId())
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Inventario no encontrado"));

        BigDecimal anterior = nvl(inv.getCantidadActual());
        BigDecimal delta    = abs(nvl(dto.getCantidad()));   // <-- ABS SIEMPRE

        if (anterior.compareTo(delta) < 0) {
            throw new GlobalException(HttpStatus.BAD_REQUEST,
                String.format("Stock insuficiente. Disponible: %s, Solicitado: %s", anterior, delta));
        }

        BigDecimal nueva = anterior.subtract(delta);         // <-- SALIDA = RESTA

        dto.setCantidadAnterior(anterior);
        dto.setCantidadNueva(nueva);

        var entity = inventoryMovementsMapper.createToEntity(dto);
        entity.setFarmId(dto.getFarmId());

        if (Boolean.FALSE.equals(dto.getRequiereAprobacion())) {
            inv.setCantidadActual(nueva);
            inventoryJPARepository.save(inv);

            entity.setAprobadoPor(dto.getCreatedBy());
            entity.setFechaAprobacion(LocalDateTime.now());
            entity.setRequiereAprobacion(false);
        }

        var saved = inventoryMovementsJPARepository.save(entity);
        return inventoryMovementsMapper.toDto(saved);
    }
    @Transactional
    @Override
    public Boolean processReturn(Long movementId, BigDecimal returnQuantity, Long userId) {
        var movement = inventoryMovementsJPARepository.findById(movementId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Movimiento no encontrado"));

        // 1) Solo devolver si el movimiento ya afectó inventario
        boolean aplicado = !Boolean.TRUE.equals(movement.getRequiereAprobacion()) || movement.getAprobadoPor() != null;
        if (!aplicado) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "No puedes devolver un movimiento que aún no aplica al inventario");
        }

        // 2) Determinar si el movimiento original es ENTRADA o SALIDA por flags
        var tipo = tiposMovimientosService.findById(movement.getTipoMovimientoId());
        boolean esEntrada = Long.valueOf(1L).equals(tipo.getEsEntrada());
        boolean esSalida  = Long.valueOf(1L).equals(tipo.getEsSalida());
        if (!esEntrada && !esSalida) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Tipo de movimiento no soportado para devolución");
        }

        // 3) Normalizar cantidades y validar pendientes
        BigDecimal original     = abs(nvl(movement.getCantidad()));
        BigDecimal devueltaAcum = abs(nvl(movement.getCantidadDevuelta()));  
        BigDecimal toReturn     = abs(nvl(returnQuantity));                   
        if (toReturn.signum() == 0) throw new GlobalException(HttpStatus.BAD_REQUEST, "Cantidad de devolución inválida");

        BigDecimal pendiente = original.subtract(devueltaAcum);
        if (toReturn.compareTo(pendiente) > 0) {
            throw new GlobalException(HttpStatus.BAD_REQUEST,
                String.format("Devolución excede lo pendiente. Pendiente: %s, Intentado: %s", pendiente, toReturn));
        }

        // 4) Ajustar stock según el tipo original
        var inventory = inventoryJPARepository.findByIdAndFarmId(movement.getInventoryId(), movement.getFarmId())
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Inventario no encontrado"));

        BigDecimal anterior = nvl(inventory.getCantidadActual());
        BigDecimal nueva;
        if (esSalida) {
            // Devolución de SALIDA: suma al stock
            nueva = anterior.add(toReturn);
        } else { // esEntrada
            // Devolución de ENTRADA: resta del stock
            nueva = anterior.subtract(toReturn);
            if (nueva.signum() < 0) throw new GlobalException(HttpStatus.BAD_REQUEST, "La devolución deja inventario negativo");
        }

        inventory.setCantidadActual(nueva);
        inventoryJPARepository.save(inventory);

        // 5) Actualizar acumulados del movimiento (sin tocar cantidad original)
        movement.setCantidadDevuelta(devueltaAcum.add(toReturn));
        movement.setCantidadUsada(esSalida ? original.subtract(movement.getCantidadDevuelta()) : null);
        movement.setFechaDevolucion(LocalDateTime.now());
        movement.setUpdatedBy(userId);
        inventoryMovementsJPARepository.save(movement);

        return true;
    }
        // ---------- Consultas (las que pide tu controller) ----------
    @Override
    public List<InventoryMovementsDto> findByInventoryAndFarm(Long inventoryId, Long farmId) {
        return inventoryMovementsJPARepository
            .findByInventoryIdAndFarmIdOrderByFechaMovimientoDesc(inventoryId, farmId)
            .stream()
            .map(inventoryMovementsMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<InventoryMovementsDto> findByFarmAndDateRange(Long farmId, LocalDateTime startDate, LocalDateTime endDate) {
        return inventoryMovementsJPARepository
            .findByFarmIdAndFechaMovimientoBetweenAndDeletedAtIsNullOrderByFechaMovimientoDesc(farmId, startDate, endDate)
            .stream()
            .map(inventoryMovementsMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<InventoryMovementsDto> listPendingApprovals(Long farmId) {
        return inventoryMovementsJPARepository
            .findByFarmIdAndRequiereAprobacionAndAprobadoPorIsNull(farmId, true)
            .stream()
            .map(inventoryMovementsMapper::toDto)
            .collect(Collectors.toList());
    }
    @Override
    public Boolean reject(Long id, Long approverUserId, String reason) {
        InventoryMovementsEntity entity = inventoryMovementsJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Movimiento no encontrado"));
        // Marcamos como revisado (no pendiente), y dejamos evidencia en notas
        entity.setRequiereAprobacion(false);
        entity.setFechaAprobacion(LocalDateTime.now());
        entity.setAprobadoPor(null); // explícito: no aprobado
        String notas = (entity.getNotas() == null ? "" : entity.getNotas() + " | ");
        entity.setNotas(notas + "RECHAZADO por usuario " + approverUserId + (reason != null ? " - Motivo: " + reason : ""));
        inventoryMovementsJPARepository.save(entity);
        return true;
    }

}