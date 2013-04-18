/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

public class QueryFileSelectionPage extends WizardPage {

	private static final Logger LOG = LoggerFactory.getLogger(QueryFileSelectionPage.class);
	private final ContainerSelectionPage page1;

	private TableViewer filesTable;
	private Button copyQueryTextCheck;

	protected QueryFileSelectionPage(String pageName, ContainerSelectionPage page1) {
		super(pageName);
		this.page1 = page1;

		setTitle("Choose query");
		setDescription("Choose the query to execute to get the data.");
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		final Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData((GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL)));
		rootComposite.setLayout(new GridLayout(1, true));

		final Composite tableComposite = new Composite(rootComposite, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		filesTable = new TableViewer(tableComposite, SWT.FULL_SELECTION);
		final Table table = filesTable.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);

		final TableViewerColumn fileNameColumn = new TableViewerColumn(filesTable, SWT.NONE);
		fileNameColumn.getColumn().setText("Query file");
		tableColumnLayout.setColumnData(fileNameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		fileNameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				final IFile file = (IFile) cell.getElement();
				cell.setText(file.getName());
			}
		});

		filesTable.setContentProvider(ArrayContentProvider.getInstance());
		filesTable.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() != null) {
					setPageComplete(true);
				}
			}

		});

		copyQueryTextCheck = new Button(rootComposite, SWT.CHECK);
		copyQueryTextCheck.setText("Copy query into file (query-file and dashboard part are independent)");

		finishCreation(rootComposite);
	}

	public IFile getQueryFile() {
		final IStructuredSelection selection = (IStructuredSelection) filesTable.getSelection();
		return (IFile) selection.getFirstElement();
	}

	public boolean isQueryFileCopy() {
		return copyQueryTextCheck.getSelection();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible == true) {
			final IPath path = page1.getContainerFullPath();
			final IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);

			final List<IFile> queryFiles = Lists.newArrayList();
			traverse(resource, queryFiles);

			filesTable.setInput(queryFiles);
			filesTable.refresh();
			setPageComplete(false);
		}
	}

	private void finishCreation(Composite rootComposite) {
		setErrorMessage(null);
		setMessage(null);
		setControl(rootComposite);
		setPageComplete(false);
	}

	private static void traverse(IResource resource, List<IFile> foundFiles) {
		if (resource instanceof IContainer) {
			final IContainer container = (IContainer) resource;
			try {
				for (final IResource res : container.members()) {
					traverse(res, foundFiles);
				}
			} catch (final CoreException e) {
				LOG.error("Exception during finding query-Files.", e);
			}
		} else if (resource instanceof IFile) {
			final IFile file = (IFile) resource;
			if (OdysseusRCPEditorTextPlugIn.QUERY_TEXT_EXTENSION.equals(file.getFileExtension())) {
				foundFiles.add(file);
			}
		}
	}
}
