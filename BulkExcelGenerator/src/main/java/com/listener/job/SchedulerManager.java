package com.listener.job;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.io.jobs.ExcelGenerator;


@WebListener
public class SchedulerManager implements ServletContextListener {

	private ScheduledExecutorService scheduler;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		scheduler.shutdownNow();
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new ExcelGenerator(), 5, 60, TimeUnit.SECONDS);
        
		
	}
	
}
