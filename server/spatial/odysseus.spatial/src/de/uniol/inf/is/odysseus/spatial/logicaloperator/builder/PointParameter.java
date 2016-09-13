package de.uniol.inf.is.odysseus.spatial.logicaloperator.builder;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractParameter;

public class PointParameter extends AbstractParameter<Point> {

	private static final long serialVersionUID = 9013436143817490692L;

	public PointParameter() {
		super();
	}

	public PointParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}

	@Override
	protected void internalAssignment() {
		if (inputValue instanceof String) {
			Geometry g;
			try {
				g = new WKTReader().read((String) inputValue);
				if (g instanceof Point) {
					setValue((Point) g);
				}
			} catch (ParseException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}

	}

	@Override
	protected String getPQLStringInternal() {
		return "";
	}

}
