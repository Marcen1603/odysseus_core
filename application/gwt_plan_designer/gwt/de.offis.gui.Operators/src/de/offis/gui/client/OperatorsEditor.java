package de.offis.gui.client;

import java.util.Comparator;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import de.offis.client.ScaiLink;
import de.offis.client.iconsgrid.IconGridCategory;
import de.offis.client.iconsgrid.IconGridWidget;
import de.offis.client.iconsgrid.IconGridWidget.GridIconsWidgetMapping;
import de.offis.client.iconsgrid.ListDataProvider;
import de.offis.gui.client.events.OperatorGroupCreatedEvent;
import de.offis.gui.client.events.OperatorGroupLoadedEvent;
import de.offis.gui.client.events.OperatorGroupSavedEvent;
import de.offis.gui.client.module.AbstractModuleModel;
import de.offis.gui.client.module.inputs.AbstractInputSvg;
import de.offis.gui.client.module.operators.AbstractOperatorSvg;
import de.offis.gui.client.module.outputs.AbstractOutputSvg;
import de.offis.gui.client.res.OperatorsIcons;
import de.offis.gui.client.rpc.OperatorsServiceProxy;
import de.offis.gui.client.util.CustomResizeHandler;
import de.offis.gui.client.util.SuccessCallback;
import de.offis.gui.client.widgets.CustomSplitLayoutPanel;
import de.offis.gui.client.widgets.HeaderBar;
import de.offis.gui.client.widgets.OperatorGroupsWidget;
import de.offis.gui.client.widgets.ProjectContainer;
import de.offis.gui.client.widgets.OperatorButton;
import de.offis.gui.client.widgets.SvgProjectPanel;
import de.offis.gui.shared.InputModuleModel;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gui.shared.OutputModuleModel;
import de.offis.gui.shared.ScaiLoadedOperatorGroup;
import de.offis.gui.shared.ScaiOperatorsData;
import de.offis.gwtsvgeditor.client.svg.SvgModule;

/**
 * Main widget which is shown to the user.
 *
 * @author Alexander Funk
 * 
 */
public class OperatorsEditor extends Composite implements OperatorGroupSavedEvent.Handler, ResizeHandler {

	private static OperatorsEditorUiBinder uiBinder = GWT
			.create(OperatorsEditorUiBinder.class);

	interface OperatorsEditorUiBinder extends
			UiBinder<Widget, OperatorsEditor> {
	}

	@UiField
	HTMLPanel html;
	
	private static final int EAST_DEFAULT_SIZE = 270;
    private static final int WEST_DEFAULT_SIZE = 270;
    private static final int SPLITTER_DEFAULT_SIZE = 9;

    private CustomSplitLayoutPanel main;
    
    private IconGridWidget<AbstractModuleModel> moduleGrid;
    
    private OperatorGroupsWidget opgroups;
    private ProjectContainer project;
    
    private EventBus events;
    
    private HeaderBar headerbar;

    private ListDataProvider<AbstractModuleModel> moduleDataProvider;
    
