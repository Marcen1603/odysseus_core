package de.offis.gui.client.widgets;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.offis.client.OperatorGroup;
import de.offis.client.iconsgrid.IconGridWidget;
import de.offis.client.iconsgrid.IconGridWidget.GridIconsWidgetMapping;
import de.offis.client.iconsgrid.ListDataProvider;
import de.offis.client.iconsgrid.SelectionHandler;
import de.offis.gui.client.OperatorsEditor;
import de.offis.gui.client.events.OperatorGroupSavedEvent;
import de.offis.gui.client.res.OperatorsIcons;
import de.offis.gui.client.rpc.OperatorsServiceProxy;
import de.offis.gui.client.util.SuccessCallback;

/**
 * Widget will show a list of operator groups.
 *
 * @author Alexander Funk
 * 
 */
public class OperatorGroupsWidget extends Composite implements OperatorGroupSavedEvent.Handler {	

    private IconGridWidget<OperatorGroup> operatorGroupGrid;
    private ListDataProvider<OperatorGroup> operatorGroupDataProvider;
	
    private HorizontalPanel buttonPanel;
    
	private VerticalPanel container = new VerticalPanel();
	
	private OperatorsEditor scaiOperatorsEditor;
	
    public OperatorGroupsWidget(EventBus events, OperatorsEditor scaiOperatorsEditor) {
    	this.scaiOperatorsEditor = scaiOperatorsEditor;
    	initOperatorGroupGrid();
    	initButtonPanel();
    	events.addHandler(OperatorGroupSavedEvent.TYPE, this);
    	ScrollPanel scroll = new ScrollPanel(operatorGroupGrid);
    	scroll.setSize("100%", "100%");
    	container.add(scroll);
    	container.add(buttonPanel);
    	container.setCellHeight(buttonPanel, "60px");
    	container.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
    	container.setCellVerticalAlignment(buttonPanel, HasVerticalAlignment.ALIGN_BOTTOM);
    	container.setStyleName("operatorGroupsWidget-container");
    	container.setSize("100%", "100%");
        initWidget(container);
        
        refresh();
    }
    
    private void initButtonPanel(){
    	buttonPanel = new HorizontalPanel();
    	buttonPanel.setSpacing(5);

    	final OperatorButton changeStatus = new OperatorButton(OperatorsIcons.INSTANCE.bullet_red_green(), "Change Status of selected OperatorGroup", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				OperatorGroup selected = operatorGroupGrid.getSelectedItem();
				if(selected != null){
					changeOperatorGroupStatus(selected);
				}
			}
		});
    	
    	final OperatorButton load = new OperatorButton(OperatorsIcons.INSTANCE.box_open(), "Load selected OperatorGroup", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				OperatorGroup selected = operatorGroupGrid.getSelectedItem();
				if(selected != null){
					scaiOperatorsEditor.loadProject(selected.getName());
				}
			}
		});
    	
    	final OperatorButton remove = new OperatorButton(OperatorsIcons.INSTANCE.package_delete(), "Remove selected OperatorGroup", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				OperatorGroup selected = operatorGroupGrid.getSelectedItem();
				if(selected != null){
					removeOperatorGroup(selected);
				}
			}
		});
    	
    	buttonPanel.add(load);
    	buttonPanel.add(changeStatus);
    	buttonPanel.add(remove);
    	
    	load.setEnabled(false);
    	changeStatus.setEnabled(false);
    	remove.setEnabled(false);
    	
    	operatorGroupGrid.addSelectionHandler(new SelectionHandler<OperatorGroup>() {
			
			@Override
			public void onItemSelected(OperatorGroup item) {
				load.setEnabled(true);
				changeStatus.setEnabled(true);
				remove.setEnabled(true);
			}

			@Override
			public void onItemDeselected(OperatorGroup item) {
				load.setEnabled(false);
				changeStatus.setEnabled(false);
				remove.setEnabled(false);
			}
		});
    }
    
    private void initOperatorGroupGrid(){
		operatorGroupDataProvider = new ListDataProvider<OperatorGroup>();
		GridIconsWidgetMapping<OperatorGroup> dataMapping = new GridIconsWidgetMapping<OperatorGroup>() {

			@Override
			public String getCategory(OperatorGroup item) {
				return "OperatorGroups";
			}

			@Override
			public String getName(OperatorGroup item) {
				return item.getName();
			}

			@Override
			public Image getIcon(OperatorGroup item) {
				if(item.isRunning()){
					return new Image(OperatorsIcons.INSTANCE.packagegreen());
				} else {
					return new Image(OperatorsIcons.INSTANCE.packagered());
				}
			}
		};
		operatorGroupGrid = new IconGridWidget<OperatorGroup>(operatorGroupDataProvider, dataMapping);
		operatorGroupGrid.setStyleName("operatorGroupGrid");
	}
    
    private void refresh(){
    	OperatorsServiceProxy.get().listAllOperatorGroups(new SuccessCallback<List<OperatorGroup>>() {

			@Override
			public void onSuccess(List<OperatorGroup> result) {
		        operatorGroupDataProvider.clear();		        
		        operatorGroupDataProvider.addAll(result);
			}
		});
    }

    private void removeOperatorGroup(OperatorGroup object){
    	OperatorsServiceProxy.get().removeOperatorGroup(object.getName(), new RefreshCallback<Void>());
    }
    
    private void changeOperatorGroupStatus(OperatorGroup object){
    	if(object.isRunning()){
    		OperatorsServiceProxy.get().undeployOperatorGroup(object.getName(), new RefreshCallback<Void>());
    	} else {
    		OperatorsServiceProxy.get().deployOperatorGroup(object.getName(), new RefreshCallback<Void>());
    	}    	
    }

	@Override
	public void onOperatorGroupSaved(OperatorGroupSavedEvent e) {
		refresh();
	}
	
	private class RefreshCallback<T> extends SuccessCallback<T> {

		@Override
		public void onSuccess(T result) {
			refresh();
		}		
	}
}
