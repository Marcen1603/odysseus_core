package de.uniol.inf.is.odysseus.salsa.sensor.model;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class Background {

    public static Background merge(final Background base, final Measurement diff) {
        final Background result = new Background(base.size());
        for (int i = 0; i < base.size(); i++) {
            result.setDistance(i, Math.min(base.getDistance(i), diff.getSamples()[i].getDist1()));
        }
        return result;
    }

    float[] distances;

    public Background(final int size) {
        this.distances = new float[size];
    }

    public Background(final Measurement measurement) {
        this.distances = new float[measurement.getSamples().length];
        for (final Sample sample : measurement.getSamples()) {
            this.distances[sample.getIndex()] = sample.getDist1() * 0.99f;
        }
    }

    public float getDistance(final int index) {
        if (index < this.distances.length) {
            return this.distances[index];
        }
        else {
            return Float.MAX_VALUE;
        }
    }

    public void setDistance(final int index, final float distance) {
        if (index < this.distances.length) {
            this.distances[index] = distance;
        }
    }

    public int size() {
        return this.distances.length;
    }
}
