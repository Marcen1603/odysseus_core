package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
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

	public static Spinner createSpinner(Composite topComposite, int minValue, int maxValue) {
		Spinner spinner = new Spinner(topComposite, SWT.BORDER);
		spinner.setMinimum(minValue);
		spinner.setMaximum(maxValue);
		spinner.setIncrement(1);
		return spinner;
	}

	public static Combo createCombo(Composite topComposite, String[] items) {
		Combo combo = new Combo(topComposite, SWT.DROP_DOWN);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (items != null) {
			combo.setItems(items);
		}
		return combo;
	}

	public static Combo createCombo(Composite topComposite, String[] items, String selectedItem) {
		Combo combo = new Combo(topComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (items != null) {
			combo.setItems(items);
			if (selectedItem != null) {
				int index = indexOf(items, selectedItem);
				if (index != -1) {
					combo.select(index);
				}
			}
		}
		return combo;
	}

	private static int indexOf(String[] items, String selectedItem) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].equalsIgnoreCase(selectedItem)) {
				return i;
			}
		}
		return -1;
	}

	public static Combo createAttributeDropDown(Composite comp, Collection<SDFAttribute> attributes) {
		String[] attributeNames = determineAttributeNames(attributes);
		return createCombo(comp, attributeNames);
	}

	private static String[] determineAttributeNames(Collection<SDFAttribute> attributes) {
		if (attributes == null || attributes.isEmpty()) {
			return new String[0];
		}

		String[] names = new String[attributes.size()];
		SDFAttribute[] attrArray = attributes.toArray(new SDFAttribute[0]);
		for (int i = 0; i < attributes.size(); i++) {
			names[i] = attrArray[i].getAttributeName();
		}
		return names;
	}

	public static Combo createAttributeDropDown(Composite comp, SDFSchema schema) {
		return createAttributeDropDown(comp, schema.getAttributes());
	}

	public static Button createCheckBox(Composite topComposite, String text, boolean selected) {
		Button box = new Button(topComposite, SWT.CHECK);
		box.setText(text != null ? text : "");
		box.setSelection(selected);
		return box;
	}

	public static List<Button> createMultiCheckBox(Composite comp, String title, List<String> items) {
		Group group = new Group(comp, SWT.NONE);
		group.setText(title != null ? title : "");

		// FIXME: check boxes (buttons) are not in the group but below

		List<Button> buttons = Lists.newArrayList();
		for (String item : items) {
			buttons.add(createCheckBox(comp, item, false));
		}
		return buttons;
	}

	/**
	 * Creates a list control which allows to select multiple values.
	 * 
	 * @param comp
	 *            the parent control
	 * @param values
	 *            the values of the list
	 * @param selection
	 * @return the list
	 */
	private static org.eclipse.swt.widgets.List createMultiSelectList(Composite comp, ArrayList<String> values,
			String[] selection) {
		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(comp,
				SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		int index = 0;
		for (String s : values) {
			list.add(s);
			if (selection != null && selection.length > 0) {
				for (String sel : selection) {
					if (s.equals(sel)) {
						list.select(index);
					}
				}
			}
			++index;
		}
		if (selection == null || selection.length == 0) {
			list.selectAll();
		}
		return list;
	}

	public static org.eclipse.swt.widgets.List createAttributeMultiSelector(Composite comp,
			Collection<SDFAttribute> attributes, String[] selection) {
		// return createMultiCheckBox(comp, title,
		// Lists.newArrayList(determineAttributeNames(attributes)));
		return createMultiSelectList(comp, Lists.newArrayList(determineAttributeNames(attributes)), selection);
	}

	public static org.eclipse.swt.widgets.List createAttributeMultiSelector(Composite comp, SDFSchema schema,
			String[] selection) {
		return createAttributeMultiSelector(comp, schema.getAttributes(), selection);
	}

	public static void updateAttributeMultiSelector(org.eclipse.swt.widgets.List list,
			Collection<SDFAttribute> attributes) {
		updateMultiSelectList(list, Lists.newArrayList(determineAttributeNames(attributes)));
	}

	public static void updateAttributeMultiSelector(org.eclipse.swt.widgets.List list, SDFSchema schema) {
		updateAttributeMultiSelector(list, schema.getAttributes());
	}

	private static void updateMultiSelectList(org.eclipse.swt.widgets.List list, ArrayList<String> values) {
		list.removeAll();
		for (String s : values) {
			list.add(s);
		}
		list.selectAll();
	}

	public static Button createRadioButton(Composite comp, String title) {
		Button radio = new Button(comp, SWT.RADIO);
		radio.setText(title);
		return radio;
	}
}
