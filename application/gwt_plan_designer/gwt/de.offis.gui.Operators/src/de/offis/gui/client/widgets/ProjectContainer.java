package de.offis.gui.client.widgets;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;

import de.offis.gui.client.MessageConstants;
import de.offis.gui.client.Operators;
import de.offis.gui.client.events.OperatorGroupClosedEvent;
import de.offis.gui.client.util.CompositeEvents;

/**
 * Contains the open project and will display welcomeproject if no project is open.
 *
 * @author Alexander Funk
 * 
 */
public class ProjectContainer extends CompositeEvents {
	private SimplePanel panel;
	private WelcomeProject welcomeProject = new WelcomeProject();
	private SvgProjectPanel openProject;
	
	public ProjectContainer(EventBus events) {
		super(events);
		panel = new SimplePanel(welcomeProject);
    	initWidget(panel);
    	setStylePrimaryName("so-project-container");
	}
	
	public boolean closeProject(){		
		if(openProject != null && !openProject.onProjectClose() && !Window.confirm(MessageConstants.REALLY_CLOSE_PROJECT)){
			return false;
		}
		
		if(welcomeProject == null){
			welcomeProject = new WelcomeProject();
		}
		
		panel.setWidget(welcomeProject);
		
		Operators.resetHeaderButtons();
		events.fireEvent(new OperatorGroupClosedEvent());
		return true;
	}

	public void add(SvgProjectPanel project, String name) {
		Operators.resetHeaderButtons();
		
		// remove welcomeProject
    	if(welcomeProject != null){
    		welcomeProject.removeFromParent();
    		welcomeProject = null;
    	}
		
    	panel.setWidget(project);
    	this.openProject = project;
    	
    	for(OperatorButton b : this.openProject.getTitleBarButtons()){        	
        	Operators.addTemporaryHeaderButton(b);
    	}
	}
	
	public void onResize(){
		if(openProject != null){
			openProject.onResize();
		}
	}
}
