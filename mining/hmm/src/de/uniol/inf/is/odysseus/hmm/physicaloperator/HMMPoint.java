package de.uniol.inf.is.odysseus.hmm.physicaloperator;

public class HMMPoint {
	private double point_x; 
	private double point_y;
	private double minDistance = 0.2;
	private int clusterAreas = 16;
	
	
	public HMMPoint(double point_x, double point_y) {
		this.point_x = point_x;
		this.point_y = point_y;
	}


	/**
	 * Calculates the distance between last valid point and new incoming point. Sets new point coordinates if 
	 * the distance is great enough
	 * @param current_x X-value of the incoming point.
	 * @param current_y
	 * @return True if distance is greater than threshold, otherwise false.
	 */
	public boolean isNewObservation(double current_x, double current_y) {
		double distance = calculateDistance(current_x, current_y);
		if(distance > minDistance) {
			return true;
		}
		return false;
	}
	
	private double calculateAngle(double current_x, double current_y) {
		System.out.print("Angle: Math.atan(( " + current_y + " - " + point_y + ")/( " + current_x + " - " + point_x + ")) = ");
		double tmp = Math.atan((current_y-point_y)/(current_x-point_x))*180/Math.PI;
		//X correction
		if(point_x > current_x) {
			tmp = 180 + tmp;
		}
		//Y cprrection
		if(tmp < 0) {
			tmp = 360 + tmp;
		}
		System.out.println(tmp);
		return tmp;
	}
	
	private double calculateDistance(double current_x, double current_y) {
		return Math.sqrt(Math.pow((point_x-current_x), 2) + Math.pow((point_y-current_y), 2));
	}
	
	private int clusterAngle(double angle) {
		double cluster = (angle/(360/clusterAreas));
//		System.out.println("ANGLE: " + angle);
		return (int) Math.round(cluster);
	}

	public int calculateCluster(double current_x, double current_y) {
		return clusterAngle(calculateAngle(current_x, current_y));
	}
	
	public void setPoint(double current_x, double current_y) {
		point_x = current_x;
		point_y = current_y;
	}
}
