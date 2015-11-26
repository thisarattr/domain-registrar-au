/**
 * 
 */
package org.thisarattr.domain.registrar.model;

import java.math.BigDecimal;

/**
 * @author Thisara
 *
 */
public class Domain {
	
	public static enum DomainType {ORDINARY, PREMIUM}
	
	/**
	 * @param id
	 * @param name
	 * @param type
	 * @param price
	 */
	public Domain(String name, DomainType type, BigDecimal price) {
		super();
		this.name = name;
		this.type = type;
		this.price = price;
	}
	
	
	private Long id;
	private String name;
	private DomainType type;
	private BigDecimal price;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DomainType getType() {
		return type;
	}
	public void setType(DomainType type) {
		this.type = type;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
