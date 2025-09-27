package fiap.tech.challenge.marcador_consultas.consulta_producer.controller;

import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import fiap.tech.challenge.marcador_consultas.consulta_producer.service.ConsultaPublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendarConsulta")
public class ConsultaController {

    private ConsultaPublisherService consultaPublisherService;

    public ConsultaController(ConsultaPublisherService consultaPublisherService) {
        this.consultaPublisherService = consultaPublisherService;
    }

    @PostMapping
    public ResponseEntity<String> agendarConsulta(@RequestBody ConsultaIn consultaIn) {
        consultaPublisherService.sendNewConsulta(consultaIn);
        return ResponseEntity.ok("Solicitacao de consulta enviada para marcação");
    }
}
