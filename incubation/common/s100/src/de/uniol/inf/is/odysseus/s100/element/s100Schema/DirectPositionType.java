package de.uniol.inf.is.odysseus.s100.element.s100Schema;

/**
 * DirectPosition instances hold the coordinates for a position within some
 * coordinate reference system (CRS). Since DirectPositions, as data types, will
 * often be included in larger objects (such as geometry elements) that have
 * references to CRS, the "srsName" attribute will in general be missing, if this
 * particular DirectPosition is included in a larger element with such a reference
 * to a CRS. In this case, the CRS is implicitly assumed to take on the value of
 * the containing object's CRS.
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:06
 */
public class DirectPositionType extends doubleList {

	public SRSReferenceGroup m_SRSReferenceGroup;

	public DirectPositionType(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}