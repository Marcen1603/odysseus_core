package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases.CreateOutputTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases.GroupId;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases.Merge;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases.Nest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases.NestOfNesting;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases.TwoGroupingAttributeNest;
import junit.framework.TestSuite;

public class NestPOAllTests extends TestSuite {
    public NestPOAllTests() {
        this.setName("Asserting all NestPO tests");
        
        this.addTest(new GroupId());
        this.addTest(new Merge());
      //  this.addTest(new Update());
        this.addTest(new CreateOutputTuple());
        this.addTest(new Nest());
        this.addTest(new TwoGroupingAttributeNest());
        this.addTest(new NestOfNesting());
    }
}

