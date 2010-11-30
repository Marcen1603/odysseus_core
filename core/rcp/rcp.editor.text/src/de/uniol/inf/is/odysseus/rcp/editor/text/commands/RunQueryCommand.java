package de.uniol.inf.is.odysseus.rcp.editor.text.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

import de.uniol.inf.is.odysseus.rcp.editor.text.editor.SimpleEditor;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IEditorTextParserConstants;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.PreParserStatement;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParser;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;

public class RunQueryCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object obj = structuredSelection.getFirstElement();

			if (obj instanceof IFile) {
				run((IFile)obj);
				return null;
			} 
		}
		
		// Check if we have an active Editor
		IEditorPart part = HandlerUtil.getActiveEditor(event);
		if( part instanceof SimpleEditor ) {
			SimpleEditor editor = (SimpleEditor)part;
			FileEditorInput input = (FileEditorInput)editor.getEditorInput();
			run(input.getFile());
			return null;
		}
		
		return null;
	}
	
	private void run( IFile queryFile ) {
		try {
			// Datei öffnen
			if (!queryFile.isSynchronized(IResource.DEPTH_ZERO))
				queryFile.refreshLocal(IResource.DEPTH_ZERO, null);

			// Datei einlesen
			ArrayList<String> lines = new ArrayList<String>();
			BufferedReader br = new BufferedReader( new InputStreamReader(queryFile.getContents()));
			String line = br.readLine();
			while( line != null ) {
				lines.add(line);
				line = br.readLine();
			}
			br.close();
			
			execute(lines.toArray(new String[lines.size()]));
			
		} catch (Exception ex) {
			ex.printStackTrace();
			new ExceptionWindow(ex);
		}
	}
	
	public void execute(final String[] text) {
		// Dieser Teil geschieht asynchron zum UIThread und wird als Job ausgeführt
		// Job-Mechanismus wird von RCP gestellt.
		Job job = new Job("Parsing and Executing Query") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					// Befehle holen
					final List<PreParserStatement> statements = QueryTextParser.getInstance().parseScript(text);
					
					// Erst Text testen
					monitor.beginTask("Executing Commands", statements.size() * 2);
					monitor.subTask("Validating");
					
					Map<String, Object> variables = new HashMap<String, Object>();
					for( PreParserStatement stmt : statements ) {
						stmt.validate(variables);
						monitor.worked(1);
						
						// Wollte der Nutzer abbrechen?
						if( monitor.isCanceled()) 
							return Status.CANCEL_STATUS;
					}
					
					// Dann ausführen
					variables = new HashMap<String, Object>();
					int counter = 1;
					for( PreParserStatement stmt : statements ) {
						monitor.subTask("Executing (" + counter + " / " + statements.size() + ")" );
						stmt.execute(variables);
						monitor.worked(1);
						counter++;
					}
					monitor.done();
				} catch( QueryTextParseException ex ) {
					ex.printStackTrace();
					return new Status(Status.ERROR, IEditorTextParserConstants.PLUGIN_ID, "Cant execute query", ex );
				}
				
				return Status.OK_STATUS;
			}
		};
		job.setUser(true); // gibt an, dass der Nutzer dieses Job ausgelöst hat
		job.schedule(); // dieser Job soll nun ausgeführt werden
	}
	
	
	
}
