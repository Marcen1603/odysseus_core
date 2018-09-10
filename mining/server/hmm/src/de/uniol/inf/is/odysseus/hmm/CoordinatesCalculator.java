package de.uniol.inf.is.odysseus.hmm;

/**
 * Class to work with coordinates. Currently used by the Feature Extraction operator
 * to determine the distance and orientation angle between two coordinates 
 * 
 * @author Michael Moebes, mmo
 * @author Christian Pieper, cpi
 *
 */
public class CoordinatesCalculator {
	//Attributes
	private double point_x;
	private double point_y;

	// private double minDistance = 0.2;
	// private int clusterAreas = 16;

	
	//Constructors
	public CoordinatesCalculator() {

	}

	public CoordinatesCalculator(double point_x, double point_y) {
		this.point_x = point_x;
		this.point_y = point_y;
	}
	
	
	//Methods
	/**
	 * Calculates the distance between last valid point and new incoming point.
	 * Sets new point coordinates if the distance is great enough
	 * 
	 * @param current_x
	 *            X-value of the incoming point.
	 * @param current_y
	 * @return True if distance is greater than threshold, otherwise false.
	 */
	public boolean isNewObservation(double current_x, double current_y,
			double minDistance) {
		double distance = calculateDistance(current_x, current_y);
		if (distance > minDistance) {
			return true;
		}
		return false;
	}

	/**
	 * Calculates the angle between a given coordinate and a horizontal line,
	 * resulting in an orientation e.g. of a hand
	 * 
	 * @param current_x
	 *            given x-coordinate
	 * @param current_y
	 *            given y-coordinate
	 * @return angle in degree
	 */
	public double calculateAngle(double current_x, double current_y) {
		// System.out.print("Angle: Math.atan(( " + current_y + " - " + point_y
		// + ")/( " + current_x + " - " + point_x + ")) = ");
		double tmp = Math.atan((current_y - point_y) / (current_x - point_x)) * 180 / Math.PI;
		// X correction
		if (point_x > current_x) {
			tmp = 180 + tmp;
		}
		// Y correction
		if (tmp < 0) {
			tmp = 360 + tmp;
		}
		// System.out.println(tmp);
		return tmp;
	}

	private double calculateDistance(double current_x, double current_y) {
		return Math.sqrt(Math.pow((point_x - current_x), 2)	+ Math.pow((point_y - current_y), 2));
	}

	public int determineTargetCluster(double angle, int numCluster) {
		angle = angle + (360 / numCluster / 2);
		double cluster = (angle / (360 / numCluster));
		if (cluster < 0) {
			cluster = cluster * (-1);
		}
		// System.out.println("ANGLE: " + angle);
		int output = (int) Math.floor(cluster);
		if (output == numCluster) {
			output = 0;
		}
		return output;
	}

	// public int calculateCluster(double current_x, double current_y) {
	// return clusterAngle(calculateAngle(current_x, current_y));
	// }

	public void setPoint(double current_x, double current_y) {
		point_x = current_x;
		point_y = current_y;
	}

	public double getPoint_x() {
		return point_x;
	}

	public double getPoint_y() {
		return point_y;
	}
}
