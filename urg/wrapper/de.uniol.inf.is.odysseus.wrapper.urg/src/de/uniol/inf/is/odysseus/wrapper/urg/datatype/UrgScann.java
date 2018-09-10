package de.uniol.inf.is.odysseus.wrapper.urg.datatype;

public class UrgScann {
	private int startingStep;
	private int endStep;
	private int scanInterval;
	private int clusterCount;
	private int remainingScans;
	private Precision precision;
	private String message;
	private int statusCode;
	private int timeStamp;
	private float[] data;
	
	public enum Precision {
		Double("D"),
		Single("S");
		
		private String s;
		Precision(String s) {
			this.s = s;
		}
		
		String getString() {
			return s;
		}
	}
	
	public UrgScann() {
		
	}

	public int getStartingStep() {
		return startingStep;
	}

	public void setStartingStep(int startingStep) {
		this.startingStep = startingStep;
	}

	public int getEndStep() {
		return endStep;
	}

	public void setEndStep(int endStep) {
		this.endStep = endStep;
	}

	public int getScanInterval() {
		return scanInterval;
	}

	public void setScanInterval(int scanInterval) {
		this.scanInterval = scanInterval;
	}

	public int getClusterCount() {
		return clusterCount;
	}

	public void setClusterCount(int clusterCount) {
		this.clusterCount = clusterCount;
	}

	public int getRemainingScans() {
		return remainingScans;
	}

	public void setRemainingScans(int remainingScans) {
		this.remainingScans = remainingScans;
	}

	public Precision getPrecision() {
		return precision;
	}

	public void setPrecision(Precision precision) {
		this.precision = precision;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public int getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}

	public float[] getData() {
		return data;
	}

	public void setData(float[] data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (data != null) {
			for (int i = 0; i < data.length; i++) {
				sb.append(data[i] + ", ");
			}
		}
		return "M" + precision.getString() + " " + startingStep + " " + endStep + " " + clusterCount + " " + scanInterval + " " + remainingScans + " " + message + "\n" +
				statusCode + "\n" + timeStamp + "\n" + sb;
	}
	
	public String toCsvString() {
		StringBuilder sb = new StringBuilder();
		if (data != null) {
			for (int i = 0; i < data.length; i++) {
				sb.append(data[i] + ";");
			}
		}
		return System.currentTimeMillis() + ";" + startingStep + ";" + endStep + ";" + sb;
	}
}
