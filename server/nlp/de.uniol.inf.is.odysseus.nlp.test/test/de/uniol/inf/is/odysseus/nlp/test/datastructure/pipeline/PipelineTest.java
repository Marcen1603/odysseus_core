package de.uniol.inf.is.odysseus.nlp.test.datastructure.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelPrerequisitesNotFulfilledException;
import de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.mock.AnotherTestAnnotation;
import de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.mock.TestAnnotation;
import de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.model.mock.AnotherTestModel;
import de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.model.mock.TestModel;
import de.uniol.inf.is.odysseus.nlp.test.datastructure.pipeline.mock.TestPipeline;

/**
 * Tests the abstract Pipeline class.
 * All methods except the annotate method can be tested. 
 * The annotate method has to be tested in the implementing toolkit bundle.
 */
public class PipelineTest {
	private final String[] correctOrder = {"testannotation", "anothertestannotation"};
	private final String[] wrongOrder = {"anothertestannotation", "testannotation"};
	private final String[] incorrectOrder = {"anothertestannotation"};
	private TestPipeline pipeline;
	private TestPipeline empty;

	
	@Before
	public void setUp() throws Exception {
		pipeline = new TestPipeline(Arrays.asList(correctOrder), null);
		empty = new TestPipeline();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConstruction() throws NLPException {
		new TestPipeline(Arrays.asList(correctOrder), null);
	}

	@Test(expected=NLPModelPrerequisitesNotFulfilledException.class)
	public void testConstructionWrongOrder() throws NLPException {
		new TestPipeline(Arrays.asList(wrongOrder), null);
	}
	
	@Test(expected=NLPModelPrerequisitesNotFulfilledException.class)
	public void testConstructionUnfulfilledPrerequsites() throws NLPException {
		new TestPipeline(Arrays.asList(incorrectOrder), null);
	}
	
	@Test
	public void testEmptyConstruction() throws NLPException {
		new TestPipeline();
	}
	
	/*
	identifierToModel(String)
	equals(Object)
	exchange(AnnotationModel<?>)
	*/
	
	@Test
	public void testAnnotate(){
		Annotated annotated = new Annotated("Test string.", Arrays.asList(correctOrder));
		pipeline.annotate(annotated);
		assertTrue(annotated.getAnnotations().get("testannotation") instanceof TestAnnotation);
		assertTrue(annotated.getAnnotations().get("anothertestannotation") instanceof AnotherTestAnnotation);
	}

	@Test
	public void testIdentifierToModel() throws NLPModelNotFoundException{
		assertEquals(pipeline.identifierToModel("testannotation"), TestModel.class);
		assertEquals(pipeline.identifierToModel("anothertestannotation"), AnotherTestModel.class);
	}
	
	@Test(expected = NLPModelNotFoundException.class)
	public void testIdentifierToModelFailed() throws NLPModelNotFoundException{
		pipeline.identifierToModel("unknownannotation");
	}

	@Test
	public void testEquals() throws NLPException{
		assertTrue(pipeline.equals(new TestPipeline(Arrays.asList(correctOrder), new HashMap<String, Option>())));
	}
	
	@Test
	public void testEqualsFailed(){
		assertFalse(pipeline.equals(empty));
	}
	
	@Test
	public void testExchange() throws NLPModelNotFoundException, NLPException{
		pipeline.exchange(new TestModel(true, null));
		TestModel model = (TestModel)pipeline.getPipeline().get(0);
		assertTrue(model.isExchanged());
	}

}
