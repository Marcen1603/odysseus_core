package de.uniol.inf.is.odysseus.codegenerator.osgi;

/**
 * Helper class to handle the OSGi-Bundle informationen.
 * This model is used in the ExtractOSGIBundle.java to parse the
 * OSGi-Bundle information.
 * 
 * @author MarcPreuschaft
 *
 */
public class RequireBundleModel {
	
	//bundle name
	private String name;
	
	//bundle version
	private String version;
	
	public RequireBundleModel(String name, String version){
		this.name = name;
		this.version = version;
	}	

	/**
	 * return the bundle name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * set the bundle name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * return the bundle version
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * set the bundle version
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	

}