	public OperatorsEditor(final EventBus events) {
		initWidget(uiBinder.createAndBindUi(this));
		setSize("100%", "100%");
		
		project  = new ProjectContainer(events);
		
		this.events = events;
    	this.events.addHandler(OperatorGroupSavedEvent.TYPE, this);

    	initHeader();
    	
        main = new CustomSplitLayoutPanel(SPLITTER_DEFAULT_SIZE);
        main.getElement().getStyle().setMargin(0, Unit.PX);
        main.setSize("100%", "100%");

        opgroups = new OperatorGroupsWidget(events, this);

        initModuleGrid();
        
        main.addWest(moduleGrid, WEST_DEFAULT_SIZE);   
        main.addEast(opgroups, EAST_DEFAULT_SIZE);        
        
        main.add(project);
        
        main.addResizeHandler(new CustomResizeHandler() {
			
			@Override
			public void onResize() {
				project.onResize();
			}
		});    
        
        html.add(headerbar, "header");
        html.add(main, "mainArea");
        
        Window.addResizeHandler(this);        
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {				
				onResize(null);
				project.onResize();
			}
		});
        
        update();
	}
	
	private void initModuleGrid(){
		moduleDataProvider = new ListDataProvider<AbstractModuleModel>();
		moduleDataProvider.setDataSorter(new Comparator<AbstractModuleModel>() {
			
			@Override
			public int compare(AbstractModuleModel o1, AbstractModuleModel o2) {				
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
        GridIconsWidgetMapping<AbstractModuleModel> dataMapping = new GridIconsWidgetMapping<AbstractModuleModel>() {

			@Override
			public String getCategory(AbstractModuleModel item) {
				if(item instanceof InputModuleModel){
					return "Sensors";
				} else if(item instanceof OutputModuleModel){
					return "Outputs";
				} else {
					return "Operators";
				}
			}

			@Override
			public String getName(AbstractModuleModel item) {
				return item.getName();
			}

			@Override
			public Image getIcon(AbstractModuleModel item) {
				if(item instanceof InputModuleModel){
					InputModuleModel m = (InputModuleModel) item;
					Image img = new Image(Operators.BACKGROUND_IMAGES.getUrlForSensorTypeImage(m.getSensorType()));
					img.setPixelSize(32, 32);
					return img;
				} else if(item instanceof OutputModuleModel){
					OutputModuleModel m = (OutputModuleModel) item;
					Image img = new Image(Operators.BACKGROUND_IMAGES.getUrlForOutputImage(m.getName()));
					img.setPixelSize(32, 32);
					return img;
				} else {
					OperatorModuleModel m = (OperatorModuleModel) item;
					Image img = new Image(Operators.BACKGROUND_IMAGES.getUrlForMetaTypeImage(m.getMetaType().toString().toLowerCase()));
					img.setPixelSize(32, 32);
					return img;
				}
			}
		};
		
        moduleGrid = new IconGridWidget<AbstractModuleModel>(moduleDataProvider, dataMapping);
        moduleGrid.setStyleName("itemsTreeWidget");
        moduleGrid.setCategorySorter(new Comparator<IconGridCategory<AbstractModuleModel>>() {

			@Override
			public int compare(IconGridCategory<AbstractModuleModel> o1, IconGridCategory<AbstractModuleModel> o2) {
				if(o1.getCategory() == "Sensors"){
					return -1;
				} else if (o2.getCategory() == "Sensors"){
					return 1;
				} else if (o1.getCategory() == "Outputs") {
					return 1;
				} else if (o2.getCategory() == "Outputs") {
					return -1;
				} else if (o1.getCategory() == "Operators" && o2.getCategory() == "Sensors") {
					return 1;
				} else {
					return -1;
				}
			}
		});
	}
	
	private void initHeader(){
		headerbar = new HeaderBar(events);
    	OperatorButton newProj = new OperatorButton(OperatorsIcons.INSTANCE.chart_organisation_add(), "New Project", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				openNewProject();
			}
		});                
    	OperatorButton closeProj = new OperatorButton(OperatorsIcons.INSTANCE.chart_organisation_delete(), "Close Project", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				closeProject();				
			}
		});
    	headerbar.addPermanentButton(newProj);
        headerbar.addPermanentButton(closeProj);
	}
	
	public boolean closeProject(){
    	return project.closeProject();
    }
    
    public void loadProject(final String op){
    	if(op.equals("") || op == null)
    		return;
    	
    	final SvgProjectPanel project = openNewProject(op);
    	
    	if(project == null){
    		return;
    	}
    	
    	OperatorsServiceProxy.get().loadOperatorGroup(op, new SuccessCallback<ScaiLoadedOperatorGroup>() {
    		public void onSuccess(ScaiLoadedOperatorGroup result) {
    			// retrieve positions from localstorage
				OpsStorage stor = OpsStorage.get();
				project.getSvgEditor().setViewBox(stor.getScreenX(op), stor.getScreenY(op), stor.getScreenWidth(op), stor.getScreenHeight(op));				
				
    			for(AbstractModuleModel m : result.getModels()){
    				double x = stor.getModuleX(op, m.getId());
    				double y = stor.getModuleY(op, m.getId());
					if(m instanceof InputModuleModel){
						InputModuleModel sensor = (InputModuleModel)m;
						InputModuleModel sensorCache = Operators.getSensor(sensor.getDomain(), sensor.getSensor());
						
						sensor.setSensorType(sensorCache.getSensorType());
						sensor.setDataElements(sensorCache.getDataElements());
						
						SvgModule svg = AbstractInputSvg.getInstanceOfSvg(project.getSvgEditor(), sensor, x, y);
						svg.setPreview(false);
						
						project.getSvgEditor().createVisualObject(svg);
					} else if(m instanceof OperatorModuleModel){
						OperatorModuleModel operator = (OperatorModuleModel)m;
						OperatorModuleModel operatorCache = Operators.getOperator(operator.getOperatorType());
						
						operator.setMetaType(operatorCache.getMetaType());
							
						SvgModule svg = AbstractOperatorSvg.getInstanceOfSvg(project.getSvgEditor(), (OperatorModuleModel)m, x, y);
						svg.setPreview(false);
						
						project.getSvgEditor().createVisualObject(svg);
					} else if(m instanceof OutputModuleModel){
						OutputModuleModel output = (OutputModuleModel)m;
						InputModuleModel outputCache = Operators.getSensor(output.getVsSensorDomain(), output.getVsSensorName());
						
						output.setVsSensorType(outputCache.getSensorType());
						
						SvgModule svg = AbstractOutputSvg.getInstanceOfSvg(project.getSvgEditor(), output, x, y);						
												
						svg.setPreview(false);
						
						project.getSvgEditor().createVisualObject(svg);
					}
				}    			
    			
    			// links    			
				for(ScaiLink link : result.getLinks()){
					project.loadLinkHelper(link.getSourceName(), link.getDestinationName());
				}				
				
				if(!stor.hasDataForOperatorGroup(op)){					
					project.arrangeModules();
				}

				events.fireEvent(new OperatorGroupLoadedEvent(op));
    		};
		});
    }
        
    public SvgProjectPanel openNewProject(String name){
    	if(!closeProject()){
    		return null;
    	}
    	
    	SvgProjectPanel panel = new SvgProjectPanel(name, events, moduleGrid);
    	project.add(panel, name);
    	events.fireEvent(new OperatorGroupCreatedEvent(name));
    	return panel;
    }
    
    public SvgProjectPanel openNewProject(){    	
    	String date = DateTimeFormat.getFormat("yyyy-MM-dd-HH:mm:ss").format(new Date());
    	String name = Window.prompt(MessageConstants.ASK_NEW_PROJECTNAME, "Project-[" + date + "]");
    	if(name == null){
    		return null;
    	}
    	return openNewProject(name);
    }
    
    private void update(){    	
    	
    	OperatorsServiceProxy.get().loadScaiOperatorsData(new SuccessCallback<ScaiOperatorsData>() {
			
			@Override
			public void onSuccess(ScaiOperatorsData result) {
				moduleDataProvider.clear();
				
				moduleDataProvider.addAll(result.getInputs());
				moduleDataProvider.addAll(result.getOutputs());
				moduleDataProvider.addAll(result.getOperators());
			}
		});
    }

	@Override
	public void onOperatorGroupSaved(OperatorGroupSavedEvent e) {
		update();
	}

	@Override
	public void onResize(ResizeEvent event) {
		// for browsers that dont support the calc() function from css3    	
    	int h = Window.getClientHeight();
    	DOM.getElementById("mainArea").getStyle().setHeight(h - 62, Unit.PX);
	}
	

    
    public void addTemporaryHeaderButton(ButtonBase button){
    	headerbar.addTemporaryButton(button);
    }
    
    public void resetHeaderButtons(){
    	headerbar.reset();
    }
}
