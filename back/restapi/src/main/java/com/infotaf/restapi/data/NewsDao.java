package com.infotaf.restapi.data;


import org.springframework.stereotype.Repository;

import com.infotaf.restapi.model.News;

@Repository
public class NewsDao extends GenericDao<News> implements INewsDao{
		
}