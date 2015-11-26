package org.thisarattr.domain.registrar.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.thisarattr.domain.registrar.dao.DomainDao;
import org.thisarattr.domain.registrar.exception.DomainRegistrarBusinessException;
import org.thisarattr.domain.registrar.exception.DomainRegistrarDaoException;
import org.thisarattr.domain.registrar.model.Domain;
import org.thisarattr.domain.registrar.model.Domain.DomainType;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author Thisara
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DomainRegistrarServiceImplTest {

	private DomainRegistrarService domainRegistrarService;
	@Mock
	private DomainDao domainDao;
	
	
	@Before
	public void setUp() throws Exception {
		Injector appInjector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(DomainDao.class).toInstance(domainDao);
				bind(DomainRegistrarService.class).to(DomainRegistrarServiceImpl.class);
			}
		});
		domainRegistrarService = appInjector.getInstance(DomainRegistrarService.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=DomainRegistrarBusinessException.class)
	public void negativeTestCalculateWNullDomainList() throws DomainRegistrarBusinessException {
		domainRegistrarService.calculate(null);
	}
	
	@Test
	public void testCalculateDomainList() throws DomainRegistrarBusinessException, DomainRegistrarDaoException {
		doReturn(new Domain(".com", DomainType.ORDINARY, new BigDecimal(10))).when(domainDao).get("a-domain.com", DomainType.ORDINARY);
		doReturn(new Domain(".net", DomainType.ORDINARY, new BigDecimal(9))).when(domainDao).get("another-domain.net", DomainType.ORDINARY);
		doReturn(new Domain("dict.com", DomainType.PREMIUM, new BigDecimal(800))).when(domainDao).get("dict.com", DomainType.PREMIUM);
		
		Map<String, Double> domainList = new HashMap<String, Double>();
		domainList.put("a-domain.com", 1D);
		domainList.put("another-domain.net", 2D);
		domainList.put("dict.com", 5D);
		BigDecimal sum = domainRegistrarService.calculate(domainList);
		assertEquals(new BigDecimal(4028), sum);
		
		Mockito.verify(domainDao).get("a-domain.com", DomainType.ORDINARY);
		Mockito.verify(domainDao).get("another-domain.net", DomainType.ORDINARY);
		Mockito.verify(domainDao).get("dict.com", DomainType.PREMIUM);
	}
	
	@Test
	public void testCalculateWInvalidDomain() throws DomainRegistrarBusinessException, DomainRegistrarDaoException {
		doReturn(new Domain(".com", DomainType.ORDINARY, new BigDecimal(10))).when(domainDao).get("a-domain.com", DomainType.ORDINARY);
		
		Map<String, Double> domainList = new HashMap<String, Double>();
		domainList.put("a-domain.com", 1D);
		domainList.put("another-domain.net", 2D);
		BigDecimal sum = domainRegistrarService.calculate(domainList);
		assertEquals(new BigDecimal(10), sum);
		
		Mockito.verify(domainDao).get("a-domain.com", DomainType.ORDINARY);
	}

}
