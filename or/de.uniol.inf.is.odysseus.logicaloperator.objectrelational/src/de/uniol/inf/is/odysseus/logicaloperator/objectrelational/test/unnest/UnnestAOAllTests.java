package de.uniol.inf.is.odysseus.logicaloperator.objectrelational.test.unnest;

import de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.unnest.cases.ComplexUnnesting;

import de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.unnest.cases.SimpleUnnesting;

import junit.framework.TestSuite;

public class UnnestAOAllTests extends TestSuite {

    public UnnestAOAllTests() {
        this.addTest(new SimpleUnnesting());
        this.addTest(new ComplexUnnesting());
    }
}
