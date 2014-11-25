package de.uniol.inf.is.odysseus.sports.mep;

import java.awt.geom.Point2D;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Jama.Matrix;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;

public class GPSToLocalCoordFunction extends AbstractFunction<Double>{

	private static final long serialVersionUID = 3258148153383784341L;
	
	private static final String LOCAL = "local";
	private static final String GPS = "gps";
	
	private static final String TOP_LEFT = "top_left";
	private static final String TOP_RIGHT = "top_right";
	private static final String BOTTOM_LEFT = "bottom_left";

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(GPSToLocalCoordFunction.class);

	protected static IDistributedDataContainer ddc;

	private static HashMap<String, HashMap<String, Point2D>> listOfAccessPoints;

	private static boolean hasBeenSetUp = false;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.STRING } };

	public GPSToLocalCoordFunction() {
		super("gpsToLocalCoord", 3, accTypes, SDFDatatype.DOUBLE);
		listOfAccessPoints = new HashMap<String, HashMap<String, Point2D>>();
	}

	/**
	 * Binds a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to bind. <br />
	 *            Must be not null.
	 */
	public static void bindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		GPSToLocalCoordFunction.ddc = ddc;
		GPSToLocalCoordFunction.LOG.debug("Bound {} as a DDC", ddc.getClass()
				.getSimpleName());

	}

	/**
	 * Removes the binding for a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		if (GPSToLocalCoordFunction.ddc == ddc) {

			GPSToLocalCoordFunction.ddc = null;
			GPSToLocalCoordFunction.LOG.debug("Unbound {} as a DDC", ddc
					.getClass().getSimpleName());

		}
	}

	private static void setUpField() {
		try {
			
			String[] xminKey = "field,xmin".split(",");
			String[] yminKey = "field,ymin".split(",");
			String[] xmaxKey = "field,xmax".split(",");
			String[] ymaxKey = "field,ymax".split(",");
			
			String[] xGPSBottomLeftKey = "gps,bottomLeftX".split(",");
			String[] yGPSBottomLeftKey = "gps,bottomLeftY".split(",");
			String[] xGPSTopLeftKey = "gps,topLeftX".split(",");
			String[] yGPSTopLeftKey = "gps,topLeftY".split(",");
			String[] xGPSTopRightKey = "gps,topRightX".split(",");
			String[] yGPSTopRightKey = "gps,topRightY".split(",");

			double xmin = Double.valueOf(GPSToLocalCoordFunction.ddc
					.getValue(new DDCKey(xminKey)));
			double ymin = Double.valueOf(GPSToLocalCoordFunction.ddc
					.getValue(new DDCKey(yminKey)));
			double xmax = Double.valueOf(GPSToLocalCoordFunction.ddc
					.getValue(new DDCKey(xmaxKey)));
			double ymax = Double.valueOf(GPSToLocalCoordFunction.ddc
					.getValue(new DDCKey(ymaxKey)));
			
			double xGPSBottomLeft = Double.valueOf(GPSToLocalCoordFunction.ddc
					.getValue(new DDCKey(xGPSBottomLeftKey)));
			double yGPSBottomLeft = Double.valueOf(GPSToLocalCoordFunction.ddc
					.getValue(new DDCKey(yGPSBottomLeftKey)));
			double xGPSTopLeft = Double.valueOf(GPSToLocalCoordFunction.ddc
					.getValue(new DDCKey(xGPSTopLeftKey)));
			double yGPSTopLeft = Double.valueOf(GPSToLocalCoordFunction.ddc
					.getValue(new DDCKey(yGPSTopLeftKey)));
			double xGPSTopRight = Double.valueOf(GPSToLocalCoordFunction.ddc
					.getValue(new DDCKey(xGPSTopRightKey)));
			double yGPSTopRight = Double.valueOf(GPSToLocalCoordFunction.ddc
					.getValue(new DDCKey(yGPSTopRightKey)));

			HashMap<String, Point2D> point = new HashMap<String, Point2D>();
			point.put(LOCAL, new Point2D.Double(xmin,ymax));
			point.put(GPS, new Point2D.Double(xGPSBottomLeft, yGPSBottomLeft));
			listOfAccessPoints.put(BOTTOM_LEFT, point);
			
			point.clear();
			point.put(LOCAL, new Point2D.Double(xmin, ymin));
			point.put(GPS, new Point2D.Double(xGPSTopLeft, yGPSTopLeft));
			listOfAccessPoints.put(TOP_LEFT, point);
			
			point.clear();
			point.put(LOCAL, new Point2D.Double(xmax, ymin));
			point.put(GPS, new Point2D.Double(xGPSTopRight, yGPSTopRight));
			listOfAccessPoints.put(TOP_RIGHT, point);

			hasBeenSetUp = true;

		} catch (MissingDDCEntryException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public Double getValue() {
		if (!hasBeenSetUp) {
			setUpField();
		}
		
		Point2D aLocal = listOfAccessPoints.get(TOP_LEFT).get(LOCAL);
		Point2D aGPS = listOfAccessPoints.get(TOP_LEFT).get(GPS);
		
		Point2D bLocal = listOfAccessPoints.get(TOP_RIGHT).get(LOCAL);
		Point2D bGPS = listOfAccessPoints.get(TOP_RIGHT).get(GPS);
		
		Point2D cLocal = listOfAccessPoints.get(BOTTOM_LEFT).get(LOCAL);
		Point2D cGPS = listOfAccessPoints.get(BOTTOM_LEFT).get(GPS);
		
		Point2D searchedPoint = calculatePointXY(
				aLocal, getDistance(aGPS.getX(), aGPS.getY(), (double) getInputValue(0), (double) getInputValue(1)), 
				bLocal, getDistance(bGPS.getX(), bGPS.getY(), (double) getInputValue(0), (double) getInputValue(1)),
				cLocal, getDistance(cGPS.getX(), cGPS.getY(), (double) getInputValue(0), (double) getInputValue(1)));

		if (getInputValue(2).toString().equals("x")) {
			return searchedPoint.getX();
		} else {
			return searchedPoint.getY();
		}
	}
	
	/**
	 * Calculate the point in local coordinate system
	 * @param a local coord a
	 * @param r1 distance gps a to searched points gps
	 * @param k local point k
	 * @param r2 distance gps k to searched points gps
	 * @param c local point c
	 * @param r3 distance gps c to searched points gps
	 * @return searched point in local coordinate system
	 */
	private Point2D calculatePointXY(Point2D a, double r1, Point2D k,double r2, Point2D c, double r3){
		double x1= a.getX();
	    double x2= k.getX();
	    double x3 = c.getX();
	        
	        
	    double y1= a.getY();
	    double y2= k.getY();
	    double y3 = c.getY();


	    Matrix A, b, x ;

	    double m[][] = {
	    		{ x3- x1, y3 - y1},
	            { x3- x2, y3 - y2}
	    };

	    double b_temp[][] = {
	    		{((r1*r1)-(r3*r3)) - ((x1*x1)-(x3*x3)) - ((y1*y1)-(y3*y3))},
	            {((r2*r2)-(r3*r3)) - ((x2*x2)-(x3*x3)) - ((y2*y2)-(y3*y3))}
	    };

	    b = new Matrix(b_temp);
	    A = new Matrix(m);
	    A=  A.times(2.0);
	    x = A.solve( b );


	    double x_result = x.getArray()[0][0];
	    double y_result = x.getArray()[1][0];

	    return new Point2D.Double(x_result, y_result);
	}

	
	/**
	 * Get distance between to gps points
	 * 
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return distance in mm
	 */
	public double getDistance(double lat1, double lon1, double lat2, double lon2) {
		final double R = 6371; // Radius of the earth
		double latDistance = toRad(lat2 - lat1);
		double lonDistance = toRad(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(toRad(lat1)) * Math.cos(toRad(lat2))
				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;

		return distance*1000;
	}
	
	private static double toRad(double value) {
		return value * Math.PI / 180;
	}
}
