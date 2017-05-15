package de.uniol.inf.is.odysseus.test.component.parser.cql;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.osgi.service.jdbc.DataSourceFactory;

import de.uniol.inf.is.odysseus.core.server.monitoring.MyStats;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnectionFactory;
import de.uniol.inf.is.odysseus.test.StatusCode;
import de.uniol.inf.is.odysseus.test.component.AbstractQueryTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.QueryTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

public class CQLParserTest extends AbstractQueryTestComponent<BasicTestContext, QueryTestSet> {

	public CQLParserTest() 
	{
		super(false);
	}
	
	@Override
	public List<QueryTestSet> createTestSets(BasicTestContext context) 
	{
		File dir;
		List<File> queryFiles = new ArrayList<>();
		List<File> pqlFiles = new ArrayList<>();
		List<QueryTestSet> testsets = new ArrayList<>();
		try 
		{
			URL bundleroot = context.getDataRootPath();
			dir = new File(bundleroot.toURI());
			searchQueryFilesRecursive(dir, queryFiles, pqlFiles);
			for (File qf : queryFiles) 
			{
				
				QueryTestSet set = TestSetFactory.createQueryTestSetFromFile(qf.toURI().toURL(), bundleroot);
				if (set != null)
					testsets.add(set);
			}
		} 
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		
//		CQLGenerator generator = new CQLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration().getInstance(CQLGenerator.class);
		
		//
		return testsets;
	}
	
	@Override
	protected StatusCode executeTestSet(QueryTestSet set) 
	{
		
		
		StatusCode status= super.executeTestSet(set);
		switch(status)
		{
		case ERROR_QUERY_NOT_INSTALLABLE:
		case ERROR_EXCEPTION_DURING_TEST:
			
		break;
		default:
		}
		return status;
	}
	
	private static void searchQueryFilesRecursive(File dir, List<File> queryFiles, List<File> pqlFiles)
	{
		if (dir.isDirectory()) 
		{
			for (File child : dir.listFiles()) 
			{
				if (child.isDirectory())
					searchQueryFilesRecursive(child, queryFiles, pqlFiles);
				else 
				{
					if (child.getName().endsWith(".qry")) 
						queryFiles.add(child);
					else if(child.getName().endsWith(".pql"))
						pqlFiles.add(child);
				}
			}
		}
	}
	
	@Override
	public String getName() { return "CQL Parser Test"; }
	
	@Override
	public boolean isActivated() {	return true; }
	
}
