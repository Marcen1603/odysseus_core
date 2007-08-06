package weasim.cdc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Vector {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CMDSeq")
	private Integer id;

	@OneToOne
	private AnalogueValue mag;

	@OneToOne
	private AnalogueValue ang;

	public AnalogueValue getAng() {
		return ang;
	}

	public void setAng(AnalogueValue ang) {
		this.ang = ang;
	}

	public AnalogueValue getMag() {
		return mag;
	}

	public void setMag(AnalogueValue mag) {
		this.mag = mag;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((ang == null) ? 0 : ang.hashCode());
		result = PRIME * result + ((mag == null) ? 0 : mag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Vector other = (Vector) obj;
		if (ang == null) {
			if (other.ang != null)
				return false;
		} else if (!ang.equals(other.ang))
			return false;
		if (mag == null) {
			if (other.mag != null)
				return false;
		} else if (!mag.equals(other.mag))
			return false;
		return true;
	}
}
