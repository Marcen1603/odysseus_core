package de.uniol.inf.is.odysseus.rcp.viewer.editors.impl;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.manage.impl.OdysseusGraphViewFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.SWTSymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusGraphView;

public class PhysicalGraphEditorInput implements IEditorInput {

	private static OdysseusGraphViewFactory GRAPH_VIEW_FACTORY = new OdysseusGraphViewFactory();
	private static final ISymbolElementFactory<IPhysicalOperator> SYMBOL_FACTORY = new SWTSymbolElementFactory<IPhysicalOperator>();

	private String name;
	private IOdysseusGraphModel model;
	private IOdysseusGraphView view;

	public PhysicalGraphEditorInput(IModelProvider<IPhysicalOperator> provider, String name) {
		this.name = name;

		// Modell erzeugen
		model = (IOdysseusGraphModel) provider.get();

		// View erzezugen
		view = (IOdysseusGraphView) GRAPH_VIEW_FACTORY.createGraphView(model, OdysseusRCPViewerPlugIn.SYMBOL_CONFIGURATION, SYMBOL_FACTORY);
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return name;
	}

	public IOdysseusGraphView getGraphView() {
		return view;
	}
	
	public IOdysseusGraphModel getGraphModel() {
		return model;
	}
	
}
