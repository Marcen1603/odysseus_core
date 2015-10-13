package de.uniol.inf.is.odysseus.query.codegenerator.rcp.composite;

import java.io.File;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.rcp.modell.IRcpOptionComposite;
import de.uniol.inf.is.odysseus.query.codegenerator.rcp.registry.IRcpSpecialOption;
import de.uniol.inf.is.odysseus.query.codegenerator.rcp.registry.RcpSpecialOptionRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.rcp.window.QueryTransformationWindow;
import de.uniol.inf.is.odysseus.query.codegenerator.scheduler.registry.CSchedulerRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.target.platform.registry.TargetPlatformRegistry;
import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguartionException;
import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;

public class QueryTransformationParameterComposite extends AbstractParameterComposite {

	private Text txtTargetDirectory;
	private Text txtOdysseusCode;

	private Button btnChooseTargetDirectory;
	private Combo targetPlatform;
	private Combo comboExecutor;

	private Composite inputDirectoryComposite;
	private Composite inputTwoGridComposite;
	private Composite inputOptionComposite;
	
	private Composite parentComposite;
	

	private IRcpOptionComposite optionComposite;
	
	
	private Composite buttonComposite;
	private QueryTransformationWindow window;
	private Shell parentShell;
	
	private int style; 
	
	private static String rcpConfig = "CODEGEN";

	public QueryTransformationParameterComposite(
			final QueryTransformationWindow window, Composite parent,
			int style, int windowWidth) {
		super(parent, style);
		this.window = window;
		this.parentShell = parent.getShell();
		this.style = style;
		this.parentComposite = parent;

		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.horizontalAlignment = SWT.FILL;
		contentGridData.widthHint = windowWidth;
		this.setLayoutData(contentGridData);
		setLayout(new GridLayout(1, false));

		inputDirectoryComposite = new Composite(this, SWT.FILL);
		inputDirectoryComposite.setLayoutData(new GridData(SWT.FILL,
				SWT.CENTER, true, false, 1, 1));

		inputTwoGridComposite = new Composite(this, SWT.FILL);
		inputTwoGridComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		
		inputOptionComposite  = new Composite(this, SWT.FILL);
		inputOptionComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		inputOptionComposite.setLayout(new GridLayout(1, false));

		createContent();

		// Create a horizontal separator
		Label separator = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		buttonComposite = new Composite(this, SWT.NONE);
		GridData griDDatabuttonComposite = new GridData(SWT.BEGINNING);
		griDDatabuttonComposite.horizontalAlignment = SWT.FILL;
		buttonComposite.setLayoutData(griDDatabuttonComposite);
		buttonComposite.setLayout(new GridLayout(3, false));
		

	
		createButton();
		
		loadRCPConfig();
		
		IRcpSpecialOption rcpSpecialOption = RcpSpecialOptionRegistry.getRCPSpeicalOption(targetPlatform.getText());
		
		if(rcpSpecialOption != null){
			optionComposite = rcpSpecialOption.getComposite(inputOptionComposite, style);
		}
	
		refreshComboExecutor();
		
		parent.pack();
		parent.setVisible(true);
	}

