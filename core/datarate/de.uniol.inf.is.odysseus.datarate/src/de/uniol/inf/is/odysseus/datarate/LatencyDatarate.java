package de.uniol.inf.is.odysseus.datarate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractCombinedMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;

final public class LatencyDatarate extends AbstractCombinedMetaAttribute implements IDatarate, ILatency {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[]{
		ILatency.class, IDatarate.class
	};

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(CLASSES.length);
	static{
		schema.addAll(Latency.schema);
		schema.addAll(Datarate.schema);
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	final private ILatency latency;
	final private IDatarate datarate;


	public LatencyDatarate() {
		latency = new Latency();
		datarate = new Datarate();
	}

	public LatencyDatarate( LatencyDatarate other ) {
		latency = (ILatency) other.latency.clone();
		datarate = (IDatarate) other.datarate.clone();
	}

	@Override
	public LatencyDatarate clone() {
		return new LatencyDatarate(this);
	}

	@Override
	public String getName() {
		return "LatencyDatarate";
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		latency.retrieveValues(values);
		datarate.retrieveValues(values);
	}

	@Override
	public void writeValues(List<Tuple<?>> values) {
		latency.writeValue(values.get(0));
		datarate.writeValue(values.get(1));
	}

	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		list.addAll(latency.getInlineMergeFunctions());
		list.addAll(datarate.getInlineMergeFunctions());
		return list;
	}

	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return latency.getValue(0, index);
			case 1:
				return datarate.getValue(0, index);

		}
		return null;
	}

	@Override
	public String toString() {
		return "( l = " + super.toString() + " | " + datarate.toString() + " )";
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	// ------------------------------------------------------------------------------
	// Delegates for datarate
	// ------------------------------------------------------------------------------


	@Override
	public void setDatarate(String key, double datarate) {
		this.datarate.setDatarate(key, datarate);
	}

	@Override
	public double getDatarate(String key) {
		return datarate.getDatarate(key);
	}

	@Override
	public Map<String, Double> getDatarates() {
		return datarate.getDatarates();
	}

	@Override
	public void setDatarates(Map<String, Double> datarates) {
		datarate.setDatarates(datarates);
	}

	// ------------------------------------------------------------------------------
	// Delegates for latency
	// ------------------------------------------------------------------------------


	@Override
	public final long getLatency() {
		return latency.getLatency();
	}

	@Override
	public long getMaxLatency() {
		return latency.getMaxLatency();
	}

	@Override
	public final long getLatencyEnd() {
		return latency.getLatencyEnd();
	}

	@Override
	public final long getLatencyStart() {
		return latency.getLatencyStart();
	}

	@Override
	public long getMaxLatencyStart() {
		return latency.getMaxLatencyStart();
	}

	@Override
	public final void setLatencyEnd(long timestamp) {
		latency.setLatencyEnd(timestamp);
	}

	@Override
	public final void setMinLatencyStart(long timestamp) {
		latency.setMinLatencyStart(timestamp);
	}

	@Override
	public void setMaxLatencyStart(long timestamp) {
		latency.setMaxLatencyStart(timestamp);
	}

}
