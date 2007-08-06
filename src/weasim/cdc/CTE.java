package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "CTESEQ", sequenceName = "CTESEQ", allocationSize = 1, initialValue = 1)
public class CTE implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CTESEQ")
	private Integer id;

	@OneToOne
	private SPC manRs;

	@OneToOne
	private INC hisRs;

	@OneToOne
	private INS actCtVal;

	@OneToOne
	private INS oldCtVal;

	private Integer ctTot;

	// TODO arrays behandeln
	// private Integer[] dly;
	//    
	// private Integer[] mly;
	//    
	// private Integer[] yly;

	private Integer tot;

	@Enumerated
	private TimePeriodicalReset rsPer;

	public CTE() {
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

	public void setActCtVal(INS param) {
		this.actCtVal = param;
	}

	public INS getActCtVal() {
		return actCtVal;
	}

	public void setOldCtVal(INS param) {
		this.oldCtVal = param;
	}

	public INS getOldCtVal() {
		return oldCtVal;
	}

	public void setCtTot(Integer param) {
		this.ctTot = param;
	}

	public Integer getCtTot() {
		return ctTot;
	}

	// public void setDly(Integer[] param) {
	// this.dly = param;
	// }
	//
	// public Integer[] getDly() {
	// return dly;
	// }
	//
	// public void setMly(Integer[] param) {
	// this.mly = param;
	// }
	//
	// public Integer[] getMly() {
	// return mly;
	// }
	//
	// public void setYly(Integer[] param) {
	// this.yly = param;
	// }
	//
	// public Integer[] getYly() {
	// return yly;
	// }

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

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((actCtVal == null) ? 0 : actCtVal.hashCode());
		result = PRIME * result + ((ctTot == null) ? 0 : ctTot.hashCode());
		result = PRIME * result + ((hisRs == null) ? 0 : hisRs.hashCode());
		result = PRIME * result + ((manRs == null) ? 0 : manRs.hashCode());
		result = PRIME * result + ((oldCtVal == null) ? 0 : oldCtVal.hashCode());
		result = PRIME * result + ((rsPer == null) ? 0 : rsPer.hashCode());
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
		final CTE other = (CTE) obj;
		if (actCtVal == null) {
			if (other.actCtVal != null)
				return false;
		} else if (!actCtVal.equals(other.actCtVal))
			return false;
		if (ctTot == null) {
			if (other.ctTot != null)
				return false;
		} else if (!ctTot.equals(other.ctTot))
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
		if (oldCtVal == null) {
			if (other.oldCtVal != null)
				return false;
		} else if (!oldCtVal.equals(other.oldCtVal))
			return false;
		if (rsPer == null) {
			if (other.rsPer != null)
				return false;
		} else if (!rsPer.equals(other.rsPer))
			return false;
		if (tot == null) {
			if (other.tot != null)
				return false;
		} else if (!tot.equals(other.tot))
			return false;
		return true;
	}
}
