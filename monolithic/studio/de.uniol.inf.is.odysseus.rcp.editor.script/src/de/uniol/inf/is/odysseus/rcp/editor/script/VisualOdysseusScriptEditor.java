package de.uniol.inf.is.odysseus.rcp.editor.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.google.common.collect.Lists;

public class VisualOdysseusScriptEditor extends EditorPart {

	public VisualOdysseusScriptEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof IFileEditorInput)) {
			throw new PartInitException("Input is not valid for visual odysseus script editor");
		}

		try {
			IFileEditorInput fileInput = (IFileEditorInput) input;
			InputStream inputStream = fileInput.getFile().getContents();
			List<String> lines = readLines(inputStream);
			
		} catch (CoreException | IOException e) {
			throw new PartInitException("Could not read contents of file", e);
		}

		setSite(site);
		setInput(input);

		setPartName("Visual OdysseusScript Editor");
	}

	private static List<String> readLines(InputStream inputStream) throws IOException {
		List<String> lines = Lists.newArrayList();

		try( BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)) ) {
			String line = reader.readLine();
			while( line != null ) {
				lines.add(line);
				System.err.println(line);
				line = reader.readLine();
			}
		}
		return lines;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {

	}

	@Override
	public void setFocus() {

	}
}
