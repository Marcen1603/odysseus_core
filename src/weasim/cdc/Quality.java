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
@SequenceGenerator(name = "QualSeq", sequenceName = "QUALSEQ", allocationSize = 1,
    initialValue = 1)
public class Quality implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QualSeq")
    private Integer id;

    @Enumerated
    private Validity validity;
    
    @OneToOne
    private DetailQual detailQual;
    
    @Enumerated
    Source source;
    
    boolean test;
    
    boolean operatorBlocked;
    
    public Quality() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setValidity(Validity param) {
        this.validity = param;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setDetailQual(DetailQual param) {
        this.detailQual = param;
    }

    public DetailQual getDetailQual() {
        return detailQual;
    }

    public void setSource(Source param) {
        this.source = param;
    }

    public Source getSource() {
        return source;
    }

    public void setTest(boolean param) {
        this.test = param;
    }

    public boolean isTest() {
        return test;
    }

    public void setOperatorBlocked(boolean param) {
        this.operatorBlocked = param;
    }

    public boolean isOperatorBlocked() {
        return operatorBlocked;
    }

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((detailQual == null) ? 0 : detailQual.hashCode());
		result = PRIME * result + (operatorBlocked ? 1231 : 1237);
		result = PRIME * result + ((source == null) ? 0 : source.hashCode());
		result = PRIME * result + (test ? 1231 : 1237);
		result = PRIME * result + ((validity == null) ? 0 : validity.hashCode());
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
		final Quality other = (Quality) obj;
		if (detailQual == null) {
			if (other.detailQual != null)
				return false;
		} else if (!detailQual.equals(other.detailQual))
			return false;
		if (operatorBlocked != other.operatorBlocked)
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (test != other.test)
			return false;
		if (validity == null) {
			if (other.validity != null)
				return false;
		} else if (!validity.equals(other.validity))
			return false;
		return true;
	}
}
