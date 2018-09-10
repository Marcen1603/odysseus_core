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

public abstract class AbstractKeyValueProvider<T extends IPhysicalOperator> extends AbstractOperatorDetailProvider<T> {

	private final List<Font> createdFonts = Lists.newArrayList();
	
	private Composite composite;
	private Composite parent;
	private T operator;

	@Override
	public void createPartControl(Composite parent, T operator) {
		this.parent = parent;
		this.operator = operator;
		
		composite = createContent(parent, operator);
		parent.layout();
	}
	
	public final void refresh() {
		disposeFonts();
		disposeComposite();
		
		if( !parent.isDisposed() ) {
			composite = createContent(parent, operator);
			parent.layout();
		}
	}
	
	@Override
	public void dispose() {
		disposeFonts();
		disposeComposite();
	}

	protected abstract Map<String, String> getKeyValuePairs(T operator);

	private void disposeComposite() {
		if( !composite.isDisposed() ) {
			composite.dispose();
		}
	}

	private void disposeFonts() {
		for (Font font : createdFonts) {
			if( !font.isDisposed() ) {
				font.dispose();
			}
		}
		createdFonts.clear();
	}
	
	private Composite createContent(Composite parent, T operator) {
		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		composite.setLayout(new GridLayout(2, false));
		
		Map<String, String> map = getKeyValuePairs(operator);
		
		for( String key : map.keySet() ){
			addKeyValuePair(composite, key, map.get(key));
		}
		return composite;
	}
	
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
