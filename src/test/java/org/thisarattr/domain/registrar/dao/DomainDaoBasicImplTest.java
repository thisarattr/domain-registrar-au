package org.thisarattr.domain.registrar.dao;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.thisarattr.domain.registrar.exception.DomainRegistrarDaoException;
import org.thisarattr.domain.registrar.model.Domain;
import org.thisarattr.domain.registrar.model.Domain.DomainType;

/**
 * @author Thisara
 *
 */
public class DomainDaoBasicImplTest {
	
	private DomainDao domainDao = null;

	@Before
	public void setUp() throws Exception {
		domainDao = new DomainDaoBasicImpl();
	}

	@Test
	public void testGetOrdinaryDomain() throws DomainRegistrarDaoException {
		Domain domain = domainDao.get("thisara.com", DomainType.ORDINARY);
		assertEquals(".com", domain.getName());
		assertEquals(new BigDecimal(10), domain.getPrice());
	}

	@Test
	public void testGetPremiumDomain() throws DomainRegistrarDaoException {
		Domain domain = domainDao.get("thisara.apple.com.au", DomainType.PREMIUM);
		assertEquals("apple.com.au", domain.getName());
		assertEquals(new BigDecimal(1000), domain.getPrice());
	}
	
	@Test
	public void testGetInvalidDomain() throws DomainRegistrarDaoException {
		Domain domain = domainDao.get("thisara.apple.com.sg", DomainType.PREMIUM);
		assertEquals(null, domain);
	}
	
	@Test(expected=DomainRegistrarDaoException.class)
	public void negativeTestGetNullDomain() throws DomainRegistrarDaoException {
		domainDao.get(null, DomainType.PREMIUM);
	}
	
	@Test(expected=DomainRegistrarDaoException.class)
	public void negativeTestGetDomainWithNullType() throws DomainRegistrarDaoException {
		domainDao.get("thisara.apple.com.au", null);

	}
	
	@Test(expected=DomainRegistrarDaoException.class)
	public void negativeTestCreateExistingDomain() throws DomainRegistrarDaoException {
		Domain domain = new Domain(".com", DomainType.ORDINARY, BigDecimal.ONE);
		domainDao.create(domain);
	}
	
	@Test(expected=DomainRegistrarDaoException.class)
	public void negativeTestCreateDomainWoName() throws DomainRegistrarDaoException {
		Domain domain = new Domain(null, DomainType.ORDINARY, BigDecimal.ONE);
		domainDao.create(domain);
	}
	
	@Test(expected=DomainRegistrarDaoException.class)
	public void negativeTestCreateDomainWoType() throws DomainRegistrarDaoException {
		Domain domain = new Domain(".com", null, BigDecimal.ONE);
		domainDao.create(domain);
	}
	
	@Test(expected=DomainRegistrarDaoException.class)
	public void negativeTestCreateDomainWoPrice() throws DomainRegistrarDaoException {
		Domain domain = new Domain(".com", DomainType.ORDINARY, null);
		domainDao.create(domain);
	}
	
	@Test
	public void testCreateDomain() throws DomainRegistrarDaoException {
		Domain domain = new Domain(".com.sg", DomainType.ORDINARY, BigDecimal.TEN);
		Boolean flag = domainDao.create(domain);
		assertEquals(true, flag);
		
		Domain domainGet = domainDao.getOrdinary("thisara.com.sg");
		assertEquals(".com.sg", domainGet.getName());
	}
	
}
