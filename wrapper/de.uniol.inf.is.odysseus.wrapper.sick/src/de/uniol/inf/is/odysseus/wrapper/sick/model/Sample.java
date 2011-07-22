package de.uniol.inf.is.odysseus.wrapper.sick.model;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Sample {

    private int index;
    private float dist1;
    private float dist2;
    private float rssi1;
    private float rssi2;
    private double angle;

    public double getAngle() {
        return this.angle;
    }

    public float getDist1() {
        return this.dist1;
    }

    public Coordinate getDist1Vector() {
        final Coordinate vector = new Coordinate();
        final double angle = Math.toRadians(this.getAngle());
        vector.x = this.getDist1() * Math.sin(angle);
        vector.y = this.getDist1() * Math.cos(angle);
        return vector;
    }

    public float getDist2() {
        return this.dist2;
    }

    public Coordinate getDist2Vector() {
        final Coordinate vector = new Coordinate();
        final double angle = Math.toRadians(this.getAngle());
        vector.x = this.getDist2() * Math.sin(angle);
        vector.y = this.getDist2() * Math.cos(angle);
        return vector;
    }

    public int getIndex() {
        return this.index;
    }

    public float getRssi1() {
        return this.rssi1;
    }

    public float getRssi2() {
        return this.rssi2;
    }

    public double getX() {
        return this.getDist1() * Math.sin(Math.toRadians(this.getAngle()));
    }

    public double getY() {
        return this.getDist1() * Math.cos(Math.toRadians(this.getAngle()));
    }

    public void setAngle(final double angle) {
        this.angle = angle;
    }

    public void setDist1(final float dist1) {
        this.dist1 = dist1;
    }

    public void setDist2(final float dist2) {
        this.dist2 = dist2;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public void setRssi1(final float rssi1) {
        this.rssi1 = rssi1;
    }

    public void setRssi2(final float rssi2) {
        this.rssi2 = rssi2;
    }

    @Override
    public String toString() {
        return "Sample [dist1=" + this.dist1 + ", dist2=" + this.dist2 + ", rssi1=" + this.rssi1
                + ", rssi2=" + this.rssi2 + ", angle=" + this.angle + "]";
    }

}
