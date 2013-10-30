package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.part;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.DashboardGraphicsPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.BackgroundImageLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.GraphicsLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.policy.GraphXYLayoutEditPolicy;

public class GraphicalLayerEditPart extends AbstractGraphicalEditPart implements Observer {
	
	private static Logger LOGGER = LoggerFactory.getLogger(DashboardGraphicsPart.class);
	
	private String imagepath;
	private boolean stretch;

	public GraphicalLayerEditPart(GraphicsLayer group) {
		setModel(group);
		this.imagepath = group.getBackgroundImagePath();
		this.stretch = group.isBackgroundFileStretch();
	}

	protected IFigure createFigure() {
		FreeformLayer layer = new FreeformLayer();		
		if(imagepath!=null){
			File f = new File(imagepath);
			if(f.exists()){
				layer = new BackgroundImageLayer(imagepath, stretch);				
			}else{
				LOGGER.warn("Background image \""+imagepath+"\" not found, using no background!");
			}
		}	
		layer.setLayoutManager(new FreeformLayout());
		return layer;
	}

	protected List<Pictogram> getModelChildren() {
		ArrayList<Pictogram> result = new ArrayList<Pictogram>();
		result.addAll(((GraphicsLayer)getModel()).getPictograms());
		return result;
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new GraphXYLayoutEditPolicy());
	}
	
	public void activate() {
		if (!isActive()) {
			((GraphicsLayer)getModel()).addObserver(this);
			super.activate();
		}
	}
	
	public void deactivate() {
		if (isActive()) {
			((GraphicsLayer)getModel()).deleteObserver(this);
			super.deactivate();
		}
	}
				
	public void update(Observable observable, Object message) {
		refreshChildren();
	}
}
