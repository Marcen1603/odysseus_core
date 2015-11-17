package de.uniol.inf.is.odysseus.codegenerator.rcp.composite;

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

import de.uniol.inf.is.odysseus.codegenerator.model.TransformationParameter;
import de.uniol.inf.is.odysseus.codegenerator.rcp.model.IRcpOptionComposite;
import de.uniol.inf.is.odysseus.codegenerator.rcp.registry.IRcpSpecialOption;
import de.uniol.inf.is.odysseus.codegenerator.rcp.registry.RcpSpecialOptionRegistry;
import de.uniol.inf.is.odysseus.codegenerator.rcp.window.QueryTransformationWindow;
import de.uniol.inf.is.odysseus.codegenerator.scheduler.registry.CSchedulerRegistry;
import de.uniol.inf.is.odysseus.codegenerator.target.platform.registry.TargetPlatformRegistry;
import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguartionException;
import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;


/**
 * composite class for configuration content of codegenerator UI
 * 
 * @author MarcPreuschaft
 *
 */
public class QueryTransformationParameterComposite extends AbstractParameterComposite {

	private Text txtTargetDirectory;
	private Text txtOdysseusCode;

	private Button btnChooseTargetDirectory;
	private Combo targetPlatform;
	private Combo comboScheduler;

	private Composite inputDirectoryComposite;
	private Composite inputTwoGridComposite;
	private Composite inputOptionComposite;
	
	private Composite parentComposite;

	private IRcpOptionComposite optionComposite;
	
	private Composite buttonComposite;
	private QueryTransformationWindow window;
	private Shell parentShell;
	
	private int style; 
	
	private static String rcpConfig = "codegen";

	public QueryTransformationParameterComposite(
			final QueryTransformationWindow window, Composite parent,
			int style, int windowWidth) {
		super(parent, style);
		this.window = window;
		this.parentShell = parent.getShell();
		this.style = style;
		this.parentComposite = parent;

		//set grid layout
		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.horizontalAlignment = SWT.FILL;
		contentGridData.widthHint = windowWidth;
		this.setLayoutData(contentGridData);
		setLayout(new GridLayout(1, false));

		//set the composite layout
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
		
		//create foot buttons
		createButton();
		
		//load the last codegeneration config
		loadRCPConfig();
		
		//load the special options composite
		IRcpSpecialOption rcpSpecialOption = RcpSpecialOptionRegistry.getRCPSpeicalOption(targetPlatform.getText());
		
		if(rcpSpecialOption != null){
			optionComposite = rcpSpecialOption.getComposite(inputOptionComposite, style);
		}
	
		//refresh the scheduler combobox
		refreshComboScheduler();
		
		//referesh and show the composite
		parent.pack();
		parent.setVisible(true);
	}
	
	
	/**
	 * create the content of this composite
	 */
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
				dlg.setFilterPath(txtTargetDirectory.getText());
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
				dlg.setFilterPath(txtOdysseusCode.getText());
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
		    	 //if the targetPlatform changed, disable the special option children
		    	 Control[] children = inputOptionComposite.getChildren();
		    	    for (int i = 0 ; i < children.length; i++) {
		    	        children[i].dispose();
		    	    }
		    	 //load the new special options from the targetplatform 
		  		IRcpSpecialOption rcpSpecialOption = RcpSpecialOptionRegistry.getRCPSpeicalOption(targetPlatform.getText());
		  		
		  		//if special option not null 
		  		if(rcpSpecialOption != null){
		  			optionComposite = rcpSpecialOption.getComposite(inputOptionComposite, style);
		  		}
		  		
		  		//refresh the combo box
		  		refreshComboScheduler();
			
