package de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.model.mock.ATokensModel;
import de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.model.mock.BTokensModel;

public class AnnotationModelTest {
	ATokensModel a = new ATokensModel();
	BTokensModel b = new BTokensModel();
	
	@Before
	public void setUp(){
		
	}
	
	@Test
	public void testEquals(){
		assertEquals(a, b);
		assertTrue(a.equals(b));
	}
}
