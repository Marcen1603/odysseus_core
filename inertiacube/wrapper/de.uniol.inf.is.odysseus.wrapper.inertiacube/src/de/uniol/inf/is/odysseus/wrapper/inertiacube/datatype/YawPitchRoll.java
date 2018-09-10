package de.uniol.inf.is.odysseus.wrapper.inertiacube.datatype;

public class YawPitchRoll {
	private float yaw;
	private float pitch;
	private float roll;

	public YawPitchRoll() {
	}

	public YawPitchRoll(float yaw, float pitch, float roll) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public float getValue(int index) {
		switch (index) {
		case 0:
			return yaw;
		case 1:
			return pitch;
		case 2:
			return roll;
		}
		return -1;
	}
	
	@Override
	public String toString() {
		return String.format("yaw: %.2f, pitch: %.2f, roll: %.2f", yaw, pitch, roll);
	}
}
