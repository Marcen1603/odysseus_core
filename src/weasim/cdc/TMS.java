package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class TMS implements Serializable {
    @Id
    private Integer id;

    @OneToOne
    private SPC manRs;
    
    @OneToOne
    private INC hisRs;
    
    @OneToOne
    private INS actTmVal;
    
    @OneToOne
    private INS oldTmVal;
        
    private Integer tmTot;
    
//    TODO arrays implementieren
//    private Integer[] dly;
//    
//    private Integer[] mly;
//    
//    private Integer[] yly;
    
    private Integer tot;
    
    private TimePeriodicalReset rsPer;
    
    private String d;
    private String dU;
    private String cdcNs;
    private String cdcName;
    private String dataNs;
        
        
        
    public TMS() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setManRs(SPC param) {
        this.manRs = param;
    }

    public SPC getManRs() {
        return manRs;
    }

    public void setHisRs(INC param) {
        this.hisRs = param;
    }

    public INC getHisRs() {
        return hisRs;
    }

    public void setActTmVal(INS param) {
        this.actTmVal = param;
    }

    public INS getActTmVal() {
        return actTmVal;
    }

    public void setOldTmVal(INS param) {
        this.oldTmVal = param;
    }

    public INS getOldTmVal() {
        return oldTmVal;
    }

    public void setTmTot(Integer param) {
        this.tmTot = param;
    }

    public Integer getTmTot() {
        return tmTot;
    }

    public void setTot(Integer param) {
        this.tot = param;
    }

    public Integer getTot() {
        return tot;
    }

    public void setRsPer(TimePeriodicalReset param) {
        this.rsPer = param;
    }

    public TimePeriodicalReset getRsPer() {
        return rsPer;
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
		result = PRIME * result + ((actTmVal == null) ? 0 : actTmVal.hashCode());
		result = PRIME * result + ((cdcName == null) ? 0 : cdcName.hashCode());
		result = PRIME * result + ((cdcNs == null) ? 0 : cdcNs.hashCode());
		result = PRIME * result + ((d == null) ? 0 : d.hashCode());
		result = PRIME * result + ((dU == null) ? 0 : dU.hashCode());
		result = PRIME * result + ((dataNs == null) ? 0 : dataNs.hashCode());
		result = PRIME * result + ((hisRs == null) ? 0 : hisRs.hashCode());
		result = PRIME * result + ((manRs == null) ? 0 : manRs.hashCode());
		result = PRIME * result + ((oldTmVal == null) ? 0 : oldTmVal.hashCode());
		result = PRIME * result + ((rsPer == null) ? 0 : rsPer.hashCode());
		result = PRIME * result + ((tmTot == null) ? 0 : tmTot.hashCode());
		result = PRIME * result + ((tot == null) ? 0 : tot.hashCode());
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
		final TMS other = (TMS) obj;
		if (actTmVal == null) {
			if (other.actTmVal != null)
				return false;
		} else if (!actTmVal.equals(other.actTmVal))
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
		if (hisRs == null) {
			if (other.hisRs != null)
				return false;
		} else if (!hisRs.equals(other.hisRs))
			return false;
		if (manRs == null) {
			if (other.manRs != null)
				return false;
		} else if (!manRs.equals(other.manRs))
			return false;
		if (oldTmVal == null) {
			if (other.oldTmVal != null)
				return false;
		} else if (!oldTmVal.equals(other.oldTmVal))
			return false;
		if (rsPer == null) {
			if (other.rsPer != null)
				return false;
		} else if (!rsPer.equals(other.rsPer))
			return false;
		if (tmTot == null) {
			if (other.tmTot != null)
				return false;
		} else if (!tmTot.equals(other.tmTot))
			return false;
		if (tot == null) {
			if (other.tot != null)
				return false;
		} else if (!tot.equals(other.tot))
			return false;
		return true;
	}
}
