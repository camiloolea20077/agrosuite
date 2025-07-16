package com.erp.backend_erp.mappers.employees;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.employees.CreateEmployeesDto;
import com.erp.backend_erp.dto.employees.EmployeesDto;
import com.erp.backend_erp.dto.employees.UpdateEmployeesDto;
import com.erp.backend_erp.entity.employees.EmployeesEntity;

@Mapper(componentModel = "spring")
public interface EmployeesMapper {
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "nombre", source = "dto.nombre"),
        @Mapping(target = "identificacion", source = "dto.identificacion"),
        @Mapping(target = "cargo", source = "dto.cargo"),
        @Mapping(target = "fecha_ingreso", source = "dto.fecha_ingreso"),
        @Mapping(target = "activo", source = "dto.activo")
    })
    EmployeesEntity createToEntity(CreateEmployeesDto dto);
    @Mappings({
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "nombre", source = "entity.nombre"),
        @Mapping(target = "identificacion", source = "entity.identificacion"),
        @Mapping(target = "cargo", source = "entity.cargo"),
        @Mapping(target = "fecha_ingreso", source = "entity.fecha_ingreso"),
        @Mapping(target = "activo", source = "entity.activo")
    })
    EmployeesDto toDto(EmployeesEntity entity);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "nombre", source = "dto.nombre"),
        @Mapping(target = "identificacion", source = "dto.identificacion"),
        @Mapping(target = "cargo", source = "dto.cargo"),
        @Mapping(target = "fecha_ingreso", source = "dto.fecha_ingreso"),
        @Mapping(target = "activo", source = "dto.activo")
    })
    void updateEntityFromDto(UpdateEmployeesDto dto, @MappingTarget EmployeesEntity entity);
}
