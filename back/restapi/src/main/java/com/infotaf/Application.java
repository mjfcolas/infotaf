package com.infotaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.infotaf.taffilemanager.TafFileManager;

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class Application{
			
    public static void main(String[] args) {
    	ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
    
    	TafFileManager fileWatcher = ctx.getBean(TafFileManager.class);
    	try {
			fileWatcher.checkFiles();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    }
}
