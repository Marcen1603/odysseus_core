package de.uniol.inf.is.odysseus.logicaloperator.objectrelational.test.nest;


import de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.nest.cases.NestOfNesting;

import de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.nest.cases.SimpleNesting;

import junit.framework.TestSuite;

public class NestAOAllTests extends TestSuite {
    public NestAOAllTests() {
        this.addTest(new SimpleNesting());
        this.addTest(new NestOfNesting());
    }
}
