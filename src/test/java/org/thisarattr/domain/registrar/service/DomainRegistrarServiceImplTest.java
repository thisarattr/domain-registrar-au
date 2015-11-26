package org.thisarattr.domain.registrar.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.NoSuchFileException;
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
	
	@Test
	public void testReadFile() throws IOException {
		String path = "resources/domain-names.csv";
		Map<String, Double> domainList =  new HashMap<String, Double>();
		domainRegistrarService.readFile(path, domainList);
		assertEquals(1.0d, domainList.get("a-domain.com"), 0.001);
		assertEquals(2.0d, domainList.get("another-domain.net"), 0.001);
		assertEquals(5.0d, domainList.get("dict.com"), 0.001);
	}
	
	@Test
	public void testReadFileNConsoleInputs() throws IOException {
		String path = "resources/domain-names.csv";
		Map<String, Double> domainList =  new HashMap<String, Double>();
		domainList.put("a-domain.com", 2.0D);
		domainRegistrarService.readFile(path, domainList);
		assertEquals(3.0d, domainList.get("a-domain.com"), 0.001);
		assertEquals(2.0d, domainList.get("another-domain.net"), 0.001);
		assertEquals(5.0d, domainList.get("dict.com"), 0.001);
	
	}
	
	@Test(expected=NoSuchFileException.class)
	public void negativeTestReadFile() throws IOException {
		String path = "resources/domain-names.csv.invalid";
		Map<String, Double> domainList =  new HashMap<String, Double>();
		domainRegistrarService.readFile(path, domainList);
	}
	
	@Test
	public void testReadFileSkipParseError() throws IOException {
		String path = "resources/test-domain-names.csv";
		Map<String, Double> domainList =  new HashMap<String, Double>();
		domainRegistrarService.readFile(path, domainList);
		assertEquals(null, domainList.get("a-domain.com"));
		assertEquals(2.0d, domainList.get("another-domain.net"), 0.001);
		assertEquals(null, domainList.get("dict.com"));
	}

	@Test
	public void testProcessInputBothNP() throws IOException {
		Map<String, Double> domainList =  new HashMap<String, Double>();
		String[] args = new String[] {"-n", "a-domain.com,2", "-p", "resources/domain-names.csv", "-n", "dict.com,1"};
		String filePath = domainRegistrarService.processInput(args, domainList);
		assertEquals(2d, domainList.get("a-domain.com"), 0.001);
		assertEquals(null, domainList.get("another-domain.net"));
		assertEquals(1d, domainList.get("dict.com") ,0.001);
		assertEquals("resources/domain-names.csv", filePath);
	}
	
	@Test
	public void testProcessInputPath() throws IOException {
		Map<String, Double> domainList =  new HashMap<String, Double>();
		String[] args = new String[] {"-p", "resources/domain-names.csv"};
		String filePath = domainRegistrarService.processInput(args, domainList);
		assertEquals("resources/domain-names.csv", filePath);
	}
	
	@Test
	public void testProcessInputParams() throws IOException {
		Map<String, Double> domainList =  new HashMap<String, Double>();
		String[] args = new String[] {"-n", "a-domain.com,2", "-n", "dict.com,1"};
		domainRegistrarService.processInput(args, domainList);
		assertEquals(2d, domainList.get("a-domain.com"), 0.001);
		assertEquals(null, domainList.get("another-domain.net"));
		assertEquals(1d, domainList.get("dict.com") ,0.001);
	}
	
	@Test
	public void testProcessInputInvalidParams() throws IOException {
		Map<String, Double> domainList =  new HashMap<String, Double>();
		String[] args = new String[] {"-n", "-n", "a-domain.com,2", "-n", "dict.com,1"};
		domainRegistrarService.processInput(args, domainList);
		assertEquals(2d, domainList.get("a-domain.com"), 0.001);
		assertEquals(null, domainList.get("another-domain.net"));
		assertEquals(1d, domainList.get("dict.com") ,0.001);
	}
}
