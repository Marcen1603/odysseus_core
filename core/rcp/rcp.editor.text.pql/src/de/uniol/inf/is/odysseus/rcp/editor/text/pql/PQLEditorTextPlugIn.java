package de.uniol.inf.is.odysseus.rcp.editor.text.pql;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;

public class PQLEditorTextPlugIn extends AbstractUIPlugin {

	private static IOperatorBuilderFactory operatorBuilderFactory;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
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
}
