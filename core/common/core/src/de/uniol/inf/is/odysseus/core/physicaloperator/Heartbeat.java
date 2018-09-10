package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

final public class Heartbeat extends AbstractPunctuation {

	private static final long serialVersionUID = 7733672970811260652L;
	final static byte NUMBER = 1;

	public static final SDFSchema schema;

	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("Heartbeat", "type", SDFDatatype.INTEGER));
		attributes.add(new SDFAttribute("Heartbeat", "point", SDFDatatype.TIMESTAMP));
		schema = SDFSchemaFactory.createNewSchema("Heartbeat", Tuple.class, attributes);
	}

	private Heartbeat(long point) {
		super(point);
	}

	private Heartbeat(PointInTime point) {
		super(point);
	}

	@Override
	public boolean isHeartbeat() {
		return true;
	}

	@Override
	public AbstractPunctuation clone() {
		return this;
	}

	@Override
	public AbstractPunctuation clone(PointInTime point) {
		return new Heartbeat(point);
	}

	@Override
	public byte getNumber() {
		return NUMBER;
	}

	@Override
	public String toString() {
		return "Heartbeat " + getTime();
	}

	static public Heartbeat createNewHeartbeat(long point) {
		return new Heartbeat(point);
	}

	static public Heartbeat createNewInstance(Tuple<?> input) {
		return new Heartbeat(new PointInTime((long) input.getAttribute(1)));
	}

	static public Heartbeat createNewHeartbeat(PointInTime point) {
		return new Heartbeat(point);
	}

	@Override
	public SDFSchema getSchema() {
		return schema;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> getValue() {
		Tuple<?> ret = new Tuple(2, false);
		ret.setAttribute(0, NUMBER);
		ret.setAttribute(1, getTime().getMainPoint());
		return ret;
	}

}
