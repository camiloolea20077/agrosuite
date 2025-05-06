package com.erp.backend_erp.util;

import java.lang.reflect.Field;
import java.sql.Array;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MapperRepository {

    private final ObjectMapper objectMapper;

    private final ModelMapper modelMapper = new ModelMapper();

    public MapperRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Método que convierte un Map<String, Object> en un objeto del tipo
     * especificado
     *
     * @param <T>
     * @param map      Map<String, Object>
     * @param dtoClass Class<T>
     * @return T
     */
    public <T> T mapToDto(Map<String, Object> map, Class<T> dtoClass) {
        T dto;
        try {
            // Crear una nueva instancia del objeto DTO
            dto = dtoClass.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();

                // Si el valor del campo es nulo, se establece como una cadena vacía
                fieldValue = fieldValue == null ? "" : fieldValue;
                try {
                    Field field = dtoClass.getDeclaredField(fieldName);
                    field.setAccessible(true);

                    // Manejo de campos de tipo array
                    if (field.getType().isArray() && fieldValue instanceof Array) {
                        handleArrayField(field, dto, (Array) fieldValue);
                    } // Manejo de campos de tipo Integer
                    else if (field.getType().equals(Integer.class)) {
                        handleIntegerField(field, dto, fieldValue);
                    } // Manejo de campos de tipo LocalDateTime
                    else if (field.getType().equals(LocalDateTime.class)) {
                        handleLocalDateTimeField(field, dto, fieldValue);
                    } // Asignación directa si el tipo del campo es compatible
                    else if (field.getType().isAssignableFrom(fieldValue.getClass())) {
                        field.set(dto, fieldValue);
                    }
                } catch (NoSuchFieldException e) {
                    // Ignorar si el campo no existe en la clase DTO
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return dto;
    }

    /**
     * Método que convierte una lista de Map<String, Object> en una lista de
     * objetos del tipo especificado
     *
     * @param <T>
     * @param mapList  List<Map<String, Object>>
     * @param dtoClass Class<T>
     * @return List<T>
     */
    public <T> List<T> mapListToDtoList(List<Map<String, Object>> mapList, Class<T> dtoClass) {
        List<T> dtoList = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            dtoList.add(mapToDto(map, dtoClass));
        }
        return dtoList;
    }

    /**
     * Método auxiliar para manejar campos de tipo array
     *
     * @param <T>
     * @param field
     * @param dto
     * @param fieldValue
     * @throws Exception
     */
    private <T> void handleArrayField(Field field, T dto, Array fieldValue) throws Exception {
        Object[] objectArray = (Object[]) fieldValue.getArray();
        Object[] combinedArray = new Object[objectArray.length];

        for (int i = 0; i < objectArray.length; i++) {
            combinedArray[i] = objectArray[i];
        }

        field.set(dto, combinedArray);
    }

    /**
     * Método auxiliar para manejar campos de tipo Integer
     *
     * @param <T>
     * @param field
     * @param dto
     * @param fieldValue
     * @throws IllegalAccessException
     */
    private <T> void handleIntegerField(Field field, T dto, Object fieldValue) throws IllegalAccessException {
        if (fieldValue instanceof Long) {
            field.set(dto, ((Long) fieldValue).intValue());
        } else if (fieldValue instanceof Integer) {
            field.set(dto, fieldValue);
        } else {
            field.set(dto, 0);
        }
    }

    /**
     * Método auxiliar para manejar campos de tipo LocalDateTime
     *
     * @param <T>
     * @param field
     * @param dto
     * @param fieldValue
     * @throws IllegalAccessException
     */
    private <T> void handleLocalDateTimeField(Field field, T dto, Object fieldValue) throws IllegalAccessException {
        LocalDateTime localDateTime = null;

        if (fieldValue instanceof Timestamp) {
            localDateTime = ((Timestamp) fieldValue).toLocalDateTime();
        } else if (fieldValue instanceof String) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                localDateTime = LocalDateTime.parse((String) fieldValue, formatter);
            } catch (Exception e) {
                field.set(dto, null);
            }
        }
        field.set(dto, localDateTime);
    }

    /**
     * Convierte un objecto a un elemento mapeable
     *
     * @param obj Object
     * @return Map<String, Object>
     */
    public Map<String, Object> convertObjectToMap(Object obj) {
        return objectMapper.convertValue(obj, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * Convierte un json a un objeto mapeable
     *
     * @param <T>        Tipo generico
     * @param json       String
     * @param targetType Class<T>
     * @return <T>
     */
    public <T> T convertJsonToObject(String json, Class<T> targetType) {
        try {
            return objectMapper.readValue(json, targetType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convierte un json a una lista mapeable
     *
     * @param <T>
     * @param json       String
     * @param targetType Class<T>
     * @return List<T>
     */
    public <T> List<T> convertJsonToList(String json, Class<T> targetType) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<T>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convierte un objeto a un json
     *
     * @param obj Object
     * @return String
     */
    public String convertObjectToJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <D, T> D mapToDtoModel(T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public <D, T> List<D> mapListToDtoModel(List<T> entityList, Class<D> outClass) {
        return entityList.stream()
                .map(entity -> modelMapper.map(entity, outClass))
                .collect(Collectors.toList());
    }
}