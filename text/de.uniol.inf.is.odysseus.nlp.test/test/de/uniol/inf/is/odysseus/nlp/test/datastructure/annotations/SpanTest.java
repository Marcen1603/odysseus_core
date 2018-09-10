package de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations;

import org.junit.Test;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.InvalidSpanException;

public class SpanTest {

	@Test(expected=InvalidSpanException.class)
	public void constructorFailedTest() throws InvalidSpanException {
		new Span(0,-1, "Beispiel Satz.");
	}
	
	@Test
	public void constructorTest() throws InvalidSpanException{
		new Span(0, 1, "Beispiel Satz.");
	}

}
