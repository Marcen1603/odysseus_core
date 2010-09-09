package de.uniol.inf.is.odysseus.priority_interval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


public class SingleAttributeHashFunctionWrapper<T extends RelationalTuple<?>> extends AbstractHashFunctionWrapper<T> {

	final private int hashCode;

	public SingleAttributeHashFunctionWrapper(int attributePos, T element) {
		super(element);
		this.hashCode = element.getAttribute(attributePos).hashCode();
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

}
