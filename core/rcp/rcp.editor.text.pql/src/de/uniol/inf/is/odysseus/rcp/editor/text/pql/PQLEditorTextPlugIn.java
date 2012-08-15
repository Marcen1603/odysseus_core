package de.uniol.inf.is.odysseus.rcp.editor.text.pql;

import java.util.List;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;

public class PQLEditorTextPlugIn extends AbstractUIPlugin {

	private static final Logger LOG = LoggerFactory.getLogger(PQLEditorTextPlugIn.class);
	
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.editor.text.pql";
	public static final String PQL_OPERATOR_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLOperatorView";

	public static final String REFRESH_PQL_OPERATOR_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.editor.text.pql.RefreshPQLOperatorView";

	private static PQLEditorTextPlugIn instance;
	private static IOperatorBuilderFactory operatorBuilderFactory;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		instance = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);

		instance = null;
	}

	public void bindOperatorBuilderFactory(IOperatorBuilderFactory builder) {
		operatorBuilderFactory = builder;
		
		LOG.debug("Bound OperatorBuilderFactory {}.", builder);
	}

	public void unbindOperatorBuilderFactory(IOperatorBuilderFactory builder) {
		if( operatorBuilderFactory == builder ) {
			operatorBuilderFactory = null;
			
			LOG.debug("Unbound OperatorBuilderFactory {}.", builder);
		}
	}

	public static String[] getPQLKeywords() {
		return operatorBuilderFactory != null ? determineNames(operatorBuilderFactory.getOperatorBuilder()) : new String[0];
	}
	
	public static IOperatorBuilderFactory getOperatorBuilderFactory() {
		return operatorBuilderFactory;
	}

	public static PQLEditorTextPlugIn getDefault() {
		return instance;
	}

	private static String[] determineNames(List<IOperatorBuilder> builders) {
		if( builders == null || builders.isEmpty() ) {
			return new String[0];
		}
		
		String[] names = new String[builders.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = builders.get(i).getName().toUpperCase();
		}
		return names;
	}
}
