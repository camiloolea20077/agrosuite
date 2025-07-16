package com.erp.backend_erp.services.implementations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.sales.CreateSaleDto;
import com.erp.backend_erp.dto.sales.SaleDto;
import com.erp.backend_erp.dto.sales.SaleProductDto;
import com.erp.backend_erp.entity.produts.ProductEntity;
import com.erp.backend_erp.entity.sales.SaleEntity;
import com.erp.backend_erp.entity.sales.SaleProductEntity;
import com.erp.backend_erp.mappers.sales.SaleProductMapper;
import com.erp.backend_erp.mappers.sales.SalesMapper;
import com.erp.backend_erp.repositories.products.ProductsJPARepository;
import com.erp.backend_erp.repositories.sales.SalesJPARepository;
import com.erp.backend_erp.security.JwtTokenProvider;
import com.erp.backend_erp.services.SalesService;
import com.erp.backend_erp.util.GlobalException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class SalesServiceImpl implements SalesService {

    @Autowired
    private SalesJPARepository salesJPARepository;

    @Autowired
    private ProductsJPARepository productsJPARepository;

    @Autowired
    private SalesMapper salesMapper;

    @Autowired
    private SaleProductMapper saleProductMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private HttpServletRequest request;

    @Transactional
    public SaleDto createSale(CreateSaleDto createSaleDto) {
        Long companyId = jwtTokenProvider.getCompanyIdFromToken(request);
    
        SaleEntity saleEntity = salesMapper.createToEntity(createSaleDto);
        saleEntity.setCompanyId(companyId);
    
        BigDecimal total = BigDecimal.ZERO;
        List<SaleProductEntity> saleProducts = new ArrayList<>();
    
        for (SaleProductDto dto : createSaleDto.getProducts()) {
            ProductEntity product = productsJPARepository.findById(dto.getProductId())
                    .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "Producto no encontrado"));
    
            if (!product.getCompany_id().equals(companyId)) {
                throw new GlobalException(HttpStatus.BAD_REQUEST, "El producto no pertenece a esta empresa");
            }
    
            BigDecimal requestedQty = BigDecimal.valueOf(dto.getQuantity());
            if (product.getQuantity().compareTo(requestedQty) < 0) {
                throw new GlobalException(HttpStatus.BAD_REQUEST, "Stock insuficiente para el producto: " + product.getName());
            }
    
            product.setQuantity(product.getQuantity().subtract(requestedQty));
    
            BigDecimal price = dto.getUnitPrice().multiply(requestedQty);
            total = total.add(price);
    
            SaleProductEntity spe = saleProductMapper.toEntity(dto);
            spe.setProduct(product);
            spe.setSale(saleEntity);
            spe.setPrice(price);
    
            saleProducts.add(spe);
        }
    
        saleEntity.setProducts(saleProducts);
        saleEntity.setTotal(total);
    
        SaleEntity savedSale = salesJPARepository.save(saleEntity);
        return salesMapper.toDto(savedSale);
    }
    
}