package de.uniol.inf.is.odysseus.classification.objects;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public interface IObjectRule {
    public IObjectType getType();

    public double getTypeAffinity(Geometry segment);

    public Geometry getPredictedPolygon(Geometry segment);
}
