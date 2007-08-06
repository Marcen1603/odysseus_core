package mg.dynaquest.queryoptimization.restruct;

import junit.framework.TestCase;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.SortPO;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;

/**
 * @author  Marco Grawunder
 */
public class SwitchJoinTest extends TestCase {

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
	 * @uml.property  name="attrC"
	 * @uml.associationEnd  
	 */
	private SDFAttribute attrC;

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
	 * @uml.property  name="outC"
	 * @uml.associationEnd  
	 */
	private SDFAttributeList outC;
	/**
	 * @uml.property  name="outAB"
	 * @uml.associationEnd  
	 */
	private SDFAttributeList outAB;
	/**
	 * @uml.property  name="outBC"
	 * @uml.associationEnd  
	 */
	private SDFAttributeList outBC;

	/**
	 * @uml.property  name="compareOPEqual"
	 * @uml.associationEnd  
	 */
	private SDFCompareOperator compareOPEqual;

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
	 * @uml.property  name="access3"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SchemaTransformationAccessPO access3 = new SchemaTransformationAccessPO();

	/**
	 * @uml.property  name="join"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JoinPO join = new JoinPO();

	/**
	 * @uml.property  name="join2"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JoinPO join2 = new JoinPO();

	/**
	 * @uml.property  name="wurzel"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SortPO wurzel = new SortPO();
	
	/**
	 * @uml.property  name="switchJoin"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SwitchJoin switchJoin = new SwitchJoin();

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(SwitchJoinTest.class);
	}

	protected void setUp() throws Exception {
		attrA = new SDFAttribute("#attrA");
		attrB = new SDFAttribute("#attrB");
		attrC = new SDFAttribute("#attrC");

		outA = new SDFAttributeList();
		outA.addAttribute(attrA);

		outB = new SDFAttributeList();
		outB.addAttribute(attrB);

		outC = new SDFAttributeList();
		outC.addAttribute(attrC);

		outAB = new SDFAttributeList();
		outAB.addAttribute(attrA);
		outAB.addAttribute(attrB);

		outBC = new SDFAttributeList();
		outBC.addAttribute(attrB);
		outBC.addAttribute(attrC);

		compareOPEqual = SDFCompareOperatorFactory
				.getCompareOperator(SDFPredicates.Equal);

		access1.setPOName("Access 1");
		access1.setOutElements(outA);
		access1.setInputAttributes(outA);

		access2.setPOName("Access 2");
		access2.setOutElements(outB);
		access2.setInputAttributes(outB);

		access3.setPOName("Access 3");
		access3.setOutElements(outC);
		access3.setInputAttributes(outC);

		join.setOutElements(outAB);
		join.setPOName("Join");

		join.setInputPO(0, access1);
		join.setInputPO(1, access2);

//		SDFJoinPredicate joinPredicate = SDFJoinPredicateFactory
//				.createJoinPredicate("wumpe2", attrA, attrB, compareOPEqual);
//
//		join.setPredicate(joinPredicate);

		join2.setOutElements(outBC);
		join2.setPOName("Join 2");

		join2.setInputPO(0, join);
		join2.setInputPO(1, access3);

//		SDFJoinPredicate joinPredicate2 = SDFJoinPredicateFactory
//				.createJoinPredicate("wumpe3", attrB, attrC, compareOPEqual);
//
//		join2.setPredicate(joinPredicate2);
	
		wurzel.setInputPO(join2);
		wurzel.setPOName("wurzel(Sort)");
		//wurzel.setSortAttrib(attrA);
		wurzel.setOutElements(outA);

		switchJoin.test(wurzel);
	}

	/*
	 * Methode für
	 * 'mg.dynaquest.queryoptimization.restruct.SwitchJoin.test(PlanOperator)'
	 * testen
	 */
	public void testTest() {
		assertTrue("test()-Methode muss erfolgreich sein", switchJoin
				.test(wurzel));
	}

	/*
	 * Methode für
	 * 'mg.dynaquest.queryoptimization.restruct.SwitchJoin.process(PlanOperator)'
	 * testen
	 */
	public void testProcess() {
		switchJoin.process(wurzel);
		assertEquals(
				"Nach Restrukturierung muss der rechte Nachfolger von join join2 sein",
				join2, join.getInputPO(1));
	}

}
