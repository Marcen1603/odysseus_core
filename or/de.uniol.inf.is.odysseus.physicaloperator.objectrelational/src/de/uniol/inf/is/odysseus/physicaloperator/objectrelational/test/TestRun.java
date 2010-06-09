package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test;


import junit.textui.TestRunner;

import org.junit.Test;

public class TestRun {

    @Test
    public void now() {
        TestRunner.run(new TestSuiteAllTests());
    }

}
