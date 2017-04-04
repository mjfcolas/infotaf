package com.infotaf.filewatcher;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.infotaf.restapi.model.Manip;
import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.model.PgManip;
import com.infotaf.restapi.service.ManipService;
import com.infotaf.restapi.service.PgService;
import com.opencsv.CSVReader;

/**
 * Fichier taf serialisé mais brut
 * @author emmanuel
 *
 */
@Component
@Scope(value = "prototype")
public class RawTafFile {
	
	/**
	 * Hashmap contenant le type des colonnes (première ligne du fichier
	 */
	private Map<Integer, String> columnsMap = new HashMap<Integer, String>();
	/**
	 * Hashmap contenant le type des lignes (première colonne du fichier
	 */
	private Map<Integer, String> rowsMap = new HashMap<Integer, String>();
	/**
	 * Tout le fichier dans une double liste
	 */
	private List<List<String>> content = new ArrayList<List<String>>();
	
	//Clés de lignes et colonnes utilisées dans le fichier
	private final String pgKey = "PG";
	private final String priceKey = "PRICE";
	private final String headKey = "HEAD";
	private final String numsKey = "NUMS";
	private final String lastnameKey = "LASTNAME";
	private final String firstnameKey = "FIRSTNAME";
	private final String manipKey = "MANIP";
	private final String cotizKey = "COTIZ";
	private final String apportKey = "APPORT";
	
	//Valeurs par défaut du Tbk et de la proms si pas présent dans le fichier
	private final String defaultTbk = "Li";
	private final String defaultProms = "212";
	
	@Autowired
	protected PgService pgService;
	@Autowired
	protected ManipService manipService;
	
	/**
	 * Parse d'un fichier Csv
	 * @param reader :CSVReader
	 * @throws IOException
	 */
	public RawTafFile(CSVReader reader) throws IOException {
		String[] rawLine;
		int i = 0;
		int j = 0;
        while ((rawLine = reader.readNext()) != null) {
        	List<String> parsedline = new ArrayList<String>();
            for (String cell : rawLine) {
				if(j == 0 && i != 0){//Première ligne
					if(cell != null && !cell.equals("")){
						columnsMap.put(i, cell);
					}
				}else if(i == 0 && j != 0){//Première colonne
					if(cell != null && !cell.equals("")){
						rowsMap.put(j, cell);
					}
				}
				parsedline.add(cell == null ? "" : cell);
				i++;
			}
            content.add(parsedline);
            i=0;
            j++;
        }
	}
	
	/**
	 * Récupération des index de colonnes pour une clé donnée
	 * @param key : La clé à considérer (Voir attributs de la classe
	 * @return Liste d'index
	 */
	private List<Integer> getCidsForKey(String key){
		List<Integer> result = new ArrayList<Integer>();
		for (Integer mapKey : columnsMap.keySet()) {
			if(columnsMap.get(mapKey) != null && columnsMap.get(mapKey).equals(key)){
				result.add(mapKey);
			}
		}
		return result;
	}
	/**
	 * Récupération des index de colonnes pour plusieurs clés
	 * @param key : Les clés à considérer (Voir attributs de la classe)
	 * @return Liste d'index
	 */
	private List<Integer> getCidsForKeys(List<String> keys){
		List<Integer> result = new ArrayList<Integer>();
		for (Integer mapKey : columnsMap.keySet()) {
			if(columnsMap.get(mapKey) != null && keys.contains(columnsMap.get(mapKey))){
				result.add(mapKey);
			}
		}
		return result;
	}
	/**
	 * Récupération des index de lignes pour une clé donnée
	 * @param key : La clé à considérer (Voir attributs de la classe)
	 * @return Liste d'index
	 */
	private List<Integer> getRidsForKey(String key){
		List<Integer> result = new ArrayList<Integer>();
		for (Integer mapKey : rowsMap.keySet()) {
			if(rowsMap.get(mapKey) != null && rowsMap.get(mapKey).equals(key)){
				result.add(mapKey);
			}
		}
		return result;
	}
	/**
	 * Récupération du premier index de colonne pour une clé donnée
	 * @param key : La clé à considérer (Voir attributs de la classe)
	 * @return index
	 */
	private Integer getCidForKey(String key){
		for (Integer mapKey : columnsMap.keySet()) {
			if(columnsMap.get(mapKey) != null && columnsMap.get(mapKey).equals(key)){
				return mapKey;
			}
		}
		return null;
	}
	/**
	 * Récupération du premier index de ligne pour une clé donnée
	 * @param key : La clé à considérer (Voir attributs de la classe)
	 * @return index
	 */
	private Integer getRidForKey(String key){
		for (Integer mapKey : rowsMap.keySet()) {
			if(rowsMap.get(mapKey) != null && rowsMap.get(mapKey).equals(key)){
				return mapKey;
			}
		}
		return null;
	}
	
