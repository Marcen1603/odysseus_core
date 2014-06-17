/**
 * 
 */
package de.uniol.inf.is.odysseus.test.component.probabilistic;

import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryExpectedOutputTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.ExpectedOutputTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticTestComponent extends AbstractQueryExpectedOutputTestComponent<BasicTestContext, ExpectedOutputTestSet> {

    @Override
    public List<ExpectedOutputTestSet> createTestSets(BasicTestContext context) {
        return TestSetFactory.createExpectedOutputTestSetsFromBundleRoot(context.getDataRootPath(), "ProbabilisticTuple");
    }

    @Override
    public boolean isActivated() {
        return true;
    }

    @Override
    public String getName() {
        return "Probabilistic Test Component";
    }

}