		  		//refresh the gui
		  		inputOptionComposite.layout();
		  		parentComposite.pack();
		  	
		      }
		});

		comboScheduler = createComboWithLabel(inputTwoGridComposite,
				"Scheduler:", CSchedulerRegistry.getAllScheduler("JRE"),0);
		
	}

	/**
	 * create the ok and cancel button for the composite
	 * 
	 */
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
							true, comboScheduler.getText(), options);
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

	/**
	 * validate the configuration
	 * 
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * check if the directory exist and is readable
	 * 
	 * @param folder
	 * @return
	 */
	private boolean checkDirecotry(String folder) {
		File folderFile = new File(folder);

		if (folderFile.exists() && folderFile.canRead()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * create a error dialog 
	 * 
	 * @param t
	 */
	private void createErrorDialog(Throwable t) {
		MessageDialog dialog = new MessageDialog(parentShell, "Error", null,
				t.getLocalizedMessage(), MessageDialog.ERROR,
				new String[] { "ok" }, 0);
		dialog.open();
	}
	
	/**
	 * load the last codegeneration config 
	 */
	private void loadRCPConfig(){
		
			try {
				String targetDirectoryRCPConfig = OdysseusRCPConfiguration.get(rcpConfig+"TargetDirectory");
				txtTargetDirectory.setText(targetDirectoryRCPConfig);
				
			} catch (OdysseusRCPConfiguartionException e) {
				e.printStackTrace();
			}
			
			try {
				String odysseusDirectoryRCPConfig = OdysseusRCPConfiguration.get(rcpConfig+"OdysseusDirectory");
				txtOdysseusCode.setText(odysseusDirectoryRCPConfig);
			} catch (OdysseusRCPConfiguartionException e) {
				e.printStackTrace();
			}
			
			
			try {
				String targetPlatformRCPConfig = OdysseusRCPConfiguration.get(rcpConfig+"TargetPlatform");
				String[] items = targetPlatform.getItems();
				
				for(int i=0;i<items.length; i++){
					 if (items[i].equals(targetPlatformRCPConfig)) {
						 targetPlatform.select(i);
				     }
				}
				refreshComboScheduler();
				
			} catch (OdysseusRCPConfiguartionException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * save all inputfields in the codegeneration config
	 * 
	 */
	private void saveInputFields(){
		OdysseusRCPConfiguration.set(rcpConfig+"TargetDirectory", txtTargetDirectory.getText());
		OdysseusRCPConfiguration.set(rcpConfig+"OdysseusDirectory", txtOdysseusCode.getText());
		OdysseusRCPConfiguration.set(rcpConfig+"TargetPlatform", targetPlatform.getText());
		OdysseusRCPConfiguration.set(rcpConfig+"Scheduler", comboScheduler.getText());
	}
	
	/**
	 * refresh the scheduler combobox
	 */
	private void refreshComboScheduler(){
		
		if(CSchedulerRegistry.getAllScheduler(targetPlatform.getText())!=null){
			
			String[] schedulers = CSchedulerRegistry.getAllScheduler(targetPlatform.getText()).toArray(new String[CSchedulerRegistry.getAllScheduler(targetPlatform.getText()).size()]);
			comboScheduler.setItems(stringArrayToUpperCase(schedulers));
			comboScheduler.select(0);
			
			for(int i=0; i<schedulers.length;i++){
				 try {
					if (schedulers[i].toLowerCase().equals(OdysseusRCPConfiguration.get(rcpConfig+"Scheduler").toLowerCase())) {
						comboScheduler.select(i);
					 }
				} catch (OdysseusRCPConfiguartionException e) {
					e.printStackTrace();
				}
				
			}
			comboScheduler.setEnabled(true);
		}else{
			comboScheduler.setEnabled(false);
		}
	}
	
	/**
	 * help function, stringArray to upperCase
	 * used by the refreshComboScheduler function 
	 * for a nice look
	 * 
	 * @param stringArray
	 * @return
	 */
	private String[] stringArrayToUpperCase(String[] stringArray){
		
		for(int i=0; i< stringArray.length; i++){
			stringArray[i] = stringArray[i].toUpperCase();
		}
		
		return stringArray;
	}

}
