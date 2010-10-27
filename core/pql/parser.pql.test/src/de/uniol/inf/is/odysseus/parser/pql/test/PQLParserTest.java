package de.uniol.inf.is.odysseus.parser.pql.test;

import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.parser.pql.PQLParser;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

public class PQLParserTest implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		try {
			OperatorBuilderFactory.putOperatorBuilderType("muh", MuhBuilder.class);
			String queryString = "a = muh()\nb=muh()\nc=muh(a,b)\n"
					+ "OUT= muh({ [x=1,[y=2]], z='a>b']}, c)";
			PQLParser parser = new PQLParser();
			List<IQuery> ops = parser.parse(queryString, UserManagement.getInstance().getSuperUser());
			System.out.println(ops.get(0).getLogicalPlan());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
