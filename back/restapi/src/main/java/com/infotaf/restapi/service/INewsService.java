package com.infotaf.restapi.service;

import java.util.List;

import com.infotaf.restapi.model.News;

public interface INewsService{
	/**
	 * Récupération des news en base
	 * @return
	 */
	public List<News> getNews();
	/**
	 * Sauvegarde d'une news après lui avoir ajouté la date actuelle
	 * @param news news à sauvegarder sans la date
	 * @return L'id de l'objet sauvegardé
	 */
	public int saveNews(News news);
	/**
	 * Suppression d'une news
	 * @param newsId ID de la news
	 */
	public void deleteNews(int newsId);
}