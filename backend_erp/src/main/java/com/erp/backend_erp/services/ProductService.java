package com.erp.backend_erp.services;

import com.erp.backend_erp.dto.products.CreateProductDto;
import com.erp.backend_erp.dto.products.ProductDto;
import com.erp.backend_erp.dto.products.UpdateProductDto;


public interface ProductService {
    ProductDto create(CreateProductDto createProductDto);
    Boolean update(UpdateProductDto updateProductDto);
    Boolean delete(Long id);
}
