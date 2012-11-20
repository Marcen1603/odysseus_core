package de.offis.client.iconsgrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IconGridWidget<T> extends Composite implements UsesDataProvider<T> {
	
	public interface GridIconsWidgetMapping<T> {
		String getCategory(T item);
		String getName(T item);
		Image getIcon(T item);
		
	}
	
	public static class IconGridItem<T> extends Composite implements ClickHandler {
		private IconGridWidget<T> parent;
		private String text;
		private String category;
		private VerticalPanel vp = new VerticalPanel();
		
		private Image image;
		private Label label;
		private T userObject;
		
		private boolean selected = false;
		
		private static final int MAX_CHARS_PER_LINE_LABEL = 7;
		
		public IconGridItem(IconGridWidget<T> parent, String category, String text, Image icon, T userObject) {
			this.parent = parent;
			this.category = category;
			this.text = text;
			this.userObject = userObject;			
			this.image = icon;
			this.label = generateLabel(text);
						
			SimplePanel sp = new SimplePanel(vp);
			sp.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
			sp.getElement().getStyle().setMargin(1, Unit.PX);
			initWidget(sp);

			vp.add(image);
			vp.setCellHorizontalAlignment(image, HasHorizontalAlignment.ALIGN_CENTER);
			vp.setCellVerticalAlignment(image, HasVerticalAlignment.ALIGN_MIDDLE);
			vp.setCellHeight(image, "34px");
			
			vp.add(label);
			vp.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER);
			vp.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_TOP);
			
			vp.setTitle(text);			
			vp.setSize("64px", "64px");

			vp.getElement().getStyle().setProperty("borderRadius", "5px");
			vp.getElement().getStyle().setProperty("padding", "3px");
			
			vp.addDomHandler(this, ClickEvent.getType());
			
			vp.getElement().getStyle().setCursor(Cursor.POINTER);			
		}
		
		private Label generateLabel(String text){
			String text2 = text;
			
			if(text.length() > MAX_CHARS_PER_LINE_LABEL * 2){
				text2 = text.substring(0, (MAX_CHARS_PER_LINE_LABEL*2)-1) + "...";
			}
			
			Label bla = new Label(text2);
			bla.setTitle(this.text);
			bla.setWordWrap(true);
			bla.getElement().getStyle().setProperty("wordWrap", "break-word");
			bla.setSize("64px", "36px");
			return bla;
		}
		
		public String getText() {
			return text;
		}
		
		public Image getIcon() {
			return image;
		}

		public void select(boolean select){
			if(select){
				vp.getElement().getStyle().setBackgroundColor("orange");
				selected = true;
				parent.informSelect(this);
			} else {
				vp.getElement().getStyle().clearBackgroundColor();
				selected = false;
				parent.informDeselect(this);
			}
		}
		
		@Override
		public void onClick(ClickEvent event) {
			if(selected){
				select(false);
			} else {
				select(true);
			}
		}
		
		public T getUserObject() {
			return userObject;
		}
		
		public String getCategory() {
			return category;
		}
	}
	
	
	
	private VerticalPanel root = new VerticalPanel();
	private HashMap<String, IconGridCategory<T>> categories = new HashMap<String, IconGridCategory<T>>();
	private IconGridItem<T> selected = null;
	
	private DataProvider<T> dataProvider;
	private GridIconsWidgetMapping<T> dataMapping;
	

	
	public IconGridWidget(final DataProvider<T> dataProvider, GridIconsWidgetMapping<T> dataMapping) {
		this.dataProvider = dataProvider;
		this.dataMapping = dataMapping;
		
		this.dataProvider.registerChangeHandler(this);
		
		SimplePanel sp = new SimplePanel(root);
		initWidget(sp);
		root.setWidth("100%");
		root.getElement().getStyle().setBackgroundColor("white");
	}

	public void addCategory(String category){
		if(!categories.containsKey(category)){
			IconGridCategory<T> cat = new IconGridCategory<T>(category);
			root.add(cat);
			categories.put(category, cat);
			
			// sort
			if(this.categoryComparator != null){
				ArrayList<IconGridCategory<T>> temp = new ArrayList<IconGridCategory<T>>(categories.values()); 
				Collections.sort(temp, this.categoryComparator);
				
				for(IconGridCategory<T> t : temp){
					root.add(t);
				}
			}
		}		
	}
	
