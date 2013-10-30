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

import java.util.Observable;

/**
 * @author DGeesen
 * 
 */
public class Connection extends Observable{

	private Pictogram source;
	private Pictogram target;
	private String sourceText = "";
	private String targetText = "";	
	private GraphicsLayer graphicsLayer;
	
	

	public Pictogram getSource() {
		return source;
	}

	public void setSource(Pictogram source) {
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

	public Pictogram getTarget() {
		return target;
	}

	public void setTarget(Pictogram target) {
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

	public void reconnect(Pictogram sourceNode, Pictogram targetNode) {
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

	
	private void update(){				
		setChanged();
		notifyObservers();
	}

	public GraphicsLayer getGraphicsLayer() {
		return graphicsLayer;
	}

	public void setGraphicsLayer(GraphicsLayer graphicsLayer) {
		this.graphicsLayer = graphicsLayer;
	}
}
