package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "STVSeq", sequenceName = "STVSEQ", allocationSize = 1,
    initialValue = 1)
public class STV implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVSeq")
    private Integer id;
    
    @OneToOne
    private INS actSt;
    @OneToOne
    private INS oldSt;
    @OneToOne
    private TMS stTm;
    @OneToOne
    private CTE stCt;
    private Integer preTmms;
    private Integer pstTmms;
    private Integer smpTmms;
    private String dataSetMx;
    private String d;
    private String dU;
    private String cdcNs;
    private String cdcName;
    private String dataNs;

    public STV() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setActSt(INS param) {
        this.actSt = param;
    }

    public INS getActSt() {
        return actSt;
    }

    public void setOldSt(INS param) {
        this.oldSt = param;
    }

    public INS getOldSt() {
        return oldSt;
    }

    public void setStTm(TMS param) {
        this.stTm = param;
    }

    public TMS getStTm() {
        return stTm;
    }

    public void setStCt(CTE param) {
        this.stCt = param;
    }

    public CTE getStCt() {
        return stCt;
    }

    public void setPreTmms(Integer param) {
        this.preTmms = param;
    }

    public Integer getPreTmms() {
        return preTmms;
    }

    public void setPstTmms(Integer param) {
        this.pstTmms = param;
    }

    public Integer getPstTmms() {
        return pstTmms;
    }

    public void setSmpTmms(Integer param) {
        this.smpTmms = param;
    }

    public Integer getSmpTmms() {
        return smpTmms;
    }

    public void setDataSetMx(String param) {
        this.dataSetMx = param;
    }

    public String getDataSetMx() {
        return dataSetMx;
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
		result = PRIME * result + ((actSt == null) ? 0 : actSt.hashCode());
		result = PRIME * result + ((cdcName == null) ? 0 : cdcName.hashCode());
		result = PRIME * result + ((cdcNs == null) ? 0 : cdcNs.hashCode());
		result = PRIME * result + ((d == null) ? 0 : d.hashCode());
		result = PRIME * result + ((dU == null) ? 0 : dU.hashCode());
		result = PRIME * result + ((dataNs == null) ? 0 : dataNs.hashCode());
		result = PRIME * result + ((dataSetMx == null) ? 0 : dataSetMx.hashCode());
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
		result = PRIME * result + ((oldSt == null) ? 0 : oldSt.hashCode());
		result = PRIME * result + ((preTmms == null) ? 0 : preTmms.hashCode());
		result = PRIME * result + ((pstTmms == null) ? 0 : pstTmms.hashCode());
		result = PRIME * result + ((smpTmms == null) ? 0 : smpTmms.hashCode());
		result = PRIME * result + ((stCt == null) ? 0 : stCt.hashCode());
		result = PRIME * result + ((stTm == null) ? 0 : stTm.hashCode());
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
		final STV other = (STV) obj;
		if (actSt == null) {
			if (other.actSt != null)
				return false;
		} else if (!actSt.equals(other.actSt))
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
		if (dataSetMx == null) {
			if (other.dataSetMx != null)
				return false;
		} else if (!dataSetMx.equals(other.dataSetMx))
			return false;
		if (oldSt == null) {
			if (other.oldSt != null)
				return false;
		} else if (!oldSt.equals(other.oldSt))
			return false;
		if (preTmms == null) {
			if (other.preTmms != null)
				return false;
		} else if (!preTmms.equals(other.preTmms))
			return false;
		if (pstTmms == null) {
			if (other.pstTmms != null)
				return false;
		} else if (!pstTmms.equals(other.pstTmms))
			return false;
		if (smpTmms == null) {
			if (other.smpTmms != null)
				return false;
		} else if (!smpTmms.equals(other.smpTmms))
			return false;
		if (stCt == null) {
			if (other.stCt != null)
				return false;
		} else if (!stCt.equals(other.stCt))
			return false;
		if (stTm == null) {
			if (other.stTm != null)
				return false;
		} else if (!stTm.equals(other.stTm))
			return false;
		return true;
	}
}
