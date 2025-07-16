package com.erp.backend_erp.services.implementations;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.clients.ClientsDto;
import com.erp.backend_erp.dto.clients.ClientsTableDto;
import com.erp.backend_erp.dto.clients.CreateClientsDto;
import com.erp.backend_erp.dto.clients.UpdateClientsDto;
import com.erp.backend_erp.entity.clients.ClientsEntity;
import com.erp.backend_erp.mappers.clients.ClientsMapper;
import com.erp.backend_erp.repositories.clients.ClientsJPARepository;
import com.erp.backend_erp.repositories.clients.ClientsQueryRepository;
import com.erp.backend_erp.services.ClientsService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@Service
public class ClientsServiceImpl implements ClientsService {
    
    private final ClientsQueryRepository clientsQueryRepository;
    private final ClientsJPARepository clientsJPARepository;
    private final ClientsMapper clientsMapper;
    
    public ClientsServiceImpl(ClientsQueryRepository clientsQueryRepository, ClientsJPARepository clientsJPARepository, ClientsMapper clientsMapper) {
        this.clientsQueryRepository = clientsQueryRepository;
        this.clientsMapper = clientsMapper;
        this.clientsJPARepository = clientsJPARepository;
    }

        @Override
        public ClientsDto create(CreateClientsDto createClientsDto) {
            Boolean exists = clientsQueryRepository.findByDocument(createClientsDto.getDocument().toLowerCase());
            if (exists)
                throw new GlobalException(HttpStatus.BAD_REQUEST, "El Documento del Cliente ya se encuentra registrado");

            try {
                ClientsEntity clientsEntity = clientsMapper
                        .createToEntity(createClientsDto);
                ClientsEntity saveClientsEntity = clientsJPARepository
                        .save(clientsEntity);
                        return clientsMapper.toDto(saveClientsEntity);
            } catch (Exception e) {
                System.err.println("Error al crear el usuario: " + e.toString());
                e.printStackTrace();
                throw new RuntimeException("Error al crear el usuario: " + e.getMessage(), e);
            }
        }

        @Override
        public Boolean update(UpdateClientsDto updateClientsDto) {
            ClientsEntity roleEntity = clientsJPARepository.findById(updateClientsDto.getId())
                    .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
            try {
                clientsMapper.updateEntityFromDto(updateClientsDto, roleEntity);
                clientsJPARepository.save(roleEntity);
                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al actualizar el registro");
            }
        }

    @Override
    public PageImpl<ClientsTableDto> pageClients(PageableDto<Object> pageableDto) {
        return clientsQueryRepository.listConventions(pageableDto);
    }
}
