package weasim.cdc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ScaledValueConfig implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CMDSeq")
	private Integer id;

	private Integer scaleFactor;

	private Integer offset;

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(Integer scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((offset == null) ? 0 : offset.hashCode());
		result = PRIME * result + ((scaleFactor == null) ? 0 : scaleFactor.hashCode());
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
		final ScaledValueConfig other = (ScaledValueConfig) obj;
		if (offset == null) {
			if (other.offset != null)
				return false;
		} else if (!offset.equals(other.offset))
			return false;
		if (scaleFactor == null) {
			if (other.scaleFactor != null)
				return false;
		} else if (!scaleFactor.equals(other.scaleFactor))
			return false;
		return true;
	}
}
