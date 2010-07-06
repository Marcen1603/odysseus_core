package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking.test.nest.NestPOAllTests;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking.test.unnest.UnnestPOAllTests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests extends TestSuite {
    public static Test suite() {
    	TestSuite suite = new TestSuite("AllTests");    	
        
    	suite.addTest(NestPOAllTests.suite());
        suite.addTest(UnnestPOAllTests.suite());
        
    	return suite;
    } 
}