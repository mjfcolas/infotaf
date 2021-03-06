package com.infotaf.restapi.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infotaf.restapi.data.NewsDao;
import com.infotaf.restapi.model.News;

@Service
public class NewsService implements INewsService{
    
	private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
	
    @Autowired
    protected NewsDao newsDao;
    
    @Transactional(readOnly = true)
    public List<News> getNews(){
    	logger.debug("IN");
    	return newsDao.findAll();
	}
    
    @Transactional(readOnly = false)
    public int saveNews(News news){
    	logger.debug("IN");
    	news.setDate(new Date());
    	newsDao.save(news);
    	return news.getId();
	}
    
    @Transactional(readOnly = false)
    public void deleteNews(int newsId){
    	logger.debug("IN");
    	News news = new News();
    	news.setId(newsId);
    	newsDao.delete(news);
	}
}