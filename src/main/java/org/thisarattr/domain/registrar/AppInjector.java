package org.thisarattr.domain.registrar;

import org.thisarattr.domain.registrar.dao.DomainDao;
import org.thisarattr.domain.registrar.dao.DomainDaoBasicImpl;
import org.thisarattr.domain.registrar.service.DomainRegistrarService;
import org.thisarattr.domain.registrar.service.DomainRegistrarServiceImpl;

import com.google.inject.AbstractModule;


public class AppInjector extends AbstractModule {

	@Override
	protected void configure() {
		bind(DomainDao.class).to(DomainDaoBasicImpl.class);
		bind(DomainRegistrarService.class).to(DomainRegistrarServiceImpl.class);
	}

}
