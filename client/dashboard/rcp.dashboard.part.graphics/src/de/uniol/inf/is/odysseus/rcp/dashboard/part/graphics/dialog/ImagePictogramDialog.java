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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
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

/**
 * @author DGeesen
 * 
 */
public class ImagePictogramDialog extends AbstractPictogramDialog<ImagePictogram> {

	private String location;
	private boolean stretch = true;
	private boolean center = false;
	private boolean keepRatio = true;
	private String predicate;

	private Text predicateText;
	private Button stretchCheckButton;
	private Text imgText;
	private Button centerCheckButton;
	private Button keepRatioCheckButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.NewPictogramDialog#createWidgetAdrea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createWidgetAdrea(final Composite parent) {

		Label lblChooseAnImage = new Label(parent, SWT.NONE);
		lblChooseAnImage.setText("Choose an image");		

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout compositeLayout = new GridLayout(2, false);
		compositeLayout.marginWidth = 0;
		compositeLayout.marginHeight = 0;
		composite.setLayout(compositeLayout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		imgText = new Text(composite, SWT.BORDER);
		GridData gd_dataFolderText = new GridData(GridData.FILL_HORIZONTAL);
		gd_dataFolderText.widthHint = 287;
		imgText.setLayoutData(gd_dataFolderText);
		imgText.setText(location);

		Button button = new Button(composite, SWT.PUSH);
		button.setText("Browse...");

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {				
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
				dialog.setInput(getProject());
				dialog.setAllowMultiple(false);
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
						if (selection.length != 1) {
							return new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, "You have to choose a single image");	
						} 
						Object sel = selection[0];
						if (sel instanceof IFile) {
							IFile file = (IFile) sel;
							if (validFileResouce(file)) {
								return Status.OK_STATUS;
							}
						}
						return new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, "You can only choose an image");
					}
				});
				dialog.setTitle("Choose an image");

				if (dialog.open() == Window.OK) {
					IResource resource = (IResource) dialog.getFirstResult();
					imgText.setText(resource.getProjectRelativePath().toString());
				}			
			}
		});
		
		

		Label lblPredicate = new Label(parent, SWT.NONE);
		lblPredicate.setText("Predicate to show image or not");		

		predicateText = new Text(parent, SWT.BORDER);
		predicateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		predicateText.setText(predicate);
		

		stretchCheckButton = new Button(parent, SWT.CHECK);
		stretchCheckButton.setText("Resize the image to fit the container size");
		stretchCheckButton.setSelection(stretch);
		
		keepRatioCheckButton = new Button(parent, SWT.CHECK);
		keepRatioCheckButton.setText("Keep ratio if the image is resized to fit the container");
		keepRatioCheckButton.setSelection(keepRatio);
		
		centerCheckButton = new Button(parent, SWT.CHECK);
		centerCheckButton.setText("Centers the image if it does not fit into container");
		centerCheckButton.setSelection(center);
		
		return parent;
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
	public void saveValues(ImagePictogram pg) {		
		pg.setFilename(imgText.getText());
		pg.setPredicate(predicateText.getText());
		pg.setStretch(stretchCheckButton.getSelection());
		pg.setCenter(centerCheckButton.getSelection());
		pg.setKeepRatio(keepRatioCheckButton.getSelection());		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.NewPictogramDialog#loadValues(de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram)
	 */
	@Override
	public void loadValues(ImagePictogram pg) {		
		this.stretch = pg.isStretch();
		this.keepRatio = pg.isKeepRatio();
		this.center = pg.isCenter();
		
		if (pg.getFile() != null) {
			this.location = pg.getFile().getProjectRelativePath().toOSString();
		} else {
			this.location = "";
		}
		this.predicate = pg.getPredicate().toString();

	}
}
