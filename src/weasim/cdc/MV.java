package weasim.cdc;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;


@Entity
@SequenceGenerator(name = "MVSEQ", sequenceName = "MVSEQ", allocationSize = 1,
    initialValue = 1)
public class MV implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MVSEQ")
    private Integer id;
    
    @OneToOne(cascade={CascadeType.ALL})
    private AnalogueValue inst;
    
    @OneToOne(cascade={CascadeType.ALL})
    private AnalogueValue instMag;

    public MV() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AnalogueValue getInst() {
        return inst;
    }

    public AnalogueValue getInstMag() {
        return instMag;
    }

    public void setInst(AnalogueValue param) {
        this.inst = param;
    }

    public void setInstMag(AnalogueValue param) {
        this.instMag = param;
    }

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((inst == null) ? 0 : inst.hashCode());
		result = PRIME * result + ((instMag == null) ? 0 : instMag.hashCode());
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
		final MV other = (MV) obj;
		if (inst == null) {
			if (other.inst != null)
				return false;
		} else if (!inst.equals(other.inst))
			return false;
		if (instMag == null) {
			if (other.instMag != null)
				return false;
		} else if (!instMag.equals(other.instMag))
			return false;
		return true;
	}
}
