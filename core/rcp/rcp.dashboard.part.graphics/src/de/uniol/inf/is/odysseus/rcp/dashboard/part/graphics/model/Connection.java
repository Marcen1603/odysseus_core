/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.ConnectionDialog;

/**
 * @author DGeesen
 * 
 */
public class Connection extends AbstractPart{

	private AbstractPictogram source;
	private AbstractPictogram target;
	private String sourceText = "";
	private String targetText = "";
	private int width;
	

	public Connection(){
		
	}

	public Connection(Connection old) {
		this.source = old.source;
		this.target = old.target;
		this.sourceText = old.sourceText;
		this.targetText = old.targetText;
	}

	public AbstractPictogram getSource() {
		return source;
	}

	public void setSource(AbstractPictogram source) {
		if (source == this.source)
			return;
		if (this.source != null) {
			this.source.removeSourceConnection(this);
		}
		this.source = source;
		if (source != null) {
			source.addSourceConnection(this);			
		}
		update();
	}

	public AbstractPictogram getTarget() {
		return target;
	}

	public void setTarget(AbstractPictogram target) {
		if (target == this.target)
			return;
		if (this.target != null) {
			this.target.removeTargetConnection(this);
		}
		this.target = target;
		if (target != null) {			
			target.addTargetConnection(this);								
		}
		update();
	}

	public void reconnect(AbstractPictogram sourceNode, AbstractPictogram targetNode) {
		setTarget(targetNode);
		setSource(sourceNode);		
	}

//	public void getXML(Node parent, Document builder) {
//		Element targetElement = builder.createElement("target");
//		targetElement.setTextContent(target.getXMLIdentifier());
//		parent.appendChild(targetElement);
//		
//		Element sourceElement = builder.createElement("source");
//		sourceElement.setTextContent(source.getXMLIdentifier());
//		parent.appendChild(sourceElement);
//		
//		Element targetPortElement = builder.createElement("targetPort");
//		targetPortElement.setTextContent(Integer.toString(targetPort));
//		parent.appendChild(targetPortElement);
//		
//		Element sourcePortElement = builder.createElement("sourcePort");
//		sourcePortElement.setTextContent(Integer.toString(sourcePort));
//		parent.appendChild(sourcePortElement);
//	}

	public String getTargetText() {
		return targetText;
	}

	public void setTargetText(String targetText) {
		this.targetText = targetText;
		update();
	}

	public String getSourceText() {
		return sourceText;
	}

	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
		update();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart#createPictogramFigure()
	 */
	@Override
	public IFigure createPictogramFigure() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart#load(java.util.Map)
	 */
	@Override
	protected void load(Map<String, String> values) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart#save(java.util.Map)
	 */
	@Override
	protected void save(Map<String, String> values) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart#open(de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)
	 */
	@Override
	protected void open(IPhysicalOperator root) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart#process(de.uniol.inf.is.odysseus.core.collection.Tuple)
	 */
	@Override
	protected void process(Tuple<?> tuple) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart#clone()
	 */
	@Override
	public Connection clone() {
		return new Connection(this);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart#getConfigurationDialog()
	 */
	@Override
	public Class<ConnectionDialog> getConfigurationDialog() {
		return ConnectionDialog.class;
	}

	/**
	 * @param color
	 * @param predicate
	 */
	public void addColor(Color color, String predicate) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param parseInt
	 */
	public void setWidth(int width) {
		this.width = width;
		
	}

	/**
	 * 
	 */
	public void clearColors() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return
	 */
	public int getWidth() {
		return width;
	}
}
