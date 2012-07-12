package de.uniol.inf.is.odysseus.rcp.editor.text.pql;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;

public class PQLEditorTextPlugIn extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.editor.text.pql";
	public static final String PQL_OPERATOR_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLOperatorView";
	
	public static final String REFRESH_PQL_OPERATOR_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.editor.text.pql.RefreshPQLOperatorView";
	
	private static IOperatorBuilderFactory operatorBuilderFactory;
	private static PQLEditorTextPlugIn instance;
	
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
	}

	public void unbindOperatorBuilderFactory(IOperatorBuilderFactory builder) {
		operatorBuilderFactory = null;
	}
	
	public static IOperatorBuilderFactory getOperatorBuilderFactory() {
		return operatorBuilderFactory;
	}
	
	public static PQLEditorTextPlugIn getDefault() {
		return instance;
	}
}
