package de.offis.gui.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.logging.client.DevelopmentModeLogHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.RootPanel;

import de.offis.gui.client.rpc.OperatorsServiceProxy;
import de.offis.gui.client.util.BackgroundImageUtil;
import de.offis.gui.client.util.SuccessCallback;
import de.offis.gui.client.widgets.WaitInfoWidget;
import de.offis.gui.shared.InputModuleModel;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gui.shared.OutputModuleModel;
import de.offis.gui.shared.ScaiOperatorsData;
import de.offis.gwtsvgeditor.client.Keyboard;
import de.offis.scai.GwtScai;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Operators implements EntryPoint {
    public static GwtScai cache;
    public static final String SCAI_SERVLET_PATTERN = "de.offis.gui.Operators/wiring";
    public static boolean BROWSER_SUPPORTS_SVG = false;
    public static String DOMAIN_FROM_FIRST_DROPPED_SENSOR = null;
    public static boolean blockDelete = false;

    private EventBus events = new SimpleEventBus();
    public static final BackgroundImageUtil BACKGROUND_IMAGES = new BackgroundImageUtil();
    
    private static OperatorsEditor editor;
    
    public void onModuleLoad() {
    	cache = new GwtScai(SCAI_SERVLET_PATTERN);
    	
    	testForSVG();               
        new Keyboard(events);        
        initLoggers();
        
        editor = new OperatorsEditor(events);
        RootPanel.get().add(editor);
        
        refreshCache();
    }
    
    public static void addTemporaryHeaderButton(ButtonBase button){
    	editor.addTemporaryHeaderButton(button);
    }
    
    public static void resetHeaderButtons(){
    	editor.resetHeaderButtons();
    }
    
    private void initLoggers(){
    	Logger.getLogger("ScaiOperators.OperatorModuleSvg").addHandler(new DevelopmentModeLogHandler());
        Logger.getLogger("ScaiOperators.OutputModuleSvg").addHandler(new DevelopmentModeLogHandler());
        Logger.getLogger("ScaiOperators").addHandler(new DevelopmentModeLogHandler());
    }
    
    public static void refreshCache(){
    	refreshCache(null);
    }
    
    public static void refreshCache(final Command successCmd){
    	if(ongoingRefresh){
    		return;
    	} else {
    		ongoingRefresh = true;
    	}
    	
    	OperatorsServiceProxy.get().loadScaiOperatorsData(
				new SuccessCallback<ScaiOperatorsData>() {
					@Override
					public void onSuccess(ScaiOperatorsData result) {
						scaiData = result;
						
						if(successCmd != null)
							successCmd.execute();
						
						for(AsyncCallback<ScaiOperatorsData> callback : cmdsOnCacheRefresh0){
							callback.onSuccess(scaiData);
						}
						
						cmdsOnCacheRefresh0.clear();
						
						ongoingRefresh = false;
					}
		});
    }
    
    private void testForSVG(){
    	String agent = Window.Navigator.getUserAgent();

        if (agent.contains("chrome") || agent.contains("safari") || agent.contains("webkit") || agent.contains("Mozilla/5.0")) {
            BROWSER_SUPPORTS_SVG = true;
        } else {
        	BROWSER_SUPPORTS_SVG = false;
        }
    }
    
    private static ScaiOperatorsData scaiData = null;
    private static ArrayList<AsyncCallback<ScaiOperatorsData>> cmdsOnCacheRefresh0 = new ArrayList<AsyncCallback<ScaiOperatorsData>>();
    
    private static boolean ongoingRefresh = false;
    
    public static void getScaiOperatorsData(final AsyncCallback<ScaiOperatorsData> callback){
    	if (scaiData != null && !ongoingRefresh) {
			callback.onSuccess(scaiData);
		} else {
			cmdsOnCacheRefresh0.add(callback);
			
			if(!ongoingRefresh){
				refreshCache();
			}
		}
    }
    
    public static void getOperatorsWithoutCache( final AsyncCallback<List<OperatorModuleModel>> callback ) {    			
    	refreshCache(new Command() {
			
			@Override
			public void execute() {
				callback.onSuccess(scaiData.getOperators());
			}
		});
	}
    
	public static void getSensorsWithoutCache(final AsyncCallback<List<InputModuleModel>> callback) {
		refreshCache(new Command() {
			
			@Override
			public void execute() {
				callback.onSuccess(scaiData.getInputs());
			}
		});
	}

	public static void getOutputsWithoutCache(final AsyncCallback<List<OutputModuleModel>> callback) {
		refreshCache(new Command() {
			
			@Override
			public void execute() {
				callback.onSuccess(scaiData.getOutputs());
			}
		});
	}
    
    public static InputModuleModel getSensor(final String domain, final String name){
    	ArrayList<InputModuleModel> sensors = scaiData.getInputs();
    	if(sensors != null){
    		for(InputModuleModel m : sensors){
    			if(m.getName().equals(name) && m.getDomain().equals(domain)){
    				return m;
    			}
    		}
    	}
    	
    	return null;
    }
    
    public static OperatorModuleModel getOperator(final String operatorType){
    	ArrayList<OperatorModuleModel> operators = scaiData.getOperators();
    	if(operators != null){
    		for(OperatorModuleModel m : operators){
    			if(m.getOperatorType().equals(operatorType)){
    				return m;
    			}
    		}
    	}
    	
    	return null;
    }

	public static OutputModuleModel getOutput(final String name) {
		ArrayList<OutputModuleModel> outputs = scaiData.getOutputs();
		if(outputs != null){
    		for(OutputModuleModel m : outputs){
    			if(m.getName().equals(name)){
    				return m;
    			}
    		}
    	}
    	
    	return null;
	}
	
	private static WaitInfoWidget waitPanel = new WaitInfoWidget();
	
	public static void hideLoadingPanel(String text){
		if(waitPanel != null){
			waitPanel.hide(text);
		}
	}
	
	public static void showLoadingPanel(String text){
		if(waitPanel != null){
			waitPanel.show(text);
		}
	}
}
