package it.prova.triage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DottoreResponseDTO {

	private String codiceDottore;
	private String nome;
	private String cognome;
}
