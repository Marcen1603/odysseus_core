package de.uniol.inf.is.odysseus.rcp.editor.script.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;

public class VisualOdysseusScriptEditor extends EditorPart implements IVisualOdysseusScriptContainer {

	private static final Logger LOG = LoggerFactory.getLogger(VisualOdysseusScriptEditor.class);

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
		setDirty(false);

		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		try {
			String script = scriptModel.generateOdysseusScript();
			file.setContents(new ByteArrayInputStream(script.getBytes()), IResource.KEEP_HISTORY | IResource.FORCE, null);

		} catch (VisualOdysseusScriptException | CoreException e) {
			LOG.error("Could not save odysseus script to file {}.", file.getName(), e);
			new ExceptionWindow("Could not save odysseus script to file " + file.getName(), e);
		}
	}

	@Override
	public void doSaveAs() {
		// do nothing
		// saveAs is not allowed (see method isSaveAsAllowed())
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
			IFile file = fileInput.getFile();
			if (!file.isSynchronized(IResource.DEPTH_ZERO)) {
				file.refreshLocal(IResource.DEPTH_ZERO, null);
			}

			InputStream inputStream = file.getContents();
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
		GridLayout contentLayout = new GridLayout();
		contentLayout.verticalSpacing = 0;
		contentLayout.marginHeight = 0;
		contentLayout.marginWidth = 0;
		contentComposite.setLayout(contentLayout);

		scrollComposite.setContent(contentComposite);

		for (IVisualOdysseusScriptBlock textBlock : scriptModel.getVisualTextBlocks()) {

			Composite topBlockComposite = new Composite(contentComposite, SWT.NONE);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.widthHint = parent.getBounds().width;
			topBlockComposite.setLayoutData(gd);
			topBlockComposite.setLayout(new GridLayout());
			
			String title = textBlock.getTitle();
			
			Label titleLabel = new Label(topBlockComposite, SWT.BORDER);
			titleLabel.setText(Strings.isNullOrEmpty(title) ? "<No title>" : title);
			titleLabel.setAlignment(SWT.CENTER);
			titleLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
			titleLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			titleLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			Composite textBlockComposite = new Composite(contentComposite, SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.widthHint = parent.getBounds().width;
			textBlockComposite.setLayoutData(gd);
			textBlockComposite.setLayout(new FillLayout());

			textBlock.createPartControl(textBlockComposite, this);
		}

		scrollComposite.setMinSize(scrollComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	@Override
	public void setFocus() {
		if (scrollComposite != null) {
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
