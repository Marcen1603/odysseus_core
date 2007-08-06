package mg.dynaquest.monitor;

public class TimeRingBuffer {

	/**
	 * @uml.property  name="elements" multiplicity="(0 -1)" dimension="1"
	 */
	long[] elements = null;

	/**
	 * @uml.property  name="distance" multiplicity="(0 -1)" dimension="1"
	 */
	long[] distance = null;

	/**
	 * @uml.property  name="currentPos"
	 */
	int currentPos = -1;

	/**
	 * @uml.property  name="size"
	 */
	int size = 0;

	/**
	 * @uml.property  name="overflow"
	 */
	boolean overflow = false;

	public TimeRingBuffer(int size) {
		this.size = size;
		this.elements = new long[size];
		this.distance = new long[size - 1];
	}

	public void add(long element) {
		elements[getNextPos()] = element;
		// System.out.println(currentPos+" "+element);
		// Hier auch gleich immer den Abstand zwischen den beiden Einträgen
		// errechnen
		if (currentPos > 0 && currentPos < size) {
			distance[currentPos - 1] = elements[currentPos]
					- elements[currentPos - 1];
		} else {
			if (currentPos == 0 && overflow) {
				distance[size - 2] = elements[0] - elements[size - 1];
			}
		}
	}

	public double getDatarate() {
		double datarate = 0.0;
		if (currentPos > 1 || overflow) {
			long last = this.elements[currentPos];
			long first = this.elements[0];
			long noObjects = currentPos;
			if (overflow && currentPos < size - 1) {
				first = this.elements[currentPos + 1];
			}
			if (overflow) {
				noObjects = size;
			}
			// Anzahl pro Sekunde
			System.out.println(last + "-" + first + "=" + (last - first)
					+ " Anzahl Objekte " + noObjects);

			datarate = (noObjects * 1000.0) / ((last - first) * 1.0);
		}
		return datarate;
	}

	public double getAverageTimeBetweenObjects() {
		long sum = 0;
		for (int i = 0; i < distance.length; i++) {
			sum = sum + distance[i];
		}
		return (sum * 1.0) / distance.length;
	}

	public double getStandardDeviation() {
		double avg = this.getAverageTimeBetweenObjects();
		int n = distance.length;
		double sum = 0;
		for (int i = 0; i < n; i++) {
			sum = sum + (distance[i] - avg) * (distance[i] - avg);
		}
		double stddev = Math.sqrt(sum / (n - 1));
		return stddev;
	}

	private int getNextPos() {
		if (currentPos == size - 1) {
			currentPos = 0;
			overflow = true;
		} else {
			currentPos++;
		}
		return currentPos;
	}

}

