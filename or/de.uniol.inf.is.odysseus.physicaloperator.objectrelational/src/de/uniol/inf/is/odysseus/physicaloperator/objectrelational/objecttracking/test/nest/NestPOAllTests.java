package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.CreateOutputTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.GroupId;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.Merge;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.Nest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.NestOfNesting;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.TwoGroupingAttributeNest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.suites.UpdateAllTests;
import junit.framework.Test;
import junit.framework.TestSuite;

public class NestPOAllTests extends TestSuite {
    public NestPOAllTests() {
        this.setName("Asserting all NestPO tests");
    }
    
    public static Test suite() {
    	TestSuite suite = new TestSuite("ObjectTracking NestPO tests");
    	
    	suite.addTest(new GroupId());
    	//suite.addTest(new Merge());
    	suite.addTest(new UpdateAllTests());
    	suite.addTest(new CreateOutputTuple());
    	suite.addTest(new Nest());
    	suite.addTest(new TwoGroupingAttributeNest());
    	suite.addTest(new NestOfNesting());
    	
		return suite;
    }    
}

