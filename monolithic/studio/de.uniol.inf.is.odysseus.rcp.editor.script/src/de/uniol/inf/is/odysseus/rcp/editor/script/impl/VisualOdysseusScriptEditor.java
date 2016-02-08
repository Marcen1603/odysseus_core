package de.uniol.inf.is.odysseus.rcp.editor.script.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptTextBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;

public class VisualOdysseusScriptEditor extends EditorPart implements IVisualOdysseusScriptContainer {

	private VisualOdysseusScriptModel scriptModel;
	private ScrolledComposite scrollComposite;
	private Composite parent;
	private Composite contentComposite;
	
	private boolean isDirty;

	public VisualOdysseusScriptEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO
	}

	@Override
	public void doSaveAs() {
		// TODO
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof IFileEditorInput)) {
			throw new PartInitException("Input is not valid for visual odysseus script editor");
		}

		setSite(site);
		setInput(input);

		try {
			IFileEditorInput fileInput = (IFileEditorInput) input;
			InputStream inputStream = fileInput.getFile().getContents();
			List<String> lines = readLines(inputStream);

			scriptModel = new VisualOdysseusScriptModel();
			scriptModel.parse(lines);

			setPartName(fileInput.getName());
		} catch (CoreException | IOException e) {
			throw new PartInitException("Could not read contents of file", e);
		} catch (VisualOdysseusScriptException e) {
			throw new PartInitException("Could not parse odysseus script file", e);
		}
	}

	private static List<String> readLines(InputStream inputStream) throws IOException {
		List<String> lines = Lists.newArrayList();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
		}
		return lines;
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		
		scrollComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.setExpandVertical(true);
		
		contentComposite = new Composite(scrollComposite, SWT.BORDER);
		contentComposite.setLayout(new GridLayout());
		
		scrollComposite.setContent(contentComposite);
		
		for( IVisualOdysseusScriptTextBlock textBlock : scriptModel.getTextBlocks()) {
			
			Composite textBlockComposite = new Composite(contentComposite, SWT.BORDER);
			textBlockComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			textBlockComposite.setLayout(new GridLayout());
			
			textBlock.createPartControl(textBlockComposite, this);
		}
		
		scrollComposite.setMinSize(scrollComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}

	@Override
	public void setFocus() {
		if( scrollComposite != null ) {
			scrollComposite.setFocus();
		}
	}

	@Override
	public void layoutAll() {
		parent.layout();
		scrollComposite.layout();
		contentComposite.layout();

		scrollComposite.setMinSize(scrollComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	@Override
	public void setDirty(boolean dirty) {
		if (dirty != this.isDirty) {
			this.isDirty = dirty;
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					firePropertyChange(IEditorPart.PROP_DIRTY);
				}
			});
		}
	}
}
