package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.logicaloperator.builder.PredicateItem;
import de.uniol.inf.is.odysseus.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.AbstractParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;

public class PredicateEditor extends AbstractParameterEditor implements
		IParameterEditor {

	private Combo typeCombo;
	private Label predLabel;
	private Text predText;

	public PredicateEditor() {
	}

	@Override
	public void createControl(Composite parent) {
		PredicateParameter parameter = (PredicateParameter)getParameter();
		PredicateItem item = (PredicateItem)parameter.getInputValue();
		
		Composite container = new Composite( parent, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		
		container.setLayout(layout);
		
		Label typeLabel = new Label(container, SWT.NONE);
		typeLabel.setText("Predicatetype");
		
		typeCombo = new Combo(container, SWT.READ_ONLY);
		typeCombo.setLayoutData( new GridData(GridData.FILL_HORIZONTAL));
		int i = 0;
		for( String s : OperatorBuilderFactory.getPredicateBuilderNames() ) {
			typeCombo.add(s);
			if( item != null && s.equals(item.getPredicateType()))
				typeCombo.select(i);
			i++;
		}
		if( item == null ) 
			typeCombo.select(0);
		
		predLabel = new Label(container, SWT.NONE);
		predLabel.setText("Predicate");
		
		predText = new Text(container, SWT.BORDER);
		if( item != null ) 
			predText.setText(item.getPredicate());
		else
			predText.setText("");
		
		predText.setLayoutData( new GridData(GridData.FILL_HORIZONTAL));
		predText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				save();
			}
		});
	}

	@Override
	public void close() {
		save();
	}
	
	private void save() {
		if( predText.getText().length() > 0 ) {
			PredicateItem item = new PredicateItem(typeCombo.getItem(typeCombo.getSelectionIndex()), predText.getText());
			setValue(item);
		} else {
			setValue(null);
		}
	}

}
