package de.uniol.inf.is.odysseus.iql.qdl.ui;


import javax.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import de.uniol.inf.is.odysseus.iql.basic.ui.parser.IIQLUiParser;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLModel;
import de.uniol.inf.is.odysseus.iql.qdl.ui.internal.QDLActivator;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionErrorDialog;


public class RunQueryCommandHandler extends AbstractHandler{
	
	@Inject
	private IIQLUiParser parser;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		XtextEditor editor = (XtextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        IFileEditorInput input = (IFileEditorInput)editor.getEditorInput() ;
        final IProject project = input.getFile().getProject();
        
        editor.getDocument().modify(new IUnitOfWork<QDLModel, XtextResource>() {

			@Override
			public QDLModel exec(XtextResource state) throws Exception {
				EObject obj = state.getParseResult().getRootASTElement();
				execute((QDLModel)obj, project);
				return null;
			}
		});
        return null;
	}
	
	private void execute(final QDLModel file, final IProject project) {
        Job job = new Job("Parsing and Executing Query") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                   parser.parse(file, project);
                } catch (final Throwable ex) {
                	ex.printStackTrace();
                    ExceptionErrorDialog.open(new Status(IStatus.ERROR, QDLActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_QDL_QDL, "'Parsing and Executing Query' has encountered a problem.\n\nScript Execution Error: "
                            + ex.getMessage(), ex), ex);
                }
                return Status.OK_STATUS;
            }
        };
        job.setUser(true);
        job.schedule();
    }
	
	

}
