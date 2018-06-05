package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPictogramDialog;

/**
 * This is anything that can be viewed in a container and connected by
 * connections
 * 
 * @author DGeesen
 * 
 */
public abstract class AbstractPictogram extends AbstractPart {

	private int id = -1;

	private Rectangle constraint;

	private List<Connection> sourceConnections = new ArrayList<>();
	private List<Connection> targetConnections = new ArrayList<>();

	private String textTop = "";
	private String textMiddle = "";
	private String textBottom = "";

	private RelationalExpression<IMetaAttribute> bottomExpression;
	private RelationalExpression<IMetaAttribute> middleExpression;
	private RelationalExpression<IMetaAttribute> topExpression;

	private String currentTextBottom;
	private String currentTextMiddle;
	private String currentTextTop;

	public AbstractPictogram() {

	}

	public AbstractPictogram(AbstractPictogram old) {
		super(old);
		this.constraint = old.constraint.getCopy();
		setTextBottom(old.getTextBottom());
		setTextTop(old.getTextTop());
		setTextMiddle(old.getTextMiddle());

	}

	public abstract IFigure createPictogramFigure();

	public Rectangle getConstraint() {
		return constraint;
	}

	public void setLocation(Point location) {
		this.constraint = new Rectangle(location, getPreferedSize());
		setDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #internalOpen
	 * (de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)
	 */
	@Override
	protected void internalOpen(IPhysicalOperator root) {
		try {
			if (this.bottomExpression != null) {
				this.bottomExpression.initVars(root.getOutputSchema());
			}
			if (this.middleExpression != null) {
				this.middleExpression.initVars(root.getOutputSchema());
			}
			if (this.topExpression != null) {
				this.topExpression.initVars(root.getOutputSchema());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.internalOpen(root);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #internalProcessRelavant(de.uniol.inf.is.odysseus.core.collection.Tuple)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void internalProcessRelavant(Tuple<?> tuple) {
		if (this.topExpression != null) {
			this.currentTextTop = getExpressionValue(this.topExpression, (Tuple<IMetaAttribute>) tuple);
		}
		if (this.middleExpression != null) {
			this.currentTextMiddle = getExpressionValue(this.middleExpression, (Tuple<IMetaAttribute>)tuple);
		}
		if (this.bottomExpression != null) {
			this.currentTextBottom = getExpressionValue(this.bottomExpression, (Tuple<IMetaAttribute>)tuple);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #internalSave(java.util.Map)
	 */
	@Override
	protected void internalSave(Map<String, String> values) {
		super.internalSave(values);
		values.put("text_bottom", this.textBottom);
		values.put("text_top", this.textTop);
		values.put("text_middle", this.textMiddle);
		values.put("identifier", getXMLIdentifier());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #internalLoad(java.util.Map)
	 */
	@Override
	protected void internalLoad(Map<String, String> values) {
		setTextBottom(loadValue(values.get("text_bottom"), ""));
		setTextTop(loadValue(values.get("text_top"), ""));
		setTextMiddle(loadValue(values.get("text_middle"), ""));
		setId(Integer.parseInt(loadValue(values.get("identifier"), "-1")));
		super.internalLoad(values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #loadFromXML(org.w3c.dom.Node)
	 */
	@Override
	public void loadFromXML(Node parent, GraphicsLayer layer) {
		super.loadFromXML(parent, layer);
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
				setConstraint(rect);
			}
		}
		layer.addPictogram(this);
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
		Element constElement = builder.createElement("position_dimensions");
		constElement.setAttribute("x", Integer.toString(constraint.x));
		constElement.setAttribute("y", Integer.toString(constraint.y));
		constElement.setAttribute("width", Integer.toString(constraint.width));
		constElement.setAttribute("height", Integer.toString(constraint.height));
		parent.appendChild(constElement);
	}

	public String getXMLIdentifier() {
		return Integer.toString(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
			topExpression = new RelationalExpression<>(new SDFExpression(textTop.substring(1), null, MEP.getInstance()));
		} else {
			topExpression = null;
			this.currentTextTop = this.textTop;
		}
		setDirty();
	}
	

	public String getTextTopToShow() {
		return currentTextTop;
	}

	

	public String getTextMiddle() {
		return textMiddle;
	}

	public void setTextMiddle(String textMiddle) {
		this.textMiddle = textMiddle;
		if (textMiddle.startsWith("=")) {
			middleExpression = new RelationalExpression<>(new SDFExpression(textMiddle.substring(1), null, MEP.getInstance()));
		} else {
			middleExpression = null;
			this.currentTextMiddle = this.textMiddle;
		}
		setDirty();
	}
	
	public String getTextMiddleToShow() {
		return currentTextMiddle;
	}


	public String getTextBottom() {
		return textBottom;
	}

	public String getTextBottomToShow() {
		return currentTextBottom;
	}

	public void setTextBottom(String textBottom) {
		this.textBottom = textBottom;
		if (textBottom.startsWith("=")) {
			bottomExpression = new RelationalExpression<IMetaAttribute>(new SDFExpression(textBottom.substring(1),null, MEP.getInstance()));
		} else {
			bottomExpression = null;
			this.currentTextBottom = this.textBottom;
		}
		setDirty();
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
		connection.setGraphicsLayer(getGraphicsLayer());
		update();
	}

	public void addTargetConnection(Connection connection) {
		getTargetConnections().add(connection);
		connection.addObserver(this);
		connection.setGraphicsLayer(getGraphicsLayer());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart
	 * #clone()
	 */
	@Override
	public abstract AbstractPictogram clone();

	@Override
	public abstract Class<? extends AbstractPictogramDialog<? extends AbstractPictogram>> getConfigurationDialog();
}
