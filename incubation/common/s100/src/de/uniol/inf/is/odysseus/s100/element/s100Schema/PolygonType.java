package de.uniol.inf.is.odysseus.s100.element.s100Schema;

/**
 * A Polygon is a special surface that is defined by a single surface patch. The
 * boundary of this patch is coplanar and the polygon uses planar interpolation in
 * its interior. It is backwards compatible with the Polygon of GML 2, GM_Polygon
 * of ISO 19107 is implemented by PolygonPatch.
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:09
 */
public class PolygonType extends AbstractSurfaceType {

	public exterior m_exterior;
	public interior m_interior;

	public PolygonType(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}