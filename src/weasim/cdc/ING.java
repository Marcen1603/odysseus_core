package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ING implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INCSeq")
	private Integer id;

	private Integer setVal;

	private Integer maxVal;

	private Integer minVal;

	private Integer stepSize;

	private String d;

	private String dU;

	private String cdcNs;

	private String cdcName;

	private String dataNs;

	public ING() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSetVal(Integer param) {
		this.setVal = param;
	}

	public Integer getSetVal() {
		return setVal;
	}

	public void setMaxVal(Integer param) {
		this.maxVal = param;
	}

	public Integer getMaxVal() {
		return maxVal;
	}

	public void setMinVal(Integer param) {
		this.minVal = param;
	}

	public Integer getMinVal() {
		return minVal;
	}

	public void setStepSize(Integer param) {
		this.stepSize = param;
	}

	public Integer getStepSize() {
		return stepSize;
	}

	public void setD(String param) {
		this.d = param;
	}

	public String getD() {
		return d;
	}

	public void setDU(String param) {
		this.dU = param;
	}

	public String getDU() {
		return dU;
	}

	public void setCdcNs(String param) {
		this.cdcNs = param;
	}

	public String getCdcNs() {
		return cdcNs;
	}

	public void setCdcName(String param) {
		this.cdcName = param;
	}

	public String getCdcName() {
		return cdcName;
	}

	public void setDataNs(String param) {
		this.dataNs = param;
	}

	public String getDataNs() {
		return dataNs;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((cdcName == null) ? 0 : cdcName.hashCode());
		result = PRIME * result + ((cdcNs == null) ? 0 : cdcNs.hashCode());
		result = PRIME * result + ((d == null) ? 0 : d.hashCode());
		result = PRIME * result + ((dU == null) ? 0 : dU.hashCode());
		result = PRIME * result + ((dataNs == null) ? 0 : dataNs.hashCode());
		result = PRIME * result + ((maxVal == null) ? 0 : maxVal.hashCode());
		result = PRIME * result + ((minVal == null) ? 0 : minVal.hashCode());
		result = PRIME * result + ((setVal == null) ? 0 : setVal.hashCode());
		result = PRIME * result + ((stepSize == null) ? 0 : stepSize.hashCode());
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
		final ING other = (ING) obj;
		if (cdcName == null) {
			if (other.cdcName != null)
				return false;
		} else if (!cdcName.equals(other.cdcName))
			return false;
		if (cdcNs == null) {
			if (other.cdcNs != null)
				return false;
		} else if (!cdcNs.equals(other.cdcNs))
			return false;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		if (dU == null) {
			if (other.dU != null)
				return false;
		} else if (!dU.equals(other.dU))
			return false;
		if (dataNs == null) {
			if (other.dataNs != null)
				return false;
		} else if (!dataNs.equals(other.dataNs))
			return false;
		if (maxVal == null) {
			if (other.maxVal != null)
				return false;
		} else if (!maxVal.equals(other.maxVal))
			return false;
		if (minVal == null) {
			if (other.minVal != null)
				return false;
		} else if (!minVal.equals(other.minVal))
			return false;
		if (setVal == null) {
			if (other.setVal != null)
				return false;
		} else if (!setVal.equals(other.setVal))
			return false;
		if (stepSize == null) {
			if (other.stepSize != null)
				return false;
		} else if (!stepSize.equals(other.stepSize))
			return false;
		return true;
	}
}
