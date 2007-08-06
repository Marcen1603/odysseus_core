package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class LPL implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DPLSEQ")
	private Integer id;

	private String vendor;

	private String swRef;

	private String d;

	private String dU;

	private String configRev;

	private String ldNs;

	private String lnNs;

	private String cdcNs;

	private String cdcName;

	private String dataNs;

	public LPL() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setVendor(String param) {
		this.vendor = param;
	}

	public String getVendor() {
		return vendor;
	}

	public void setSwRef(String param) {
		this.swRef = param;
	}

	public String getSwRef() {
		return swRef;
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

	public void setConfigRev(String param) {
		this.configRev = param;
	}

	public String getConfigRev() {
		return configRev;
	}

	public void setLdNs(String param) {
		this.ldNs = param;
	}

	public String getLdNs() {
		return ldNs;
	}

	public void setLnNs(String param) {
		this.lnNs = param;
	}

	public String getLnNs() {
		return lnNs;
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
		result = PRIME * result + ((configRev == null) ? 0 : configRev.hashCode());
		result = PRIME * result + ((d == null) ? 0 : d.hashCode());
		result = PRIME * result + ((dU == null) ? 0 : dU.hashCode());
		result = PRIME * result + ((dataNs == null) ? 0 : dataNs.hashCode());
		result = PRIME * result + ((ldNs == null) ? 0 : ldNs.hashCode());
		result = PRIME * result + ((lnNs == null) ? 0 : lnNs.hashCode());
		result = PRIME * result + ((swRef == null) ? 0 : swRef.hashCode());
		result = PRIME * result + ((vendor == null) ? 0 : vendor.hashCode());
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
		final LPL other = (LPL) obj;
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
		if (configRev == null) {
			if (other.configRev != null)
				return false;
		} else if (!configRev.equals(other.configRev))
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
		if (ldNs == null) {
			if (other.ldNs != null)
				return false;
		} else if (!ldNs.equals(other.ldNs))
			return false;
		if (lnNs == null) {
			if (other.lnNs != null)
				return false;
		} else if (!lnNs.equals(other.lnNs))
			return false;
		if (swRef == null) {
			if (other.swRef != null)
				return false;
		} else if (!swRef.equals(other.swRef))
			return false;
		if (vendor == null) {
			if (other.vendor != null)
				return false;
		} else if (!vendor.equals(other.vendor))
			return false;
		return true;
	}
}
