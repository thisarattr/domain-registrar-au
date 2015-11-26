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
	
	/**
	 * Calculate total for domain name list per each year <domainName, # of years>
	 * 
	 * @param domainList Map<domainName, # of years>
	 * @return BigDecimal total
	 * @throws DomainRegistrarBusinessException
	 */
	public BigDecimal calculate(Map<String, Double> domainList) throws DomainRegistrarBusinessException;
	/**
	 * Process console input arguments and populate domain list and return file path if there is any.
	 * -p --> file path string
	 * -n --> [domainName,#of years] 
	 * 
	 * @param args array of console arguments
	 * @param domainList Map<domainName, # of years>
	 * @return String file path if there is any
	 */
	public String processInput(String[] args, Map<String, Double> domainList) ;
	
	/**
	 * Read file path and populate domain name map with list of values 
	 * 
	 * @param filePath String file path
	 * @param domainList Map<domainName, # of years>
	 * @throws IOException
	 */
	public void readFile(String filePath, Map<String, Double> domainList) throws IOException;

}
