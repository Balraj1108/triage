package it.prova.triage.web.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.triage.dto.UtenteDTO;
import it.prova.triage.dto.UtenteDTOAggiornamento;
import it.prova.triage.web.api.exception.IdNotNullForInsertException;
import it.prova.triage.web.api.exception.UtenteNotFoundException;
import it.prova.triage.model.Utente;
import it.prova.triage.security.dto.UtenteInfoJWTResponseDTO;
import it.prova.triage.service.UtenteService;

@RestController
@RequestMapping("/api/utente")
public class UtenteController {

	@Autowired
	private UtenteService utenteService;

	// questa mi serve solo per capire se solo ADMIN vi ha accesso
	@GetMapping("/testSoloAdmin")
	public String test() {
		return "OK";
	}
	
	@GetMapping
	public List<UtenteDTO> listaAllUtenti() {
		// senza DTO qui hibernate dava il problema del N + 1 SELECT
		// (probabilmente dovuto alle librerie che serializzano in JSON)
		return UtenteDTO.createUtenteListDTOFromModel(utenteService.listAllUtenti());
	}
	
	@GetMapping("/{id}")
	public UtenteDTO listaUtenteById(@PathVariable(value = "id", required = true) long id) {
		
		return UtenteDTO.buildUtenteDTOFromModel(utenteService.caricaSingoloUtenteConRuoli(id));
	}
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UtenteDTO createNewUtente(@Valid @RequestBody UtenteDTO utenteInput) {
		
		if (utenteInput.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
		
		Utente utente = utenteService.inserisciNuovo(utenteInput.buildUtenteModel(true));
		
		return UtenteDTO.buildUtenteDTOFromModel(utente);
	}
	
	@GetMapping("/abilita/{id}")
	public UtenteDTO abilitaUtenteById(@PathVariable(value = "id", required = true) long id) {
		
		
		return UtenteDTO.buildUtenteDTOFromModel(utenteService.changeUserAbilitation(id));
	}
	
	@PutMapping("/{id}")
	public UtenteDTO update(@Valid @RequestBody UtenteDTOAggiornamento utenteInput, @PathVariable(required = true) Long id) {
		Utente utente = utenteService.caricaSingoloUtenteConRuoli(id);

		if (utente == null)
			throw new UtenteNotFoundException("Utente not found con id: " + id);

		utenteInput.setId(id);
		Utente registaAggiornato = utenteService.aggiorna(utenteInput.buildUtenteModel(true));
		return UtenteDTO.buildUtenteDTOFromModel(registaAggiornato);
	}

	@GetMapping(value = "/userInfo")
	public ResponseEntity<UtenteInfoJWTResponseDTO> getUserInfo() {

		// se sono qui significa che sono autenticato quindi devo estrarre le info dal
		// contesto
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		// estraggo le info dal principal
		Utente utenteLoggato = utenteService.findByUsername(username);
		List<String> ruoli = utenteLoggato.getRuoli().stream().map(item -> item.getCodice())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new UtenteInfoJWTResponseDTO(utenteLoggato.getNome(), utenteLoggato.getCognome(),
				utenteLoggato.getUsername(), utenteLoggato.getEmail(), ruoli));
	}
}
