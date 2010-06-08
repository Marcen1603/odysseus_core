package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	NestPOTestGroupId.class,
	NestPOTestMerge.class,
	NestPOTestUpdate.class,
	NestPOTestCreateOutputTuple.class,
	NestPOTestNest.class,
	NestPOTestTwoGAttrNest.class,
	NestPOTestNestOfNesting.class
})

public class NestPOTestAllTests {
}

