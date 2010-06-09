package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.unnest;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.unnest.cases.Unnest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.unnest.cases.UnnestOfNestWithNestings;
import junit.framework.TestSuite;

public class UnnestPOAllTests extends TestSuite {
    public UnnestPOAllTests() {        
        this.setName("Asserting all NestPO tests");
        
        this.addTest(new Unnest());
        this.addTest(new UnnestOfNestWithNestings());  
    }
}


