package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class SPV implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVSeq")
    private Integer id;
    
    @OneToOne
    private SPC chaManRs;
    @OneToOne
    private APC ActVal;
    @OneToOne
    private APC oldVal;
    @OneToOne
    private AnalogueValue minMxVal;
    @OneToOne
    private AnalogueValue maxMxVal;
    @OneToOne
    private AnalogueValue totAvVal;
    @OneToOne
    private AnalogueValue sdvVal;
    @OneToOne
    private Unit units;
    @OneToOne
    private AnalogueValue minval;
    @OneToOne
    private AnalogueValue maxVal;
    @OneToOne
    private AnalogueValue incRate;
    @OneToOne
    private AnalogueValue decRate;
    @OneToOne
    private AnalogueValue spAcs;
    @Enumerated
    private TimePeriodicalReset chaPerRs;
    private String d;
    private String dU;
    private String cdcNs;
    private String cdcName;
    private String dataNs;

    public SPV() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setChaManRs(SPC param) {
        this.chaManRs = param;
    }

    public SPC getChaManRs() {
        return chaManRs;
    }

    public void setActVal(APC actVal) {
        this.ActVal = actVal;
    }

    public APC getActVal() {
        return ActVal;
    }

    public void setOldVal(APC param) {
        this.oldVal = param;
    }

    public APC getOldVal() {
        return oldVal;
    }

    public void setMinMxVal(AnalogueValue param) {
        this.minMxVal = param;
    }

    public AnalogueValue getMinMxVal() {
        return minMxVal;
    }

    public void setMaxMxVal(AnalogueValue param) {
        this.maxMxVal = param;
    }

    public AnalogueValue getMaxMxVal() {
        return maxMxVal;
    }

    public void setTotAvVal(AnalogueValue param) {
        this.totAvVal = param;
    }

    public AnalogueValue getTotAvVal() {
        return totAvVal;
    }

    public void setSdvVal(AnalogueValue param) {
        this.sdvVal = param;
    }

    public AnalogueValue getSdvVal() {
        return sdvVal;
    }

    public void setUnits(Unit param) {
        this.units = param;
    }

    public Unit getUnits() {
        return units;
    }

    public void setMinval(AnalogueValue param) {
        this.minval = param;
    }

    public AnalogueValue getMinval() {
        return minval;
    }

    public void setMaxVal(AnalogueValue param) {
        this.maxVal = param;
    }

    public AnalogueValue getMaxVal() {
        return maxVal;
    }

    public void setIncRate(AnalogueValue param) {
        this.incRate = param;
    }

    public AnalogueValue getIncRate() {
        return incRate;
    }

    public void setDecRate(AnalogueValue param) {
        this.decRate = param;
    }

    public AnalogueValue getDecRate() {
        return decRate;
    }

    public void setSpAcs(AnalogueValue param) {
        this.spAcs = param;
    }

    public AnalogueValue getSpAcs() {
        return spAcs;
    }

    public void setChaPerRs(TimePeriodicalReset param) {
        this.chaPerRs = param;
    }

    public TimePeriodicalReset getChaPerRs() {
        return chaPerRs;
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
		result = PRIME * result + ((ActVal == null) ? 0 : ActVal.hashCode());
		result = PRIME * result + ((cdcName == null) ? 0 : cdcName.hashCode());
		result = PRIME * result + ((cdcNs == null) ? 0 : cdcNs.hashCode());
		result = PRIME * result + ((chaManRs == null) ? 0 : chaManRs.hashCode());
		result = PRIME * result + ((chaPerRs == null) ? 0 : chaPerRs.hashCode());
		result = PRIME * result + ((d == null) ? 0 : d.hashCode());
		result = PRIME * result + ((dU == null) ? 0 : dU.hashCode());
		result = PRIME * result + ((dataNs == null) ? 0 : dataNs.hashCode());
		result = PRIME * result + ((decRate == null) ? 0 : decRate.hashCode());
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
		result = PRIME * result + ((incRate == null) ? 0 : incRate.hashCode());
		result = PRIME * result + ((maxMxVal == null) ? 0 : maxMxVal.hashCode());
		result = PRIME * result + ((maxVal == null) ? 0 : maxVal.hashCode());
		result = PRIME * result + ((minMxVal == null) ? 0 : minMxVal.hashCode());
		result = PRIME * result + ((minval == null) ? 0 : minval.hashCode());
		result = PRIME * result + ((oldVal == null) ? 0 : oldVal.hashCode());
		result = PRIME * result + ((sdvVal == null) ? 0 : sdvVal.hashCode());
		result = PRIME * result + ((spAcs == null) ? 0 : spAcs.hashCode());
		result = PRIME * result + ((totAvVal == null) ? 0 : totAvVal.hashCode());
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
		final SPV other = (SPV) obj;
		if (ActVal == null) {
			if (other.ActVal != null)
				return false;
		} else if (!ActVal.equals(other.ActVal))
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
		if (chaManRs == null) {
			if (other.chaManRs != null)
				return false;
		} else if (!chaManRs.equals(other.chaManRs))
			return false;
		if (chaPerRs == null) {
			if (other.chaPerRs != null)
				return false;
		} else if (!chaPerRs.equals(other.chaPerRs))
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
		if (decRate == null) {
			if (other.decRate != null)
				return false;
		} else if (!decRate.equals(other.decRate))
			return false;
		if (incRate == null) {
			if (other.incRate != null)
				return false;
		} else if (!incRate.equals(other.incRate))
			return false;
		if (maxMxVal == null) {
			if (other.maxMxVal != null)
				return false;
		} else if (!maxMxVal.equals(other.maxMxVal))
			return false;
		if (maxVal == null) {
			if (other.maxVal != null)
				return false;
		} else if (!maxVal.equals(other.maxVal))
			return false;
		if (minMxVal == null) {
			if (other.minMxVal != null)
				return false;
		} else if (!minMxVal.equals(other.minMxVal))
			return false;
		if (minval == null) {
			if (other.minval != null)
				return false;
		} else if (!minval.equals(other.minval))
			return false;
		if (oldVal == null) {
			if (other.oldVal != null)
				return false;
		} else if (!oldVal.equals(other.oldVal))
			return false;
		if (sdvVal == null) {
			if (other.sdvVal != null)
				return false;
		} else if (!sdvVal.equals(other.sdvVal))
			return false;
		if (spAcs == null) {
			if (other.spAcs != null)
				return false;
		} else if (!spAcs.equals(other.spAcs))
			return false;
		if (totAvVal == null) {
			if (other.totAvVal != null)
				return false;
		} else if (!totAvVal.equals(other.totAvVal))
			return false;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
		return true;
	}
}
