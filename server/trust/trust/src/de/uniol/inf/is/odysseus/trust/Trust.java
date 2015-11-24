package de.uniol.inf.is.odysseus.trust;

import java.text.NumberFormat;
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

public final class Trust extends AbstractBaseMetaAttribute implements ITrust {

	private static final String TRUSTVALUE = "trustvalue";
	private static final String NAME = "Trust";

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { ITrust.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<>(classes.length);
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute(NAME, TRUSTVALUE,
				SDFDatatype.DOUBLE));
		schema.add(SDFSchemaFactory.createNewMetaSchema(NAME, Tuple.class,
				attributes, ITrust.class));
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}
	
	private static final long serialVersionUID = -426212407481918604L;
	private double trustValue;
	
	public Trust(){
		trustValue = 1;
	}
	
	public Trust(Trust trust){
		this.trustValue = trust.trustValue;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		@SuppressWarnings("rawtypes")
		Tuple t = new Tuple(1, false);
		t.setAttribute(0, trustValue);
		values.add(t);	
	}

	@Override
	public void writeValue(Tuple<?> value) {
		this.trustValue = value.getAttribute(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getValue(int subtype, int index) {
		switch (index) {
		case 0:
			return (K)(Double)trustValue;
		}
		return null;
	}

	@Override
	public String csvToString(WriteOptions options) {
		NumberFormat numberFormatter = options.getNumberFormatter();
		StringBuffer retBuffer = new StringBuffer();
		if (numberFormatter != null) {
			retBuffer.append(numberFormatter.format(this.trustValue));
		} else {
			retBuffer.append(this.trustValue);
		}
		return retBuffer.toString();
	}

	@Override
	public String getCSVHeader(char delimiter) {
		return TRUSTVALUE;
	}

	@Override
	public void setTrust(double trustValue) {
		this.trustValue = trustValue;
	}

	@Override
	public double getTrust() {
		return trustValue;
	}

	@Override
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new TrustMergeFunction();
	}

	@Override
	public ITrust clone() {
		return new Trust(this);
	}
	
	@Override
	public String toString() {
		return ""+trustValue;
	}

}
