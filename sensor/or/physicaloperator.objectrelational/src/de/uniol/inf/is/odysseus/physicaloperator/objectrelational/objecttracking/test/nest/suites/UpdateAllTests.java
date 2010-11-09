package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.suites;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.update.IncomingTIContainedInPartialTI;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.update.IncomingTIContainsPartialTI;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.update.IncomingTIEqualPartialTI;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.update.IncomingTIOverlappingLeftPartialTI;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.update.IncomingTIOverlappingRightPartialTI;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.update.Step;
import junit.framework.TestSuite;

public class UpdateAllTests extends TestSuite {
    public UpdateAllTests() {
        this.setName("Asserting all NestPO update tests");
        
        this.addTest(new IncomingTIContainedInPartialTI());
        this.addTest(new IncomingTIContainsPartialTI());
        this.addTest(new IncomingTIEqualPartialTI());
        this.addTest(new IncomingTIOverlappingLeftPartialTI());
        this.addTest(new IncomingTIOverlappingRightPartialTI());
        this.addTest(new Step());
    }
}
