package it.prova.triage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class DottorePazienteResponseDTO {
	private String codiceDottore;
	private String codFiscalePazienteAttualmenteInVisita;
}