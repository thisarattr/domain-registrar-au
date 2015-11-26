/**
 * 
 */
package org.thisarattr.domain.registrar.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;
import org.thisarattr.domain.registrar.dao.DomainDao;
import org.thisarattr.domain.registrar.exception.DomainRegistrarBusinessException;
import org.thisarattr.domain.registrar.exception.DomainRegistrarDaoException;
import org.thisarattr.domain.registrar.model.Domain;
import org.thisarattr.domain.registrar.model.Domain.DomainType;



/**
 * @author Thisara
 *
 */
@Singleton
public class DomainRegistrarServiceImpl implements DomainRegistrarService {
	
	@Inject
	private DomainDao domainDao;
	private Logger logger = (Logger) Logger.getLogger(DomainRegistrarServiceImpl.class);

	/* (non-Javadoc)
	 * @see org.thisarattr.domain.registrar.service.DomainRegistrarService#calculate(java.util.Map)
	 */
	@Override
	public BigDecimal calculate(Map<String, Double> domainList) throws DomainRegistrarBusinessException {
		if (domainList == null || domainList.isEmpty()) {
			throw new DomainRegistrarBusinessException("input domain list is empty");
		}
		BigDecimal sum = BigDecimal.ZERO;
		for (Entry<String, Double> record : domainList.entrySet()) {
			Domain domain = getDomain(record);
			sum = sumTotal(sum, record, domain);
		}
		return sum;
	}

	private Domain getDomain(Entry<String, Double> record) {
		Domain domain = null;
		try {
			domain = domainDao.get(record.getKey(), DomainType.PREMIUM);
			if (domain == null) {
				domain = domainDao.get(record.getKey(), DomainType.ORDINARY);
			}
		} catch (DomainRegistrarDaoException e) {
			logger.warn("failed to get domain for key:" + record.getKey() , e);
		}
		return domain;
	}

	private BigDecimal sumTotal(BigDecimal sum, Entry<String, Double> record, Domain domain) {
		String format = "%s registered for %s %s = %s";
		if (domain == null) {
			logger.info(String.format(format, record.getKey(), ((record.getValue() > 1) ? record.getValue() + " years" : record.getValue() + " year"), "--not found--", "$0.0"));
		} else {
			BigDecimal price = domain.getPrice().multiply(new BigDecimal(record.getValue()));
			logger.info(String.format(format, record.getKey(), ((record.getValue() > 1) ? record.getValue() + " years" : record.getValue() + " year"), "at $" + domain.getPrice().toPlainString() + " per year", NumberFormat
					.getCurrencyInstance().format(price)));
			sum = sum.add(price);
		}
		return sum;
	}

	/* (non-Javadoc)
	 * @see org.thisarattr.domain.registrar.service.DomainRegistrarService#processInput(java.lang.String[], java.util.Map)
	 */
	@Override
	public String processInput(String[] args, Map<String, Double> domainList) {
		String filePath = null;
		for (int i = 0; i < args.length; i++) {
			if ("-p".equals(args[i].trim()) && args.length > i + 1 && !args[i + 1].startsWith("-")) {
				filePath = args[i + 1].trim();
				i++;
			} else if ("-n".equals(args[i].trim()) && args.length > i + 1 && !args[i + 1].startsWith("-")) {
				String[] vals = args[i + 1].trim().split(",");
				populateDomainList(domainList, vals);
				i++;
			}
		}
		return filePath;
	}

	private void populateDomainList(Map<String, Double> domainList, String[] vals) {
		if (vals.length == 2) {
			try {
				String key = vals[0].toLowerCase();
				Double value = Double.valueOf(vals[1]);
				if (domainList.containsKey(key)) {
					domainList.put(key, domainList.get(key) + value);
				} else {
					domainList.put(key, value);
				}
			} catch (NumberFormatException e) {
				logger.info("failed to parse value:" + vals[1] + ", skip...");
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.thisarattr.domain.registrar.service.DomainRegistrarService#readFile(java.lang.String, java.util.Map)
	 */
	@Override
	public void readFile(String filePath, Map<String, Double> domainList) throws IOException {
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			stream.forEach(entry -> {
				String[] vals = entry.split(",");
				populateDomainList(domainList, vals);
			});
		} catch (IOException e) {
			logger.warn("failed to read file:" + filePath);
			throw e;
		}
	}
}
