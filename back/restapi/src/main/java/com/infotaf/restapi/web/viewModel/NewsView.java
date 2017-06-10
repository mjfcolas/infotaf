package com.infotaf.restapi.web.viewModel;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infotaf.restapi.model.News;

/**
 * Objet représentant une news
 * @author emmanuel
 *
 */
public class NewsView{
	
	private static final Logger logger = LoggerFactory.getLogger(ManipBase.class);
	
	protected int id;
	protected String title;
	protected String content;
	protected Date date;
	
	NewsView(News news){
		logger.debug("IN - news.id: {}", news.getId());
		this.id = news.getId();
		this.title = news.getTitle();
		this.content=news.getContent();
		this.date=news.getDate();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}