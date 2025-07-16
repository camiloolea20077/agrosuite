package com.erp.backend_erp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.products.CreateProductDto;
import com.erp.backend_erp.dto.products.ProductDto;
import com.erp.backend_erp.dto.products.UpdateProductDto;
import com.erp.backend_erp.services.ProductService;
import com.erp.backend_erp.util.ApiResponse;

@RestController
@RequestMapping("/products")
public class ProductsController {


    @Autowired
    ProductService productsService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createCompanies(
        @Valid @RequestBody CreateProductDto createProductDto) throws Exception {
        try {
            ProductDto saveProductDto = productsService
                .create(createProductDto);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                "Registro creado exitosamente", false, saveProductDto);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PutMapping("/update")
	public ResponseEntity<ApiResponse<Object>> update(@Valid @RequestBody UpdateProductDto updateProductDto)
			throws Exception {
		try {
			Boolean isUpdated = productsService.update(updateProductDto);
			ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
					"Registro actualizado correctamente", false, isUpdated);
			return ResponseEntity.ok(response);
		}
		catch (Exception ex) {
			throw ex;
		}
	}

    @DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable("id") Long id) throws Exception {
		try {
			Boolean isDeleted = productsService.delete(id);
			ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "Registro eliminado correctamente",
					false, isDeleted);
			return ResponseEntity.ok(response);
		}
		catch (Exception ex) {
			throw ex;
		}
	}
}
