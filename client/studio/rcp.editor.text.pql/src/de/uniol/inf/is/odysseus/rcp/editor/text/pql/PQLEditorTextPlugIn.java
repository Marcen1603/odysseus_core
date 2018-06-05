package de.uniol.inf.is.odysseus.rcp.editor.text.pql;

import java.util.List;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

public class PQLEditorTextPlugIn extends AbstractUIPlugin {
	
	
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.editor.text.pql";
	public static final String PQL_OPERATOR_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLOperatorView";

	public static final String REFRESH_PQL_OPERATOR_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.editor.text.pql.RefreshPQLOperatorView";

	private static PQLEditorTextPlugIn instance;	
	private static ImageManager imageManager;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		instance = this;
		imageManager = new ImageManager(context.getBundle());

		imageManager.register("pqlOperator", "icons/operator.png");
		imageManager.register("pqlAttribute", "icons/manParameter.png");
		imageManager.register("pqlOptionalAttribute", "icons/optParameter.png");
		imageManager.register("sources", "icons/sources.png");
		imageManager.register("sinks", "icons/sinks.png");
		imageManager.register("attribute", "icons/attribute.png");
		imageManager.register("value", "icons/value.png");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);

		instance = null;
	
		imageManager.disposeAll();
		imageManager = null;
	}

		
	public static PQLEditorTextPlugIn getDefault() {
		return instance;
	}
	
	public static ImageManager getImageManager() {
		return imageManager;
	}
	
	public static List<String> getOperatorNames() {
		return OdysseusRCPEditorTextPlugIn.getExecutor().getOperatorNames(OdysseusRCPPlugIn.getActiveSession());
	}
	
	public static List<LogicalOperatorInformation> getOperatorInformations() {
		return OdysseusRCPEditorTextPlugIn.getExecutor().getOperatorInformations(OdysseusRCPPlugIn.getActiveSession());
	}

	public static List<ViewInformation> getCurrentSources() {
		return OdysseusRCPEditorTextPlugIn.getExecutor().getStreamsAndViewsInformation(OdysseusRCPPlugIn.getActiveSession());
		
	}
}
