package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.TuplePredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.ColorManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPictogramDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.ShapePictogramDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.RectanglePictogramFigure;

public class RectanglePictogram extends AbstractPictogram {

	private List<Pair<TuplePredicate, Color>> shapeColors = new ArrayList<>();
	private Color currentColor;
	private int width = 3;

	public RectanglePictogram() {
		super();		
	}

	public RectanglePictogram(RectanglePictogram rp) {
		super(rp);
		this.shapeColors = new ArrayList<>();
		for (Pair<TuplePredicate, Color> p : rp.shapeColors) {
			this.shapeColors.add(new Pair<TuplePredicate, Color>(p.getE1().clone(), p.getE2()));
		}
	}

	@Override
	public IFigure createPictogramFigure() {
		return new RectanglePictogramFigure();
	}

	@Override
	public AbstractPictogram clone() {
		return new RectanglePictogram(this);
	}

	@Override
	public Class<? extends AbstractPictogramDialog<RectanglePictogram>> getConfigurationDialog() {
		return ShapePictogramDialog.class;
	}

	@Override
	protected void load(Map<String, String> values) {
		int count = loadValue(Integer.parseInt(values.get("count")), 0);
		this.width = loadValue(Integer.parseInt(values.get("stroke_width")), 3);
		for (int i = 0; i < count; i++) {
			int colorR = Integer.parseInt(values.get("shape__" + i + "__color__r"));
			int colorG = Integer.parseInt(values.get("shape__" + i + "__color__g"));
			int colorB = Integer.parseInt(values.get("shape__" + i + "__color__b"));
			String predicate = values.get("shape__" + i + "__predicate");
			Color color = ColorManager.createColor(colorR, colorG, colorB);
			addShapeColor(predicate, color);
		}
		

	}

	public void addShapeColor(String predicate, Color color) {
		TuplePredicate tuplepredicate = new TuplePredicate(new SDFExpression(predicate, MEP.getInstance()));
		this.shapeColors.add(new Pair<TuplePredicate, Color>(tuplepredicate, color));
	}

	@Override
	protected void save(Map<String, String> values) {
		int i = 0;
		for (Pair<TuplePredicate, Color> pair : shapeColors) {
			values.put("shape__" + i + "__color__r", Integer.toString(pair.getE2().getRed()));
			values.put("shape__" + i + "__color__g", Integer.toString(pair.getE2().getGreen()));
			values.put("shape__" + i + "__color__b", Integer.toString(pair.getE2().getBlue()));
			values.put("shape__" + i + "__predicate", pair.getE1().getExpression().getExpressionString());
			i++;
		}
		values.put("count", Integer.toString(i));
		values.put("stroke_width", Integer.toString(this.width));
	}

	@Override
	protected void open(IPhysicalOperator root) {
		try {
			for (Pair<TuplePredicate, Color> pair : this.shapeColors) {
				pair.getE1().init(root.getOutputSchema(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void process(Tuple<?> tuple) {
		boolean oneMatched = false;
		for (Pair<TuplePredicate, Color> entry : this.shapeColors) {
			if (!oneMatched && entry.getE1().evaluate(tuple)) {
				oneMatched = true;
				setVisibile(true);
				this.currentColor = entry.getE2();
				return;
			}
		}
		if (!oneMatched) {
			setVisibile(false);
		}
	}

	public List<Pair<TuplePredicate, Color>> getPredicates() {
		return shapeColors;
	}

	public void setPredicates(List<Pair<TuplePredicate, Color>> predicates) {
		this.shapeColors = predicates;
	}

	public void clearShapeColors() {
		this.shapeColors.clear();
		
	}

	public List<Pair<TuplePredicate, Color>> getShapeColors() {
		return Collections.unmodifiableList(shapeColors);
	}

	public Color getCurrentColor() {
		if(this.currentColor==null){
			return ColorConstants.black;
		}else{
			return this.currentColor;
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
