package de.uniol.inf.is.odysseus.rcp.editor.text.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

import de.uniol.inf.is.odysseus.rcp.editor.text.editor.SimpleEditor;
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
			} 
		}
		
		// Check if we have an active Editor
		IEditorPart part = HandlerUtil.getActiveEditor(event);
		if( part instanceof SimpleEditor ) {
			SimpleEditor editor = (SimpleEditor)part;
			FileEditorInput input = (FileEditorInput)editor.getEditorInput();
			run(input.getFile());
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
			
			// Erst Text testen
			QueryTextParser parser = new QueryTextParser();
			parser.parse(lines.toArray(new String[lines.size()]), true);
			
			// Dann ausführen
			parser = new QueryTextParser();
			parser.parse(lines.toArray(new String[lines.size()]), false);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			new ExceptionWindow(ex);
		}
	}
}
