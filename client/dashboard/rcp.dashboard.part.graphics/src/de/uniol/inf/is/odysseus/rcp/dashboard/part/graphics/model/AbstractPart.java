package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.TuplePredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPartDialog;

/**
 * This part reflects anything that can be visualized and changed by a tuple. This includes e.g. connections, pictograms, images...
 * 
 * @author DGeesen
 * 
 */
public abstract class AbstractPart extends Observable implements Observer {

	private boolean visibile = true;
	private GraphicsLayer graphicsLayer;
	private TuplePredicate relevancePredicate = new TuplePredicate(new SDFExpression("true", null, MEP.getInstance()));
	private String selectedRootName;
	private boolean dirty = true;
	private Collection<IPhysicalOperator> roots;

	public AbstractPart() {

	}

	public AbstractPart(AbstractPart old) {
		this.visibile = old.visibile;
		this.graphicsLayer = old.graphicsLayer;
		this.setRelevancePredicate(old.relevancePredicate.getExpression().getExpressionString());		
		this.selectedRootName = old.selectedRootName;
		this.dirty = old.dirty;
		this.roots = old.roots;
	}

	public void setDirty() {
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

	
	protected abstract void internalProcessRelavant(Tuple<?> t);

	protected String getExpressionValue(RelationalExpression<IMetaAttribute> expression, Tuple<IMetaAttribute> tuple) {
		return expression.evaluate(tuple, null, null)+"";
	}

	private void refreshVisuals() {
		setChanged();
		notifyObservers();
	}

	public boolean loadValue(String value, boolean defaultValue){
		if(value==null){
			return defaultValue;
		}
		if (value.isEmpty()) {
			return defaultValue;
		}		
		return Boolean.parseBoolean(value);
	}
	
	public int loadValue(String value, int defaultValue){
		if(value==null){
			return defaultValue;
		}
		if (value.isEmpty()) {
			return defaultValue;
		}		
		return Integer.parseInt(value);
	}
	
	public float loadValue(String value, float defaultValue){
		if(value==null){
			return defaultValue;
		}
		if (value.isEmpty()) {
			return defaultValue;
		}		
		return Float.parseFloat(value);
	}
	
	public String loadValue(String value, String defaultValue){
		if(value==null){
			return defaultValue;
		}
		if (value.isEmpty()) {
			return defaultValue;
		}		
		return value;
	}
	
//	public <T> T loadValue(T value, T defaultValue) {
//		if (value == null) {
//			return defaultValue;
//		}
//		if (value.toString().isEmpty()) {
//			return defaultValue;
//		}
//		return value;
//	}

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

	public void loadFromXML(Node parent, GraphicsLayer layer) {
		this.graphicsLayer = layer; 
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

	public TuplePredicate getRelevancePredicate() {
		return relevancePredicate;
	}

	public void setRelevancePredicate(String relevancePredicate) {
		if (relevancePredicate == null || relevancePredicate.isEmpty()) {
			relevancePredicate = "true";
		}
		this.relevancePredicate = new TuplePredicate(new SDFExpression(relevancePredicate, null, MEP.getInstance()));
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

	protected abstract void load(Map<String, String> values);

	protected abstract void save(Map<String, String> values);

	protected abstract void open(IPhysicalOperator root);

	protected abstract void process(Tuple<?> tuple);

	@Override
	public abstract AbstractPart clone();

	public abstract Class<? extends AbstractPartDialog<? extends AbstractPart>> getConfigurationDialog();

}
