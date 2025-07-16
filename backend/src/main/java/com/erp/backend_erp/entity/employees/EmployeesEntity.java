package com.erp.backend_erp.entity.employees;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Getter
@Setter
@SQLDelete(sql = "UPDATE employees SET deleted_at = NOW() WHERE id=?")
public class EmployeesEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;


    private String nombre;


    private String identificacion;


    private String cargo;


    private String fecha_ingreso;


    private Long activo;
}
