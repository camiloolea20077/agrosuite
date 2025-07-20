package com.erp.backend_erp.services.implementations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.sales.CreateSaleDto;
import com.erp.backend_erp.dto.sales.SaleDto;
import com.erp.backend_erp.entity.sales.SaleEntity;
import com.erp.backend_erp.entity.sales.SaleProductEntity;
import com.erp.backend_erp.mappers.sales.SaleProductMapper;
import com.erp.backend_erp.mappers.sales.SalesMapper;
import com.erp.backend_erp.repositories.products.InventoryJPARepository;
import com.erp.backend_erp.repositories.sales.SalesJPARepository;
import com.erp.backend_erp.security.JwtTokenProvider;
import com.erp.backend_erp.services.SalesService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class SalesServiceImpl implements SalesService {

    @Autowired
    private SalesJPARepository salesJPARepository;

    @Autowired
    private InventoryJPARepository productsJPARepository;

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
    
        saleEntity.setProducts(saleProducts);
        saleEntity.setTotal(total);
    
        SaleEntity savedSale = salesJPARepository.save(saleEntity);
        return salesMapper.toDto(savedSale);
    }
    
}