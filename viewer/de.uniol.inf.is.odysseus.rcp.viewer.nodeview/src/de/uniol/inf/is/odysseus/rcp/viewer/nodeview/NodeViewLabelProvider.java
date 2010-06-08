package de.uniol.inf.is.odysseus.rcp.viewer.nodeview;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.rcp.resource.ResourceManager;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusNodeView;

public class NodeViewLabelProvider implements ILabelProvider {

	private final Map<String, Image> images = new HashMap<String, Image>();
	
//	private static final int IMAGE_HEIGHT = 20;
//	private static final int IMAGE_WIDTH = 20;
		
	@Override
	public Image getImage(Object element) {
		if (element instanceof IMonitoringData<?>) {
			return ResourceManager.getInstance().getImage("metadata");
		}

		if (element instanceof IOdysseusNodeView) {
//			IOdysseusNodeView node = (IOdysseusNodeView)element;
//			
//			if( images.containsKey(node.getModelNode().getName()))
//				return images.get(node.getModelNode().getName());
//						
//			Image image = new Image(PlatformUI.getWorkbench().getDisplay(), IMAGE_WIDTH, IMAGE_HEIGHT);
//			GC gc = new GC(image);
//			for( ISymbolElement<IPhysicalOperator> sym : node.getSymbolContainer() ) {
//				if( sym instanceof SWTSymbolElement<?> ) {
//					SWTSymbolElement<IPhysicalOperator> ele = (SWTSymbolElement<IPhysicalOperator>)sym;
//					ele.setActualGC(gc);
//					ele.draw(Vector.EMPTY_VECTOR, IMAGE_WIDTH, IMAGE_HEIGHT, 1.0f);
//				}
//			}
//			images.put(node.getModelNode().getName(), image);		
//			return image;
			
			return ResourceManager.getInstance().getImage("node");
		}

		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IOdysseusNodeView) {
			IOdysseusNodeView node = (IOdysseusNodeView)element;
			String name = node.getModelNode().getName();
			int sources = node.getConnectionsAsEnd().size();
			int sinks = node.getConnectionsAsStart().size();
			return name + "[" + sources + "/" + sinks + "]";
		}
		if (element instanceof IMonitoringData<?>) {
			final IMonitoringData<?> monData = (IMonitoringData<?>) element;
			final String type = monData.getType();
			final String value = monData.getValue() != null ? monData.getValue().toString() : "null";
			return type + " = " + value;
		}
		
		if( element instanceof String )
			return element.toString();

		return element.getClass().getName();
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		for( String key : images.keySet()) 
			images.get(key).dispose();
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
