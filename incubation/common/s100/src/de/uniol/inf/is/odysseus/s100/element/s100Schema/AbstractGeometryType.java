package de.uniol.inf.is.odysseus.s100.element.s100Schema;

/**
 * All geometry elements are derived directly or indirectly from this abstract
 * supertype. A geometry element may have an identifying attribute ("gml:id"), a
 * name (attribute "name") and a description (attribute "description"). It may be
 * associated with a spatial reference system (attribute "srsName"). The following
 * rules shall be adhered: - Every geometry type shall derive from this abstract
 * type. - Every geometry element (i.e. an element of a geometry type) shall be
 * directly or indirectly in the substitution group of _Geometry.
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:05
 */
public abstract class AbstractGeometryType extends AbstractS100Type {

	public SRSReferenceGroup m_SRSReferenceGroup;

	public AbstractGeometryType(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}