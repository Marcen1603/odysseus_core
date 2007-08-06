package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "INSSEQ", sequenceName = "INSSEQ", allocationSize = 1,
    initialValue = 1)
public class INS implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSSEQ")
    private Integer id;
    
    private Integer stVal;
    
    @OneToOne
    private Quality q;
    
    @OneToOne
    private TimeStamp t;
    
    public INS() {
    }
    
    public Integer getId() {
        return this.id;
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

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
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
		final INS other = (INS) obj;
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
