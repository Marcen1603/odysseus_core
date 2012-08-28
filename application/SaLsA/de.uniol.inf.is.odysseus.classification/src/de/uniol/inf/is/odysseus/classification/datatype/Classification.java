package de.uniol.inf.is.odysseus.classification.datatype;

import java.io.Serializable;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Classification implements Serializable, Cloneable {
    /**
     * 
     */
    private static final long serialVersionUID = -2159988511659907102L;
    private final Geometry    geometry;
    private final String      name;
    private final double      affinity;

    public Classification(final Geometry geometry, final String name, final double affinity) {
        this.geometry = geometry;
        this.name = name;
        this.affinity = affinity;
    }

    public Classification(final Classification classification) {
        this.geometry = (Geometry) classification.geometry.clone();
        this.name = classification.name;
        this.affinity = classification.affinity;
    }

    @Override
    protected Classification clone() {
        return new Classification(this);
    }
}
