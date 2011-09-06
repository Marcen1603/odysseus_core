/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.viewer.editors.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.ISubscription;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class GraphOutlineLabelProvider implements ILabelProvider {

	private final Map<String, Image> images = new HashMap<String, Image>();
	
//	private static final int IMAGE_HEIGHT = 20;
//	private static final int IMAGE_WIDTH = 20;
		
	@Override
	public Image getImage(Object element) {
		if (element instanceof IMonitoringData<?>) {
			return OdysseusRCPViewerPlugIn.getDefault().getImageRegistry().get("metadata");
		}

		if (element instanceof SDFAttributeList) {
			return OdysseusRCPViewerPlugIn.getDefault().getImageRegistry().get("schema");
		}
		
		if (element instanceof IPredicate){
			return OdysseusRCPViewerPlugIn.getDefault().getImageRegistry().get("predicate");
		}
		
		if (element instanceof SDFAttribute){
			return OdysseusRCPViewerPlugIn.getDefault().getImageRegistry().get("attribute");
		}
		
		if (element instanceof OwnerWrapper){
			return OdysseusRCPViewerPlugIn.getDefault().getImageRegistry().get("partof_icon");
		}

		if (element instanceof ISubscription){
			return OdysseusRCPViewerPlugIn.getDefault().getImageRegistry().get("subscription");
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
				return OdysseusRCPViewerPlugIn.getDefault().getImageRegistry().get("sink_icon");
			}
			if (!op.isSink() &&  op.isSource()){
				return OdysseusRCPViewerPlugIn.getDefault().getImageRegistry().get("source_icon");
			}
			return OdysseusRCPViewerPlugIn.getDefault().getImageRegistry().get("pipe_icon");
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
		if (element != null && element instanceof ISubscription){
			ISubscription<?> s = (ISubscription<?>) element;
			return " In("+s.getSinkInPort()+") "+" out("+s.getSourceOutPort()+") "+s.getTarget();
		}
		if (element != null && element instanceof SDFAttributeList){				
			return "OutputSchema";
		}

		if (element != null && element instanceof IPredicate){				
			return element.toString();
		}

		if (element != null && element instanceof SDFAttribute){
			SDFAttribute a = (SDFAttribute) element;
			StringBuffer name = new StringBuffer(a.getPointURI());
			name.append(":").append(a.getDatatype().getURI());
			return name.toString();
		}
		if (element != null && element instanceof IMonitoringData<?>) {
			final IMonitoringData<?> monData = (IMonitoringData<?>) element;
			final String type = monData.getType();
			
			final Object value = monData.getValue();
			String valueString = "";
			if( value instanceof Double ) {
				Double d = (Double)value;
				valueString = String.format("%-8.6f", d);
			} else {
				valueString = value != null ? value.toString() : "null";
			}
			return type + " = " + valueString;
		}
		
		if (element != null && element instanceof OwnerWrapper){
			return ((OwnerWrapper)element).content;
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
