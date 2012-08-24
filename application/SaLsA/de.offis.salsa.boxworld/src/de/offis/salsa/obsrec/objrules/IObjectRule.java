package de.offis.salsa.obsrec.objrules;

import java.awt.Polygon;
import java.util.List;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.TrackedObject.Type;

public interface IObjectRule {
	public Type getType();
	public double getTypeAffinity(List<Sample> segment);
	public Polygon getPredictedPolygon(List<Sample> segment);
}
