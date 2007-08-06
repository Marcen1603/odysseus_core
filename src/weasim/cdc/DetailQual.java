package weasim.cdc;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class DetailQual {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CMDSeq")
    private Integer id;
    
    private boolean overflow;
    private boolean outOfRange;
    private boolean badReference;
    private boolean oscillatory;
    private boolean failure;
    private boolean oldData;
    private boolean inconsistent;
    private boolean inaccurate;

    public void setOverflow(boolean param) {
        this.overflow = param;
    }

    public boolean isOverflow() {
        return overflow;
    }

    public void setOutOfRange(boolean param) {
        this.outOfRange = param;
    }

    public boolean isOutOfRange() {
        return outOfRange;
    }

    public void setBadReference(boolean param) {
        this.badReference = param;
    }

    public boolean isBadReference() {
        return badReference;
    }

    public void setOscillatory(boolean param) {
        this.oscillatory = param;
    }

    public boolean isOscillatory() {
        return oscillatory;
    }

    public void setFailure(boolean param) {
        this.failure = param;
    }

    public boolean isFailure() {
        return failure;
    }

    public void setOldData(boolean param) {
        this.oldData = param;
    }

    public boolean isOldData() {
        return oldData;
    }

    public void setInconsistent(boolean param) {
        this.inconsistent = param;
    }

    public boolean isInconsistent() {
        return inconsistent;
    }

    public void setInaccurate(boolean param) {
        this.inaccurate = param;
    }

    public boolean isInaccurate() {
        return inaccurate;
    }

    public void setId(Integer param) {
        this.id = param;
    }

    public Integer getId() {
        return id;
    }

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (badReference ? 1231 : 1237);
		result = PRIME * result + (failure ? 1231 : 1237);
		result = PRIME * result + (inaccurate ? 1231 : 1237);
		result = PRIME * result + (inconsistent ? 1231 : 1237);
		result = PRIME * result + (oldData ? 1231 : 1237);
		result = PRIME * result + (oscillatory ? 1231 : 1237);
		result = PRIME * result + (outOfRange ? 1231 : 1237);
		result = PRIME * result + (overflow ? 1231 : 1237);
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
		final DetailQual other = (DetailQual) obj;
		if (badReference != other.badReference)
			return false;
		if (failure != other.failure)
			return false;
		if (inaccurate != other.inaccurate)
			return false;
		if (inconsistent != other.inconsistent)
			return false;
		if (oldData != other.oldData)
			return false;
		if (oscillatory != other.oscillatory)
			return false;
		if (outOfRange != other.outOfRange)
			return false;
		if (overflow != other.overflow)
			return false;
		return true;
	}
}
