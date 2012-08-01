/**
 * 
 */
package de.uniol.inf.is.soop.sensorregistry.sensors;

import java.util.List;

/**
 * @author jbrode
 *
 */
public class ScaiSensor implements ISensor {

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.soop.sensorregistry.sensors.ISensor#getValue(java.lang.String)
	 */
	private String name;
	private String domain;
	private String url;
	private List<String> attributes;
	
	public ScaiSensor(String url, String domain, List<String> attributes){
		this.domain = domain;
		this.url = url;
		this.attributes = attributes;
	}
	
	public ScaiSensor(){}
	
	public String getValue(String attId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the attributes
	 */
	public List<String> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
