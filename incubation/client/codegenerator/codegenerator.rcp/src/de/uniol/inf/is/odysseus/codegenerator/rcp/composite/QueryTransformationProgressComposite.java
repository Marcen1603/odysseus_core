package de.uniol.inf.is.odysseus.codegenerator.rcp.composite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

import de.uniol.inf.is.odysseus.codegenerator.model.CodegeneratorMessageEvent;

/**
 * composite for the query analyse (progressbar and textfield for 
 * message)
 * 
 * @author MarcPreuschaft
 *
 */
public class QueryTransformationProgressComposite extends Composite{

	private static final String NEWLINE = System.getProperty("line.separator");
	private ProgressBar progressInitializeQuery;
	private StyledText textArea;
	private Button finishButton;
	private Composite parent;
	
	
	public QueryTransformationProgressComposite(Composite parent, int style, int windowWidth) {
		super(parent, style);
		this.parent = parent;
		
		//set the grid layout for this composite
		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.widthHint = windowWidth;
		this.setLayoutData(contentGridData);
		GridLayout gridLayout = new GridLayout();
		this.setLayout(gridLayout);

		//create content
		createContent();
		
		parent.pack();
		parent.setVisible(true);
	}

	/**
	 * create the content of the composite
	 */
	private void createContent() {
		progressInitializeQuery = new ProgressBar(this, SWT.SMOOTH);
		progressInitializeQuery.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		progressInitializeQuery.setMinimum(0);
		progressInitializeQuery.setMaximum(100);
		
		textArea = new StyledText(this, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData styledtextAreaLayout = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		styledtextAreaLayout.heightHint = 266;
		textArea.setLayoutData(styledtextAreaLayout);
		
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
	

	/**
	 * update the progressbar and textfield
	 * 
	 * @param updateInfo
	 */
	public void updateProgress(CodegeneratorMessageEvent updateInfo) {
		
		if(updateInfo.getProgressValue() != -1){
			progressInitializeQuery.setSelection(updateInfo.getProgressValue());
		}

		StringBuilder tempText = new StringBuilder();
	
		if(!textArea.getText().equals("")){
			tempText.append(textArea.getText());
			tempText.append(NEWLINE);
		}
	
		tempText.append(getCurrentTime()+" - "+ updateInfo.getStatusType().getText()+": ");
		tempText.append(updateInfo.getText());
		
		textArea.setText(tempText.toString());
	}
	

	/**
	 * get current time in a nice format 
	 * HH:mm:ss
	 * 
	 * @return
	 */
	private String getCurrentTime(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	/**
	 * set the visible of the finish button
	 * only show the finish button if the transformation is finished
	 * 
	 * @param visible
	 */
	public void showFinishButton(boolean visible){
		finishButton.setVisible(visible);
	}
	
}
