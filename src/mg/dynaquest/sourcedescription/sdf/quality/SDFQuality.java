package mg.dynaquest.sourcedescription.sdf.quality;

import mg.dynaquest.sourcedescription.sdf.schema.SDFDatatype;
import mg.dynaquest.sourcedescription.sdf.schema.SDFEntity;

/**
 * Alle Qualitaetseigenschaften sind Instanzen dieser Klasse Eine Qualitätseigenschaft ist dabei ein Attribut mit dem speziellen Datentyp Quality
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

	public void setDatatype(SDFDatatype datatype) {
		// Ignorieren. Der Datentyp darf nicht verändert werden
	}

}