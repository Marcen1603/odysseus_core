package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@Entity
public class WYE implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPCSEQ")
    private Integer id;
    
    @OneToOne
    private CMV phsA;
    @OneToOne
    private CMV phsB;
    @OneToOne
    private CMV phsC;
    @OneToOne
    private CMV neut;
    @OneToOne
    private CMV net;
    @OneToOne
    private CMV res;
    @Enumerated
    private AngRef2 angRef2;
    
    private String d;
    private String dU;
    private String cdcNs;
    private String cdcName;
    private String dataNs;

    public WYE() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPhsA(CMV param) {
        this.phsA = param;
    }

    public CMV getPhsA() {
        return phsA;
    }

    public void setPhsB(CMV param) {
        this.phsB = param;
    }

    public CMV getPhsB() {
        return phsB;
    }

    public void setPhsC(CMV param) {
        this.phsC = param;
    }

    public CMV getPhsC() {
        return phsC;
    }

    public void setNeut(CMV param) {
        this.neut = param;
    }

    public CMV getNeut() {
        return neut;
    }

    public void setNet(CMV param) {
        this.net = param;
    }

    public CMV getNet() {
        return net;
    }

    public void setRes(CMV param) {
        this.res = param;
    }

    public CMV getRes() {
        return res;
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
		result = PRIME * result + ((net == null) ? 0 : net.hashCode());
		result = PRIME * result + ((neut == null) ? 0 : neut.hashCode());
		result = PRIME * result + ((phsA == null) ? 0 : phsA.hashCode());
		result = PRIME * result + ((phsB == null) ? 0 : phsB.hashCode());
		result = PRIME * result + ((phsC == null) ? 0 : phsC.hashCode());
		result = PRIME * result + ((res == null) ? 0 : res.hashCode());
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
		final WYE other = (WYE) obj;
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
		if (net == null) {
			if (other.net != null)
				return false;
		} else if (!net.equals(other.net))
			return false;
		if (neut == null) {
			if (other.neut != null)
				return false;
		} else if (!neut.equals(other.neut))
			return false;
		if (phsA == null) {
			if (other.phsA != null)
				return false;
		} else if (!phsA.equals(other.phsA))
			return false;
		if (phsB == null) {
			if (other.phsB != null)
				return false;
		} else if (!phsB.equals(other.phsB))
			return false;
		if (phsC == null) {
			if (other.phsC != null)
				return false;
		} else if (!phsC.equals(other.phsC))
			return false;
		if (res == null) {
			if (other.res != null)
				return false;
		} else if (!res.equals(other.res))
			return false;
		return true;
	}
}
