package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class BCR implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CTESEQ")
	private Integer id;

	private byte actVal;

	private byte frVal;

	@OneToOne
	private TimeStamp frTm;

	@OneToOne
	private Quality q;

	@OneToOne
	private TimeStamp t;

	@OneToOne
	private Unit units;

	private Double pulsCty;

	private boolean frEna;

	@OneToOne
	private TimeStamp strTm;

	private Integer frPd;

	private boolean frRs;

	private String d;

	private String dU;

	private String cdcNs;

	private String cdcName;

	private String dataNs;

	public BCR() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setActVal(byte param) {
		this.actVal = param;
	}

	public byte getActVal() {
		return actVal;
	}

	public void setFrVal(byte param) {
		this.frVal = param;
	}

	public byte getFrVal() {
		return frVal;
	}

	public void setFrTm(TimeStamp param) {
		this.frTm = param;
	}

	public TimeStamp getFrTm() {
		return frTm;
	}

	public void setQ(Quality param) {
		this.q = param;
	}

	public Quality getQ() {
		return q;
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

	public void setPulsCty(Double param) {
		this.pulsCty = param;
	}

	public Double getPulsCty() {
		return pulsCty;
	}

	public void setFrEna(boolean param) {
		this.frEna = param;
	}

	public boolean isFrEna() {
		return frEna;
	}

	public void setStrTm(TimeStamp param) {
		this.strTm = param;
	}

	public TimeStamp getStrTm() {
		return strTm;
	}

	public void setFrPd(Integer param) {
		this.frPd = param;
	}

	public Integer getFrPd() {
		return frPd;
	}

	public void setFrRs(boolean param) {
		this.frRs = param;
	}

	public boolean isFrRs() {
		return frRs;
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
		result = PRIME * result + (frEna ? 1231 : 1237);
		result = PRIME * result + ((frPd == null) ? 0 : frPd.hashCode());
		result = PRIME * result + (frRs ? 1231 : 1237);
		result = PRIME * result + ((frTm == null) ? 0 : frTm.hashCode());
		result = PRIME * result + ((pulsCty == null) ? 0 : pulsCty.hashCode());
		result = PRIME * result + ((q == null) ? 0 : q.hashCode());
		result = PRIME * result + ((strTm == null) ? 0 : strTm.hashCode());
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
		final BCR other = (BCR) obj;
		if (actVal != other.actVal)
			return false;
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
		if (frEna != other.frEna)
			return false;
		if (frPd == null) {
			if (other.frPd != null)
				return false;
		} else if (!frPd.equals(other.frPd))
			return false;
		if (frRs != other.frRs)
			return false;
		if (frTm == null) {
			if (other.frTm != null)
				return false;
		} else if (!frTm.equals(other.frTm))
			return false;
		if (frVal != other.frVal)
			return false;
		if (pulsCty == null) {
			if (other.pulsCty != null)
				return false;
		} else if (!pulsCty.equals(other.pulsCty))
			return false;
		if (q == null) {
			if (other.q != null)
				return false;
		} else if (!q.equals(other.q))
			return false;
		if (strTm == null) {
			if (other.strTm != null)
				return false;
		} else if (!strTm.equals(other.strTm))
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
