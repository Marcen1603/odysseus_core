package de.uniol.inf.is.odysseus.test.component.parser.cql;

import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.QueryTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

public class CQLParserTest extends AbstractQueryTestComponent<BasicTestContext, QueryTestSet> {

	@Override
	public List<QueryTestSet> createTestSets(BasicTestContext context) 
	{
		System.out.println("Hey!!");
		return TestSetFactory.createQueryTestSetsFromBundleRoot(context.getDataRootPath());//returns all .qry files from the bundle context
		///test first if the mapping to pql is correct
//		TestSetFactory.createExpectedOutputTestSetsFromBundleRoot(bundleroot)
		//
//		return null;
	}

	public void checkMapping(QueryTestSet query)
	{
		
		
		
	}
	
	@Override
	public String getName() {
		return "CQL Parser Test";
	}
	
	@Override
	public boolean isActivated() {	
		return true;
	}
	
}
