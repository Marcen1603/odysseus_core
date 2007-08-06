package mg.dynaquest.queryoptimization.restruct;

import junit.framework.TestCase;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.algebra.SortPO;
import mg.dynaquest.queryexecution.po.algebra.UnionPO;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicateFactory;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;

/**
 * @author  Marco Grawunder
 */
public class SwitchSelectionUnionDifferenceTest extends TestCase {

	/**
	 * @uml.property  name="attrA"
	 * @uml.associationEnd  
	 */
	private SDFAttribute attrA;
	/**
	 * @uml.property  name="attrB"
	 * @uml.associationEnd  
	 */
	private SDFAttribute attrB;

	/**
	 * @uml.property  name="outA"
	 * @uml.associationEnd  
	 */
	private SDFAttributeList outA;
	/**
	 * @uml.property  name="outB"
	 * @uml.associationEnd  
	 */
	private SDFAttributeList outB;
	/**
	 * @uml.property  name="outAB"
	 * @uml.associationEnd  
	 */
	private SDFAttributeList outAB;
	
	/**
	 * @uml.property  name="compareOPEqual"
	 * @uml.associationEnd  
	 */
	private SDFCompareOperator compareOPEqual;
	
	/**
	 * @uml.property  name="simppred"
	 * @uml.associationEnd  
	 */
	private SDFSimplePredicate simppred;

	/**
	 * @uml.property  name="access1"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SchemaTransformationAccessPO access1 = new SchemaTransformationAccessPO();
	/**
	 * @uml.property  name="access2"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SchemaTransformationAccessPO access2 = new SchemaTransformationAccessPO();

	/**
	 * @uml.property  name="select"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SelectPO select = new SelectPO();
	
	/**
	 * @uml.property  name="union"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private UnionPO union = new UnionPO();
	
	/**
	 * @uml.property  name="wurzel"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SortPO wurzel = new SortPO();
	
	/**
	 * @uml.property  name="switchSelUnionDiff"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SwitchSelectionUnionDifference switchSelUnionDiff = new SwitchSelectionUnionDifference();
	
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(SwitchSelectionUnionDifferenceTest.class);
	}

	protected void setUp() throws Exception {
		attrA = new SDFAttribute("#attrA");
		attrB = new SDFAttribute("#attrB");

		outA = new SDFAttributeList();
		outA.addAttribute(attrA);

		outB = new SDFAttributeList();
		outB.addAttribute(attrB);

		outAB = new SDFAttributeList();
		outAB.addAttribute(attrA);
		outAB.addAttribute(attrB);

		access1.setPOName("Access 1");
		access1.setOutElements(outA);
		access1.setInputAttributes(outA);

		access2.setPOName("Access 2");
		access2.setOutElements(outAB);
		access2.setInputAttributes(outAB);
		
		union.setInputPO(0, access1);
		union.setInputPO(1, access2);
		union.setPOName("Union");
		union.setOutElements(outA);
		
		compareOPEqual = SDFCompareOperatorFactory
		.getCompareOperator(SDFPredicates.Equal);
		
		simppred = SDFSimplePredicateFactory
		.createSimplePredicate(
				"test",
				"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_predicates.sdf#StringPredicate",
				attrA, compareOPEqual, "egal", null, null, null);
		
		select.setInputPO(union);
		select.setPOName("Select");
		select.setPredicate(simppred);
		select.setOutElements(outA);
		
		wurzel.setInputPO(select);
		wurzel.setPOName("wurzel(Sort)");
		//wurzel.setSortAttrib(attrA);
		wurzel.setOutElements(outA);
		
		switchSelUnionDiff.test(wurzel);
	}

	/*
	 * Methode für 'mg.dynaquest.queryoptimization.restruct.SwitchSelectionUnionDifference.test(NAryPlanOperator)' testen
	 */
	public void testTest() {
		assertTrue("test()-Methode muss erfolgreich sein", switchSelUnionDiff
				.test(wurzel));
	}

	/*
	 * Methode für 'mg.dynaquest.queryoptimization.restruct.SwitchSelectionUnionDifference.process(NAryPlanOperator)' testen
	 */
	public void testProcess() {
		switchSelUnionDiff.process(wurzel);
		assertEquals(
				"Nach Restrukturierung muss linker Nachfolger von union der alte SelectPO sein",
				select, union.getInputPO(0));
		assertEquals(
				"Nach Restrukturierung muss rechter Nachfolger von union die Kopie des alten SelectPO sein",
				"Select Kopie", union.getInputPO(1).getPOName());
	}

}
