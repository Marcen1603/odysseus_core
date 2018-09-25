package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.part;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.window.Window;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.CopyAction;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPictogramDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.AbstractPictogramFigure;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Connection;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.policy.PictogramComponentEditPolicy;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.policy.PictogramGraphicalNodeEditPolicy;

public class PictogramEditPart extends AbstractGraphicalEditPart implements Observer {

	public PictogramEditPart(AbstractPictogram node) {
		setModel(node);
	}

	@Override
	protected IFigure createFigure() {
		return ((AbstractPictogram) getModel()).createPictogramFigure();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new PictogramGraphicalNodeEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new PictogramComponentEditPolicy());
	}

	@Override
	public void refreshVisuals() {
		@SuppressWarnings("unchecked")
		AbstractPictogramFigure<AbstractPictogram> figure = (AbstractPictogramFigure<AbstractPictogram>) getFigure();
		AbstractPictogram node = (AbstractPictogram) getModel();
		figure.updateValuesInternal(node);		
		figure.refresh();
		GraphicalLayerEditPart parent = (GraphicalLayerEditPart) getParent();
		Rectangle constraint = node.getConstraint();
		if (constraint != null) {
			Rectangle r = new Rectangle(constraint);
			parent.setLayoutConstraint(this, figure, r);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef.Request)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void performRequest(Request req) {
		AbstractPictogram pg = ((AbstractPictogram) getModel());
		if (req.getType() == RequestConstants.REQ_OPEN) {
			try {				
				AbstractPictogramDialog dialog = pg.getConfigurationDialog().newInstance();
				dialog.init(pg);
				if (Window.OK == dialog.open()) {
					// hint: save is invoked by ok button 
					refreshVisuals();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(req.getType().equals(CopyAction.GRAPHICS_COPY_ACTION)){
			System.out.println("COPY!!");
		}
		super.performRequest(req);
	}

	@Override
	public void activate() {
		if (!isActive()) {
			((AbstractPictogram) getModel()).addObserver(this);
		}
		super.activate();
	}

	@Override
	public void deactivate() {
		if (isActive()) {
			((AbstractPictogram) getModel()).deleteObserver(this);
		}
		super.deactivate();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		refreshVisuals();
		refreshSourceConnections();
		refreshTargetConnections();
	}
	
	
	@Override
	protected List<Connection> getModelSourceConnections() {
		return ((AbstractPictogram) getModel()).getSourceConnections();
	}

	@Override
	protected List<Connection> getModelTargetConnections() {
		return ((AbstractPictogram) getModel()).getTargetConnections();
	}
}
