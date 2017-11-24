package de.uniol.inf.is.odysseus.server.moitoringCPU.CPUUsage;

public class OperatorCPUUsage implements IMeasurableValue {

	private String operatorName;
	private boolean confirmed;

	public OperatorCPUUsage(String operatorName) {
		this.setOperatorName(operatorName);
		this.setConfirmed(false);
	}

	@Override
	public void startMeasurement(long timestamp) {
		
	}
	@Override
	public void stopMeasurement(long timestamp) {
		
	}
	@Override
	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
}
