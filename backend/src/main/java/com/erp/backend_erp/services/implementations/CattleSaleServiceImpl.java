package com.erp.backend_erp.services.implementations;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.backend_erp.dto.cattleSales.CattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.CreateCattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.CreateCattleSaleItemDto;
import com.erp.backend_erp.dto.cattleSales.OtpRequestInputDto;
import com.erp.backend_erp.dto.cattleSales.SalesTableDto;
import com.erp.backend_erp.dto.cattleSales.ViewCattleSaleDto;
import com.erp.backend_erp.dto.emails.EmailRequestDto;
import com.erp.backend_erp.entity.UserEntity;
import com.erp.backend_erp.entity.cattleSales.CattleSaleEntity;
import com.erp.backend_erp.entity.cattleSales.CattleSaleItemEntity;
import com.erp.backend_erp.entity.cattleSales.OtpRequestEntity;
import com.erp.backend_erp.mappers.cattleSales.CattleSaleMapper;
import com.erp.backend_erp.repositories.births.BirthsQueryRepository;
import com.erp.backend_erp.repositories.cattle_sales.CattleSalesJPARepository;
import com.erp.backend_erp.repositories.cattle_sales.CattleSalesQueryRepository;
import com.erp.backend_erp.repositories.cattle_sales.OtpRequestJPARepository;
import com.erp.backend_erp.repositories.ganado.GanadoQueryRepository;
import com.erp.backend_erp.repositories.users.UserJPARepository;
import com.erp.backend_erp.services.CattleSaleService;
import com.erp.backend_erp.services.EmailService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;
@Service
public class CattleSaleServiceImpl implements CattleSaleService{

    @Autowired
    private CattleSalesJPARepository salesRepository;

    @Autowired
    private CattleSalesQueryRepository salesQueryRepository;
    @Autowired
    private OtpRequestJPARepository otpRequestRepository;

    @Autowired
    private EmailService emailService;
        
    @Autowired
    private CattleSaleMapper mapper;

    @Autowired
    private GanadoQueryRepository ganadoQueryRepository;

    @Autowired
    private BirthsQueryRepository birthsQueryRepository;


    @Autowired
    private UserJPARepository userRepository; // JPA, no el QueryRepository

    @Override
    @Transactional
    public CattleSaleDto create(CreateCattleSaleDto dto) {
        // 1. Validar existencia de cada ganado/ternero (origen)
        for (CreateCattleSaleItemDto item : dto.getItems()) {
            boolean exists = salesQueryRepository.existsByOrigen(
                item.getTipoOrigen(),
                item.getIdOrigen(),
                dto.getFarmId()
            );

            if (!exists) {
                throw new RuntimeException("El " + item.getTipoOrigen().toLowerCase()
                    + " con ID " + item.getIdOrigen()
                    + " no existe o no pertenece a la finca");
            }
        }
        // 2. Mapear entidad principal
        CattleSaleEntity saleEntity = mapper.toEntity(dto);
        saleEntity.setEstado("PENDIENTE"); // ← Nueva línea para definir estado inicial

        // 3. Mapear ítems con referencia a la entidad
        List<CattleSaleItemEntity> items = dto.getItems().stream().map(itemDto -> {
            CattleSaleItemEntity itemEntity = mapper.toEntityItem(itemDto);
            itemEntity.setSale(saleEntity);
            itemEntity.setFarmId(dto.getFarmId());
            return itemEntity;
        }).collect(Collectors.toList());
        saleEntity.setItems(items);
        // 4. Guardar venta con sus items
        CattleSaleEntity saved = salesRepository.save(saleEntity);
        // 5. (Ya NO se marca como vendido aquí)
        return mapper.toDto(saved);
    }


