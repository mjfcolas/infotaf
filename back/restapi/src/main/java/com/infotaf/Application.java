package com.infotaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.infotaf.filewatcher.FileWatcher;
import com.infotaf.restapi.service.PgService;

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class Application {
	
	@Autowired
	PgService pgService;
	
    public static void main(String[] args) {
    	ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
    
    	FileWatcher fileWatcher = ctx.getBean(FileWatcher.class);
    	try {
			fileWatcher.checkFiles();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
