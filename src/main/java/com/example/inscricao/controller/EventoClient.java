package com.example.inscricao.controller;

import com.example.inscricao.dto.EventoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-evento")
public interface EventoClient {

    @GetMapping("/{id}")
    EventoResponse buscarPorId(@PathVariable Long id);
}