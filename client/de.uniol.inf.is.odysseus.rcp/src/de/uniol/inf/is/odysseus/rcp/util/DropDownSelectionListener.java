package de.uniol.inf.is.odysseus.rcp.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

import java.util.Objects;
import com.google.common.base.Strings;

public class DropDownSelectionListener extends SelectionAdapter {
	
	public static interface ICallback {
		public void itemSelected();
	}
	
	private ToolItem dropdown;
	private Menu menu;

	public DropDownSelectionListener(ToolItem dropdown) {
		this.dropdown = dropdown;
		menu = new Menu(dropdown.getParent().getShell());
		
		this.dropdown.addSelectionListener(this);
	}

	public MenuItem add(String item, ICallback callback) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(item), "item must not be null or empty!");

		return add(item, null, callback);
	}
	
	public MenuItem add(Image image, ICallback callback) {
		Objects.requireNonNull(image, "image must not be null!");

		return add(null, image, callback);
	}
	
	public MenuItem add(String item, Image image, ICallback callback) {
		Objects.requireNonNull(callback, "callback must not be null!");

		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				callback.itemSelected();
			}
		});
		
		if( !Strings.isNullOrEmpty(item)) {
			menuItem.setText(item);
		}
		if( image != null ) {
			menuItem.setImage(image);
		}
		
		return menuItem;
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
