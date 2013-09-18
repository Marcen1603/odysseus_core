package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;

public class DashboardGraphicsPartConfigurer extends AbstractDashboardPartConfigurer<DashboardGraphicsPart> {

	private Text backgroundImageText;
	private DashboardGraphicsPart dashboardPart;
	private Collection<IPhysicalOperator> roots;
	private Button checkButton;

	@Override
	public void init(DashboardGraphicsPart dashboardPartToConfigure, Collection<IPhysicalOperator> roots) {
		this.dashboardPart = dashboardPartToConfigure;
		this.roots = roots;
		
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
		if(dashboardPart.getBackgroundFile()==null){
			folder = dashboardPart.getBackgroundFile();
		}		
		backgroundImageText.setText(folder);
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				FileDialog dlg = new FileDialog(parent.getShell());
				dlg.setFilterPath(backgroundImageText.getText());
				String dir = dlg.open();
				if (dir != null) {
					// Set the text box to the new selection
					backgroundImageText.setText(dir);
				}
			}
		});
		
		backgroundImageText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setBackgroundFile(backgroundImageText.getText());				
				fireListener();
			}
		});
		dashboardPart.setRoots(roots);
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
