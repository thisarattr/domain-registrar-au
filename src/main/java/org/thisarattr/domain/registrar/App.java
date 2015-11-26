package org.thisarattr.domain.registrar;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.thisarattr.domain.registrar.exception.DomainRegistrarBusinessException;
import org.thisarattr.domain.registrar.service.DomainRegistrarService;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class App {

	public static void main(String[] args) throws DomainRegistrarBusinessException {
		PropertyConfigurator.configure("log4j.properties");
		Logger logger = (Logger) Logger.getLogger(App.class);
		Injector injector = Guice.createInjector(new AppInjector());
		DomainRegistrarService domainRegistrarService = injector.getInstance(DomainRegistrarService.class);
		
		Map<String, Double> domainList = new HashMap<String, Double>();
		String filePath = domainRegistrarService.processInput(args, domainList);
		if (filePath != null) {
			try {
				domainRegistrarService.readFile(filePath, domainList);
			} catch (IOException e) {
				System.out.println("failed to read file:" + filePath);
			}
		}
		BigDecimal total = domainRegistrarService.calculate(domainList);
		System.out.println("Total: "+total);
		logger.info("Total: "+total);
		
	}
}
