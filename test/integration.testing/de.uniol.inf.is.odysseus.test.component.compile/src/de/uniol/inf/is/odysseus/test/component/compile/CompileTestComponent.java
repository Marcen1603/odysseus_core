package de.uniol.inf.is.odysseus.test.component.compile;

import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.QueryTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

public class CompileTestComponent extends AbstractQueryTestComponent<BasicTestContext, QueryTestSet>{

	public CompileTestComponent(){
		super(false);
	}
	
	@Override
	public List<QueryTestSet> createTestSets(BasicTestContext context) {
		return TestSetFactory.createQueryTestSetsFromBundleRoot(context.getDataRootPath());		
	}
	
	@Override
	public String getName() {		
		return "Compile Test Component";
	}
	
	@Override
	public boolean isActivated() {	
		return true;
	}

}
