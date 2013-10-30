package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.IFigure;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPartDialog;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * This part reflects anything that can be visualized and changed by a tuple. This includes e.g. connections, pictograms, images...
 * 
 * @author DGeesen
 * 
 */
public abstract class AbstractPart extends Observable implements Observer {

	private boolean visibile = true;
	private GraphicsLayer graphicsLayer;
	private RelationalPredicate relevancePredicate = new RelationalPredicate(new SDFExpression("true", MEP.getInstance()));
	private String selectedRootName;
	private boolean dirty = true;
	private Collection<IPhysicalOperator> roots;

	public AbstractPart() {

	}

	public AbstractPart(AbstractPart old) {
		this.visibile = old.visibile;
		this.graphicsLayer = old.graphicsLayer;
		this.relevancePredicate = old.relevancePredicate.clone();
		this.selectedRootName = old.selectedRootName;
		this.dirty = old.dirty;
	}

	protected void setDirty() {
		refreshVisuals();
		this.dirty = true;
		if (this.getGraphicsLayer() != null) {
			this.getGraphicsLayer().setDirty();
		}
	}

	protected void internalProcess(Tuple<?> tuple) {
		if (dirty) {
			internalOpen(roots);
			dirty = false;
		}
		if (this.relevancePredicate.evaluate(tuple)) {	
			internalProcessRelavant(tuple);
			process(tuple);
			refreshVisuals();
		}
	}

	
	protected void internalProcessRelavant(Tuple<?> tuple) {
				
	}

	protected String getExpressionValue(SDFExpression expression, Tuple<?> tuple) {
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

		internalSave(values);

		for (Entry<String, String> value : values.entrySet()) {
			Element element = builder.createElement(value.getKey());
			element.setTextContent(value.getValue());
			parent.appendChild(element);
		}
	}

	protected void internalSave(Map<String, String> values) {
		save(values);
		values.put("relevancePredicate", this.relevancePredicate.toString());
		values.put("selected_root", this.selectedRootName);
	}

	public void loadFromXML(Node parent) {
		Map<String, String> values = new HashMap<String, String>();
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			list.item(i);
			Element elem = (Element) list.item(i);
			if (!list.item(i).getNodeName().equals("position_dimensions")) {
				String key = elem.getNodeName();
				String value = elem.getTextContent();
				values.put(key, value);
			}
		}

		internalLoad(values);

	}

	protected void internalLoad(Map<String, String> values) {
		setRelevancePredicate(loadValue(values.get("relevancePredicate"), "true"));
		setSelectedRootName(loadValue(values.get("selected_root"), ""));
		load(values);
	}

	protected void update() {
		setChanged();
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		update();
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

	public abstract IFigure createPictogramFigure();

	protected abstract void load(Map<String, String> values);

	protected abstract void save(Map<String, String> values);

	protected abstract void open(IPhysicalOperator root);

	protected abstract void process(Tuple<?> tuple);

	public abstract AbstractPart clone();

	public abstract Class<? extends AbstractPartDialog<? extends AbstractPart>> getConfigurationDialog();

}
