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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.TuplePredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.ColorManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.ConnectionDialog;

/**
 * @author DGeesen
 * 
 */
public class Connection extends AbstractPart {

	private AbstractPictogram source;
	private AbstractPictogram target;

	public enum TextPosition {
		Top, Bottom, Target, Source
	};

	private int width = 2;
	private Color currentColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private List<Pair<Color, TuplePredicate>> entries = new ArrayList<>();

	private String bottomText = "";
	private RelationalExpression<IMetaAttribute> bottomExpression;
	private String currentTextBottom;

	private String topText = "";
	private RelationalExpression<IMetaAttribute> topExpression;
	private String currentTextTop;

	private String sourceText = "";
	private RelationalExpression<IMetaAttribute> sourceExpression;
	private String currentTextSource;

	private String targetText = "";
	private RelationalExpression<IMetaAttribute> targetExpression;
	private String currentTextTarget;

	public Connection() {

	}

	public Connection(Connection old) {
		this.source = old.source;
		this.target = old.target;
		this.sourceText = old.sourceText;
		this.targetText = old.targetText;
		this.bottomText = old.bottomText;
		this.topText = old.topText;
		this.width = old.width;
		this.currentColor = old.currentColor;
		for (Pair<Color, TuplePredicate> e : entries) {
			this.addColor(e.getE1(), e.getE2().getExpression().getExpressionString());
		}
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

	public String getTargetText() {
		return targetText;
	}

	public void setTargetText(String targetText) {
		this.targetText = targetText;
		if (targetText.startsWith("=")) {
			targetExpression = new RelationalExpression<>(new SDFExpression(targetText.substring(1), null, MEP.getInstance()));
		} else {
			targetExpression = null;
			this.currentTextTarget = this.targetText;
		}
		setDirty();
	}

	public String getSourceText() {
		return sourceText;
	}

	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
		if (sourceText.startsWith("=")) {
			sourceExpression = new RelationalExpression<>(new SDFExpression(sourceText.substring(1), null,  MEP.getInstance()));
		} else {
			sourceExpression = null;
			this.currentTextSource = this.sourceText;
		}
		setDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #load(java.util.Map)
	 */
	@Override
	protected void load(Map<String, String> values) {
		setWidth(loadValue(values.get("width"), 2));
		setTargetText(loadValue(values.get("targetText"), ""));
		setSourceText(loadValue(values.get("sourceText"), ""));
		setBottomText(loadValue(values.get("bottomText"), ""));
		setTopText(loadValue(values.get("topText"), ""));
		String targetId = values.get("targetNode");
		if (targetId != null) {
			setTarget(getGraphicsLayer().getAbstractPictogramById(Integer.parseInt(targetId)));
		}
		String sourceId = values.get("sourceNode");
		if (sourceId != null) {
			setSource(getGraphicsLayer().getAbstractPictogramById(Integer.parseInt(sourceId)));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #save(java.util.Map)
	 */
	@Override
	protected void save(Map<String, String> values) {
		values.put("targetNode", target.getXMLIdentifier());
		values.put("sourceNode", source.getXMLIdentifier());
		values.put("targetText", targetText);
		values.put("sourceText", sourceText);
		values.put("bottomText", bottomText);
		values.put("topText", topText);
		values.put("width", Integer.toString(width));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #getXML(org.w3c.dom.Node, org.w3c.dom.Document)
	 */
	@Override
	public void getXML(Node parent, Document builder) {
		super.getXML(parent, builder);
		Element colorsElement = builder.createElement("colors");
		for (Pair<Color, TuplePredicate> ce : this.entries) {
			Element colorElement = builder.createElement("color");
			colorElement.setAttribute("predicate", ce.getE2().getExpression().getExpressionString());
			colorElement.setAttribute("r", Integer.toString(ce.getE1().getRGB().red));
			colorElement.setAttribute("g", Integer.toString(ce.getE1().getRGB().green));
			colorElement.setAttribute("b", Integer.toString(ce.getE1().getRGB().blue));
			colorsElement.appendChild(colorElement);
		}
		parent.appendChild(colorsElement);
	}

	@Override
	public void loadFromXML(Node parent, GraphicsLayer layer) {
		super.loadFromXML(parent, layer);
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			list.item(i);
			Element elem = (Element) list.item(i);
			if (elem.getNodeName().equals("colors")) {
				NodeList colorsList = elem.getChildNodes();
				for (int k = 0; k < colorsList.getLength(); k++) {
					Element colorElem = (Element) colorsList.item(k);
					String predicate = colorElem.getAttribute("predicate");
					int r = Integer.parseInt(colorElem.getAttribute("r"));
					int g = Integer.parseInt(colorElem.getAttribute("g"));
					int b = Integer.parseInt(colorElem.getAttribute("b"));
					Color color = ColorManager.createColor(r, g, b);
					addColor(color, predicate);
				}

			}
		}
		layer.addPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #open(de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)
	 */
	@Override
	protected void open(IPhysicalOperator root) {
		try {
			for (Pair<Color, TuplePredicate> ce : this.entries) {
				ce.getE2().init(root.getOutputSchema(), null);
			}
			if (this.bottomExpression != null) {
				this.bottomExpression.initVars(root.getOutputSchema());
			}
			if (this.topExpression != null) {
				this.topExpression.initVars(root.getOutputSchema());
			}
			if (this.sourceExpression != null) {
				this.sourceExpression.initVars(root.getOutputSchema());
			}
			if (this.targetExpression != null) {
				this.targetExpression.initVars(root.getOutputSchema());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #process(de.uniol.inf.is.odysseus.core.collection.Tuple)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void process(Tuple<?> tuple) {
		if (this.topExpression != null) {
			this.currentTextTop = getExpressionValue(this.topExpression, (Tuple<IMetaAttribute>) tuple);
		}
		if (this.bottomExpression != null) {
			this.currentTextBottom = getExpressionValue(this.bottomExpression, (Tuple<IMetaAttribute>) tuple);
		}
		if (this.targetExpression != null) {
			this.currentTextTarget = getExpressionValue(this.targetExpression, (Tuple<IMetaAttribute>) tuple);
		}
		if (this.sourceExpression != null) {
			this.currentTextSource = getExpressionValue(this.sourceExpression, (Tuple<IMetaAttribute>) tuple);
		}
		for (Pair<Color, TuplePredicate> ce : entries) {
			if (ce.getE2().evaluate(tuple)) {
				setCurrentColor(ce.getE1());
				break;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #clone()
	 */
	@Override
	public Connection clone() {
		return new Connection(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #getConfigurationDialog()
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
		TuplePredicate relPredicate = new TuplePredicate(new SDFExpression(predicate, null, MEP.getInstance()));
		Pair<Color, TuplePredicate> ce = new Pair<Color, TuplePredicate>(color, relPredicate);
		this.entries.add(ce);
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
		entries.clear();
	}

	/**
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	public Color getCurrentColor() {
		return currentColor;
	}

	public void setCurrentColor(Color currentColor) {
		this.currentColor = currentColor;
	}

	public List<Pair<Color, TuplePredicate>> getColorPredicates() {
		return Collections.unmodifiableList(this.entries);
	}

	public String getTopText() {
		return topText;
	}

	public void setTopText(String topText) {
		this.topText = topText;
		if (topText.startsWith("=")) {
			topExpression = new RelationalExpression<>(new SDFExpression(topText.substring(1), null,  MEP.getInstance()));
		} else {
			topExpression = null;
			this.currentTextTop = this.topText;
		}
		setDirty();
	}

	public String getBottomText() {
		return bottomText;
	}

	public void setBottomText(String textBottom) {
		this.bottomText = textBottom;
		if (textBottom.startsWith("=")) {
			bottomExpression = new RelationalExpression<>(new SDFExpression(textBottom.substring(1), null, MEP.getInstance()));
		} else {
			bottomExpression = null;
			this.currentTextBottom = this.bottomText;
		}
		setDirty();
	}

	public String getTextBottomToShow() {
		return currentTextBottom;
	}

	public String getTextTopToShow() {
		return currentTextTop;
	}

	public String getTextTargetToShow() {
		return currentTextTarget;
	}

	public String getTextSourceToShow() {
		return currentTextSource;
	}

	public String getTextByPosition(TextPosition position) {
		switch (position) {
		case Top:
			return getTopText();
		case Bottom:
			return getBottomText();
		case Source:
			return getSourceText();
		case Target:
			return getTargetText();
		default:
			return "";
		}
	}

	public void setTextByPosition(TextPosition position, String text) {
		switch (position) {
		case Top:
			setTopText(text);
			break;
		case Bottom:
			setBottomText(text);
			break;
		case Source:
			setSourceText(text);
			break;
		case Target:
			setTargetText(text);
			break;
		default:
		}

	}

	@Override
	protected void internalProcessRelavant(Tuple<?> t) {
	}
}
