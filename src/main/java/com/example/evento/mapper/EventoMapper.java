package com.example.evento.mapper;

import com.example.evento.domain.Evento;
import com.example.evento.dto.EventoRequestDTO;
import com.example.evento.dto.EventoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    Evento toEntity(EventoRequestDTO dto);

    EventoResponseDTO toDTO(Evento entity);

}