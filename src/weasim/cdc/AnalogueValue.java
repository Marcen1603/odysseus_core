package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "AVSEQ", sequenceName = "AVSEQ", allocationSize = 1, initialValue = 1)
public class AnalogueValue implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AVSEQ")
	private Integer id;

	private Integer i;

	private Double f;

	public AnalogueValue() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getI() {
		return i;
	}

	public Double getF() {
		return f;
	}

	public void setI(Integer param) {
		this.i = param;
	}

	public void setF(Double param) {
		this.f = param;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((f == null) ? 0 : f.hashCode());
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
		final AnalogueValue other = (AnalogueValue) obj;
		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}
}
