package de.uniol.inf.is.odysseus.rcp.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

class DropdownSelectionListener extends SelectionAdapter {
	
	public static interface ICallback {
		public void itemSelected();
	}
	
	private ToolItem dropdown;
	private Menu menu;

	public DropdownSelectionListener(ToolItem dropdown) {
		this.dropdown = dropdown;
		menu = new Menu(dropdown.getParent().getShell());
	}

	public void add(String item, ICallback callback) {
		Preconditions.checkNotNull(callback, "callback must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(item), "item must not be null or empty!");

		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(item);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				callback.itemSelected();
			}
		});
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		if (event.detail == SWT.ARROW) {
			ToolItem item = (ToolItem) event.widget;
			Rectangle rect = item.getBounds();
			Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
			menu.setLocation(pt.x, pt.y + rect.height);
			menu.setVisible(true);
		} else {
			System.out.println(dropdown.getText() + " Pressed");
		}
	}
}
