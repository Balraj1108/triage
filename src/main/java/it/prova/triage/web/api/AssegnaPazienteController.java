package it.prova.triage.web.api;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import it.prova.triage.dto.DottorePazienteRequestDTO;
import it.prova.triage.dto.DottorePazienteResponseDTO;
import it.prova.triage.dto.DottoreResponseDTO;
import it.prova.triage.dto.PazienteRequestDTO;
import it.prova.triage.service.PazienteService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/assegnaPaziente")
public class AssegnaPazienteController {

	private static final Logger LOGGER = LogManager.getLogger(AssegnaPazienteController.class);

	@Autowired
	private WebClient webClient;

	@Autowired
	private PazienteService pazienteService;

	@PostMapping
	public DottoreResponseDTO verificaDisponibilitaDottore(
			@RequestBody PazienteRequestDTO paziente) {

		LOGGER.info(".........invocazione servizio esterno............");

		DottoreResponseDTO dottoreResponseDTO = webClient.get().uri("/verifica/" + paziente.getCodiceDottore()).retrieve()
				.bodyToMono(DottoreResponseDTO.class).block();
		
		LOGGER.info(".........invocazione servizio esterno completata............");
		
		if (dottoreResponseDTO != null) {
			pazienteService.impostaCodiceDottore(paziente.getCodiceFiscale(), paziente.getCodiceDottore());
			
			LOGGER.info(".........invocazione servizio esterno............");

			ResponseEntity<DottorePazienteResponseDTO> response = webClient
					.post().uri("/impostaVisita").body(
							Mono.just(DottorePazienteRequestDTO.builder().codiceDottore(paziente.getCodiceDottore())
									.codFiscalePazienteAttualmenteInVisita(
											paziente.getCodFiscalePazienteAttualmenteInVisita())
									.build()),
							DottorePazienteRequestDTO.class)
					.retrieve().toEntity(DottorePazienteResponseDTO.class).block();

			LOGGER.info(".........invocazione servizio esterno completata............");
			
		}

		

		return dottoreResponseDTO;
	}
	
}