	/**
	 * Récupère les Pgs présent dans le fichier de taf
	 * @return List de Pg pret à êtr eenregistrés en base
	 */
	public List<Pg> getPgs(){
		List<Pg> pgs = new ArrayList<Pg>();
		List<Integer> pgLineIds = getRidsForKey(pgKey);
		Integer numsColId = getCidForKey(numsKey);
		Integer lastnameColId = getCidForKey(lastnameKey);
		Integer firstnameColId = getCidForKey(firstnameKey);
		
		for (Integer currentRid : pgLineIds) {
			List<String> currentLine = content.get(currentRid);
			Pg currentPg = new Pg();
			currentPg.setFirstName(currentLine.get(firstnameColId));
			currentPg.setLastName(currentLine.get(lastnameColId));
			currentPg.setNums(currentLine.get(numsColId));
			currentPg.setProms(defaultProms);
			currentPg.setTbk(defaultTbk);
			pgs.add(currentPg);
		}
		
		
		return pgs;
	}
	
	/**
	 * Récupère les manips/cotizs/apports présentes dans le fichier de taf
	 * @return Liste des manips prète à être enregistrée en base
	 */
	public List<Manip> getManips(){
		List<Manip> manips = new ArrayList<Manip>();
		List<String> keysForPgManips = new ArrayList<String>();
		keysForPgManips.add(cotizKey);
		keysForPgManips.add(manipKey);
		keysForPgManips.add(apportKey);
		List<Integer> manipColIds = getCidsForKeys(keysForPgManips);
		Integer nameRowId = getRidForKey(headKey);
		Integer priceRowId = getRidForKey(priceKey);
		
		for (Integer currentColid : manipColIds) {
			Manip currentManip = new Manip();
			currentManip.setNom(content.get(nameRowId).get(currentColid));
			String rawPrice = content.get(priceRowId).get(currentColid);
			rawPrice = rawPrice.replace(',', '.');
			BigDecimal price = new BigDecimal(rawPrice);
			currentManip.setPrix(price);
			if(columnsMap.get(currentColid).equals(manipKey)){
				currentManip.setType(1);
			}else if(columnsMap.get(currentColid).equals(cotizKey)){
				currentManip.setType(2);
			}else if(columnsMap.get(currentColid).equals(apportKey)){
				currentManip.setType(3);
			}
			manips.add(currentManip);
		}
		
		return manips;
	}
	
	/**
	 * Récupération des participations des Pgs aux manips
	 * @return Liste de PgManip prète à être enregistrée en base
	 */
	public List<PgManip> getPgManips(){
		List<PgManip> pgManips = new ArrayList<PgManip>();
		List<String> keysForPgManips = new ArrayList<String>();
		keysForPgManips.add(cotizKey);
		keysForPgManips.add(manipKey);
		keysForPgManips.add(apportKey);
		
		List<Integer> manipColIds = getCidsForKeys(keysForPgManips);
		List<Integer> pgLineIds = getRidsForKey(pgKey);
		Integer headRowId = getRidForKey(headKey);
		Integer numsColId = getCidForKey(numsKey);
		
		for (Integer pgRowId : pgLineIds) {
			for (Integer manipColId : manipColIds) {
				try{
					String qteString = content.get(pgRowId).get(manipColId).replace(',', '.');
					if(qteString != null){
						//Remplacement des valeurs vides par des 0, Utile pour gérer les cotizs
						if(qteString.equals("")){
							qteString = "0";
						}
						String manipName = content.get(headRowId).get(manipColId);
						String pgNums = content.get(pgRowId).get(numsColId);
						Manip currentManip = manipService.getManipByName(manipName);
						Pg currentPg = pgService.getPgByNumsTbkProms(pgNums, defaultTbk, defaultProms);
						BigDecimal quantite = new BigDecimal(qteString);
						try{
							PgManip currentPgManip = new PgManip();
							currentPgManip.setManip(currentManip);
							currentPgManip.setPg(currentPg);
							currentPgManip.setQuantite(quantite);
							pgManips.add(currentPgManip);
						}catch(NumberFormatException e){
							System.out.println("Impossible de parser : "+ qteString);
							throw new NumberFormatException();
						}
					}
				}catch(Exception e){
					System.out.println("Erreur pour la case " + pgRowId.toString() + " " + manipColId.toString());
				}
			}
		}
		
		return pgManips;
	}
}