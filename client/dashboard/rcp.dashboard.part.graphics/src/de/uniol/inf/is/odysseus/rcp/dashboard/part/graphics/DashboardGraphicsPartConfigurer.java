package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;

public class DashboardGraphicsPartConfigurer extends AbstractDashboardPartConfigurer<DashboardGraphicsPart> {

	private Text backgroundImageText;
	private DashboardGraphicsPart dashboardPart;	
	private Button checkButton;

	@Override
	public void init(DashboardGraphicsPart dashboardPartToConfigure, Collection<IPhysicalOperator> roots) {
		this.dashboardPart = dashboardPartToConfigure;		
	}

	@Override
	public void createPartControl(final Composite parent) {
		Label labelImg = new Label(parent, SWT.NONE);
		labelImg.setText("Backgound Image");
		
		backgroundImageText = new Text(parent, SWT.BORDER);
		GridData gd_dataFolderText = new GridData(GridData.FILL_HORIZONTAL);
		gd_dataFolderText.widthHint = 287;
		backgroundImageText.setLayoutData(gd_dataFolderText);
		String folder = "";
		if(dashboardPart.getBackgroundFile()!=null){
			folder = dashboardPart.getBackgroundFile();
			backgroundImageText.setText(folder);
		}		
		
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
					ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
					
					dialog.setInput(dashboardPart.getProject());
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
						backgroundImageText.setText(resource.getProjectRelativePath().toString());
					}			
				}		
				
//				FileDialog dlg = new FileDialog(parent.getShell());
//				dlg.setFilterPath(backgroundImageText.getText());
//				String dir = dlg.open();
//				if (dir != null) {
//					// Set the text box to the new selection
//					backgroundImageText.setText(dir);
//				}
//			}
		});
		
		backgroundImageText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setBackgroundFile(backgroundImageText.getText());				
				fireListener();
			}
		});
		
		Label label = new Label(parent, SWT.NONE);
		label.setText("Stretch backgound to fit size");
		
		checkButton = new Button(parent, SWT.CHECK);
		checkButton.setSelection(true);
		if(!dashboardPart.isBackgroundFileStretch()){
			checkButton.setSelection(false);
		}
		checkButton.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setBackgroundFileStretch(checkButton.getSelection());				
				fireListener();
			}
		});
		
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
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
