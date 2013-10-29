package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPictogramDialog;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public abstract class Pictogram extends Observable {

	private Rectangle constraint;
	private boolean visibile = true;	
	private PictogramGroup parentGroup;
	private RelationalPredicate relevancePredicate = new RelationalPredicate(new SDFExpression("true", MEP.getInstance()));

	public Rectangle getConstraint() {
		return constraint;
	}

	public void setLocation(Point location) {
		this.constraint = new Rectangle(location, getPreferedSize());
		changed();
	}

	protected abstract void load(Map<String, String> values);

	protected abstract void save(Map<String, String> values);

	protected abstract void open(Collection<IPhysicalOperator> roots);

	protected abstract void process(Tuple<?> tuple);

	public abstract Class<? extends AbstractPictogramDialog<? extends Pictogram>> getConfigurationDialog();

	protected void internalProcess(Tuple<?> tuple) {
		if (this.relevancePredicate.evaluate(tuple)) {
			process(tuple);
			setChanged();
			notifyObservers();
		}
	}

	public <T> T loadValue(T value, T defaultValue) {
		if (value == null) {
			return defaultValue;
		} else {
			if (value.toString().isEmpty()) {
				return defaultValue;
			}
			return value;
		}
	}

	public void getXML(Node parent, Document builder) {
		Map<String, String> values = new HashMap<String, String>();

		save(values);
		values.put("relevancePredicate", this.relevancePredicate.toString());
		for (Entry<String, String> value : values.entrySet()) {
			Element element = builder.createElement(value.getKey());
			element.setTextContent(value.getValue());
			parent.appendChild(element);
		}

		Element constElement = builder.createElement("position_dimensions");
		constElement.setAttribute("x", Integer.toString(constraint.x));
		constElement.setAttribute("y", Integer.toString(constraint.y));
		constElement.setAttribute("width", Integer.toString(constraint.width));
		constElement.setAttribute("height", Integer.toString(constraint.height));
		parent.appendChild(constElement);
	}

	public void loadFromXML(Node parent) {
		Map<String, String> values = new HashMap<String, String>();
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			list.item(i);
			Element elem = (Element) list.item(i);
			if (list.item(i).getNodeName().equals("position_dimensions")) {
				int x = Integer.parseInt(elem.getAttribute("x"));
				int y = Integer.parseInt(elem.getAttribute("y"));
				int width = Integer.parseInt(elem.getAttribute("width"));
				int height = Integer.parseInt(elem.getAttribute("height"));
				Rectangle rect = new Rectangle(x, y, width, height);
				this.constraint = rect;
			} else {
				String key = elem.getNodeName();
				String value = elem.getTextContent();
				values.put(key, value);
			}
		}
		setRelevancePredicate(loadValue(values.get("relevancePredicate"), "true"));
		load(values);

	}

	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}	

	public PictogramGroup getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(PictogramGroup parentGroup) {
		this.parentGroup = parentGroup;
	}

	public RelationalPredicate getRelevancePredicate() {
		return relevancePredicate;
	}

	public void setRelevancePredicate(String relevancePredicate) {
		if (relevancePredicate == null || relevancePredicate.isEmpty()) {
			relevancePredicate = "true";
		}
		this.relevancePredicate = new RelationalPredicate(new SDFExpression(relevancePredicate, MEP.getInstance()));
	}

	protected void internalOpen(Collection<IPhysicalOperator> roots) {
		// TODO: this is only working with one root!!
		try {
			this.relevancePredicate.init(roots.iterator().next().getOutputSchema(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		open(roots);
	}

	public abstract IFigure createPictogramFigure();

	public Dimension getPreferedSize() {
		return new Dimension(50, 50);
	}

	public void setConstraint(Rectangle newConstraint) {
		this.constraint = newConstraint;
		changed();
	}

	protected void changed() {
		setChanged();
		notifyObservers();
	}

}
