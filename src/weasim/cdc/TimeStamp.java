package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "TSSeq", sequenceName = "TSSEQ", allocationSize = 1,
    initialValue = 1)
public class TimeStamp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TSSeq")
    private Integer id;
    
    private Integer SecondSinceEpoch;
    
    private Integer FractionOfSecond;
    
    @OneToOne
    private TimeQuality TimeQuality;

    public TimeStamp() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSecondSinceEpoch(Integer secondSinceEpoch) {
        this.SecondSinceEpoch = secondSinceEpoch;
    }

    public Integer getSecondSinceEpoch() {
        return SecondSinceEpoch;
    }

    public void setFractionOfSecond(Integer fractionOfSecond) {
        this.FractionOfSecond = fractionOfSecond;
    }

    public Integer getFractionOfSecond() {
        return FractionOfSecond;
    }

    public void setTimeQuality(TimeQuality timeQuality) {
        this.TimeQuality = timeQuality;
    }

    public TimeQuality getTimeQuality() {
        return TimeQuality;
    }

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((FractionOfSecond == null) ? 0 : FractionOfSecond.hashCode());
		result = PRIME * result + ((SecondSinceEpoch == null) ? 0 : SecondSinceEpoch.hashCode());
		result = PRIME * result + ((TimeQuality == null) ? 0 : TimeQuality.hashCode());
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
		final TimeStamp other = (TimeStamp) obj;
		if (FractionOfSecond == null) {
			if (other.FractionOfSecond != null)
				return false;
		} else if (!FractionOfSecond.equals(other.FractionOfSecond))
			return false;
		if (SecondSinceEpoch == null) {
			if (other.SecondSinceEpoch != null)
				return false;
		} else if (!SecondSinceEpoch.equals(other.SecondSinceEpoch))
			return false;
		if (TimeQuality == null) {
			if (other.TimeQuality != null)
				return false;
		} else if (!TimeQuality.equals(other.TimeQuality))
			return false;
		return true;
	}
}
