package com.example.inscricao.services;

import com.example.inscricao.config.RabbitMQConfig;
import com.example.inscricao.controller.EventoClient;
import com.example.inscricao.domain.Inscricao;
import com.example.inscricao.dto.EventoResponse;
import com.example.inscricao.dto.InscricaoRequest;
import com.example.inscricao.dto.InscricaoResponse;
import com.example.inscricao.exceptions.RecursoNaoEncontradoException;
import com.example.inscricao.mapper.InscricaoMapper;
import com.example.inscricao.repository.InscricaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InscricaoService {

    private static final String INSCRICAO_NAO_ENCONTRADA = "Inscrição não encontrada!";

    private final InscricaoRepository inscricaoRepository;
    private final InscricaoMapper inscricaoMapper;
    private final EventoClient eventoClient;
    private final RabbitTemplate rabbitTemplate;


    public InscricaoResponse inscrever(InscricaoRequest request) {
        EventoResponse evento = eventoClient.buscarPorId(request.eventoId());

        Inscricao inscricao = Inscricao.builder()
                .eventoId(request.eventoId())
                .nomeParticipante(request.nomeParticipante())
                .email(request.email())
                .dataInscricao(LocalDateTime.now())
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, request.email());
        inscricao = inscricaoRepository.save(inscricao);

        return inscricaoMapper.toDTO(inscricao, evento.nome());
    }

    public InscricaoResponse atualizar(Long id, InscricaoRequest request) {

        Inscricao inscricao = inscricaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(INSCRICAO_NAO_ENCONTRADA));

        EventoResponse evento = eventoClient.buscarPorId(request.eventoId());

        inscricao.setEventoId(request.eventoId());
        inscricao.setNomeParticipante(request.nomeParticipante());
        inscricao.setEmail(request.email());

        inscricao = inscricaoRepository.save(inscricao);

        return inscricaoMapper.toDTO(inscricao, evento.nome());
    }

    public InscricaoResponse buscarPorId(Long id) {
        Inscricao inscricao = inscricaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(INSCRICAO_NAO_ENCONTRADA));

        EventoResponse evento = eventoClient.buscarPorId(inscricao.getEventoId());

        return inscricaoMapper.toDTO(inscricao, evento.nome());
    }

    public List<InscricaoResponse> buscarTodos() {
        List<Inscricao> inscricoes = inscricaoRepository.findAll();

        return inscricoes.stream()
                .map(inscricao -> {
                    EventoResponse evento = eventoClient.buscarPorId(inscricao.getEventoId());
                    return inscricaoMapper.toDTO(inscricao, evento.nome());
                })
                .toList();
    }

    public List<InscricaoResponse> buscarPorEvento(Long eventoId) {
        EventoResponse evento = eventoClient.buscarPorId(eventoId);

        return inscricaoRepository.findByEventoId(eventoId).stream()
                .map(inscricao -> inscricaoMapper.toDTO(inscricao, evento.nome()))
                .toList();
    }

    public void cancelar(Long id) {
        Inscricao inscricao = inscricaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(INSCRICAO_NAO_ENCONTRADA));

        inscricaoRepository.delete(inscricao);
    }
}