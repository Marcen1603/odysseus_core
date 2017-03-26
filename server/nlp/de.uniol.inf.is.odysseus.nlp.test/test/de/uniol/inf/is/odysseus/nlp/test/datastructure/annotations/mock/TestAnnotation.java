package de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.mock;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;

public class TestAnnotation extends Annotation{
	@Override
	public Object toObject() {
		return null;
	}

	@Override
	public IClone clone(){
		return new TestAnnotation();
	}
}