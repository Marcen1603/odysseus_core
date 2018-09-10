package de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Sentences;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Tokens;

public class AnnotatedTest {

	private Annotated annotated;
	private Tokens tokens;
	private Sentences sentences;

	@Before
	public void setUp() throws Exception {
		annotated = new Annotated("This is a test.", Arrays.asList("tokens", "sentences"));
		tokens = new Tokens(new String[]{"This", "is", "a", "test", "."});
		sentences = new Sentences(new Span[]{new Span(0, 15, annotated.getText())});
		annotated.put(tokens);
		annotated.put(sentences);
	}
	
	@Test
	public void testToObject() {
		KeyValueObject<IMetaAttribute> object = annotated.toObject();
		assertEquals(object.getAttribute(tokens.identifier()), tokens.toObject());
		assertTrue(Arrays.deepEquals((Object[]) object.getAttribute(sentences.identifier()), (Object[]) sentences.toObject()));
	}

}
