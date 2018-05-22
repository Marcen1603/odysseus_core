package de.uniol.inf.odysseus.spatiotemporal.types.region;

import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.TemporalGeometry;

public class TemporalRegion extends TemporalGeometry {

	private static final long serialVersionUID = 56876639887007517L;

	public TemporalRegion(TemporalFunction<GeometryWrapper> function) {
		super(function);
	}
	
	public TemporalRegion(TemporalRegion other) {
		super(other);
	}

	@Override
	public TemporalRegion clone() {
		return new TemporalRegion(this);
	}
	
	@Override
	public String toString() {
		return "tregion: " + function.toString();
	}
	
}
