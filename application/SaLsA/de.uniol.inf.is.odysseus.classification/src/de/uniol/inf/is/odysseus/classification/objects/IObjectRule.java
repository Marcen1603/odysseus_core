package de.uniol.inf.is.odysseus.classification.objects;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public interface IObjectRule {
    public IObjectType getType();

    public double getTypeAffinity(Geometry segment);

    public Polygon getPredictedPolygon(Geometry segment);
}
