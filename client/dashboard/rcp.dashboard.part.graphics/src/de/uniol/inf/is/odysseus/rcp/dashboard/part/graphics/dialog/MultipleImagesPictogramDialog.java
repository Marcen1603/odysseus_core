/*******************************************************************************
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.MultipleImagePictogram;

/**
 * @author DGeesen
 * 
 */
public class MultipleImagesPictogramDialog extends AbstractPictogramDialog<MultipleImagePictogram> {

	private boolean stretch = true;
	private Button stretchCheckButton;

	private List<ImageEntry> entries = new ArrayList<>();
	private ScrolledComposite scroller;
	private Composite container;
	private Button keepRatioCheckButton;
	private Button centerCheckButton;
	private boolean center = false;
	private boolean keepRatio = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	@Override
	protected boolean isResizable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.NewPictogramDialog#createWidgetAdrea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createWidgetAdrea(final Composite parent) {

		Label lblChooseAnImage = new Label(parent, SWT.NONE);
		lblChooseAnImage.setText("Order-dependent list of images");

		Button btnBrowseToAdd = new Button(parent, SWT.PUSH);
		btnBrowseToAdd.setText("Browse to add images...");
		scroller = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER);
		GridData gd_scroller = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_scroller.heightHint = 250;
		scroller.setLayoutData(gd_scroller);
		container = new Composite(scroller, SWT.NONE);
		scroller.setContent(container);
		scroller.setExpandVertical(true);
		scroller.setExpandHorizontal(true);
		scroller.setMinHeight(250);

		GridLayout containerLayout = new GridLayout(2, false);
		containerLayout.marginWidth = 0;
		containerLayout.marginHeight = 0;
		container.setLayout(containerLayout);

		GridData gd_container = new GridData(GridData.FILL_HORIZONTAL);
		gd_container.heightHint = 250;
		gd_container.minimumHeight = 250;
		container.setLayoutData(gd_container);
		
		for(ImageEntry ie : entries){
			addImageEntryToContainer(ie);
		}
		
		btnBrowseToAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {				
				final ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
				dialog.setInput(getProject());
				dialog.setAllowMultiple(true);
				dialog.addFilter(new ViewerFilter() {
					@Override
					public boolean select(Viewer viewer, Object parentElement, Object element) {
						if (element instanceof IFile) {
							IFile res = (IFile) element;
							if (validFileResouce(res)) {
								return true;
							} 
							return false;
						}
						return true;
					}
				});
				dialog.setValidator(new ISelectionStatusValidator() {

					@Override
					public IStatus validate(Object[] selection) {
						dialog.getOkButton().setEnabled(false);
						if (selection.length <= 0) {
							return new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, "You have to choose at least one image");
						} 
						Object sel = selection[0];
						if (sel instanceof IFile) {
							IFile file = (IFile) sel;
							if (validFileResouce(file)) {
								dialog.getOkButton().setEnabled(true);
								return Status.OK_STATUS;
							}
						}
						return new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, "You can only choose images");
					}
				});

				dialog.setTitle("Choose one or more images");

				if (dialog.open() == Window.OK) {
					for (Object o : dialog.getResult()) {
						IResource resource = (IResource) o;
						String filename = resource.getProjectRelativePath().toOSString();
						ImageEntry ie = new ImageEntry();
						ie.image = filename;
						ie.predicate = "true";
						addImageEntryToContainer(ie);
						entries.add(ie);
					}
				}
			}

		});
		relayout(container, scroller);

		stretchCheckButton = new Button(parent, SWT.CHECK);
		stretchCheckButton.setText("Resize all images to fit the container size");
		stretchCheckButton.setSelection(stretch);
		
		keepRatioCheckButton = new Button(parent, SWT.CHECK);
		keepRatioCheckButton.setText("Keep ratio if the image is resized to fit the container");
		keepRatioCheckButton.setSelection(keepRatio);
		
		centerCheckButton = new Button(parent, SWT.CHECK);
		centerCheckButton.setText("Centers the image if it does not fit into container");
		centerCheckButton.setSelection(center);

		new Label(parent, SWT.NONE);

		return parent;
	}

	private void addImageEntryToContainer(final ImageEntry ie) {
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label imgLabel = new Label(composite, SWT.NONE);
		imgLabel.setText(ie.image);

		final Text predicateText = new Text(composite, SWT.BORDER);
		GridData gd_dataFolderText = new GridData(GridData.FILL_HORIZONTAL);
		gd_dataFolderText.widthHint = 287;
		predicateText.setLayoutData(gd_dataFolderText);
		predicateText.setText(ie.predicate);
		predicateText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				ie.predicate = predicateText.getText();
				
			}
		});
		
		ie.composite = composite;

		final Button removeButton = new Button(container, SWT.PUSH);
		removeButton.setText("X");
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ie.composite.dispose();
				entries.remove(ie);
				removeButton.dispose();
				relayout(container, scroller);
			}
		});
		relayout(container, scroller);
	}

	private void relayout(Composite container, ScrolledComposite scroller) {
		scroller.setMinHeight(container.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		container.layout();
		scroller.layout(true);
	}

	private boolean validFileResouce(IFile file) {
		String[] extensions = { "png", "gif", "jpg", "jpeg" };
		for (String ext : extensions) {
			if (ext.equalsIgnoreCase(file.getFileExtension())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.NewPictogramDialog#saveValues()
	 */
	@Override
	public void saveValues(MultipleImagePictogram pg) {
		pg.setStretch(stretchCheckButton.getSelection());
		pg.setCenter(centerCheckButton.getSelection());
		pg.setKeepRatio(keepRatioCheckButton.getSelection());		
		pg.clearImages();
		for (ImageEntry ie : entries) {
			pg.addImage(ie.predicate, ie.image);			
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.NewPictogramDialog#loadValues(de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram)
	 */
	@Override
	public void loadValues(MultipleImagePictogram pg) {
		this.stretch = pg.isStretch();
		this.keepRatio = pg.isKeepRatio();
		this.center = pg.isCenter();
		entries.clear();
		for (ImagePictogram imgPictogram : pg.getImages()) {
			ImageEntry ie = new ImageEntry();			
			ie.predicate = imgPictogram.getPredicate().toString();
			ie.image = imgPictogram.getFile().getProjectRelativePath().toOSString();
			entries.add(ie);			
		}
	}

	private class ImageEntry {
		protected String image;
		protected String predicate;		
		protected Composite composite;

	}
}
