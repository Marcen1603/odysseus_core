package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

public class NewDashboardPartWizardPage3 extends WizardPage {

	private static final Logger LOG = LoggerFactory.getLogger(NewDashboardPartWizardPage3.class);
	private final NewDashboardPartWizardPage1 page1;
	
	private TableViewer filesTable;

	protected NewDashboardPartWizardPage3(String pageName, NewDashboardPartWizardPage1 page1) {
		super(pageName);
		this.page1 = page1;

		setTitle("Choose query");
		setDescription("Choose the query to execute to get the data.");
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData((GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL)));
		rootComposite.setLayout(new GridLayout(1, true));
		
		Composite tableComposite = new Composite(rootComposite, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		filesTable = new TableViewer(tableComposite, SWT.FULL_SELECTION);
		Table table = filesTable.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);

		TableViewerColumn fileNameColumn = new TableViewerColumn(filesTable, SWT.NONE);
		fileNameColumn.getColumn().setText("Query file");
		tableColumnLayout.setColumnData(fileNameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		fileNameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				IFile file = (IFile)cell.getElement();
				cell.setText(file.getName());
			}
		});
		
		filesTable.setContentProvider(ArrayContentProvider.getInstance());
		filesTable.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if( event.getSelection() != null ) {
					setPageComplete(true);
				}
			}
			
		});

		finishCreation(rootComposite);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible == true) {
			IPath path = page1.getContainerFullPath();
			IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
			
			List<IFile> queryFiles = Lists.newArrayList();
			traverse(resource, queryFiles);
			
			filesTable.setInput(queryFiles);
			filesTable.refresh();
			setPageComplete(false);
		}
	}
	
	public IFile getQueryFile() {
		IStructuredSelection selection = (IStructuredSelection) filesTable.getSelection();
		return (IFile) selection.getFirstElement();
	}

	private static void traverse(IResource resource, List<IFile> foundFiles) {
		if( resource instanceof IContainer ) {
			IContainer container = (IContainer)resource;
			try {
				for( IResource res : container.members()) {
					traverse(res, foundFiles);
				}
			} catch (CoreException e) {
				LOG.error("Exception during finding query-Files.", e);
			}
		} else if( resource instanceof IFile ) {
			IFile file = (IFile)resource;
			if( OdysseusRCPEditorTextPlugIn.QUERY_TEXT_EXTENSION.equals(file.getFileExtension())) {
				foundFiles.add(file);
			}
		}
	}

	private void finishCreation(Composite rootComposite) {
		setErrorMessage(null);
		setMessage(null);
		setControl(rootComposite);
		setPageComplete(false);
	}
}
