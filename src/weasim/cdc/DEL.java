package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class DEL implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPCSEQ")
	private Integer id;

	@OneToOne
	private CMV phsAB;

	@OneToOne
	private CMV phsBC;

	@OneToOne
	private CMV phsCA;

	@Enumerated
	private AngRef2 angRef2;

	private String d;

	private String dU;

	private String cdcNs;

	private String cdcName;

	private String dataNs;

	public DEL() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPhsAB(CMV param) {
		this.phsAB = param;
	}

	public CMV getPhsAB() {
		return phsAB;
	}

	public void setPhsBC(CMV param) {
		this.phsBC = param;
	}

	public CMV getPhsBC() {
		return phsBC;
	}

	public void setPhsCA(CMV param) {
		this.phsCA = param;
	}

	public CMV getPhsCA() {
		return phsCA;
	}

	public void setAngRef(AngRef2 param) {
		this.angRef2 = param;
	}

	public AngRef2 getAngRef() {
		return angRef2;
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
		result = PRIME * result + ((angRef2 == null) ? 0 : angRef2.hashCode());
		result = PRIME * result + ((cdcName == null) ? 0 : cdcName.hashCode());
		result = PRIME * result + ((cdcNs == null) ? 0 : cdcNs.hashCode());
		result = PRIME * result + ((d == null) ? 0 : d.hashCode());
		result = PRIME * result + ((dU == null) ? 0 : dU.hashCode());
		result = PRIME * result + ((dataNs == null) ? 0 : dataNs.hashCode());
		result = PRIME * result + ((phsAB == null) ? 0 : phsAB.hashCode());
		result = PRIME * result + ((phsBC == null) ? 0 : phsBC.hashCode());
		result = PRIME * result + ((phsCA == null) ? 0 : phsCA.hashCode());
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
		final DEL other = (DEL) obj;
		if (angRef2 == null) {
			if (other.angRef2 != null)
				return false;
		} else if (!angRef2.equals(other.angRef2))
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
		if (phsAB == null) {
			if (other.phsAB != null)
				return false;
		} else if (!phsAB.equals(other.phsAB))
			return false;
		if (phsBC == null) {
			if (other.phsBC != null)
				return false;
		} else if (!phsBC.equals(other.phsBC))
			return false;
		if (phsCA == null) {
			if (other.phsCA != null)
				return false;
		} else if (!phsCA.equals(other.phsCA))
			return false;
		return true;
	}
}
