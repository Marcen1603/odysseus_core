package de.uniol.inf.is.odysseus.query.codegenerator.rcp.jre.composite;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.query.codegenerator.rcp.modell.AbstractRCPOptionComposite;

public class JreRcpComposite extends AbstractRCPOptionComposite {
	
	private Button autoBuild;


	public JreRcpComposite(Composite parent,int style) {
		super(parent, style);
		
		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.horizontalAlignment = SWT.FILL;
		this.setLayoutData(contentGridData);
		setLayout(new GridLayout(1, false));
		

		// Create a horizontal separator
		Label separator = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		autoBuild = new Button(this, SWT.CHECK);
		autoBuild.setText("Autobuild project");
		autoBuild.setSelection(true);
	
	}
	

	public Map<String,String> getInput(){
		Map<String,String>  options = new HashMap<String,String>();
		
		
		options.put("autobuild", Boolean.toString(autoBuild.getSelection()));
		
		return options;
	}
	
	
	public boolean isDisposed(){
		return super.isDisposed();
	}


}
