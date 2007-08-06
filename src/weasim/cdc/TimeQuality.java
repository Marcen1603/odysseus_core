package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


@Entity
@SequenceGenerator(name = "TQSeq", sequenceName = "TQSEQ", allocationSize = 1,
    initialValue = 1)
public class TimeQuality implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TQSeq")
    private Integer id;
    
    private boolean LeapSecondsKnown;
    
    private boolean ClockFailure;
    
    private boolean ClockNotSynchronized;
    
    private int TimeAccuracy;

    public TimeQuality() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isLeapSecondsKnown() {
        return LeapSecondsKnown;
    }

    public boolean isClockFailure() {
        return ClockFailure;
    }

    public boolean isClockNotSynchronized() {
        return ClockNotSynchronized;
    }

    public int getTimeAccuracy() {
        return TimeAccuracy;
    }

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (ClockFailure ? 1231 : 1237);
		result = PRIME * result + (ClockNotSynchronized ? 1231 : 1237);
		result = PRIME * result + (LeapSecondsKnown ? 1231 : 1237);
		result = PRIME * result + TimeAccuracy;
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
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
		final TimeQuality other = (TimeQuality) obj;
		if (ClockFailure != other.ClockFailure)
			return false;
		if (ClockNotSynchronized != other.ClockNotSynchronized)
			return false;
		if (LeapSecondsKnown != other.LeapSecondsKnown)
			return false;
		if (TimeAccuracy != other.TimeAccuracy)
			return false;
		return true;
	}
}
