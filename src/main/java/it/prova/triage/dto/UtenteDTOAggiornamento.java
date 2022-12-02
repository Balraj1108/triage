package it.prova.triage.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import it.prova.triage.model.Ruolo;
import it.prova.triage.model.Utente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UtenteDTOAggiornamento {

	private Long id;

	@NotBlank(message = "{username.notblank}")
	@Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String username;

	private String confermaPassword;

	@NotBlank(message = "{nome.notblank}")
	private String nome;

	@NotBlank(message = "{cognome.notblank}")
	private String cognome;

	private Long[] ruoliIds;

	// private Set<Ruolo> ruoli = new HashSet<>(0);

	public Utente buildUtenteModel(boolean includeIdRoles) {
		Utente result = // new Utente(this.id, this.username, this.nome, this.cognome);
				Utente.builder().id(id).username(username).nome(cognome).cognome(cognome).build();

		if (includeIdRoles && ruoliIds != null)
			result.setRuoli(Arrays.asList(ruoliIds).stream().map(id -> Ruolo.builder().id(id).build())
					.collect(Collectors.toList()));
		return result;
	}

	// niente password...
	public static UtenteDTOAggiornamento buildUtenteDTOFromModel(Utente utenteModel) {
		UtenteDTOAggiornamento result = UtenteDTOAggiornamento.builder().id(utenteModel.getId())
				.username(utenteModel.getUsername()).nome(utenteModel.getNome()).cognome(utenteModel.getCognome())
				.build();

		if (!utenteModel.getRuoli().isEmpty())
			result.ruoliIds = utenteModel.getRuoli().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});

		return result;
	}

	public static List<UtenteDTOAggiornamento> createUtenteDTOListFromModelList(List<Utente> modelListInput) {
		return modelListInput.stream().map(registaEntity -> {
			UtenteDTOAggiornamento result = UtenteDTOAggiornamento.buildUtenteDTOFromModel(registaEntity);

			return result;
		}).collect(Collectors.toList());
	}

}
