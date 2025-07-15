package com.erp.backend_erp.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.products.CreateProductDto;
import com.erp.backend_erp.dto.products.ProductDto;
import com.erp.backend_erp.dto.products.UpdateProductDto;
import com.erp.backend_erp.entity.produts.ProductEntity;
import com.erp.backend_erp.mappers.products.ProductsMapper;
import com.erp.backend_erp.repositories.Supplier.SupplierQueryRepository;
import com.erp.backend_erp.repositories.products.ProductsJPARepository;
import com.erp.backend_erp.repositories.products.ProductsQueryRepository;
import com.erp.backend_erp.security.JwtTokenProvider;
import com.erp.backend_erp.services.ProductService;
import com.erp.backend_erp.util.GlobalException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ProductsServiceImpl implements ProductService {
    
    private final ProductsJPARepository productsJPARepository;
    private final ProductsMapper productMapper;
    private final SupplierQueryRepository supplierQueryRepository;
    private final ProductsQueryRepository productsQueryRepository;
    @Autowired
    private JwtTokenProvider _jwtTokenProvider;
    @Autowired
    private HttpServletRequest request;
    public ProductsServiceImpl(SupplierQueryRepository supplierQueryRepository, ProductsJPARepository productsJPARepository , ProductsMapper productMapper, ProductsQueryRepository productsQueryRepository) {
        this.productMapper = productMapper;
        this.supplierQueryRepository = supplierQueryRepository;
        this.productsQueryRepository = productsQueryRepository;
        this.productsJPARepository = productsJPARepository;
    }


    @Override
    public ProductDto create(CreateProductDto createProductDto) {
        Long companyId = _jwtTokenProvider.getCompanyIdFromToken(request);

        boolean exists = productsQueryRepository.existsByName(createProductDto.getCode().toLowerCase());
        if (exists) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El Código del producto ya se encuentra registrado");
        }
        boolean proveedorValido = supplierQueryRepository.existsByIdAndCompanyId(
            createProductDto.getSupplier_id(), companyId);
        if (!proveedorValido) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El proveedor no pertenece a esta empresa");
        }
        try {
            ProductEntity productEntity = productMapper.createToEntity(createProductDto);
            productEntity.setCompany_id(companyId);

            ProductEntity savedProduct = productsJPARepository.save(productEntity);
            return productMapper.toDto(savedProduct);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el producto: " + e.getMessage(), e);
        }
    }
    

    @Override
	public Boolean update(UpdateProductDto updateProductDto) {

		ProductEntity roleEntity = productsJPARepository.findById(updateProductDto.getId())
				.orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
		try {
			productMapper.updateEntityFromDto(updateProductDto, roleEntity);
			productsJPARepository.save(roleEntity);
			return true;
		}
		catch (Exception e) {
			throw new RuntimeException("Error al actualizar el registro");
		}
	}

    @Override
	public Boolean delete(Long id) {
		productsJPARepository.findById(id)
				.orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
                productsJPARepository.deleteById(id);
		return true;
	}

}
