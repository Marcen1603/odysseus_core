package de.uniol.inf.is.odysseus.spatial.interpolation.interpolator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;

public interface IMovingObjectLocationPredictor {

	public void addLocation(TrajectoryElement trajectoryElement,
			IStreamObject<? extends IMetaAttribute> streamElement);

	public TrajectoryElement predictLocation(String movingObjectId, PointInTime time);

	public Map<String, TrajectoryElement> predictAllLocations(PointInTime time);

}