	private void createContent() {

		inputDirectoryComposite.setLayout(new GridLayout(3, false));
		inputTwoGridComposite.setLayout(new GridLayout(2, false));


		txtTargetDirectory = createTextFieldWithLabel(inputDirectoryComposite,
				"", "Target directory");

		btnChooseTargetDirectory = new Button(inputDirectoryComposite, SWT.NONE);
		btnChooseTargetDirectory.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, false, false, 1, 1));
		btnChooseTargetDirectory.setText("...");

		btnChooseTargetDirectory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				DirectoryDialog dlg = new DirectoryDialog(
						parentShell, SWT.OPEN);
				dlg.setText("Open");
				String path = dlg.open();
				if (path == null)
					return;
				txtTargetDirectory.setText(path);
			}
		});

		txtOdysseusCode = createTextFieldWithLabel(inputDirectoryComposite,
				"", "Odysseus-Code directory");

		Button buttonOdysseusCore = new Button(inputDirectoryComposite,
				SWT.NONE);
		buttonOdysseusCore.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		buttonOdysseusCore.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				DirectoryDialog dlg = new DirectoryDialog(
						parentShell, SWT.OPEN);
				dlg.setText("Open");
				String path = dlg.open();
				if (path == null)
					return;
				txtOdysseusCode.setText(path);
			}
		});
		buttonOdysseusCore.setText("...");

		targetPlatform = createComboWithLabel(inputTwoGridComposite,
				"Targetplatform:", TargetPlatformRegistry.getAllTargetPlatform(),2);
		
		targetPlatform.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
	
		    	 Control[] children = inputOptionComposite.getChildren();
		    	    for (int i = 0 ; i < children.length; i++) {
		    	        children[i].dispose();
		    	    }
		    	
		    	  
		  		IRcpSpecialOption rcpSpecialOption = RcpSpecialOptionRegistry.getRCPSpeicalOption(targetPlatform.getText());
		  		
		  		if(rcpSpecialOption != null){
		  			optionComposite = rcpSpecialOption.getComposite(inputOptionComposite, style);
		  		}
		  		
		  		refreshComboExecutor();
			
		  		inputOptionComposite.layout();
		  		parentComposite.pack();
		  	
		      }
		});

		comboExecutor = createComboWithLabel(inputTwoGridComposite,
				"Executor:", CSchedulerRegistry.getAllExecutor("JRE"),0);
		
	}

	private void createButton() {

		Button oklButton = new Button(buttonComposite, SWT.PUSH);
		oklButton.setText("OK");
		oklButton.setAlignment(SWT.CENTER);
		oklButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		oklButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					checkInputFields();
					
					saveInputFields();

					Map<String,String> options = null;
					if(optionComposite != null && !optionComposite.isDisposed()){
						options= optionComposite.getInput();
					}
					
					

					TransformationParameter parameter = new TransformationParameter(
							targetPlatform.getText(), txtTargetDirectory
									.getText(),
							window.getQueryId(), txtOdysseusCode.getText(),
							true, comboExecutor.getText(), options);
					window.startQueryTransformation(parameter);
				} catch (IllegalArgumentException ae) {
					createErrorDialog(ae);
				}
			}
		});

		Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setAlignment(SWT.CENTER);
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(buttonComposite, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (!window.getWindow().isDisposed()) {
					window.getWindow().dispose();
				}

			}
		});

	}

	private void checkInputFields() throws IllegalArgumentException {

		if (!checkDirecotry(txtTargetDirectory.getText())) {
			throw new IllegalArgumentException(
					"Please check your input\nNo access to the temp folder: "
							+ txtTargetDirectory.getText());
		}


		if (!checkDirecotry(txtOdysseusCode.getText())) {
			throw new IllegalArgumentException(
					"Please check your input\nNo access to the Odysseus code folder: "
							+ txtOdysseusCode.getText());
		}
		

	}

	private boolean checkDirecotry(String folder) {
		File folderFile = new File(folder);

		if (folderFile.exists() && folderFile.canRead()) {
			return true;
		} else {
			return false;
		}
	}

	private void createErrorDialog(Throwable t) {
		MessageDialog dialog = new MessageDialog(parentShell, "Error", null,
				t.getLocalizedMessage(), MessageDialog.ERROR,
				new String[] { "ok" }, 0);
		dialog.open();
	}
	
	private void loadRCPConfig(){
		
			
			try {
				String targetDirectoryRCPConfig = OdysseusRCPConfiguration.get(rcpConfig+"targetDirectory");
				txtTargetDirectory.setText(targetDirectoryRCPConfig);
				
			} catch (OdysseusRCPConfiguartionException e) {
				e.printStackTrace();
			}
			
			try {
				String odysseusDirectoryRCPConfig = OdysseusRCPConfiguration.get(rcpConfig+"odysseusDirectory");
				txtOdysseusCode.setText(odysseusDirectoryRCPConfig);
			} catch (OdysseusRCPConfiguartionException e) {
				e.printStackTrace();
			}
			
			
			try {
				String targetPlatformRCPConfig = OdysseusRCPConfiguration.get(rcpConfig+"targetPlatform");
				String[] items = targetPlatform.getItems();
				
				for(int i=0;i<items.length; i++){
					 if (items[i].equals(targetPlatformRCPConfig)) {
						 targetPlatform.select(i);
				     }
				}
				
		
				refreshComboExecutor();
				
			} catch (OdysseusRCPConfiguartionException e) {
				e.printStackTrace();
			}
	}
	
	
	private void saveInputFields(){
		OdysseusRCPConfiguration.set(rcpConfig+"targetDirectory", txtTargetDirectory.getText());
		OdysseusRCPConfiguration.set(rcpConfig+"odysseusDirectory", txtOdysseusCode.getText());
		OdysseusRCPConfiguration.set(rcpConfig+"targetPlatform", targetPlatform.getText());
		OdysseusRCPConfiguration.set(rcpConfig+"scheduler", comboExecutor.getText());
	}
	
	
	private void refreshComboExecutor(){
		
		if(CSchedulerRegistry.getAllExecutor(targetPlatform.getText())!=null){
			
			String[] schedulers = CSchedulerRegistry.getAllExecutor(targetPlatform.getText()).toArray(new String[CSchedulerRegistry.getAllExecutor(targetPlatform.getText()).size()]);
			comboExecutor.setItems(stringArrayToUpperCase(schedulers));
			comboExecutor.select(0);
			
			for(int i=0; i<schedulers.length;i++){
				 try {
					if (schedulers[i].toLowerCase().equals(OdysseusRCPConfiguration.get(rcpConfig+"scheduler").toLowerCase())) {
						comboExecutor.select(i);
					 }
				} catch (OdysseusRCPConfiguartionException e) {
					e.printStackTrace();
				}
				
			}
			comboExecutor.setEnabled(true);
		}else{
			comboExecutor.setEnabled(false);
		}
	}
	
	private String[] stringArrayToUpperCase(String[] stringArray){
		
		for(int i=0; i< stringArray.length; i++){
			stringArray[i] = stringArray[i].toUpperCase();
		}
		
		return stringArray;
	}

}
