package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractBaseMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.temporaltypes.merge.TemporalTrustMergeFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.GenericTemporalType;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;
import de.uniol.inf.is.odysseus.trust.ITrust;

public class TemporalTrust extends AbstractBaseMetaAttribute implements ITemporalTrust, TemporalType<ITrust> {

	private static final long serialVersionUID = 8229050105151227196L;

	private static final String TRUSTVALUE = "trustvalues";
	private static final String NAME = "TemporalTrust";

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { ITemporalTrust.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<>(classes.length);

	static {
		List<SDFAttribute> attributes = new ArrayList<>();
		attributes.add(new SDFAttribute(NAME, TRUSTVALUE, SDFDatatype.DOUBLE));
		schema.add(SDFSchemaFactory.createNewMetaSchema(NAME, Tuple.class, attributes, ITemporalTrust.class));
	}

	private GenericTemporalType<ITrust> trusts;

	public TemporalTrust() {
		trusts = new GenericTemporalType<>();
	}

	public TemporalTrust(TemporalTrust other) {
		this.trusts = new GenericTemporalType<>(other.trusts);
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		@SuppressWarnings("rawtypes")
		Tuple t = new Tuple(1, false);
		t.setAttribute(0, this.trusts);
		values.add(t);

	}

	@Override
	public void writeValue(Tuple<?> value) {
		this.trusts = value.getAttribute(0);

	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getValue(int subtype, int index) {
		switch (index) {
		case 0:
			return (K) (this.trusts);
		default:
			return null;
		}
	}

	@Override
	public IMetaAttribute clone() {
		return new TemporalTrust(this);
	}

	@Override
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new TemporalTrustMergeFunction();
	}

	@Override
	public ITrust getValue(PointInTime time) {
		return this.trusts.getValue(time);
	}

	@Override
	public ITrust[] getValues(TimeInterval interval) {
		
		int size = (int) interval.getEnd().minus(interval.getStart()).getMainPoint();
		ITrust[] trusts = new ITrust[size];
		
		int i = 0;
		for (PointInTime time = interval.getStart(); time.before(interval.getEnd()); time = time.plus(1)) {
			trusts[i] = this.getValue(time);
			i++;
		}
		return trusts;
	}

	@Override
	public void setTrust(PointInTime time, ITrust trust) {
		this.trusts.setValue(time, trust);
	}

	@Override
	public Set<PointInTime> getTemporalPoints() {
		return this.trusts.getValues().keySet();
	}

	@Override
	public double getTrust(PointInTime time) {
		// This is like "How trustworthy is the trust value"
		return 1;
	}

	@Override
	public ITrust getTrustValue(PointInTime time) {
		return this.trusts.getValue(time);
	}

}
