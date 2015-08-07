package de.uniol.inf.is.odysseus.query.transformation.rcp.composite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.query.transformation.modell.ProgressBarUpdate;

public class QueryTransformationProgressComposite extends Composite{

	private static final String NEWLINE = System.getProperty("line.separator");
	private ProgressBar progressInitializeQuery;
	private Text textArea;
	private Button finishButton;
	private Composite parent;
	
	public QueryTransformationProgressComposite(Composite parent, int style, int windowWidth) {
		super(parent, style);
		this.parent = parent;
		
		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.widthHint = windowWidth;
		this.setLayoutData(contentGridData);
		GridLayout gridLayout = new GridLayout();
		this.setLayout(gridLayout);

		createContent();
		
		parent.pack();
		parent.setVisible(true);
	}

	private void createContent() {
		progressInitializeQuery = new ProgressBar(this, SWT.SMOOTH);
		progressInitializeQuery.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		progressInitializeQuery.setMinimum(0);
		progressInitializeQuery.setMaximum(100);
		
		
		textArea = new Text(this, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData gd_textArea = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_textArea.heightHint = 266;
		textArea.setLayoutData(gd_textArea);
		
		
		finishButton = new Button(this, SWT.PUSH);
		finishButton.setText("Finish");
		finishButton.setAlignment(SWT.CENTER);
		finishButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		finishButton.setVisible(false);
		
		finishButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(!parent.isDisposed()){
					parent.dispose();
				}
			}
		});

	}
	
	
	public void showFinishButton(boolean visible){
		finishButton.setVisible(visible);
	}

	public void updateProgress(ProgressBarUpdate updateInfo) {
		progressInitializeQuery.setSelection(updateInfo.getProgressValue());
		
		StringBuilder tempText = new StringBuilder();
		if(!textArea.getText().equals("")){
			tempText.append(textArea.getText());
			tempText.append(NEWLINE);
		}
	
		tempText.append(getCurrentTime()+" - ");
		tempText.append(updateInfo.getText());
		
		textArea.setText(tempText.toString());
		
	}
	

	private String getCurrentTime(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date); //15:59:48
	}
	
}
