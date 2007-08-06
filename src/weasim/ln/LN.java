package weasim.ln;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@SequenceGenerator(name = "LNSEQ", sequenceName = "LNSEQ", allocationSize = 1,
    initialValue = 1)
public class LN implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LNSEQ")
    private Integer id;

    private String LNName;
    private String LNRef;
    
    public LN() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLNName(String lNName) {
        this.LNName = lNName;
    }

    public String getLNName() {
        return LNName;
    }

    public void setLNRef(String lNRef) {
        this.LNRef = lNRef;
    }

    public String getLNRef() {
        return LNRef;
    }

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((LNName == null) ? 0 : LNName.hashCode());
		result = PRIME * result + ((LNRef == null) ? 0 : LNRef.hashCode());
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
		final LN other = (LN) obj;
		if (LNName == null) {
			if (other.LNName != null)
				return false;
		} else if (!LNName.equals(other.LNName))
			return false;
		if (LNRef == null) {
			if (other.LNRef != null)
				return false;
		} else if (!LNRef.equals(other.LNRef))
			return false;
		return true;
	}
}
