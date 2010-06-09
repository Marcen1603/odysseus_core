package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.NestPOAllTests;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.unnest.UnnestPOAllTests;
import junit.framework.TestSuite;

public class TestSuiteAllTests extends TestSuite {
    public TestSuiteAllTests() {
        this.addTest(new NestPOAllTests());
        this.addTest(new UnnestPOAllTests());
    }
}