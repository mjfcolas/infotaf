package com.infotaf.restapi.service;

import java.util.List;

import com.infotaf.restapi.model.News;

public interface INewsService{
	/**
	 * Récupération des news en base
	 * @return
	 */
	public List<News> getNews();
}