package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import java.util.Set;

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

public class PredicateParameterEditor extends AbstractParameterEditor implements
		IParameterEditor {

	private Combo typeCombo;
	private Label predLabel;
	private Text predText;

	public PredicateParameterEditor() {
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
		
		// Comboliste mit Factorynames der Predicates füllen
		Set<String> builderNames = OperatorBuilderFactory.getPredicateBuilderNames();
		if( !builderNames.isEmpty()) {
			for( String s : builderNames ) {
				typeCombo.add(s);
				// Comboauswahl auf ausgewählten Wert setzen
				if( item != null && s.equals(item.getPredicateType()))
					typeCombo.select(i);
				i++;
			}
			// Den ersten auswählen, wenn noch nichts ausgewählt wurde
			// (z.B. der Logische Operator gerade angelegt wurde)
			if( item == null ) 
				typeCombo.select(0);
			
		} else {
			// Keine PredicateBuilder registriert. Dies als Warnung ausgeben
			typeCombo.add("No predicatetypes registered");
			typeCombo.setEnabled(false);
		}
		
		// Prädikat-label und Eingabeform
		predLabel = new Label(container, SWT.NONE);
		predLabel.setText("Predicate");
		
		predText = new Text(container, SWT.BORDER);
		if( item != null ) 
			predText.setText(item.getPredicate());
		else
			predText.setText("");
		
		predText.setLayoutData( new GridData(GridData.FILL_HORIZONTAL));
		// bei jeder Änderung versuchen es zu speichern
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
