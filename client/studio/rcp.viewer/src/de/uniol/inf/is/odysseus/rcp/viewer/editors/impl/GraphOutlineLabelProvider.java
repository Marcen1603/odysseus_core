/**********************************************************************************
 * Copyright 2011 The Odysseus Team
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

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class GraphOutlineLabelProvider extends StyledCellLabelProvider {

	private final Map<String, Image> images = new HashMap<String, Image>();

	@Override
	public void update(ViewerCell cell) {
		Object obj = cell.getElement();

		StyledString styledString = new StyledString();

		styledString = getText(obj, styledString);

		cell.setText(styledString.toString());
		cell.setStyleRanges(styledString.getStyleRanges());
		cell.setImage(getImage(obj));
		super.update(cell);

	}

	public Image getImage(Object element) {
		if (element instanceof IMonitoringData<?>) {
			return OdysseusRCPViewerPlugIn.getImageManager().get("metadata");
		}

		if (element instanceof SDFSchema) {
			return OdysseusRCPViewerPlugIn.getImageManager().get("schema");
		}

		if (element instanceof IPredicate) {
			return OdysseusRCPViewerPlugIn.getImageManager().get("predicate");
		}

		if (element instanceof SDFAttribute) {
			return OdysseusRCPViewerPlugIn.getImageManager().get("attribute");
		}

		if (element instanceof SDFConstraint){
			return OdysseusRCPViewerPlugIn.getImageManager().get("constraint");
		}

		if (element instanceof SDFUnit){
			return OdysseusRCPViewerPlugIn.getImageManager().get("unit");
		}

		if (element instanceof NamedList) {
			NamedList e = (NamedList) element;
			if (e.getValues().isEmpty()) {
				return getImage(((NamedList) element).getKey());
			}
			return getImage(e.getValues().get(0));
		}

		if (element instanceof IOperatorOwner) {
			return OdysseusRCPViewerPlugIn.getImageManager().get("partof_icon");
		}

		if (element instanceof ISubscription) {
			return OdysseusRCPViewerPlugIn.getImageManager()
					.get("subscription");
		}

		if (element instanceof Map) {
			return OdysseusRCPViewerPlugIn.getImageManager().get("information");
		}

		if (element instanceof Map.Entry) {
			return OdysseusRCPViewerPlugIn.getImageManager().get("attribute");
		}

		if (element instanceof IOdysseusNodeView) {
			IOdysseusNodeView node = (IOdysseusNodeView) element;
			IPhysicalOperator op = node.getModelNode().getContent();

			if (op.isSink() && !op.isSource()) {
				return OdysseusRCPViewerPlugIn.getImageManager().get(
						"sink_icon");
			}
			if (!op.isSink() && op.isSource()) {
				return OdysseusRCPViewerPlugIn.getImageManager().get(
						"source_icon");
			}
			return OdysseusRCPViewerPlugIn.getImageManager().get("pipe_icon");
		}

		if (element instanceof String || element instanceof StringNode) {
			return OdysseusRCPViewerPlugIn.getImageManager().get("string");
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	public StyledString getText(Object element, StyledString styledString) {
		if (element instanceof IOdysseusNodeView) {
			IOdysseusNodeView node = (IOdysseusNodeView) element;
			String name = node.getModelNode().getContent().getName();
			return styledString.append(name);
		}
		
		if (element instanceof ISubscription) {
			ISubscription<?,?> s = (ISubscription<?,?>) element;
			if (s.getSource() instanceof IPhysicalOperator) {
				styledString.append(((IPhysicalOperator) s.getSource()).getName());
			} else{
				styledString.append(""+s.getSource());
			}
			styledString.append(" In("+s.getSinkInPort()).append(") out ("+s.getSourceOutPort()).append(")");

			if (s.getSource() instanceof IPhysicalOperator){
				int open = ((AbstractPhysicalSubscription<?,?>) s).getOpenCalls();
				if (open > 0){
					styledString.append(" #Open: "+open);
				}
			}

			if (s instanceof AbstractPhysicalSubscription){
				if (((AbstractPhysicalSubscription)s).isNeedsClone()){
					styledString.append(" <clone> ");
				};
				if (((AbstractPhysicalSubscription)s).isDone()){
					styledString.append(" <done> ");
				};
			}

			if (s instanceof ControllablePhysicalSubscription){
				ControllablePhysicalSubscription csub =  (ControllablePhysicalSubscription)s;
				if (csub.isShedding()){
					styledString.append(" <s="+csub.getSheddingFactor()+"> ");
				}

				if (csub.getBufferSize() > 0){
					styledString.append(" b= "+csub.getBufferSize()+" ");
				}

			};
			return styledString;
		}
		if (element instanceof SDFSchema) {
			return styledString.append("OutputSchema ("
					+ ((SDFSchema) element).getURI() + ":"
					+ ((SDFSchema) element).getType().getSimpleName() + ")");
		}

		if (element instanceof IPredicate) {
			return styledString.append(element.toString());
		}

		if (element instanceof NamedList) {
			return getText(((NamedList) element).getKey(), styledString);
		}

		if (element instanceof IOperatorOwner) {
			IOperatorOwner owner = (IOperatorOwner) element;
			styledString.append("Query " + owner.getID());
			return styledString.append(" " + owner.hashCode(),
					StyledString.QUALIFIER_STYLER);
		}

		if (element instanceof SDFAttribute) {
			SDFAttribute attribute = (SDFAttribute) element;
			if (!Strings.isNullOrEmpty(attribute.getSourceName())) {
				styledString.append(attribute.getSourceName() + "."
						+ attribute.getAttributeName());
			} else {
				styledString.append(attribute.getAttributeName());
			}
			if (attribute.getNumber() >= 0){
				styledString.append(" "+attribute.getNumber()+" ");
			}
			styledString.append("  " + attribute.getDatatype().toString(),
					StyledString.QUALIFIER_STYLER);
			return styledString;
		}

		if (element instanceof SDFConstraint){
			SDFConstraint dt = (SDFConstraint)element;
			return styledString.append(dt.getURI()).append(" = "+dt.getValue());
		}

		if (element instanceof SDFUnit){
			SDFUnit unit = (SDFUnit) element;
			return styledString.append(unit.getClass().getSimpleName()+": "+unit.getURI());
		}

		if (element instanceof IMonitoringData<?>) {
			final IMonitoringData<?> monData = (IMonitoringData<?>) element;
			final String type = monData.getType();

			final Object value = monData.getValue();
			String valueString = "";
			if (value instanceof Double) {
				Double d = (Double) value;
				valueString = String.format("%-8.6f", d);
			} else {
				valueString = value != null ? value.toString() : "null";
			}
			return styledString.append(type + " = " + valueString);
		}

		if (element instanceof StringWrapper) {
			return styledString.append(((StringWrapper) element).content);
		}

		if (element instanceof StringNode) {
			return styledString.append("toString()");
		}

		if (element instanceof Map) {
			return styledString.append("Information");
		}
		if (element instanceof Map.Entry) {
			Map.Entry<?, ?> e = ((Map.Entry<?, ?>) element);
			return styledString.append(e.getKey() + " = " + e.getValue());

		}

		if (element instanceof String) {
			return styledString.append((String) element);
		}

		return styledString.append(element.getClass().getName());
	}

	@Override
	public void dispose() {
		for (String key : images.keySet())
			images.get(key).dispose();
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

}
