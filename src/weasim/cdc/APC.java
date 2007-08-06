package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class APC implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DPLSEQ")
	private Integer id;

	private String d;

	private String dU;

	private String cdcNs;

	private String cdcName;

	private String dataNs;

	@Enumerated
	private CtlModels ctlModel;

	@OneToOne
	private AnalogueValue maxVal;

	@OneToOne
	private AnalogueValue minVal;

	@OneToOne
	private TimeStamp operTm;

	@OneToOne
	private Originator origin;

	@OneToOne
	private Quality q;

	@OneToOne
	private AnalogueValue setMag;

	@OneToOne
	private AnalogueValue stepSize;

	@OneToOne
	private ScaledValueConfig sVC;

	@OneToOne
	private TimeStamp t;

	@OneToOne
	private Unit units;

	public APC() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public void setCtlModel(CtlModels param) {
		this.ctlModel = param;
	}

	public CtlModels getCtlModel() {
		return ctlModel;
	}

	public void setMaxVal(AnalogueValue param) {
		this.maxVal = param;
	}

	public AnalogueValue getMaxVal() {
		return maxVal;
	}

	public void setMinVal(AnalogueValue param) {
		this.minVal = param;
	}

	public AnalogueValue getMinVal() {
		return minVal;
	}

	public void setOperTm(TimeStamp param) {
		this.operTm = param;
	}

	public TimeStamp getOperTm() {
		return operTm;
	}

	public void setOrigin(Originator param) {
		this.origin = param;
	}

	public Originator getOrigin() {
		return origin;
	}

	public void setQ(Quality param) {
		this.q = param;
	}

	public Quality getQ() {
		return q;
	}

	public void setSetMag(AnalogueValue param) {
		this.setMag = param;
	}

	public AnalogueValue getSetMag() {
		return setMag;
	}

	public void setStepSize(AnalogueValue param) {
		this.stepSize = param;
	}

	public AnalogueValue getStepSize() {
		return stepSize;
	}

	public void setT(TimeStamp param) {
		this.t = param;
	}

	public TimeStamp getT() {
		return t;
	}

	public void setUnits(Unit param) {
		this.units = param;
	}

	public Unit getUnits() {
		return units;
	}

	public ScaledValueConfig getSVC() {
		return sVC;
	}

	public void setSVC(ScaledValueConfig svc) {
		sVC = svc;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((cdcName == null) ? 0 : cdcName.hashCode());
		result = PRIME * result + ((cdcNs == null) ? 0 : cdcNs.hashCode());
		result = PRIME * result + ((ctlModel == null) ? 0 : ctlModel.hashCode());
		result = PRIME * result + ((d == null) ? 0 : d.hashCode());
		result = PRIME * result + ((dU == null) ? 0 : dU.hashCode());
		result = PRIME * result + ((dataNs == null) ? 0 : dataNs.hashCode());
		result = PRIME * result + ((maxVal == null) ? 0 : maxVal.hashCode());
		result = PRIME * result + ((minVal == null) ? 0 : minVal.hashCode());
		result = PRIME * result + ((operTm == null) ? 0 : operTm.hashCode());
		result = PRIME * result + ((origin == null) ? 0 : origin.hashCode());
		result = PRIME * result + ((q == null) ? 0 : q.hashCode());
		result = PRIME * result + ((sVC == null) ? 0 : sVC.hashCode());
		result = PRIME * result + ((setMag == null) ? 0 : setMag.hashCode());
		result = PRIME * result + ((stepSize == null) ? 0 : stepSize.hashCode());
		result = PRIME * result + ((t == null) ? 0 : t.hashCode());
		result = PRIME * result + ((units == null) ? 0 : units.hashCode());
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
		final APC other = (APC) obj;
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
		if (ctlModel == null) {
			if (other.ctlModel != null)
				return false;
		} else if (!ctlModel.equals(other.ctlModel))
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
		if (operTm == null) {
			if (other.operTm != null)
				return false;
		} else if (!operTm.equals(other.operTm))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (q == null) {
			if (other.q != null)
				return false;
		} else if (!q.equals(other.q))
			return false;
		if (sVC == null) {
			if (other.sVC != null)
				return false;
		} else if (!sVC.equals(other.sVC))
			return false;
		if (setMag == null) {
			if (other.setMag != null)
				return false;
		} else if (!setMag.equals(other.setMag))
			return false;
		if (stepSize == null) {
			if (other.stepSize != null)
				return false;
		} else if (!stepSize.equals(other.stepSize))
			return false;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
		return true;
	}
}
