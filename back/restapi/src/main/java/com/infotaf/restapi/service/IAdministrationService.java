package com.infotaf.restapi.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.infotaf.restapi.model.BusinessStatus;
import com.infotaf.restapi.web.viewModel.forms.RelaunchForm;

public interface IAdministrationService {

	/**
	 * Relance des PGs qui ont un negat's supérieur à un montant donné
	 * @param form Formulaire contenant le négats
	 * @return BusinessStatus résumant l'état de l'opération
	 */
	public BusinessStatus relaunchPg(RelaunchForm form) throws IOException;
	
	/**
	 * Gestion d'un iumport de fichier de TAF
	 * @param file fichier à importer
	 * @return BusinessStatus résumant l'état de l'opération
	 */
	public BusinessStatus processTafFile(MultipartFile file);
}
