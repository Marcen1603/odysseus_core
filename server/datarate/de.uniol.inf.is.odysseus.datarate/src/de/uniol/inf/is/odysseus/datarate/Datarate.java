package de.uniol.inf.is.odysseus.datarate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractBaseMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

final public class Datarate extends AbstractBaseMetaAttribute implements IDatarate,
		Cloneable, Serializable {

	private static final long serialVersionUID = -3947845802567855438L;

	@SuppressWarnings("unchecked")
	public transient static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { IDatarate.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<>(
			CLASSES.length);
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}
	
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("Datarate", "datarate",
				SDFDatatype.DOUBLE, null));
		schema.add(SDFSchemaFactory.createNewMetaSchema("Datarate", Tuple.class,
				attributes, IDatarate.class));
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	private double datarate;

	public Datarate() {
	}

	public Datarate(Datarate other) {
		datarate = other.datarate;
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		@SuppressWarnings("rawtypes")
		Tuple t = new Tuple(1, false);
		t.setAttribute(0, datarate);
		values.add(t);
	}

	@Override
	public void writeValue(Tuple<?> value) {
		this.datarate = value.getAttribute(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <K> K getValue(int subtype, int index) {
		switch (index) {
		case 0:
			return (K) (Double) datarate;
		}
		return null;
	}

	
	@Override
	public String getName() {
		return "Datarate";
	}

	@Override
	public String csvToString(WriteOptions options) {
		if (options.hasFloatingFormatter()) {
			return options.getFloatingFormatter().format(datarate);
		} else if (options.hasNumberFormatter()) {
			return options.getNumberFormatter().format(datarate);
		}

		return String.valueOf(datarate);
	}

	@Override
	public String getCSVHeader(char delimiter) {
		return "Datarate";
	}

	@Override
	public void setDatarate(double datarate) {
		this.datarate = datarate;
	}

	@Override
	public double getDatarate() {
		return datarate;
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
	public String toString() {
		return "rate = " + datarate;
	}
}
