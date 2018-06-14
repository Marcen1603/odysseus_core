package de.uniol.inf.is.odysseus.recovery;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * Punctuation to signal that all earlier elements (those, which are in restored
 * operator states) have wrong trust values.
 * 
 * @author Michael Brand
 *
 */
public class TrustUpdatePunctuation extends AbstractPunctuation {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -5643577228315446197L;

	/**
	 * The name for the schema.
	 */
	public static final String SCHEMA_NAME = "TrustUpdatePunctuation";

	/**
	 * See {@link AbstractPunctuation#AbstractPunctuation(long)}
	 */
	public TrustUpdatePunctuation(long point) {
		super(point);
	}

	/**
	 * See {@link AbstractPunctuation#AbstractPunctuation(PointInTime)}
	 */
	public TrustUpdatePunctuation(PointInTime p) {
		super(p);
	}

	/**
	 * See {@link AbstractPunctuation#AbstractPunctuation(AbstractPunctuation)}
	 */
	public TrustUpdatePunctuation(AbstractPunctuation punct) {
		super(punct);
	}

	@Override
	public SDFSchema getSchema() {
		List<SDFAttribute> attributes = new ArrayList<>();
		attributes.add(new SDFAttribute(SCHEMA_NAME, "point", SDFDatatype.TIMESTAMP));
		return SDFSchemaFactory.createNewSchema(SCHEMA_NAME, Tuple.class, attributes);
	}

	@Override
	public Tuple<?> getValue() {
		Tuple<?> ret = new Tuple<>(1, false);
		ret.setAttribute(0, new Long(this.getTime().getMainPoint()));
		return ret;
	}

	@Override
	public TrustUpdatePunctuation clone() {
		return new TrustUpdatePunctuation(this);
	}

	@Override
	public TrustUpdatePunctuation clone(PointInTime newTime) {
		return new TrustUpdatePunctuation(newTime);
	}

}