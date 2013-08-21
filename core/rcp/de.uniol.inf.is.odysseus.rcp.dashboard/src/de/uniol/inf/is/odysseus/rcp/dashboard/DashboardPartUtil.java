package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public final class DashboardPartUtil {

	private DashboardPartUtil() {
		// do not instance this
	}
	
	// only static methods allowed here

	public static Label createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text != null ? text : "");
		return label;
	}	

	public static Text createText(Composite topComposite, String txt) {
		Text text = new Text(topComposite, SWT.BORDER);
		text.setText(txt != null ? txt : "");
		return text;
	}
	
	public static Combo createCombo(Composite topComposite, String[] items){
		Combo combo = new Combo(topComposite, SWT.DROP_DOWN);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.setItems(items);
		return combo;
	}
	
	public static Combo createAttributeDropDown(Composite comp, Collection<SDFAttribute> attributes ) {
		String[] attributeNames = determineAttributeNames(attributes);
		return createCombo(comp, attributeNames);
	}
	
	private static String[] determineAttributeNames(Collection<SDFAttribute> attributes) {
		if( attributes == null || attributes.isEmpty() ) {
			return new String[0];
		}
		
		String[] names = new String[attributes.size()];
		SDFAttribute[] attrArray = attributes.toArray(new SDFAttribute[0]);
		for( int i = 0; i < attributes.size(); i++ ) {
			names[i] = attrArray[i].getAttributeName();
		}
		return names;
	}

	public static Combo createAttributeDropDown(Composite comp, SDFSchema schema ) {
		return createAttributeDropDown(comp, schema.getAttributes());
	}
	
	public static Button createCheckBox(Composite topComposite, String text, boolean selected) {
		Button box = new Button(topComposite, SWT.CHECK);
		box.setText(text != null ? text : "");
		box.setSelection(selected);
		return box;
	}
	
	public static List<Button> createMultiCheckBox( Composite comp, String title, List<String> items ) {
		Group group = new Group(comp, SWT.NONE);
		group.setText(title != null ? title : "");
		
		List<Button> buttons = Lists.newArrayList();
		for( String item : items ) {
			buttons.add(createCheckBox(comp, item, false));
		}
		return buttons;
	}
	
	public static List<Button> createAttributeMultiSelector( Composite comp, String title, Collection<SDFAttribute> attributes ) {
		return createMultiCheckBox(comp, title, Lists.newArrayList(determineAttributeNames(attributes)));
	}
	
	public static List<Button> createAttributeMultiSelector( Composite comp, String title, SDFSchema schema ) {
		return createAttributeMultiSelector(comp, title, schema.getAttributes());
	}
}
