package de.uniol.inf.is.odysseus.sourcedescription.sdf;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDF;

/**
 * @author Marco Grawunder
 */
public abstract class SDFElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6076421686616330199L;

	/**
	 * @uml.property name="qualName"
	 */
	private String qualName = null;

	private String uRIWithoutQualName = null;

	protected SDFElement() {

	}

	public SDFElement(String URI) {
		init(URI);
	}

	public SDFElement(SDFElement copy) {
		this.qualName = copy.qualName;
		this.uRIWithoutQualName = copy.uRIWithoutQualName;
	}

	/**
	 * Initialisieren der Teilkomponenten der URI.
	 */
	protected void init(String URI) {
		if (URI != null) {
			int sharpPos = URI.indexOf("#");
			if (sharpPos > 0) {
				this.qualName = URI.substring(sharpPos + 1);
				this.uRIWithoutQualName = URI.substring(0, sharpPos);
			}else{
				this.uRIWithoutQualName = URI;
			}
			
		}
	}

	public String getURI(boolean prettyPrint) {
		if (prettyPrint) {
			return getURI(uRIWithoutQualName, qualName, true, null);
		} else {
			return getURI(uRIWithoutQualName, qualName, false, null);
		}
	}
	
	protected String getURI(boolean prettyPrint, String seperator) {
		return getURI(uRIWithoutQualName, qualName, prettyPrint, seperator);
	}
	

	/**
	 * Build an URI from these parts
	 * if one part is empty, show only the other one
	 * if substSDFNamespace is true substitute long URIs with short default namespace names
	 * @param uRIWithoutQualName
	 * @param qualName
	 * @param substSDFNamespace
	 * @return
	 */
	private String getURI(String uRIWithoutQualName, String qualName,
			boolean substSDFNamespace, String defaultSperator) {
		String sep = defaultSperator==null?"#":defaultSperator;
		StringBuffer ret = new StringBuffer();

		if (uRIWithoutQualName != null && uRIWithoutQualName.length() > 0) {
			if (substSDFNamespace) {
				String namespaceName = SDF
						.getNamespaceForUri(uRIWithoutQualName);
				if (namespaceName != null) {
					ret.append(namespaceName); 
				}
			} else {
				ret.append(uRIWithoutQualName);
			}
		} 
		if (qualName != null){
			if (uRIWithoutQualName != null && uRIWithoutQualName.length() > 0){
				if (substSDFNamespace){
					ret.append(":");
				}else{
					ret.append(sep);
				}
			}
			ret.append(qualName);
		}
		return ret.toString();
	}

	/**
	 * Liefert den qualifizierden Namen des Elements, d.h. der Teil der Uri nach
	 * dem ersten (!) Auftreten von #
	 * 
	 * @return String
	 * @uml.property name="qualName"
	 */
	public String getQualName() {
		return qualName;
	}

	public void setQualName(String name) {
		this.qualName = name;
	}

	public String getURIWithoutQualName() {
		return uRIWithoutQualName;
	}

	public void setURIWithoutQualName(String name) {
		uRIWithoutQualName = name;
	}

	@Override
	public String toString() {
		return getURI(true);
	}

	/**
	 * Returns complete name of this element.
	 * 
	 * @return
	 */
	public String getURI() {
		return getURI(uRIWithoutQualName, qualName, false, null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((qualName == null) ? 0 : qualName.hashCode());
		result = prime
				* result
				+ ((uRIWithoutQualName == null) ? 0 : uRIWithoutQualName
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SDFElement other = (SDFElement) obj;
		if (qualName == null) {
			if (other.qualName != null)
				return false;
		} else if (!qualName.equals(other.qualName))
			return false;
		if (uRIWithoutQualName == null) {
			if (other.uRIWithoutQualName != null)
				return false;
		} else if (!uRIWithoutQualName.equals(other.uRIWithoutQualName))
			return false;
		return true;
	}

}