package com.infotaf.restapi.data;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Session;

@SuppressWarnings("unchecked")
public abstract class GenericDao<E> implements IGenericDao<E>{

	@Autowired
	protected SessionFactory sessionFactory;


	private final Class<E> entityClass;

	public GenericDao() {
		this.entityClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}
	
	protected Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	public E get(final Serializable id) {
		return (E) getSession().get(this.entityClass, id);
	}
	
	public Serializable save(E entity) {
		return getSession().save(entity);
	}
	
	public void update(E entity){
		getSession().update(entity);
	}
	
	public void deleteAll() {
		List<E> entities = findAll();
		for (E entity : entities) {
			getSession().delete(entity);
		}
	}

	public List<E> findAll() {
		return getSession().createCriteria(this.entityClass).list();
	}
}