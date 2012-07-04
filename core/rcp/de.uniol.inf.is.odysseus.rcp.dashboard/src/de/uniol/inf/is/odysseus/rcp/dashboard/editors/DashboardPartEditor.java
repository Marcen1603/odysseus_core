package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.XMLDashboardPartHandler;

public class DashboardPartEditor extends EditorPart {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartEditor.class);
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();

	private FileEditorInput input;
	private IDashboardPart dashboardPart;
	private boolean dirty;
	
	private TabFolder tabFolder;

	public DashboardPartEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			DASHBOARD_PART_HANDLER.save(dashboardPart, input.getFile());
			dirty = false;
		} catch (IOException e) {
			LOG.error("Could not save DashboardPart to file {}.", input.getFile().getName(), e);
		}
	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof FileEditorInput)) {
			LOG.error("Could not open editor because input is from type {} instead of {}!", input.getClass(), FileEditorInput.class);
			throw new PartInitException("Input is from type " + input.getClass() + " instead of " + FileEditorInput.class + "!");
		}
		this.input = (FileEditorInput) input;

		setSite(site);
		setInput(input);
		setPartName(this.input.getFile().getName());

		try {
			dashboardPart = DASHBOARD_PART_HANDLER.load(this.input.getFile());
		} catch (IOException e) {
			LOG.error("Could not load DashboardPart for editor from file {}!", this.input.getFile().getName(), e);
			throw new PartInitException("Could not load DashboardPart from file " + this.input.getFile().getName(), e);
		}
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());
		
		tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setLayout(new GridLayout());
		
		Composite presentationTab = newTabComposite(tabFolder, "Presentation");
		newTabComposite(tabFolder, "Settings");
		newTabComposite(tabFolder, "XML");
		
		dashboardPart.createPartControl(presentationTab);
	}

	@Override
	public void setFocus() {
		tabFolder.setFocus();
	}

	private static Composite newTabComposite( TabFolder tabFolder, String title ) {
		TabItem presentationTab = new TabItem(tabFolder, SWT.NULL);
		presentationTab.setText(title);
		
		Composite presentationTabComposite = new Composite(tabFolder, SWT.NONE);
		presentationTabComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		presentationTabComposite.setLayout(new GridLayout());
		
		presentationTab.setControl(presentationTabComposite);
		
		return presentationTabComposite;
	}
}
