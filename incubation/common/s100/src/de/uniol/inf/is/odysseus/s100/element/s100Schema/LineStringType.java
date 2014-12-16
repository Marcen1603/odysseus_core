package de.uniol.inf.is.odysseus.s100.element.s100Schema;

/**
 * A LineString is a special curve that consists of a single segment with linear
 * interpolation. It is defined by two or more coordinate tuples, with linear
 * interpolation between them. It is backwards compatible with the LineString of
 * GML 2, GM_LineString of ISO 19107 is implemented by LineStringSegment.
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:08
 */
public class LineStringType extends AbstractCurveType {

	public ModelGroup7 m_ModelGroup7;

	public LineStringType(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}