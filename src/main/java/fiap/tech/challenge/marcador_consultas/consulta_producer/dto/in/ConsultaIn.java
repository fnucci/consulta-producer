package fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in;

import java.time.LocalDateTime;

public record ConsultaIn(
        LocalDateTime dataHoraConsulta,
        Long idPaciente,
        Long idProfissional,
        String especialidade
) {
}
