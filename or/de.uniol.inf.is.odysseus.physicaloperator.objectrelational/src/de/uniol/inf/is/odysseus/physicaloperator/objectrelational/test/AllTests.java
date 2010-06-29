package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.NestPOAllTests;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.unnest.UnnestPOAllTests;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
    public static Test suite() {
    	TestSuite suite = new TestSuite("AllTests");
    	
        suite.addTest(new NestPOAllTests());
        suite.addTest(new UnnestPOAllTests());
    	
    	return suite;
    } 
}
