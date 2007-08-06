package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class RangeConfig implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CMDSeq")
	private Integer id;

	@OneToOne
	private AnalogueValue hhLim;

	@OneToOne
	private AnalogueValue hLim;

	@OneToOne
	private AnalogueValue lLim;

	@OneToOne
	private AnalogueValue llLim;

	@OneToOne
	private AnalogueValue min;

	@OneToOne
	private AnalogueValue max;
	
	public RangeConfig() {
	}

	public AnalogueValue getHhLim() {
		return hhLim;
	}

	public void setHhLim(AnalogueValue hhLim) {
		this.hhLim = hhLim;
	}

	public AnalogueValue getHLim() {
		return hLim;
	}

	public void setHLim(AnalogueValue lim) {
		hLim = lim;
	}

	public AnalogueValue getLLim() {
		return lLim;
	}

	public void setLLim(AnalogueValue lim) {
		lLim = lim;
	}

	public AnalogueValue getLlLim() {
		return llLim;
	}

	public void setLlLim(AnalogueValue llLim) {
		this.llLim = llLim;
	}

	public AnalogueValue getMax() {
		return max;
	}

	public void setMax(AnalogueValue max) {
		this.max = max;
	}

	public AnalogueValue getMin() {
		return min;
	}

	public void setMin(AnalogueValue min) {
		this.min = min;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((hLim == null) ? 0 : hLim.hashCode());
		result = PRIME * result + ((hhLim == null) ? 0 : hhLim.hashCode());
		result = PRIME * result + ((lLim == null) ? 0 : lLim.hashCode());
		result = PRIME * result + ((llLim == null) ? 0 : llLim.hashCode());
		result = PRIME * result + ((max == null) ? 0 : max.hashCode());
		result = PRIME * result + ((min == null) ? 0 : min.hashCode());
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
		final RangeConfig other = (RangeConfig) obj;
		if (hLim == null) {
			if (other.hLim != null)
				return false;
		} else if (!hLim.equals(other.hLim))
			return false;
		if (hhLim == null) {
			if (other.hhLim != null)
				return false;
		} else if (!hhLim.equals(other.hhLim))
			return false;
		if (lLim == null) {
			if (other.lLim != null)
				return false;
		} else if (!lLim.equals(other.lLim))
			return false;
		if (llLim == null) {
			if (other.llLim != null)
				return false;
		} else if (!llLim.equals(other.llLim))
			return false;
		if (max == null) {
			if (other.max != null)
				return false;
		} else if (!max.equals(other.max))
			return false;
		if (min == null) {
			if (other.min != null)
				return false;
		} else if (!min.equals(other.min))
			return false;
		return true;
	}
}
