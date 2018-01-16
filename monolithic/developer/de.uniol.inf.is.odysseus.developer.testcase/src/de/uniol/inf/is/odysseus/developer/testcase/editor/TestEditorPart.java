/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.developer.testcase.editor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.developer.testcase.Activator;
import de.uniol.inf.is.odysseus.developer.testcase.generator.TestGenerator;
import de.uniol.inf.is.odysseus.developer.testcase.model.TestModel;
import de.uniol.inf.is.odysseus.developer.testcase.model.TestModel.AttributeParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class TestEditorPart extends EditorPart implements IResourceChangeListener, IResourceDeltaVisitor {
	public static final String ID = "de.uniol.inf.is.odysseus.rcp.test.editor.TestEditorPart"; //$NON-NLS-1$
	static final Logger LOG = LoggerFactory.getLogger(TestEditorPart.class);

	private TestModel model;

	private FileEditorInput input;
	private boolean dirty = false;

	/**
	 * Class constructor.
	 *
	 */
	public TestEditorPart() {
		super();
	}

	@Override
	public void createPartControl(final Composite parent) {
		final ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		final Composite container = new Composite(scrolledComposite, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(1, false);
		container.setLayout(gridLayout);

		final Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name:");
		final Text txtName = new Text(container, SWT.NONE);
		txtName.setText(this.model.getName());
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		txtName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				TestEditorPart.this.model.setName(txtName.getText());
				TestEditorPart.this.setDirty(true);
			}
		});

		final Label lblOperator = new Label(container, SWT.NONE);
		lblOperator.setText("Operator:");
		final Combo comboOperator = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		comboOperator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final ISession session = Activator.getSession();
		final List<LogicalOperatorInformation> operators = Activator.getExecutor().getOperatorInformations(session);
		Collections.sort(operators, new Comparator<LogicalOperatorInformation>() {

			@Override
			public int compare(final LogicalOperatorInformation o1, final LogicalOperatorInformation o2) {
				return o1.getOperatorName().compareTo(o2.getOperatorName());
			}
		});
		int index = 0;
		for (int i = 0; i < operators.size(); i++) {
			final LogicalOperatorInformation operator = operators.get(i);
			comboOperator.add(operator.getOperatorName());
			comboOperator.setData(operator.getOperatorName(), operator);
			if ((this.model.getOperator() != null)
					&& (operator.getOperatorName().equalsIgnoreCase(this.model.getOperator().getOperatorName()))) {
				index = i;
			}
		}
		comboOperator.select(index);
		final Label lblDirectory = new Label(container, SWT.NONE);
		lblDirectory.setText("Directory:");
		final Text txtDirectory = new Text(container, SWT.NONE);
		txtDirectory.setText(this.model.getDirectory());
		txtDirectory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		txtDirectory.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				TestEditorPart.this.model.setDirectory(txtDirectory.getText());
				TestEditorPart.this.setDirty(true);
			}
		});

		final Label lblTimestamp = new Label(container, SWT.NONE);
		lblTimestamp.setText("");
		final Button txtTimestamp = new Button(container, SWT.CHECK);
		txtTimestamp.setText("Timestamps");
		txtTimestamp.setSelection(this.model.isTimestamp());
		txtTimestamp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		txtTimestamp.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TestEditorPart.this.model.setTimestamp(txtTimestamp.getSelection());
				TestEditorPart.this.setDirty(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		final Composite operatorContainer = new Composite(container, SWT.NONE);
		operatorContainer.setLayout(new GridLayout(1, false));
		final Group grpSchema = new Group(operatorContainer, SWT.NONE);
		grpSchema.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		grpSchema.setLayout(new GridLayout(1, true));
		grpSchema.setText("Schema");

		final TabFolder tabFolder = new TabFolder(grpSchema, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		int ports = 1;
		if (this.model.getOperator() != null) {
			ports = this.model.getOperator().getMaxPorts();
		}
		// Restrict number of ports in case of UNION etc.
		if (ports > 10) {
			ports = 10;
		}
		for (int i = 0; i < ports; i++) {
			this.createAttributeTab(tabFolder, i);
		}

		final Group grpParameter = new Group(operatorContainer, SWT.NONE);
		grpParameter.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		grpParameter.setLayout(new GridLayout(1, false));
		grpParameter.setText("Parameter");
		this.createParameter(grpParameter);

		final Group grpMetadata = new Group(container, SWT.NONE);
		grpMetadata.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		grpMetadata.setLayout(new GridLayout(2, false));
		grpMetadata.setText("Metadata");
		this.createMetadata(grpMetadata);

		comboOperator.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final int index = comboOperator.getSelectionIndex();
				if ((model.getOperator() == null)
						|| (!model.getOperator().equals(comboOperator.getData(comboOperator.getItem(index))))) {
					TestEditorPart.this.model.setOperator(
							(LogicalOperatorInformation) comboOperator.getData(comboOperator.getItem(index)));
					int ports = 1;
					if (TestEditorPart.this.model.getOperator() != null) {
						ports = TestEditorPart.this.model.getOperator().getMaxPorts();
					}
					if (ports > 10) {
						ports = 10;
					}
					for (final TabItem item : tabFolder.getItems()) {
						item.dispose();
					}
					for (int i = 0; i < ports; i++) {
						TestEditorPart.this.createAttributeTab(tabFolder, i);
					}

					for (final Control control : grpParameter.getChildren()) {
						control.dispose();
					}

					TestEditorPart.this.createParameter(grpParameter);
					operatorContainer.layout();
					TestEditorPart.this.setDirty(true);
				}
			}
		});

		final Button btnGenerate = new Button(container, SWT.PUSH);
		btnGenerate.setText("Generate");
		btnGenerate.addSelectionListener(new SelectionAdapter() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				TestEditorPart.this.generateTest(parent.getShell());
			}
		});
		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
	}

	private void generateTest(final Shell shell) {
		if (this.isDirty()) {
			if (MessageDialog.openConfirm(shell, "Save before?", "Do you want to save before starting?")) {
				this.doSave(new NullProgressMonitor());
			} else {
				return;
			}
		}

		try {
			new ProgressMonitorDialog(shell).run(true, true,
					new TestGenerator(this.input.getFile().getParent(), this.model));
		} catch (InvocationTargetException | InterruptedException e) {
			TestEditorPart.LOG.error(e.getMessage(), e);
		}

	}

	@Override
	public void doSave(final IProgressMonitor monitor) {
		this.model.save(this.input.getFile());
		this.setDirty(false);
	}

	@Override
	public void doSaveAs() {
		// Empty block
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		setInput(input);
		setSite(site);
		this.input = (FileEditorInput) input;
		Objects.requireNonNull(this.input);
		setPartName(this.input.getName());
		try {
			this.model = TestModel.load(this.input.getFile());
		} catch (Exception e) {
			getSite().getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					MessageDialog.openError(getSite().getShell(), "Error loading test case", e.getMessage());
				}
			});
			this.model = new TestModel();
		}
		this.setPartName(this.input.getName());
		this.setSite(site);
		this.setInput(input);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		setDirty(false);
	}

	@Override
	public boolean isDirty() {
		return this.dirty;
	}

	private void setDirty(final boolean dirty) {
		this.dirty = dirty;
		this.firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public FileEditorInput getEditorInput() {
		return (FileEditorInput) super.getEditorInput();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			final IResourceDelta delta = event.getDelta();
			try {
				delta.accept(this);
			} catch (final CoreException e) {
				e.printStackTrace();
			}
		}
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			this.closeEditor(event.getResource());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocus() {
		// Empty block
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean visit(final IResourceDelta delta) throws CoreException {
		final IResource resource = delta.getResource();
		if (resource instanceof IFile) {
			final IFile file = (IFile) resource;
			if (file.equals(this.getEditorInput().getFile())) {
				switch (delta.getKind()) {
				case IResourceDelta.REMOVED:
				case IResourceDelta.REPLACED:
					this.closeEditor(delta.getResource());
					break;
				default:
					break;
				}
				return false;
			}
		}
		return true;
	}

	private void closeEditor(final IResource resource) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				final IWorkbenchPage[] pages = TestEditorPart.this.getSite().getWorkbenchWindow().getPages();
				for (final IWorkbenchPage page : pages) {
					if ((TestEditorPart.this.getEditorInput()).getFile().equals(resource)) {
						final IEditorPart editorPart = page.findEditor(TestEditorPart.this.getEditorInput());
						page.closeEditor(editorPart, true);
					}
				}
			}
		});
	}

	private void createMetadata(final Composite composite) {
		final Set<String> names = MetadataRegistry.getNames();
		for (final String name : names) {
			final Button button = new Button(composite, SWT.CHECK);
			button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
			button.setText(name);
			button.setSelection(this.model.getMetadata(name));
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					TestEditorPart.this.model.setMetadata(name, button.getSelection());
					TestEditorPart.this.setDirty(true);

				};
			});

		}
	}

	private void createParameter(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		composite.setLayout(new GridLayout(2, true));
		for (final Entry<String, String> parameter : this.model.getParameters().entrySet()) {
			final Label label = new Label(composite, SWT.NONE);
			final String name = parameter.getKey().toLowerCase();
			if (name.length() > 1) {
				label.setText(name.substring(0, 1).toUpperCase() + name.substring(1) + ":");
			} else {
				label.setText(name + ":");
			}
			final Text text = new Text(composite, SWT.NONE);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
			text.setText(parameter.getValue());
			text.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(final ModifyEvent e) {
					TestEditorPart.this.model.setParameter(parameter.getKey(), text.getText());
					TestEditorPart.this.setDirty(true);
				}
			});
		}
	}

	private void createAttributeTab(final TabFolder tabFolder, final int port) {
		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Port " + port);

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(2, false));

		final Composite comWindow = new Composite(composite, SWT.NONE);
		comWindow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		comWindow.setLayout(new GridLayout(2, false));

		final Label lblWindow = new Label(comWindow, SWT.NONE);
		lblWindow.setText("Window:");
		final Text txtWindow = new Text(comWindow, SWT.NONE);
		txtWindow.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		txtWindow.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				TestEditorPart.this.model.addWindow(port, Integer.parseInt(txtWindow.getText()));
				TestEditorPart.this.setDirty(true);
			}
		});

		txtWindow.setText(this.model.getWindow(port).toString());
		@SuppressWarnings("unused")
		Label lblEmpty = new Label(composite, SWT.NONE);

		final CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(composite,
				SWT.BORDER | SWT.FULL_SELECTION);
		checkboxTableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		checkboxTableViewer.setLabelProvider(new SchemaLabelProvider());
		checkboxTableViewer.setContentProvider(new SchemaContentProvider(port));
		checkboxTableViewer.setInput(this.model);
		checkboxTableViewer.setCheckStateProvider(new SchemaStateProvider());

		final Composite comAttributeParameter = new Composite(composite, SWT.NONE);
		comAttributeParameter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		comAttributeParameter.setLayout(new GridLayout(2, false));

		final Label lblName = new Label(comAttributeParameter, SWT.NONE);
		lblName.setText("Name:");
		final Text txtName = new Text(comAttributeParameter, SWT.NONE);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		txtName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				final int index = checkboxTableViewer.getTable().getSelectionIndex();
				if (index >= 0) {
					final String attribute = (String) checkboxTableViewer.getTable().getItem(index).getData();
					TestEditorPart.this.model.renameAttribute(port, attribute, txtName.getText());
					checkboxTableViewer.refresh();
					TestEditorPart.this.setDirty(true);
					checkboxTableViewer.getTable().select(index);
				}
			}
		});

		final Label lblType = new Label(comAttributeParameter, SWT.NONE);
		lblType.setText("Type:");
		final Combo comboType = new Combo(comAttributeParameter, SWT.NONE);
		comboType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

		final ISession session = Activator.getSession();
		final List<SDFDatatype> datatypes = new ArrayList<>(Activator.getExecutor().getRegisteredDatatypes(session));
		Collections.sort(datatypes, new Comparator<SDFDatatype>() {

			@Override
			public int compare(SDFDatatype o1, SDFDatatype o2) {
				return o1.getQualName().compareTo(o2.getQualName());
			}
		});
		for (final SDFDatatype datatype : datatypes) {
			comboType.add(datatype.getQualName());
			comboType.setData(datatype.getQualName(), datatype);
		}
		comboType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final int index = checkboxTableViewer.getTable().getSelectionIndex();
				if (index >= 0) {
					final String attribute = (String) checkboxTableViewer.getTable().getItem(index).getData();
					final AttributeParameter parameter = TestEditorPart.this.model.getSchema(port).get(attribute);
					final int datatypeIndex = ((Combo) e.getSource()).getSelectionIndex();
					if ((parameter.getType() == null)
							|| (!parameter.getType().equals(comboType.getData(comboType.getItem(datatypeIndex))))) {
						parameter.setType((SDFDatatype) comboType.getData(comboType.getItem(datatypeIndex)));
						TestEditorPart.this.setDirty(true);
					}
				}
			}
		});

		final Label lblMin = new Label(comAttributeParameter, SWT.NONE);
		lblMin.setText("Min:");
		final Text txtMin = new Text(comAttributeParameter, SWT.NONE);
		txtMin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		txtMin.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				final int index = checkboxTableViewer.getTable().getSelectionIndex();
				if (index >= 0) {
					final String attribute = (String) checkboxTableViewer.getTable().getItem(index).getData();
					final AttributeParameter parameter = TestEditorPart.this.model.getSchema(port).get(attribute);
					parameter.setMin(TestModel.getValue(txtMin.getText(), parameter.getType()));
					TestEditorPart.this.setDirty(true);
				}
			}
		});

		final Label lblMax = new Label(comAttributeParameter, SWT.NONE);
		lblMax.setText("Max:");
		final Text txtMax = new Text(comAttributeParameter, SWT.NONE);
		txtMax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		txtMax.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				final int index = checkboxTableViewer.getTable().getSelectionIndex();
				if (index >= 0) {
					final String attribute = (String) checkboxTableViewer.getTable().getItem(index).getData();
					final AttributeParameter parameter = TestEditorPart.this.model.getSchema(port).get(attribute);
					parameter.setMax(TestModel.getValue(txtMax.getText(), parameter.getType()));
					TestEditorPart.this.setDirty(true);
				}
			}
		});

		final Label lblNull = new Label(comAttributeParameter, SWT.NONE);
		lblNull.setText("Null:");
		final Button btnNull = new Button(comAttributeParameter, SWT.CHECK);
		btnNull.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final int index = checkboxTableViewer.getTable().getSelectionIndex();
				if (index >= 0) {
					final String attribute = (String) checkboxTableViewer.getTable().getItem(index).getData();
					final AttributeParameter parameter = TestEditorPart.this.model.getSchema(port).get(attribute);
					parameter.setNullValue(btnNull.getSelection());
					TestEditorPart.this.setDirty(true);
				}
			}
		});
		checkboxTableViewer.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final int index = checkboxTableViewer.getTable().getSelectionIndex();
				if ((index >= 0) && (checkboxTableViewer.getTable().getItem(index) != null)) {
					final String attribute = (String) checkboxTableViewer.getTable().getItem(index).getData();
					final AttributeParameter parameter = TestEditorPart.this.model.getSchema(port).get(attribute);
					txtName.setText(attribute);
					if (parameter != null) {
						final String datatype = parameter.getType().getQualName();
						if (datatype != null) {
							comboType.select(comboType.indexOf(datatype));
						} else {
							comboType.select(0);
						}
						txtMin.setText(TestModel.getString(parameter.getMin(), parameter.getType()));
						txtMax.setText(TestModel.getString(parameter.getMax(), parameter.getType()));
						btnNull.setSelection(parameter.isNullValue());
						if (!parameter.isActive() == checkboxTableViewer.getTable().getItem(index).getChecked()) {
							parameter.setActive(checkboxTableViewer.getTable().getItem(index).getChecked());
							TestEditorPart.this.setDirty(true);
						}
					}
				}

			}
		});

		final Composite comButton = new Composite(composite, SWT.NONE);
		comButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		comButton.setLayout(new GridLayout(6, true));

		final Button btnAdd = new Button(comButton, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				String name = "Attribute";
				if ((TestEditorPart.this.model.getSchema(port) != null)
						&& (TestEditorPart.this.model.getSchema(port).containsKey(name))) {
					int i = 1;
					while (TestEditorPart.this.model.getSchema(port).containsKey(name + "_" + i)) {
						i++;
					}
					name = name + "_" + i;
				}
				final AttributeParameter parameter = new AttributeParameter(SDFDatatype.DOUBLE, -Double.MIN_VALUE,
						Double.MAX_VALUE, true, true);
				TestEditorPart.this.model.addAttribute(port, name, parameter);
				checkboxTableViewer.refresh();
				TestEditorPart.this.setDirty(true);
				checkboxTableViewer.getTable().select(checkboxTableViewer.getTable().getItemCount() - 1);
				txtName.setText(name);
				final String datatype = parameter.getType().getQualName();
				if (datatype != null) {
					comboType.select(comboType.indexOf(datatype));
				} else {
					comboType.select(0);
				}
				txtMin.setText(TestModel.getString(parameter.getMin(), parameter.getType()));
				txtMax.setText(TestModel.getString(parameter.getMax(), parameter.getType()));
				btnNull.setSelection(parameter.isNullValue());
			}
		});
		btnAdd.setText("Add");

		final Button btnRemove = new Button(comButton, SWT.NONE);
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final int index = checkboxTableViewer.getTable().getSelectionIndex();
				if (index >= 0) {
					final String attribute = (String) checkboxTableViewer.getTable().getItem(index).getData();
					TestEditorPart.this.model.removeAttribute(port, attribute);
					checkboxTableViewer.refresh();
					TestEditorPart.this.setDirty(true);
					checkboxTableViewer.getTable().select(checkboxTableViewer.getTable().getItemCount() - 1);
				}
			}
		});
		btnRemove.setText("Remove");

		tabItem.setControl(composite);
	}

	private static class SchemaContentProvider implements IStructuredContentProvider {
		private final int port;

		/**
		 * Class constructor.
		 *
		 */
		public SchemaContentProvider(final int port) {
			this.port = port;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void dispose() {
			// Empty block
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			// Empty block
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object[] getElements(final Object inputElement) {
			if (inputElement instanceof TestModel) {
				final Map<String, AttributeParameter> schema = ((TestModel) inputElement).getSchema(this.port);
				if (schema != null) {
					return schema.keySet().toArray();
				}
			}
			if (inputElement instanceof AttributeParameter) {
				final AttributeParameter parameter = ((AttributeParameter) inputElement);
				return new Object[] { parameter.getMax(), parameter.getMax() };
			}
			return new Object[0];
		}
	}

	private static class SchemaLabelProvider implements ITableLabelProvider {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addListener(final ILabelProviderListener listener) {
			// Empty block
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void dispose() {
			// Empty block
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isLabelProperty(final Object element, final String property) {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeListener(final ILabelProviderListener listener) {
			// Empty block
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Image getColumnImage(final Object element, final int columnIndex) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getColumnText(final Object element, final int columnIndex) {
			switch (columnIndex) {
			case 0:
				if (element instanceof AttributeParameter) {
					return ((AttributeParameter) element).getType().getQualName();
				}
				if (element instanceof String) {
					return element.toString();
				}
				return null;
			case 1:
				if (element instanceof String) {
					return element.toString();
				}
				return null;
			default:
				return null;
			}
		}

	}

	private static class SchemaStateProvider implements ICheckStateProvider {

		@Override
		public boolean isChecked(final Object element) {
			if (element instanceof AttributeParameter) {
				final AttributeParameter parameter = (AttributeParameter) element;
				return parameter.isActive();
			}
			return false;
		}

		@Override
		public boolean isGrayed(final Object element) {
			return false;
		}
	}

}
