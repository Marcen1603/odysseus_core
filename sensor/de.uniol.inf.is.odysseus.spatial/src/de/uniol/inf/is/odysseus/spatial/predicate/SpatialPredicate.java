package de.uniol.inf.is.odysseus.spatial.predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.CQLAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.spatial.datatypes.ISpatialObject;

/**
 * @author Andre Bolles
 */
public class SpatialPredicate extends AbstractPredicate<RelationalTuple<?>> implements IRelationalPredicate {

	private static final long serialVersionUID = -7034775029654069291L;

	// stores which attributes are needed at which position for
	// variable bindings
	private int[] attributePositions;

	List<SDFAttribute> neededAttributes;

	private SpatialPredicateType type;

	// fromRightChannel[i] stores if the getAttribute(attributePositions[i])
	// should be called on the left or on the right input tuple
	private boolean[] fromRightChannel;

	public SpatialPredicate(String identifier1, String identifier2,
			IAttributeResolver attrRes, SpatialPredicateType type) {
		this.neededAttributes = new ArrayList<SDFAttribute>();

		this.neededAttributes.add(attrRes.getAttribute(identifier1));
		this.neededAttributes.add(attrRes.getAttribute(identifier2));

		this.type = type;
	}

	public void init(SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
		this.attributePositions = new int[neededAttributes.size()];
		this.fromRightChannel = new boolean[neededAttributes.size()];

		int i = 0;
		for (SDFAttribute curAttribute : neededAttributes) {
			CQLAttribute cqlAttr = (CQLAttribute) curAttribute;
			int pos = indexOf(leftSchema, cqlAttr);
			if (pos == -1) {
				pos = indexOf(rightSchema, cqlAttr);
				this.fromRightChannel[i] = true;
			}
			this.attributePositions[i++] = pos;
		}
	}

	private int indexOf(SDFAttributeList rightSchema, CQLAttribute cqlAttr) {
		Iterator<SDFAttribute> it = rightSchema.iterator();
		for (int i = 0; it.hasNext(); ++i) {
			if (cqlAttr.equalsCQL((CQLAttribute) it.next())) {
				return i;
			}
		}
		return -1;
	}

	public SpatialPredicate(SpatialPredicate predicate) {
		this.attributePositions = predicate.attributePositions.clone();
		this.fromRightChannel = predicate.fromRightChannel.clone();
		this.neededAttributes = new ArrayList<SDFAttribute>();
		for (SDFAttribute attr : predicate.neededAttributes) {
			this.neededAttributes.add(attr);
		}
		this.type = predicate.type;
	}

	public boolean evaluate(RelationalTuple<?> input) {
		throw new UnsupportedOperationException();
	}

	public boolean evaluate(RelationalTuple<?> left, RelationalTuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			RelationalTuple<?> r = fromRightChannel[i] ? right : left;
			values[i] = r.getAttribute(this.attributePositions[i]);
		}

		// should only be two values
		// the ordering of the values
		// doesn't matter, because
		// intersecting is commutative
		if (this.type == SpatialPredicateType.SPATIAL_INTERSECTS) {
			return ((ISpatialObject) values[0])
					.intersects((ISpatialObject) values[1]);
		} else {
			throw new RuntimeException("Not Implemented");
		}
	}

	@Override
	public SpatialPredicate clone() {
		return new SpatialPredicate(this);
	}

	public String toString() {
		return this.neededAttributes.get(0).toString() + " SPATIAL_INTERSECTS "
				+ this.neededAttributes.get(1).toString();
	}

	public List<SDFAttribute> getAttributes() {
		return this.neededAttributes;
	}
}
