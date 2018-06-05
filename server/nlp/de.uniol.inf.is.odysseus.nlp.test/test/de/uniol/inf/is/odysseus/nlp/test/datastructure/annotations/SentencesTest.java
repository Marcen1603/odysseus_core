package de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Sentences;

public class SentencesTest {

	private Sentences sentences;
	private String[] text = {"Beispiel Satz 1.", "Beispiel Satz 2."};

	@Before
	public void setUp() throws Exception {
		Span[] spans = {new Span(0,1, text[0]), new Span(1,2, text[1])};	
		sentences = new Sentences(spans);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToObject() {
		Object obj = sentences.toObject();
		assertTrue(obj instanceof String[]);
		String[] out = (String[])obj;
		
		assertTrue(Arrays.equals(out, text));
	}

}
