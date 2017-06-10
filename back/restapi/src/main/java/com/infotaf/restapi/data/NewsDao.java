package com.infotaf.restapi.data;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.infotaf.restapi.model.News;

@Repository
public class NewsDao extends GenericDao<News> implements INewsDao{
	
	private static final Logger logger = LoggerFactory.getLogger(NewsDao.class);
	
}