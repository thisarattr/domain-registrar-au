/**
 * 
 */
package org.thisarattr.domain.registrar.dao;

import org.thisarattr.domain.registrar.exception.DomainRegistrarDaoException;
import org.thisarattr.domain.registrar.model.Domain;
import org.thisarattr.domain.registrar.model.Domain.DomainType;

/**
 * @author Thisara
 *
 */
public interface DomainDao {
	
	/**
	 * Create domain name record in data store. Name, type and price are mandatory values. It will create the itself.
	 * 
	 * @param domain Domain object
	 * @return Boolean success flag
	 * @throws DomainRegistrarDaoException
	 */
	public Boolean create(Domain domain) throws DomainRegistrarDaoException;
	
	/**
	 * Retrieve the domain name record for suffix and domain type.
	 * 
	 * @param name String domain name suffix
	 * @param type DomainType enum
	 * @return Domain Object and null if no match found
	 * @throws DomainRegistrarDaoException
	 */
	public Domain get(String name, DomainType type) throws DomainRegistrarDaoException;
	
	/**
	 * This will get the matching premium domain name for given suffix.
	 * This internally invoke the same get method.
	 * 
	 * @param name premium domain name suffix
	 * @return matching premium domain object and null if no match found
	 * @throws DomainRegistrarDaoException
	 */
	public Domain getPremium(String name) throws DomainRegistrarDaoException;
	/**
	 * This will get the matching ordinary domain name for given suffix.
	 * This internally invoke the same get method.
	 * 
	 * @param name ordinary domain name suffix
	 * @return matching ordinary domain Object and null if no match found
	 * @throws DomainRegistrarDaoException
	 */
	public Domain getOrdinary(String name) throws DomainRegistrarDaoException;

}
