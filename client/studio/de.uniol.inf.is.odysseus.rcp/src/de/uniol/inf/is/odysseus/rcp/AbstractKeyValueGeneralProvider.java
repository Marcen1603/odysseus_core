package de.uniol.inf.is.odysseus.rcp;

import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public abstract class AbstractKeyValueGeneralProvider implements IOperatorGeneralDetailProvider {

	private final Map<String, Text> textMap = Maps.newHashMap();
	private final Map<String, Label> labelMap = Maps.newHashMap();
	private final Map<String, Font> fontMap = Maps.newHashMap();
	
	private Composite composite;
	private IPhysicalOperator operator;
	
	private Label noInformationLabel;
	
	@Override
	public void createPartControl(Composite parent, IPhysicalOperator operator) {
		this.operator = operator;
		
		composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new GridLayout(2, false));
		
		refresh();
	}
	
	protected final void refresh() {
		if( composite.isDisposed() ) {
			return;
		}
		
		Map<String, String> map = getKeyValuePairs(operator);
		Set<String> oldKeySet = Sets.newHashSet(textMap.keySet());
		
		for( String newKey : map.keySet() ) {
			String newValue = map.get(newKey);
			newValue = !Strings.isNullOrEmpty(newValue) ? newValue : "<no value>"; // text-control does not like null-values
			
			if( textMap.containsKey(newKey)) {
				Text text = textMap.get(newKey);
				String oldValue = text.getText();
				
				if( !oldValue.equals(newValue)) {
					text.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
					text.setText(newValue);
				} else {
					text.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
				}
				
				oldKeySet.remove(newKey);
			} else {
				addKeyValuePair(composite, newKey, newValue); // adds in textMap, labelMap and fontMap-fields
			}
		}
		
		// oldset contains only controls which has no value anymore
		for( String oldKey : oldKeySet ) {
			textMap.remove(oldKey).dispose();
			labelMap.remove(oldKey).dispose();
			fontMap.remove(oldKey).dispose();
		}
		
		if( textMap.isEmpty() && noInformationLabel == null) {
			noInformationLabel = new Label(composite, SWT.NONE);
			noInformationLabel.setText("No information available");
		}
		
		composite.layout();
	}

	@Override
	public void dispose() {
		for( Font font : fontMap.values() ) {
			font.dispose();
		}
		fontMap.clear();
	}

	protected abstract Map<String, String> getKeyValuePairs(IPhysicalOperator operator);

	private void addKeyValuePair( Composite comp, String key, String value) {
		if( comp.isDisposed() ) {
			return;
		}
		
		if( noInformationLabel != null ) {
			noInformationLabel.dispose();
			noInformationLabel = null;
		}
		
		Label keyLabel = new Label(comp, SWT.NONE);
		keyLabel.setText(Strings.isNullOrEmpty(key) ? "" : key);
		FontData[] fontData = keyLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		Font newFont = new Font(Display.getCurrent(), fontData[0]);
		keyLabel.setFont(newFont);
		
		Text valueText = new Text(comp, SWT.BORDER | SWT.READ_ONLY);
		valueText.setEditable(false);
		valueText.setText(Strings.isNullOrEmpty(value) ? "" : value);
		valueText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		textMap.put(key, valueText);
		labelMap.put(key, keyLabel);
		fontMap.put(key, newFont); // must dispose it later
	}

}
