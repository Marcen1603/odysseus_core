package de.uniol.inf.is.odysseus.rcp;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public abstract class AbstractKeyValueGeneralProvider implements IOperatorGeneralDetailProvider {

	private final List<Font> createdFonts = Lists.newArrayList();

	@Override
	public void createPartControl(Composite parent, IPhysicalOperator operator) {
		Composite comp = new Composite(parent, SWT.BORDER);
		comp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		comp.setLayout(new GridLayout(2, false));
		
		Map<String, String> map = getKeyValuePairs(operator);
		
		for( String key : map.keySet() ){
			addKeyValuePair(comp, key, map.get(key));
		}
	}

	@Override
	public final void dispose() {
		for( Font font : createdFonts ) {
			font.dispose();
		}
		createdFonts.clear();
	}

	protected abstract Map<String, String> getKeyValuePairs(IPhysicalOperator operator);

	private void addKeyValuePair( Composite comp, String key, String value) {
		Label keyLabel = new Label(comp, SWT.NONE);
		keyLabel.setText(Strings.isNullOrEmpty(key) ? "" : key);
		FontData[] fontData = keyLabel.getFont().getFontData();
		fontData[0].setStyle(SWT.BOLD);
		Font newFont = new Font(Display.getCurrent(), fontData[0]);
		keyLabel.setFont(newFont);
		createdFonts.add(newFont); // must dispose it later
		keyLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		Text valueText = new Text(comp, SWT.NONE);
		valueText.setEditable(false);
		valueText.setText(Strings.isNullOrEmpty(value) ? "" : value);
	}

}
