package de.uniol.inf.is.odysseus.logicaloperator.objectrelational.test;

import junit.framework.TestSuite;

import de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.nest.NestAOAllTests;

import de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.unnest.UnnestAOAllTests;

public class AllTests extends TestSuite {
    public AllTests() {
        this.addTest(new NestAOAllTests());
        this.addTest(new UnnestAOAllTests());
    }
}