//	public void insertCategory(int index, String category){
//		if(categories.containsKey(category)){
//			// category already there, just move it
//			
//		} else if (!categories.containsKey(category)){
//			if(items.size() == 0 || index >= items.size()){
//				addCategory(category);
//			} else {
//				root.insert(item, index);
//			}
//		}
//		
//		
//		items.add(item);
//	}
	
	public void removeCategory(String category){		
		if(categories.containsKey(category)){
			deselect();
			categories.get(category).removeFromParent();
			categories.remove(category);
		}
	}
	
	private Comparator<IconGridCategory<T>> categoryComparator = null;
	
	
	public void setCategorySorter(Comparator<IconGridCategory<T>> categoryComparator){
		this.categoryComparator = categoryComparator;
	}
	
	public void addItem(String category, String text, Image icon, T userObject){
		IconGridItem<T> item = new IconGridItem<T>(this, category, text, icon, userObject);
		addCategory(item.getCategory());
		if(categories.containsKey(item.getCategory())){
			categories.get(item.getCategory()).addItem(item);
		}
	}
	
	public void insertItem(int index, String category, String text, Image icon, T userObject){
		IconGridItem<T> item = new IconGridItem<T>(this, category, text, icon, userObject);
		addCategory(item.getCategory());
		if(categories.containsKey(item.getCategory())){
			categories.get(item.getCategory()).insertItem(index, item);
		}
	}
	
	public T getSelectedItem(){
		if(selected == null){
			return null;
		}
		
		return selected.getUserObject();
	}	
	
	public void deselect(){
		for(IconGridItem<T> it : getItems()){
			it.select(false);			
		}
	}
	
	private void informDeselect(IconGridItem<T> gridIconItem) {
		selected = null;
		fireDeselectionHandlers();
	}
	
	private void informSelect(IconGridItem<T> item){	
		for(IconGridItem<T> it : getItems()){
			if(!item.equals(it)){
				it.select(false);
			}
		}
		
		selected = item;
		
		fireSelectionHandlers();
	}
	
	public ArrayList<IconGridItem<T>> getItems(){
		ArrayList<IconGridItem<T>> items = new ArrayList<IconGridWidget.IconGridItem<T>>();
		
		for(IconGridCategory<T> cat : categories.values()){
			items.addAll(cat.getItems());
		}
		
		return items;
	}
	
	private ArrayList<SelectionHandler<T>> selectionHandlers = new ArrayList<SelectionHandler<T>>();
	
	public void addSelectionHandler(SelectionHandler<T> handler){
		selectionHandlers.add(handler);
	}
	
	private void fireDeselectionHandlers(){
		for(SelectionHandler<T> handler : selectionHandlers){
			handler.onItemDeselected(getSelectedItem());
		}
	}
	
	private void fireSelectionHandlers(){
		for(SelectionHandler<T> handler : selectionHandlers){
			handler.onItemSelected(getSelectedItem());
		}
	}

	@Override
	public void onDataProviderAction(DataChangeEvent<T> event) {
		switch(event.getType()){
		case DataChangeEvent.ADD:
			T item = event.getChangedObject();
			String category = this.dataMapping.getCategory(item);
			String text = this.dataMapping.getName(item);
			Image icon = this.dataMapping.getIcon(item);
			
			insertItem(event.getListIndex(), category, text, icon, item);
			break;
		case DataChangeEvent.REMOVE:
			// TODO 
			break;
		case DataChangeEvent.ADD_ALL:
			HashMap<Integer, T> data = event.getItems();
			ArrayList<Integer> bla = new ArrayList<Integer>(data.keySet());
			Collections.sort(bla);
			
			for(Integer index : bla){
				T it = data.get(index);
				
				String category2 = this.dataMapping.getCategory(it);
				String text2 = this.dataMapping.getName(it);
				Image icon2 = this.dataMapping.getIcon(it);
				
				insertItem(index, category2, text2, icon2, it);
			}
			break;
		case DataChangeEvent.CLEAR:
			ArrayList<String> keyset = new ArrayList<String>(categories.keySet());
			for(String key : keyset){
				removeCategory(key);
			}
			break;
		case DataChangeEvent.FORCE_UPDATE:
			// TODO alles grafische loeschen und neu einfuegen
			break;
		}
	}
}
