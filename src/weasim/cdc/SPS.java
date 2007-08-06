package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class SPS implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPCSEQ")
	private Integer id;

	private boolean stVal;

	@OneToOne
	private Quality q;
	
	@OneToOne
	private TimeStamp t;

	private boolean subEna;

	private boolean subVal;

	@OneToOne
	private Quality subQ;

	@OneToOne
	private TimeStamp subT;

	private String subId;

	private String d;

	private String dU;

	private String cdcNs;

	private String cdcName;

	private String dataNs;

	public SPS() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setStVal(boolean param) {
		this.stVal = param;
	}

	public boolean isStVal() {
		return stVal;
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

	public void setSubEna(boolean param) {
		this.subEna = param;
	}

	public boolean isSubEna() {
		return subEna;
	}

	public void setSubVal(boolean param) {
		this.subVal = param;
	}

	public boolean isSubVal() {
		return subVal;
	}

	public void setSubQ(Quality param) {
		this.subQ = param;
	}

	public Quality getSubQ() {
		return subQ;
	}

	public void setSubT(TimeStamp param) {
		this.subT = param;
	}

	public TimeStamp getSubT() {
		return subT;
	}

	public void setSubId(String param) {
		this.subId = param;
	}

	public String getSubId() {
		return subId;
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
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
		result = PRIME * result + ((q == null) ? 0 : q.hashCode());
		result = PRIME * result + (stVal ? 1231 : 1237);
		result = PRIME * result + (subEna ? 1231 : 1237);
		result = PRIME * result + ((subId == null) ? 0 : subId.hashCode());
		result = PRIME * result + ((subQ == null) ? 0 : subQ.hashCode());
		result = PRIME * result + ((subT == null) ? 0 : subT.hashCode());
		result = PRIME * result + (subVal ? 1231 : 1237);
		result = PRIME * result + ((t == null) ? 0 : t.hashCode());
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
		final SPS other = (SPS) obj;
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
		if (q == null) {
			if (other.q != null)
				return false;
		} else if (!q.equals(other.q))
			return false;
		if (stVal != other.stVal)
			return false;
		if (subEna != other.subEna)
			return false;
		if (subId == null) {
			if (other.subId != null)
				return false;
		} else if (!subId.equals(other.subId))
			return false;
		if (subQ == null) {
			if (other.subQ != null)
				return false;
		} else if (!subQ.equals(other.subQ))
			return false;
		if (subT == null) {
			if (other.subT != null)
				return false;
		} else if (!subT.equals(other.subT))
			return false;
		if (subVal != other.subVal)
			return false;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		return true;
	}
}
