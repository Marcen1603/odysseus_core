package de.offis.client.iconsgrid;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import de.offis.client.iconsgrid.IconGridWidget.IconGridItem;
import de.offis.client.res.RichUiIcons;

public class IconGridCategory<T> extends Composite implements ClickHandler {
	private String category;		
	private FlowPanel itemContainer = new FlowPanel();
	private FlowPanel root = new FlowPanel();
	
	private HorizontalPanel labelContainer = new HorizontalPanel();
	private Label label;
	private Image openCloseIcon;
	
	private ArrayList<IconGridItem<T>> items = new ArrayList<IconGridWidget.IconGridItem<T>>();
	
	public IconGridCategory(String category) {
		this.category = category;
		this.label = new Label(this.category);
		this.labelContainer.getElement().getStyle().setPadding(5, Unit.PX);
		this.label.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		this.label.getElement().getStyle().setCursor(Cursor.POINTER);
		this.label.addClickHandler(this);
		
		this.openCloseIcon = new Image(RichUiIcons.INSTANCE.cat_open());
		
		labelContainer.add(openCloseIcon);
		labelContainer.add(label);
		
		labelContainer.setCellVerticalAlignment(openCloseIcon, HasVerticalAlignment.ALIGN_MIDDLE);
		labelContainer.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);
		
		labelContainer.setSpacing(5);
		
		initWidget(root);
		root.setWidth("100%");
		itemContainer.setWidth("100%");
		root.add(labelContainer);
		root.add(itemContainer);
	}
	
	public void addItem(IconGridItem<T> item){
		itemContainer.add(item);
		items.add(item);
	}

	public void insertItem(int index, IconGridItem<T> item) {
		if(items.size() == 0 || index >= items.size()){
			itemContainer.add(item);
		} else {
			itemContainer.insert(item, index);
		}
		
		items.add(item);
	}
	
	public void removeItem(IconGridItem<T> item){
		itemContainer.remove(item);
		items.remove(item);
	}

	@Override
	public void onClick(ClickEvent event) {
		if(itemContainer.isVisible()){
			itemContainer.setVisible(false);
			this.openCloseIcon.setUrl(RichUiIcons.INSTANCE.cat_closed().getSafeUri());
		} else {
			itemContainer.setVisible(true);
			this.openCloseIcon.setUrl(RichUiIcons.INSTANCE.cat_open().getSafeUri());
		}
	}
	
	public ArrayList<IconGridItem<T>> getItems() {
		return new ArrayList<IconGridWidget.IconGridItem<T>>(items);
	}
	
	public String getCategory() {
		return category;
	}
}
