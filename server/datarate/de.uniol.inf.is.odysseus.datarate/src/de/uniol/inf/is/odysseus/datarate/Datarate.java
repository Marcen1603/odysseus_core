package de.uniol.inf.is.odysseus.datarate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractBaseMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

final public class Datarate extends AbstractBaseMetaAttribute implements IDatarate, Cloneable, Serializable {

	private static final long serialVersionUID = -3947845802567855438L;

	@SuppressWarnings("unchecked")
	public transient static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { IDatarate.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<>(CLASSES.length);

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	static {
		List<SDFAttribute> datarates = new ArrayList<>();
		datarates.add(new SDFAttribute("DatarateEntry", "key", SDFDatatype.STRING));
		datarates.add(new SDFAttribute("DatarateEntry", "Datarate", SDFDatatype.DOUBLE));

		SDFSchema dataratesEntry = SDFSchemaFactory.createNewSchema("DatarateEntry", Tuple.class, datarates);

		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("Datarate", "Measurements", SDFDatatype.createTypeWithSubSchema(SDFDatatype.LIST_TUPLE, dataratesEntry)));
		schema.add(SDFSchemaFactory.createNewMetaSchema("Datarate", Tuple.class, attributes, IDatarate.class));
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	final private Map<String, Double> datarates = new HashMap<String, Double>();

	public Datarate() {
	}

	public Datarate(Datarate other) {
		datarates.putAll(other.datarates);
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		@SuppressWarnings("rawtypes")
		Tuple t = createTupleFromState();
		values.add(t);
	}

	@SuppressWarnings("rawtypes")
	private Tuple createTupleFromState() {
		Tuple t = new Tuple(1, true);
		List<Tuple<?>> l = createEntryListFromState();
		t.setAttribute(0, l);
		return t;
	}

	@SuppressWarnings("rawtypes")
	private List<Tuple<?>> createEntryListFromState() {
		List<Tuple<?>> l = new ArrayList<>();
		for (Entry<String, Double> e:datarates.entrySet()) {
			Tuple tuple = new Tuple(2, false);
			tuple.setAttribute(0, e.getKey());
			tuple.setAttribute(1, e.getValue());
			l.add(tuple);
		}
		return l;
	}



	@Override
	public void writeValue(Tuple<?> value) {
		List<Tuple<?>> l = value.getAttribute(0);
		for(Tuple<?> e:l){
			this.datarates.put(e.getAttribute(0), e.getAttribute(1));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getValue(int subtype, int index) {
		return (K) createEntryListFromState();
	}

	@Override
	public String getName() {
		return "Datarate";
	}

	@Override
	public void setDatarate(String key, double datarate) {
		datarates.put(key, datarate);
	}

	@Override
	public void setDatarates(Map<String, Double> datarates) {
		this.datarates.putAll(datarates);
	}

	@Override
	public double getDatarate(String key) {
		return datarates.get(key);
	}

	@Override
	public Map<String, Double> getDatarates() {
		return datarates;
	}

	@Override
	public IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new DatarateMergeFunction();
	}

	@Override
	public IDatarate clone() {
		return new Datarate(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datarates == null) ? 0 : datarates.hashCode());
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
		Datarate other = (Datarate) obj;
		if (datarates == null) {
			if (other.datarates != null)
				return false;
		} else if (!datarates.equals(other.datarates))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "rates = " + datarates;
	}
}
