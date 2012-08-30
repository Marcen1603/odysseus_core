package de.offis.salsa.obsrec.objrules;

import java.util.List;

import com.vividsolutions.jts.geom.Polygon;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.models.ObjectType;

public interface IObjectRule {
	public ObjectType getType();

	public double getTypeAffinity(List<Sample> segment);

	public Polygon getPredictedPolygon(List<Sample> segment);
}
