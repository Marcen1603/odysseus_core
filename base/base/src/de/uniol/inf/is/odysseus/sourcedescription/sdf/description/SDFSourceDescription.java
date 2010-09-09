package de.uniol.inf.is.odysseus.sourcedescription.sdf.description;

import java.util.ArrayList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

/**
 * In dieser Klasse wird eine komplette Quellenbeschreibung gekapselt
 */

public class SDFSourceDescription extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7489254634426942571L;

	/**
	 * @uml.property  name="intDesc"
	 * @uml.associationEnd  
	 */
    // Die drei Teile einer Quellenbeschreibung
    SDFIntensionalSourceDescription intDesc = null;

    /**
	 * @uml.property  name="extDesc"
	 * @uml.associationEnd  
	 */
    SDFExtensionalSourceDescription extDesc = null;

    /**
	 * @uml.property  name="qualDesc"
	 * @uml.associationEnd  
	 */
    SDFQualitativeSourceDescription qualDesc = null;

    /**
	 * @uml.property  name="aboutSource"
	 * @uml.associationEnd  
	 */
    SDFSource aboutSource = null;


	/**
	 * @uml.property  name="alternateURIs"
	 */
	ArrayList<String> alternateURIs = new ArrayList<String>();

	public SDFSourceDescription(String URI) {
		super(URI);
	}

    /**
     * 
     * @uml.property name="intDesc"
     */
    public void setIntDesc(SDFIntensionalSourceDescription intDesc) {
        this.intDesc = intDesc;
    }

    /**
     * 
     * @uml.property name="intDesc"
     */
    public SDFIntensionalSourceDescription getIntDesc() {
        return intDesc;
    }

    /**
     * 
     * @uml.property name="extDesc"
     */
    public void setExtDesc(SDFExtensionalSourceDescription extDesc) {
        this.extDesc = extDesc;
    }

    /**
     * 
     * @uml.property name="extDesc"
     */
    public SDFExtensionalSourceDescription getExtDesc() {
        return extDesc;
    }

    /**
     * 
     * @uml.property name="qualDesc"
     */
    public void setQualDesc(SDFQualitativeSourceDescription qualDesc) {
        this.qualDesc = qualDesc;
    }

    /**
     * 
     * @uml.property name="qualDesc"
     */
    public SDFQualitativeSourceDescription getQualDesc() {
        return qualDesc;
    }

    /**
     * 
     * @uml.property name="aboutSource"
     */
    public void setAboutSource(SDFSource aboutSource) {
        this.aboutSource = aboutSource;
    }

    /**
     * 
     * @uml.property name="aboutSource"
     */
    public SDFSource getAboutSource() {
        return aboutSource;
    }

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + "\n");
		if (this.aboutSource != null)
			ret.append("\t" + this.aboutSource + "\n");
		if (this.intDesc != null)
			ret.append(intDesc.toString() + "\n");
		if (this.extDesc != null)
			ret.append(extDesc.toString() + "\n");
		if (this.qualDesc != null)
			ret.append(qualDesc.toString() + "\n");
		return ret.toString();
	}

}