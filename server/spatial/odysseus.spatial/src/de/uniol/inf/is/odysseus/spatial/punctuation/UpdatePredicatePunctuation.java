package de.uniol.inf.is.odysseus.spatial.punctuation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

public class UpdatePredicatePunctuation extends AbstractPunctuation {

	private static final long serialVersionUID = -1814579527166963151L;

	public static final SDFSchema schema;

	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("UpdatePredicatePunctuation", "predicate", SDFDatatype.STRING));
		schema = SDFSchemaFactory.createNewSchema("UpdatePredicatePunctuation", Tuple.class, attributes);
	}

	private String newPredicate;

	public UpdatePredicatePunctuation(PointInTime p, String newPredicate) {
		super(p);
		this.newPredicate = newPredicate;
	}

	@Override
	public SDFSchema getSchema() {
		return schema;
	}

	@Override
	public Tuple<?> getValue() {
		Tuple<?> ret = new Tuple(1, false);
		ret.setAttribute(0, newPredicate);
		return ret;
	}

	@Override
	public AbstractPunctuation clone() {
		return new UpdatePredicatePunctuation(getTime(), getNewPredicate());
	}

	@Override
	public AbstractPunctuation clone(PointInTime newTime) {
		return new UpdatePredicatePunctuation(newTime, getNewPredicate());
	}

	public String getNewPredicate() {
		return newPredicate;
	}

}
