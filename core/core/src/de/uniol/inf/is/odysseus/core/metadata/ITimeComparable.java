package de.uniol.inf.is.odysseus.core.metadata;

public interface ITimeComparable {

	boolean before(PointInTime time);
	boolean beforeOrEquals(PointInTime time);
	boolean after(PointInTime time);
	boolean afterOrEquals(PointInTime time);

}
