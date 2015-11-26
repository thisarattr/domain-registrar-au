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
	
	public Boolean create(Domain domain) throws DomainRegistrarDaoException;
	public Domain get(String name, DomainType type) throws DomainRegistrarDaoException;
	public Domain getPremium(String name) throws DomainRegistrarDaoException;
	public Domain getOrdinary(String name) throws DomainRegistrarDaoException;

}
