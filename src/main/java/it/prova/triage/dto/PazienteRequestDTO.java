package it.prova.triage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PazienteRequestDTO {

	private String codiceFiscale;
	private String codFiscalePazienteAttualmenteInVisita;	
	private String codiceDottore;
}
