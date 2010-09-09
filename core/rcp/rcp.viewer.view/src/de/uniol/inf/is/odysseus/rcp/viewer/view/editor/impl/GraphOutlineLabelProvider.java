package de.uniol.inf.is.odysseus.rcp.viewer.view.editor.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.rcp.viewer.view.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusNodeView;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class GraphOutlineLabelProvider implements ILabelProvider {

	private final Map<String, Image> images = new HashMap<String, Image>();
	
//	private static final int IMAGE_HEIGHT = 20;
//	private static final int IMAGE_WIDTH = 20;
		
	@Override
	public Image getImage(Object element) {
		if (element instanceof IMonitoringData<?>) {
			return Activator.getDefault().getImageRegistry().get("metadata");
		}

		if (element instanceof SDFAttributeList) {
			return Activator.getDefault().getImageRegistry().get("schema");
		}
		
		if (element instanceof SDFAttribute){
			return Activator.getDefault().getImageRegistry().get("attribute");
		}
		
		if (element instanceof IOdysseusNodeView) {
			IOdysseusNodeView node = (IOdysseusNodeView)element;
			IPhysicalOperator op = node.getModelNode().getContent();
			
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
			if (op.isSink() && ! op.isSource()){
				return Activator.getDefault().getImageRegistry().get("sink_icon");
			}
			if (!op.isSink() &&  op.isSource()){
				return Activator.getDefault().getImageRegistry().get("source_icon");
			}
			return Activator.getDefault().getImageRegistry().get("pipe_icon");
		}

		return null;
	}

	@Override
	public String getText(Object element) {		
		if (element instanceof IOdysseusNodeView) {
			IOdysseusNodeView node = (IOdysseusNodeView)element;
			String name = node.getModelNode().getContent().getName();
			return name;
		}
		if (element != null && element instanceof SDFAttributeList){				
			return "OutputSchema";
		}
		if (element != null && element instanceof SDFAttribute){
			SDFAttribute a = (SDFAttribute) element;
			StringBuffer name = new StringBuffer(a.getURI());
			name.append(":").append(a.getDatatype().getURI());
			return name.toString();
		}
		if (element instanceof IMonitoringData<?>) {
			final IMonitoringData<?> monData = (IMonitoringData<?>) element;
			final String type = monData.getType();
			final String value = monData.getValue() != null ? monData.getValue().toString() : "null";
			return type + " = " + value;
		}

		return element.getClass().getName();
	}

	@Override
	public void addListener(ILabelProviderListener listener) {}

	@Override
	public void dispose() {
		for( String key : images.keySet()) 
			images.get(key).dispose();
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {}

}
