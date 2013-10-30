package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

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

public abstract class Pictogram extends Observable implements Observer {

	private Rectangle constraint;
	private boolean visibile = true;
	private GraphicsLayer graphicsLayer;
	private RelationalPredicate relevancePredicate = new RelationalPredicate(new SDFExpression("true", MEP.getInstance()));
	private String selectedRootName;

	private List<Connection> sourceConnections = new ArrayList<>();
	private List<Connection> targetConnections = new ArrayList<>();

	private String textTop = "";
	private String textBottom = "";

	private SDFExpression bottomExpression;
	private SDFExpression topExpression;

	private boolean dirty = true;
	private Collection<IPhysicalOperator> roots;
	private String currentTextBottom;
	private String currentTextTop;

	public Pictogram() {

	}

	public Pictogram(Pictogram old) {
		this.constraint = old.constraint.getCopy();
		this.visibile = old.visibile;
		this.graphicsLayer = old.graphicsLayer;
		this.relevancePredicate = old.relevancePredicate.clone();
		this.selectedRootName = old.selectedRootName;
		this.textTop = old.textTop;
		this.textBottom = old.textBottom;
		this.dirty = old.dirty;
	}

	public Rectangle getConstraint() {
		return constraint;
	}

	public void setLocation(Point location) {
		this.constraint = new Rectangle(location, getPreferedSize());
		setDirty();
	}

	protected void setDirty() {
		refreshVisuals();
		this.dirty = true;
		if (this.getGraphicsLayer() != null) {
			this.getGraphicsLayer().setDirty();
		}
	}

	protected abstract void load(Map<String, String> values);

	protected abstract void save(Map<String, String> values);

	protected abstract void open(IPhysicalOperator root);

	protected abstract void process(Tuple<?> tuple);

	public abstract Pictogram clone();

	public abstract Class<? extends AbstractPictogramDialog<? extends Pictogram>> getConfigurationDialog();

	protected void internalProcess(Tuple<?> tuple) {
		if (dirty) {
			internalOpen(roots);
			dirty = false;
		}
		if (this.relevancePredicate.evaluate(tuple)) {
			if (this.topExpression != null) {
				this.currentTextTop = getExpressionValue(topExpression, tuple);
			}
			if (this.bottomExpression != null) {
				this.currentTextBottom = getExpressionValue(this.bottomExpression, tuple);
			}
			process(tuple);
			refreshVisuals();
		}
	}

	private String getExpressionValue(SDFExpression expression, Tuple<?> tuple) {
		int[] positions = expression.getAttributePositions();
		expression.bindVariables(tuple.restrict(positions, true).getAttributes());
		return expression.getValue().toString();
	}

	private void refreshVisuals() {
		setChanged();
		notifyObservers();
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
		values.put("text_bottom", this.textBottom);
		values.put("text_top", this.textTop);
		values.put("selected_root", this.selectedRootName);

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
		setTextBottom(loadValue(values.get("text_bottom"), ""));
		setTextTop(loadValue(values.get("text_top"), ""));
		setSelectedRootName(loadValue(values.get("selected_root"), ""));
		load(values);

	}

	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}

	public GraphicsLayer getGraphicsLayer() {
		return graphicsLayer;
	}

	public void setGraphicsLayer(GraphicsLayer graphicsLayer) {
		this.graphicsLayer = graphicsLayer;
	}

	public RelationalPredicate getRelevancePredicate() {
		return relevancePredicate;
	}

	public void setRelevancePredicate(String relevancePredicate) {
		if (relevancePredicate == null || relevancePredicate.isEmpty()) {
			relevancePredicate = "true";
		}
		this.relevancePredicate = new RelationalPredicate(new SDFExpression(relevancePredicate, MEP.getInstance()));
		setDirty();
	}

	protected void internalOpen(IPhysicalOperator root) {
		try {
			this.relevancePredicate.init(root.getOutputSchema(), null);
			if (this.bottomExpression != null) {
				this.bottomExpression.initAttributePositions(root.getOutputSchema());
			}
			if (this.topExpression != null) {
				this.topExpression.initAttributePositions(root.getOutputSchema());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		open(root);
	}

	protected void internalOpen(Collection<IPhysicalOperator> roots) {
		this.roots = roots;
		IPhysicalOperator root = roots.iterator().next();
		for (IPhysicalOperator candidate : roots) {
			if (candidate.getName().equals(selectedRootName)) {
				root = candidate;
				break;
			}
		}
		internalOpen(root);

	}

	public abstract IFigure createPictogramFigure();

	public Dimension getPreferedSize() {
		return new Dimension(50, 50);
	}

	public void setConstraint(Rectangle newConstraint) {
		this.constraint = newConstraint;
		setDirty();
	}

	public String getTextTop() {
		return textTop;
	}

	public void setTextTop(String textTop) {
		this.textTop = textTop;
		if (textTop.startsWith("=")) {
			topExpression = new SDFExpression(textTop.substring(1), MEP.getInstance());
		} else {
			topExpression = null;
			this.currentTextTop = this.textTop;
		}
		setDirty();
	}

	public String getTextBottom() {
		return textBottom;
	}

	public String getTextBottomToShow() {
		return currentTextBottom;
	}

	public String getTextTopToShow() {
		return currentTextTop;
	}

	public void setTextBottom(String textBottom) {
		this.textBottom = textBottom;
		if (textBottom.startsWith("=")) {
			bottomExpression = new SDFExpression(textBottom.substring(1), MEP.getInstance());
		} else {
			bottomExpression = null;
			this.currentTextBottom = this.textBottom;
		}
		setDirty();
	}

	public String getSelectedRootName() {
		return selectedRootName;
	}

	public void setSelectedRootName(String selectedRoot) {
		this.selectedRootName = selectedRoot;
		setDirty();
	}

	public Collection<IPhysicalOperator> getRoots() {
		if (roots == null) {
			if (getGraphicsLayer() != null) {
				this.roots = getGraphicsLayer().getRoots();
			}
		}
		return roots;
	}

	public List<Connection> getSourceConnections() {
		return sourceConnections;
	}

	public List<Connection> getTargetConnections() {
		return targetConnections;
	}

	public void addSourceConnection(Connection connection) {
		getSourceConnections().add(connection);
		connection.addObserver(this);
		connection.setGraphicsLayer(this.graphicsLayer);
		update();
	}

	public void addTargetConnection(Connection connection) {
		getTargetConnections().add(connection);
		connection.addObserver(this);
		connection.setGraphicsLayer(graphicsLayer);
		update();
	}

	public void removeSourceConnection(Connection connection) {
		getSourceConnections().remove(connection);
		connection.deleteObserver(this);
		update();

	}

	public void removeTargetConnection(Connection connection) {
		getTargetConnections().remove(connection);
		connection.deleteObserver(this);
		update();
	}

	private void update() {		
		setChanged();
		notifyObservers();
		// inform view
		//OperatorGraphSelectionProvider.getInstance().update();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		update();
	}

}
