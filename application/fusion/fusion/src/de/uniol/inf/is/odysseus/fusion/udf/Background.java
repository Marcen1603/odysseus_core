package de.uniol.inf.is.odysseus.fusion.udf;

import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;

/**
 * @author Christian Kuka, Kai Pancratz
 * 
 */
public class Background {

	//static float scanDistance = 6000f;
	
    public static Background merge(Background base, final PolarCoordinate[] diff) {
        final Background result = new Background(base.size());
        
        
        for (int i = 0; i < base.size(); i++) {
        	//6m Circle extraction	
        	//System.out.println(diff[i].r);
        	
        	//if( diff.getSamples()[i].getDist1() < scanDistance){
        	 
	        	if (Math.abs(base.getDistance(i) - diff[i].r) <= 5) {
	                result.setDistance(i, Math.min(base.getDistance(i), diff[i].r));
	            }
	            else {
	                result.setDistance(i, base.getDistance(i));
	            }
        	
        	//}
        	
        }
        return result;
    }

    double[] distances;

    public Background(final int size) {
        this.distances = new double[size];
    }

    public Background(final PolarCoordinate[] coordinates) {
        this.distances = new double[coordinates.length];
       
        for (int i = 0; i < coordinates.length; i++) {
        	
        	//6m Circle record
        	//if(this.distances[i] < scanDistance){
        		this.distances[i] = coordinates[i].r * 0.965;
        	//}
        	
        }
    }

    public double getDistance(final int index) {
    	if (index < this.distances.length) {
            return this.distances[index];
        }
        else {
            return Float.MAX_VALUE;
        }
    }

    public void setDistance(final int index, final double d) {
        if (index < this.distances.length) {
            this.distances[index] = d;
        }
    }

    public int size() {
        return this.distances.length;
    }
}
