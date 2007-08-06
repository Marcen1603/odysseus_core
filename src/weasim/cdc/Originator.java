package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Originator implements Serializable {
    @Id
    private Integer id;
    
    @Enumerated
    private OriginatorCategory orCat;
    
    private String OrIdent;

    public Originator() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOrCat(OriginatorCategory param) {
        this.orCat = param;
    }

    public OriginatorCategory getOrCat() {
        return orCat;
    }

    public void setOrIdent(String orIdent) {
        this.OrIdent = orIdent;
    }

    public String getOrIdent() {
        return OrIdent;
    }

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((OrIdent == null) ? 0 : OrIdent.hashCode());
		result = PRIME * result + ((orCat == null) ? 0 : orCat.hashCode());
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
		final Originator other = (Originator) obj;
		if (OrIdent == null) {
			if (other.OrIdent != null)
				return false;
		} else if (!OrIdent.equals(other.OrIdent))
			return false;
		if (orCat == null) {
			if (other.orCat != null)
				return false;
		} else if (!orCat.equals(other.orCat))
			return false;
		return true;
	}
}
