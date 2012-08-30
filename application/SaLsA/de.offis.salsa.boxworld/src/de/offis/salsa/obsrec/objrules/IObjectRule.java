package de.offis.salsa.obsrec.objrules;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

import de.offis.salsa.obsrec.models.ObjectType;

public interface IObjectRule {
	public ObjectType getType();

	public double getTypeAffinity(LineString segment);

	public Polygon getPredictedPolygon(LineString segment);
}
