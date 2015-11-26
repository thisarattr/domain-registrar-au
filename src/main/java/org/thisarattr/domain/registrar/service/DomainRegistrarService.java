/**
 * 
 */
package org.thisarattr.domain.registrar.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.thisarattr.domain.registrar.exception.DomainRegistrarBusinessException;

/**
 * @author Thisara
 *
 */
public interface DomainRegistrarService {
	
	public BigDecimal calculate(Map<String, Double> domainList) throws DomainRegistrarBusinessException;
	public String processInput(String[] args, Map<String, Double> domainList) ;
	public void readFile(String filePath, Map<String, Double> domainList) throws IOException;

}
