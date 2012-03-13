package de.uniol.inf.is.odysseus.wrapper.sick.model;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Background {

	static float scanDistance = 6000f;
	
    public static Background merge(final Background base, final Measurement diff) {
        final Background result = new Background(base.size());
        
        
        for (int i = 0; i < base.size(); i++) {
        	//6m Circle extraction	
        	System.out.println(diff.getSamples()[i].getDist1());
        	
        	//if( diff.getSamples()[i].getDist1() < scanDistance){
        	 
	        	if (Math.abs(base.getDistance(i) - diff.getSamples()[i].getDist1()) <= 5) {
	                result.setDistance(i, Math.min(base.getDistance(i), diff.getSamples()[i].getDist1()));
	            }
	            else {
	                result.setDistance(i, base.getDistance(i));
	            }
        	
        	//}
        	
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
        	//6m Circle record
        	//if(this.distances[sample.getIndex()] < scanDistance){
        		this.distances[sample.getIndex()] = sample.getDist1() * 0.965f;
        	//}
        	
        }
    }

    public float getDistance(final int index) {
    	if (index < this.distances.length) {
            return this.distances[index];
        }
        return Float.MAX_VALUE;
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
