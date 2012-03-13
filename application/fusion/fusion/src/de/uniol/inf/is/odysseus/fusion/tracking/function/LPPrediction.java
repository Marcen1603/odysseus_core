package de.uniol.inf.is.odysseus.fusion.tracking.function;


import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;



public class LPPrediction extends AbstractFunction<Polygon> {

	private static final long serialVersionUID = -3059065500918983661L;
	public static final SDFDatatype[] accTypes = new SDFDatatype[] {SDFSpatialDatatype.SPATIAL_POLYGON };

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
        return accTypes;
	}

	@Override
	public String getSymbol() {
		return "LPPrediction";
	}

	@Override
	public Polygon getValue() {
		Polygon polygon = (Polygon) this.getInputValue(0);		
		int objectClass = (Integer) this.getInputValue(1);		
		
		return predictPolygon(polygon, objectClass);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFSpatialDatatype.SPATIAL_POLYGON;
	}

	
	private Polygon predictPolygon(Polygon polygon,int objectClass){
		Polygon prediction = (Polygon) polygon.clone();
		
		
		if(objectClass == 1){
			return (Polygon) prediction.buffer(5.00);
		//	predictions.add((Polygon) polygon.getBoundary().convexHull());
			
//			for(int i=0; i < 10; i++){
//				for(Coordinate coordinate : prediction.getCoordinates()){
//					coordinate.x = coordinate.x + 10 * i;
//					coordinate.y = coordinate.y + 10 * -i;
//				}
//				predictions.add(prediction);	
//			}
			
			
		}
		
		
		if(objectClass == 2){
			return (Polygon) prediction.buffer(5.00);
		//	predictions.add((Polygon) polygon.getBoundary().convexHull());
//			for(Coordinate coordinate : prediction.getCoordinates()){
//				coordinate.x = coordinate.x + 30;
//				//coordinate.y = coordinate.y + 100;
//			}
//			predictions.add(prediction);
		}
		
		
		return null;
	}
	
}
