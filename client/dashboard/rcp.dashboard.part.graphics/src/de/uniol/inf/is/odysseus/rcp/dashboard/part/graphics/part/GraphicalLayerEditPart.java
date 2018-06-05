package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.part;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.DashboardGraphicsPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.BackgroundImageLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.GraphicsLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.policy.GraphXYLayoutEditPolicy;

public class GraphicalLayerEditPart extends AbstractGraphicalEditPart implements
		Observer {

	@SuppressWarnings("unused")
	private static Logger LOGGER = LoggerFactory
			.getLogger(DashboardGraphicsPart.class);

	private String imagepath;
	private boolean stretch;

	public GraphicalLayerEditPart(GraphicsLayer group) {
		setModel(group);
		this.imagepath = group.getBackgroundImagePath();
		this.stretch = group.isBackgroundFileStretch();
	}

	@Override
	protected IFigure createFigure() {
		FreeformLayer layer = new FreeformLayer();
		if (imagepath != null) {
			IResource file = ((GraphicsLayer) getModel()).getProject()
					.findMember(imagepath);
			layer = new BackgroundImageLayer(file.getLocation().toOSString(),
					stretch);
		}
		layer.setLayoutManager(new FreeformLayout());
		return layer;
	}

	@Override
	protected List<AbstractPictogram> getModelChildren() {
		ArrayList<AbstractPictogram> result = new ArrayList<AbstractPictogram>();
		result.addAll(((GraphicsLayer) getModel()).getPictograms());
		return result;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new GraphXYLayoutEditPolicy());
	}

	@Override
	public void activate() {
		if (!isActive()) {
			((GraphicsLayer) getModel()).addObserver(this);
			super.activate();
		}
	}

	@Override
	public void deactivate() {
		if (isActive()) {
			((GraphicsLayer) getModel()).deleteObserver(this);
			super.deactivate();
		}
	}

	@Override
	public void update(Observable observable, Object message) {
		refreshChildren();
	}
}
