package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.unnest;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking.test.unnest.cases.Unnest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking.test.unnest.cases.UnnestOfNestWithNestings;

import junit.framework.Test;
import junit.framework.TestSuite;

public class UnnestPOAllTests extends TestSuite {
    public UnnestPOAllTests() {        
        this.setName("Asserting all NestPO tests");         
    }
    
    public static Test suite() {
    	TestSuite suite = new TestSuite("ObjectTracking Unnest PO tests");

        suite.addTest(new Unnest());
        suite.addTest(new UnnestOfNestWithNestings());
        
        return suite;
    }
}