    @Override
    public PageImpl<SalesTableDto> pageSales(PageableDto<Object> pageableDto) {
        return salesQueryRepository.listSales(pageableDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ViewCattleSaleDto findById(Long id, Long farmId) {
        CattleSaleEntity entity = salesRepository.findByIdAndFarmId(id, farmId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        List<CattleSaleItemEntity> items = salesQueryRepository.findItemsBySaleId(entity.getId());

        return mapper.toViewDto(entity, items);
    }
    @Override
    @Transactional
    public void confirmarVenta(Long ventaId, Long farmId) {
        CattleSaleEntity venta = salesRepository.findByIdAndFarmId(ventaId, farmId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        if (!"PENDIENTE".equalsIgnoreCase(venta.getEstado())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "La venta no está en estado PENDIENTE");
        }

        // Obtener ítems
        List<CattleSaleItemEntity> items = salesQueryRepository.findItemsBySaleId(ventaId);

        List<Long> idsGanado = items.stream()
            .filter(i -> i.getTipoOrigen().equalsIgnoreCase("GANADO"))
            .map(CattleSaleItemEntity::getIdOrigen)
            .toList();
        List<Long> idsNacimientos = items.stream()
            .filter(i -> i.getTipoOrigen().equalsIgnoreCase("TERNERO"))
            .map(CattleSaleItemEntity::getIdOrigen)
            .toList();

        if (!idsGanado.isEmpty()) {
            ganadoQueryRepository.marcarGanadoComoVendido(idsGanado);
        }

        if (!idsNacimientos.isEmpty()) {
            birthsQueryRepository.marcarNacimientosComoVendidos(idsNacimientos);
        }

        venta.setEstado("CONFIRMADA");
        salesRepository.save(venta);
    }
    @Override
    @Transactional
    public void anularVenta(Long ventaId, Long farmId) {
        CattleSaleEntity venta = salesRepository.findByIdAndFarmId(ventaId, farmId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
        if ("ANULADA".equalsIgnoreCase(venta.getEstado())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "La venta ya está anulada");
        }
        if ("CONFIRMADA".equalsIgnoreCase(venta.getEstado())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "No se puede anular una venta que ya ha sido confirmada");
        }
        venta.setEstado("ANULADA");
        salesRepository.save(venta);
    }
    @Override
    @Transactional
    public void solicitarAnulacionConOtp(Long saleId, Long farmId, OtpRequestInputDto dto) throws Exception {
        // 1. Validar que la venta exista y pertenezca a la finca
        CattleSaleEntity venta = salesRepository.findByIdAndFarmId(saleId, farmId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        // 2. Validar que el aprobador exista
        UserEntity aprobador = userRepository.findById(dto.getApproverUserId())
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Usuario aprobador no encontrado"));

        // 3. Validar que tenga rol DUENO (3) o ADMINISTRADOR FINCA (4)
        Long roleId = aprobador.getRole_id();
        if (!List.of(1L, 3L).contains(roleId)) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "Este usuario no tiene permisos para autorizar la anulación");
        }

        // 4. Generar token único y fecha de expiración
        String token = UUID.randomUUID().toString();
        Instant expiracion = Instant.now().plusSeconds(5 * 60); // 5 minutos

        // 5. Guardar en la tabla OTP
        OtpRequestEntity otp = new OtpRequestEntity();
        otp.setSaleId(saleId);
        otp.setFarmId(farmId);
        otp.setRequestedByUserId(dto.getRequestedByUserId());
        otp.setApproverUserId(dto.getApproverUserId());
        otp.setToken(token);
        otp.setExpiration(expiracion);
        otp.setUsed(false);
        otpRequestRepository.save(otp);

        // 6. Preparar correo al aprobador
        Map<String, String> variables = new HashMap<>();
        variables.put("nombreAdmin", aprobador.getNombre_completo());
        variables.put("ventaId", saleId.toString());
        variables.put("codigoOtp", token.substring(0, 6).toUpperCase());
        variables.put("expiraEn", "5 minutos");
        variables.put("nombreApp", "AgruSuit");
        variables.put("urlAutorizacion", "http://localhost:9001/cattle-sale/autorizar-anulacion?token=" + token);
        EmailRequestDto emailDto = new EmailRequestDto();
        emailDto.setTo(aprobador.getEmail());
        emailDto.setSubject("Autorización de Anulación de Venta #" + saleId);
        emailDto.setTemplate("clave-dinamica-venta.html");
        emailDto.setVariables(variables);
        System.out.println("Enviando OTP al correo: " + aprobador.getEmail());
        emailService.enviarCorreo(emailDto);
    }
    @Override
    @Transactional
    public void autorizarAnulacionPorToken(String token) {
        // 1. Buscar el OTP
        OtpRequestEntity otp = otpRequestRepository.findByToken(token)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Token inválido o no existe"));

        // 2. Verificar si ya fue usado
        if (otp.isUsed()) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Este token ya fue utilizado");
        }

        // 3. Verificar expiración
        if (otp.getExpiration().isBefore(Instant.now())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Este token ha expirado");
        }

        // 4. Validar rol del aprobador
        UserEntity aprobador = userRepository.findById(otp.getApproverUserId())
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Usuario aprobador no encontrado"));

        Long roleId = aprobador.getRole_id();
        if (!List.of(1L, 3L).contains(roleId)) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "Este usuario no tiene permisos para autorizar la anulación");
        }

        // 5. Ejecutar la anulación usando método existente
        this.anularVenta(otp.getSaleId(), otp.getFarmId());

        // 6. Marcar OTP como usado
        otp.setUsed(true);
        otpRequestRepository.save(otp);
    }
}
