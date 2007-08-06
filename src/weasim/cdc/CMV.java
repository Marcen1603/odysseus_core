package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class CMV implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CMDSeq")
	private Integer id;

	@OneToOne
	private Vector instCVal;

	@OneToOne
	private Vector cVal;

	@Enumerated
	private Range range;

	@OneToOne
	private Quality q;

	@OneToOne
	private TimeStamp t;

	private boolean subEna;

	@OneToOne
	private Vector subCVal;

	@OneToOne
	private Quality subQ;

	private String subId;

	@OneToOne
	private Unit units;

	private Integer db;

	private Integer zeroDb;

	@OneToOne
	private RangeConfig rangeC;

	@OneToOne
	private ScaledValueConfig magSVC;

	@OneToOne
	private ScaledValueConfig angSVC;

	@Enumerated
	private AngRef1 angRef;

	private Integer smpRate;

	private String d;

	private String dU;

	private String cdcNs;

	private String cdcName;

	private String dataNs;

	public CMV() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AngRef1 getAngRef() {
		return angRef;
	}

	public void setAngRef(AngRef1 angRef) {
		this.angRef = angRef;
	}

	public ScaledValueConfig getAngSVC() {
		return angSVC;
	}

	public void setAngSVC(ScaledValueConfig angSVC) {
		this.angSVC = angSVC;
	}

	public String getCdcName() {
		return cdcName;
	}

	public void setCdcName(String cdcName) {
		this.cdcName = cdcName;
	}

	public String getCdcNs() {
		return cdcNs;
	}

	public void setCdcNs(String cdcNs) {
		this.cdcNs = cdcNs;
	}

	public Vector getCVal() {
		return cVal;
	}

	public void setCVal(Vector val) {
		cVal = val;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getDataNs() {
		return dataNs;
	}

	public void setDataNs(String dataNs) {
		this.dataNs = dataNs;
	}

	public Integer getDb() {
		return db;
	}

	public void setDb(Integer db) {
		this.db = db;
	}

	public String getDU() {
		return dU;
	}

	public void setDU(String du) {
		dU = du;
	}

	public Vector getInstCVal() {
		return instCVal;
	}

	public void setInstCVal(Vector instCVal) {
		this.instCVal = instCVal;
	}

	public ScaledValueConfig getMagSVC() {
		return magSVC;
	}

	public void setMagSVC(ScaledValueConfig magSVC) {
		this.magSVC = magSVC;
	}

	public Quality getQ() {
		return q;
	}

	public void setQ(Quality q) {
		this.q = q;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	public RangeConfig getRangeC() {
		return rangeC;
	}

	public void setRangeC(RangeConfig rangeC) {
		this.rangeC = rangeC;
	}

	public Integer getSmpRate() {
		return smpRate;
	}

	public void setSmpRate(Integer smpRate) {
		this.smpRate = smpRate;
	}

	public Vector getSubCVal() {
		return subCVal;
	}

	public void setSubCVal(Vector subCVal) {
		this.subCVal = subCVal;
	}

	public boolean isSubEna() {
		return subEna;
	}

	public void setSubEna(boolean subEna) {
		this.subEna = subEna;
	}

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public Quality getSubQ() {
		return subQ;
	}

	public void setSubQ(Quality subQ) {
		this.subQ = subQ;
	}

	public TimeStamp getT() {
		return t;
	}

	public void setT(TimeStamp t) {
		this.t = t;
	}

	public Unit getUnits() {
		return units;
	}

	public void setUnits(Unit units) {
		this.units = units;
	}

	public Integer getZeroDb() {
		return zeroDb;
	}

	public void setZeroDb(Integer zeroDb) {
		this.zeroDb = zeroDb;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((angRef == null) ? 0 : angRef.hashCode());
		result = PRIME * result + ((angSVC == null) ? 0 : angSVC.hashCode());
		result = PRIME * result + ((cVal == null) ? 0 : cVal.hashCode());
		result = PRIME * result + ((cdcName == null) ? 0 : cdcName.hashCode());
		result = PRIME * result + ((cdcNs == null) ? 0 : cdcNs.hashCode());
		result = PRIME * result + ((d == null) ? 0 : d.hashCode());
		result = PRIME * result + ((dU == null) ? 0 : dU.hashCode());
		result = PRIME * result + ((dataNs == null) ? 0 : dataNs.hashCode());
		result = PRIME * result + ((db == null) ? 0 : db.hashCode());
		result = PRIME * result + ((instCVal == null) ? 0 : instCVal.hashCode());
		result = PRIME * result + ((magSVC == null) ? 0 : magSVC.hashCode());
		result = PRIME * result + ((q == null) ? 0 : q.hashCode());
		result = PRIME * result + ((range == null) ? 0 : range.hashCode());
		result = PRIME * result + ((rangeC == null) ? 0 : rangeC.hashCode());
		result = PRIME * result + ((smpRate == null) ? 0 : smpRate.hashCode());
		result = PRIME * result + ((subCVal == null) ? 0 : subCVal.hashCode());
		result = PRIME * result + (subEna ? 1231 : 1237);
		result = PRIME * result + ((subId == null) ? 0 : subId.hashCode());
		result = PRIME * result + ((subQ == null) ? 0 : subQ.hashCode());
		result = PRIME * result + ((t == null) ? 0 : t.hashCode());
		result = PRIME * result + ((units == null) ? 0 : units.hashCode());
		result = PRIME * result + ((zeroDb == null) ? 0 : zeroDb.hashCode());
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
		final CMV other = (CMV) obj;
		if (angRef == null) {
			if (other.angRef != null)
				return false;
		} else if (!angRef.equals(other.angRef))
			return false;
		if (angSVC == null) {
			if (other.angSVC != null)
				return false;
		} else if (!angSVC.equals(other.angSVC))
			return false;
		if (cVal == null) {
			if (other.cVal != null)
				return false;
		} else if (!cVal.equals(other.cVal))
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
		if (db == null) {
			if (other.db != null)
				return false;
		} else if (!db.equals(other.db))
			return false;
		if (instCVal == null) {
			if (other.instCVal != null)
				return false;
		} else if (!instCVal.equals(other.instCVal))
			return false;
		if (magSVC == null) {
			if (other.magSVC != null)
				return false;
		} else if (!magSVC.equals(other.magSVC))
			return false;
		if (q == null) {
			if (other.q != null)
				return false;
		} else if (!q.equals(other.q))
			return false;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		if (rangeC == null) {
			if (other.rangeC != null)
				return false;
		} else if (!rangeC.equals(other.rangeC))
			return false;
		if (smpRate == null) {
			if (other.smpRate != null)
				return false;
		} else if (!smpRate.equals(other.smpRate))
			return false;
		if (subCVal == null) {
			if (other.subCVal != null)
				return false;
		} else if (!subCVal.equals(other.subCVal))
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
		if (zeroDb == null) {
			if (other.zeroDb != null)
				return false;
		} else if (!zeroDb.equals(other.zeroDb))
			return false;
		return true;
	}
}
