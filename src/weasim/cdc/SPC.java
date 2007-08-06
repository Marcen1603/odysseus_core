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
@SequenceGenerator(name = "SPCSEQ", sequenceName = "SPCSEQ", allocationSize = 1,
    initialValue = 1)
public class SPC implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPCSEQ")
    private Integer id;
    
    private boolean ctVal;
    
    @OneToOne
    private Originator origin;
    
    private boolean stVal;
    
    @OneToOne
    private Quality q;
    
    @OneToOne
    private TimeStamp t;
    
    @Enumerated
    private CtlModels ctlModel;

    public SPC() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCtVal(boolean param) {
        this.ctVal = param;
    }

    public boolean isCtVal() {
        return ctVal;
    }

    public void setOrigin(Originator param) {
        this.origin = param;
    }

    public Originator getOrigin() {
        return origin;
    }

    public void setStVal(boolean param) {
        this.stVal = param;
    }

    public boolean isStVal() {
        return stVal;
    }

    public void setQ(Quality param) {
        this.q = param;
    }

    public Quality getQ() {
        return q;
    }

    public void setT(TimeStamp param) {
        this.t = param;
    }

    public TimeStamp getT() {
        return t;
    }

    public void setCtlModel(CtlModels param) {
        this.ctlModel = param;
    }

    public CtlModels getCtlModel() {
        return ctlModel;
    }

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (ctVal ? 1231 : 1237);
		result = PRIME * result + ((ctlModel == null) ? 0 : ctlModel.hashCode());
		result = PRIME * result + ((origin == null) ? 0 : origin.hashCode());
		result = PRIME * result + ((q == null) ? 0 : q.hashCode());
		result = PRIME * result + (stVal ? 1231 : 1237);
		result = PRIME * result + ((t == null) ? 0 : t.hashCode());
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
		final SPC other = (SPC) obj;
		if (ctVal != other.ctVal)
			return false;
		if (ctlModel == null) {
			if (other.ctlModel != null)
				return false;
		} else if (!ctlModel.equals(other.ctlModel))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (q == null) {
			if (other.q != null)
				return false;
		} else if (!q.equals(other.q))
			return false;
		if (stVal != other.stVal)
			return false;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		return true;
	}
}
