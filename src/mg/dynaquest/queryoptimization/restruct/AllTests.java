package mg.dynaquest.queryoptimization.restruct;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(AllTests.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for mg.dynaquest.queryoptimization.restruct");
		//$JUnit-BEGIN$
		suite.addTestSuite(CARuleTest.class);
		suite.addTestSuite(RestructProjectionGroupTest.class);
		suite.addTestSuite(RestructSelectionGroupTest.class);
		suite.addTestSuite(SwitchJoinTest.class);
		suite.addTestSuite(SwitchProjectionJoinTest.class);
		suite.addTestSuite(SwitchProjectionUnionDifferenceTest.class);
		suite.addTestSuite(SwitchSelectionJoinTest.class);
		suite.addTestSuite(SwitchSelectionProjectionTest.class);
		suite.addTestSuite(SwitchSelectionUnionDifferenceTest.class);
		//$JUnit-END$
		return suite;
	}

}
