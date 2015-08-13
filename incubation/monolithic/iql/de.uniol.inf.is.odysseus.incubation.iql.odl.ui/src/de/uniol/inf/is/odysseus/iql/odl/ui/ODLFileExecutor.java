package de.uniol.inf.is.odysseus.iql.odl.ui;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.ui.parser.IIQLUiParser;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLModel;
import de.uniol.inf.is.odysseus.iql.odl.ui.internal.ODLActivator;
import de.uniol.inf.is.odysseus.rcp.editor.text.IFileExecutor;

public class ODLFileExecutor implements IFileExecutor{

	@Override
	public String getFileExtension() {
		return "odl";
	}

	@Override
	public void run(String text, Context context) {
		
	}

	@Override
	public void run(String text, Context context, IEditorPart editor) {
		if (editor instanceof XtextEditor) {
			XtextEditor xtextEditor = (XtextEditor) editor;
			xtextEditor.getDocument().modify(new IUnitOfWork<ODLModel, XtextResource>() {

				@Override
				public ODLModel exec(XtextResource state) throws Exception {
					EObject obj = state.getParseResult().getRootASTElement();
					IIQLUiParser parser = ODLActivator.getInstance().getInjector(ODLActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_ODL_ODL).getInstance(IIQLUiParser.class);
					parser.parse((IQLModel) obj);
					return null;
					
				}
			});
		}
		
	}

}
