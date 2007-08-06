package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "DPLSEQ", sequenceName = "DPLSEQ", allocationSize = 1,
    initialValue = 1)
public class DPL implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DPLSEQ")
    private Integer id;
    
    private String vendor;
    private String hwRef;
    private String swRef;
    private String serNum;
    private String model;
    private String location;
    private String cdcNs;
    private String cdcName;
    private String dataNs;


    public DPL() {
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

    public void setHwRef(String param) {
        this.hwRef = param;
    }

    public String getHwRef() {
        return hwRef;
    }

    public void setSwRef(String param) {
        this.swRef = param;
    }

    public String getSwRef() {
        return swRef;
    }

    public void setSerNum(String param) {
        this.serNum = param;
    }

    public String getSerNum() {
        return serNum;
    }

    public void setModel(String param) {
        this.model = param;
    }

    public String getModel() {
        return model;
    }

    public void setLocation(String param) {
        this.location = param;
    }

    public String getLocation() {
        return location;
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
		result = PRIME * result + ((dataNs == null) ? 0 : dataNs.hashCode());
		result = PRIME * result + ((hwRef == null) ? 0 : hwRef.hashCode());
		result = PRIME * result + ((location == null) ? 0 : location.hashCode());
		result = PRIME * result + ((model == null) ? 0 : model.hashCode());
		result = PRIME * result + ((serNum == null) ? 0 : serNum.hashCode());
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
		final DPL other = (DPL) obj;
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
		if (dataNs == null) {
			if (other.dataNs != null)
				return false;
		} else if (!dataNs.equals(other.dataNs))
			return false;
		if (hwRef == null) {
			if (other.hwRef != null)
				return false;
		} else if (!hwRef.equals(other.hwRef))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (serNum == null) {
			if (other.serNum != null)
				return false;
		} else if (!serNum.equals(other.serNum))
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
