package de.uniol.inf.is.odysseus.classification.objects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TypeDetails implements Iterable<IObjectType> {
    private final HashMap<IObjectType, Double> types = new HashMap<IObjectType, Double>();

    public TypeDetails(final IObjectType type, final Double affinity) {
        this.types.put(type, affinity);
    }

    public TypeDetails() {

    }

    public void normalize() {
        double sum = 0.0;
        for (final double affinity : this.types.values()) {
            sum += affinity;
        }
        if (sum > 0.0) {
            sum = 1 / sum;
            for (final IObjectType type : this.types.keySet()) {
                this.types.put(type, this.types.get(type) * sum);
            }
        }
    }

    public void addTypeAffinity(final IObjectType type, final Double affinity) {
        this.types.put(type, affinity);
    }

    public Double getTypeAffinity(final IObjectType type) {
        if (!this.types.containsKey(type)) {
            return 0.0;
        }
        else {
            return this.types.get(type);
        }
    }

    public IObjectType getMaxAffinityType() {
        Double affnity = -1.0;
        IObjectType type = null;

        for (final Entry<IObjectType, Double> t : this.types.entrySet()) {
            if ((type == null) || (t.getValue() > affnity)) {
                type = t.getKey();
                affnity = t.getValue();
            }
        }

        return type;
    }

    @Override
    public String toString() {
        return this.types.toString();
    }

    @Override
    public Iterator<IObjectType> iterator() {
        return this.types.keySet().iterator();
    }
}
