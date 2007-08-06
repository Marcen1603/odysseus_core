package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Unit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVSeq")
    private Integer id;

    @Enumerated
    private SIUnit SIUnit;
    private Integer multiplier;
    
    public Unit() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSIUnit(SIUnit sIUnit) {
        this.SIUnit = sIUnit;
    }

    public SIUnit getSIUnit() {
        return SIUnit;
    }

    public void setMultiplier(Integer param) {
        this.multiplier = param;
    }

    public Integer getMultiplier() {
        return multiplier;
    }

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((SIUnit == null) ? 0 : SIUnit.hashCode());
		result = PRIME * result + ((multiplier == null) ? 0 : multiplier.hashCode());
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
		final Unit other = (Unit) obj;
		if (SIUnit == null) {
			if (other.SIUnit != null)
				return false;
		} else if (!SIUnit.equals(other.SIUnit))
			return false;
		if (multiplier == null) {
			if (other.multiplier != null)
				return false;
		} else if (!multiplier.equals(other.multiplier))
			return false;
		return true;
	}
}
