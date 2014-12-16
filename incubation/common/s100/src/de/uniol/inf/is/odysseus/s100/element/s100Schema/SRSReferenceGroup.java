package de.uniol.inf.is.odysseus.s100.element.s100Schema;

/**
 * Optional reference to the CRS used by this geometry, with optional additional
 * information to simplify use when a more complete definition of the CRS is not
 * needed.
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:09
 */
public class SRSReferenceGroup {

	/**
	 * In general this reference points to a CRS instance of gml:
	 * CoordinateReferenceSystemType (see coordinateReferenceSystems.xsd). For well
	 * known references it is not required that the CRS description exists at the
	 * location the URI points to. If no srsName attribute is given, the CRS must be
	 * specified as part of the larger context this geometry element is part of, e.g.
	 * a geometric element like point, curve, etc. It is expected that this attribute
	 * will be specified at the direct position level only in rare cases.
	 */
//	public anyURI srsName;
	/**
	 * The "srsDimension" is the length of coordinate sequence (the number of entries
	 * in the list). This dimension is specified by the coordinate reference system.
	 * When the srsName attribute is omitted, this attribute shall be omitted. 
	 */
	public int srsDimension;

	public SRSReferenceGroup(){

	}

	public void finalize() throws Throwable {

	}

}