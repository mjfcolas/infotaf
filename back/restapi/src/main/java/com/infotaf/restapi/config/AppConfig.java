package com.infotaf.restapi.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.googleToken.GoogleToken;

/**
 * Configuration de l'application
 * @author emmanuel
 *
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass=true)
@PropertySource(value = { "classpath:application.properties" })
public class AppConfig{

	//Propriétés 
	public static Properties prop = new Properties();
	public static Properties mailProp = new Properties();
	public static Properties configConstants = new Properties();
	public static Properties messages = new Properties();
	
	public static DataSource dataSource = null;
	public static GoogleToken googleToken = null;
	
	public static DecimalFormat df = new DecimalFormat("#.00");
	public static DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
	
	static{
		try {
			prop.load(AppConfig.class.getClassLoader().getResourceAsStream("application.properties"));
			mailProp.load(AppConfig.class.getClassLoader().getResourceAsStream("mail.properties"));
			configConstants.load(AppConfig.class.getClassLoader().getResourceAsStream("configConstants.properties"));
			InputStream messageInput = AppConfig.class.getClassLoader().getResourceAsStream("message.properties");
			messages.load(new InputStreamReader(messageInput, Charset.forName("UTF-8")));
			googleToken = new GoogleToken();
			dfs.setDecimalSeparator('.');
		    df.setDecimalFormatSymbols(dfs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Autowired
	private Environment env;

	/**
	 * Initialize dataSource 
	 * @return DataSource
	 */
	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
		dataSource.setUrl(env.getRequiredProperty("db.url"));
		dataSource.setUsername(env.getRequiredProperty("db.username"));
		dataSource.setPassword(env.getRequiredProperty("db.password"));
		return dataSource;
	}

	/**
	 * Initialize hibernate properties 
	 * @return Properties
	 */
	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", env.getRequiredProperty("db.hibernate.dialect"));
		properties.put("hibernate.show_sql", env.getRequiredProperty("db.hibernate.showsql"));
		properties.put("hibernate.default_schema", env.getRequiredProperty("db.hibernate.schema"));
		return properties;
	}

	/**
	 * Initialize SessionFactory 
	 * @return LocalSessionFactoryBean
	 */
	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(getDataSource());
		sessionFactory.setPackagesToScan(new String[] { "com.infotaf.restapi.model" });
		sessionFactory.setHibernateProperties(getHibernateProperties());
		return sessionFactory;
	}

	/**
	 * Initialize Transaction Manager 
	 * @param sessionFactory
	 * @return HibernateTransactionManager
	 */
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		return txManager;
	}
	
	/**
	 * Utilisé pour pouvoir mapper correctement les objets Lazy loaded par hibernate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Bean
	public Jackson2ObjectMapperBuilder configureObjectMapper() {
	    return new Jackson2ObjectMapperBuilder()
	        .modulesToInstall(Hibernate5Module.class);
	}
}
