package de.uniol.inf.is.odysseus.parser.pql;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static final String ACCESS = "ACCESS";
	private static final String SELECT = "SELECT";
	private static final String JOIN = "JOIN";
	private static final String MAP = "MAP";
	private static final String PROJECT = "PROJECT";
	private static final String UNION = "UNION";
	private static final String RENAME = "RENAME";
	private static final String WINDOW = "WINDOW";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		PQLParser.addOperatorBuilder(ACCESS, new AccessAOBuilder());
		PQLParser.addOperatorBuilder(SELECT, new SelectAOBuilder());
		PQLParser.addOperatorBuilder(JOIN, new JoinAOBuilder());
		PQLParser.addOperatorBuilder(PROJECT, new ProjectAOBuilder());
		PQLParser.addOperatorBuilder(MAP, new MapAOBuilder());
		PQLParser.addOperatorBuilder(UNION, new UnionAOBuilder());
		PQLParser.addOperatorBuilder(RENAME, new RenameAOBuilder());
		PQLParser.addOperatorBuilder(WINDOW, new WindowAOBuilder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		PQLParser.removeOperatorBuilder(ACCESS);
		PQLParser.removeOperatorBuilder(SELECT);
		PQLParser.removeOperatorBuilder(JOIN);
		PQLParser.removeOperatorBuilder(MAP);
		PQLParser.removeOperatorBuilder(PROJECT);
		PQLParser.removeOperatorBuilder(UNION);
		PQLParser.removeOperatorBuilder(RENAME);
		PQLParser.removeOperatorBuilder(WINDOW);
	}

}
