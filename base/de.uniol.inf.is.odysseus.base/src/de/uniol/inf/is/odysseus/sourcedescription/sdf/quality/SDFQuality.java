package de.uniol.inf.is.odysseus.sourcedescription.sdf.quality;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

/**
 * Alle Qualitaetseigenschaften sind Instanzen dieser Klasse Eine Qualitaetseigenschaft ist dabei ein Attribut mit dem speziellen Datentyp Quality
 */

public class SDFQuality extends SDFEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 272535927319723816L;
	// Kein Datentyp
	static SDFDatatype dt = null;

	public SDFQuality(String URI) {
		super(URI);
		super.setDatatype(dt);
	}

	@Override
	public void setDatatype(SDFDatatype datatype) {
		// Ignorieren. Der Datentyp darf nicht veraendert werden
	}

}