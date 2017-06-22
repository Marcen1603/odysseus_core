package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
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

	private IPredicate<?> predicate;
	
	public UpdatePredicatePunctuation(PointInTime p, IPredicate<?> predicate) {
		super(p);
		this.predicate = predicate;
	}

	@Override
	public SDFSchema getSchema() {
		return schema;
	}

	@Override
	public Tuple<?> getValue() {
		Tuple<?> ret = new Tuple<>(1, false);
		ret.setAttribute(0, this.predicate);
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

	public IPredicate<?> getNewPredicate() {
		return predicate;
	}
	
	@Override
	public String toString() {
		return "UpdatePredicatePunctuation - new predicate: " + this.getNewPredicate() + "  " + this.getTime();
	}

}
