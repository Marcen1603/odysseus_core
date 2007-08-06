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
@SequenceGenerator(name = "INCSeq", sequenceName = "INCSEQ", allocationSize = 1,
    initialValue = 1)
public class INC implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INCSeq")
    private Integer id;
    
    private Integer ctlVal;
    
    @OneToOne
    private Originator origin;
    
    private Integer stVal;
    
    @OneToOne
    private Quality q;
    
    @OneToOne
    private TimeStamp t;
    
    @Enumerated
    private CtlModels ctlModel;

    public INC() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCtlVal(Integer param) {
        this.ctlVal = param;
    }

    public Integer getCtlVal() {
        return ctlVal;
    }

    public void setOrigin(Originator param) {
        this.origin = param;
    }

    public Originator getOrigin() {
        return origin;
    }

    public void setStVal(Integer param) {
        this.stVal = param;
    }

    public Integer getStVal() {
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
		result = PRIME * result + ((ctlModel == null) ? 0 : ctlModel.hashCode());
		result = PRIME * result + ((ctlVal == null) ? 0 : ctlVal.hashCode());
		result = PRIME * result + ((origin == null) ? 0 : origin.hashCode());
		result = PRIME * result + ((q == null) ? 0 : q.hashCode());
		result = PRIME * result + ((stVal == null) ? 0 : stVal.hashCode());
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
		final INC other = (INC) obj;
		if (ctlModel == null) {
			if (other.ctlModel != null)
				return false;
		} else if (!ctlModel.equals(other.ctlModel))
			return false;
		if (ctlVal == null) {
			if (other.ctlVal != null)
				return false;
		} else if (!ctlVal.equals(other.ctlVal))
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
		if (stVal == null) {
			if (other.stVal != null)
				return false;
		} else if (!stVal.equals(other.stVal))
			return false;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		return true;
	}
}
