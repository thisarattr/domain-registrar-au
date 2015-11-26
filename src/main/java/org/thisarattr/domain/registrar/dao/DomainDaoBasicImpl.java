/**
 * 
 */
package org.thisarattr.domain.registrar.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.apache.log4j.Logger;
import org.thisarattr.domain.registrar.exception.DomainRegistrarDaoException;
import org.thisarattr.domain.registrar.model.Domain;
import org.thisarattr.domain.registrar.model.Domain.DomainType;

/**
 * @author Thisara
 *
 */
@Singleton
public class DomainDaoBasicImpl implements DomainDao {
	
	private Logger logger = (Logger) Logger.getLogger(DomainDaoBasicImpl.class);
	private List<Domain> domainList = null;
	private AtomicLong seq = null;
	
	public DomainDaoBasicImpl() throws DomainRegistrarDaoException {
		super();
		domainList = new ArrayList<Domain>();
		seq = new AtomicLong();
		logger.debug("instantiating dao and inserting init values.");
		
		// ordinary
		Domain com = new Domain(".com", DomainType.ORDINARY, new BigDecimal(10));
		this.create(com);
		Domain net = new Domain(".net", DomainType.ORDINARY, new BigDecimal(9));
		this.create(net);
		Domain comAu = new Domain(".com.au", DomainType.ORDINARY, new BigDecimal(20));
		this.create(comAu);

		// premium
		Domain appleComAu = new Domain("apple.com.au", DomainType.PREMIUM, new BigDecimal(1000));
		this.create(appleComAu);
		Domain dictCom = new Domain("dict.com", DomainType.PREMIUM, new BigDecimal(800));
		this.create(dictCom);
		Domain educationNet = new Domain("education.net", DomainType.PREMIUM, new BigDecimal(300));
		this.create(educationNet);
	}

	/* (non-Javadoc)
	 * @see org.thisarattr.domain.registrar.dao.DomainDao#create(org.thisarattr.domain.registrar.models.Domain)
	 */
	@Override
	public Boolean create(Domain domain) throws DomainRegistrarDaoException {
		if (domain.getName() == null || domain.getPrice() == null || domain.getType() == null) {
			throw new DomainRegistrarDaoException("name, price and type are mandetory fields to create a new domain.");
		}
		if (get(domain.getName(), domain.getType()) == null) {
			domain.setId(seq.incrementAndGet());
			domain.setName(domain.getName().toLowerCase());
			domainList.add(domain);
			return true;
		} else {
			throw new DomainRegistrarDaoException("domain name:" + domain.getName() + ", type:" + domain.getType() + " already exsits!");
		}
	}

	/* (non-Javadoc)
	 * @see org.thisarattr.domain.registrar.dao.DomainDao#getPremium(java.lang.String)
	 */
	@Override
	public Domain getPremium(String name) throws DomainRegistrarDaoException {
		return get(name, DomainType.PREMIUM);
	}

	/* (non-Javadoc)
	 * @see org.thisarattr.domain.registrar.dao.DomainDao#get(java.lang.String, org.thisarattr.domain.registrar.model.Domain.DomainType)
	 */
	@Override
	public Domain get(String name, DomainType type) throws DomainRegistrarDaoException {
		if (name == null || type == null) {
			throw new DomainRegistrarDaoException("domain name and type are madetory to get domain!");
		}
		List<Domain> list = domainList.stream().filter(p -> p.getType() == type && name.endsWith(p.getName())).collect(Collectors.toList());

		if (list.size() == 1) {
			return list.get(0);
		} else if (list.size() == 0) {
			return null;
		} else {
			throw new DomainRegistrarDaoException("mulpitle matches found for domain name, error setting up domain name hierarchy");
		}
	}

	/* (non-Javadoc)
	 * @see org.thisarattr.domain.registrar.dao.DomainDao#getOrdinary(java.lang.String)
	 */
	@Override
	public Domain getOrdinary(String name) throws DomainRegistrarDaoException {
		return get(name, DomainType.ORDINARY);
	}

}
