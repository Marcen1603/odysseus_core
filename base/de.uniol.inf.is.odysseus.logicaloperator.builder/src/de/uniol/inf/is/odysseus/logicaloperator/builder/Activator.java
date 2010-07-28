package de.uniol.inf.is.odysseus.logicaloperator.builder;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static final String SPLIT = "SPLIT";
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
		OperatorBuilderFactory.putOperatorBuilderType(ACCESS, AccessAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(SELECT, SelectAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(JOIN, JoinAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(PROJECT, ProjectAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(MAP, MapAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(UNION, UnionAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(RENAME, RenameAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(WINDOW, WindowAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(SPLIT, SplitAOBuilder.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removeOperatorBuilderType(ACCESS);
		OperatorBuilderFactory.removeOperatorBuilderType(SELECT);
		OperatorBuilderFactory.removeOperatorBuilderType(JOIN);
		OperatorBuilderFactory.removeOperatorBuilderType(MAP);
		OperatorBuilderFactory.removeOperatorBuilderType(PROJECT);
		OperatorBuilderFactory.removeOperatorBuilderType(UNION);
		OperatorBuilderFactory.removeOperatorBuilderType(RENAME);
		OperatorBuilderFactory.removeOperatorBuilderType(WINDOW);
		OperatorBuilderFactory.removeOperatorBuilderType(SPLIT);
	}

}
