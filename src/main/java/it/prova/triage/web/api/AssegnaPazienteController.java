package it.prova.triage.web.api;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import it.prova.triage.dto.DottoreResponseDTO;
import it.prova.triage.service.PazienteService;

@RestController
@RequestMapping("/api/assegnaPaziente")
public class AssegnaPazienteController {

	private static final Logger LOGGER = LogManager.getLogger(AssegnaPazienteController.class);

	@Autowired
	private WebClient webClient;

	@Autowired
	private PazienteService pazienteService;

	@GetMapping("/{cd}")
	public DottoreResponseDTO verificaDisponibilitaDottore(@PathVariable(required = true) String cd) {

		LOGGER.info(".........invocazione servizio esterno............");

		DottoreResponseDTO dottoreResponseDTO = webClient.get().uri("/verifica/" + cd).retrieve()
				.bodyToMono(DottoreResponseDTO.class).block();

		LOGGER.info(".........invocazione servizio esterno completata............");

		return dottoreResponseDTO;
	}
	
}
