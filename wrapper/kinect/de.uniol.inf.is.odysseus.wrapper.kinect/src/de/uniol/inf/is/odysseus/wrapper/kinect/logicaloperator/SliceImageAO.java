package de.uniol.inf.is.odysseus.wrapper.kinect.logicaloperator;

import java.awt.Rectangle;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import com.google.gson.*;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SLICEIMAGE", doc="Slices images from the Kinect Camera", category={LogicalOperatorCategory.BASE})
public class SliceImageAO extends UnaryLogicalOp {
	private static final long serialVersionUID = 1050849455089671820L;
	private Rectangle slice;
	private static Gson GSON = new Gson();

	public SliceImageAO() {
	}
	
	public SliceImageAO(SliceImageAO sliceImageAO) {
		super(sliceImageAO);
		this.slice = sliceImageAO.slice;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SliceImageAO(this);
	}

	
    @Parameter(type = StringParameter.class, name = "slice")
    public void setSlice(String slice) {
        this.slice = GSON.fromJson(slice, Rectangle.class);
    }
    
    public String getSlice() {
    	StringBuffer sbf = new StringBuffer();
    	sbf.append("{");
    	sbf.append("x:"+slice.getX()+",");
    	sbf.append("y:"+slice.getY()+",");
    	sbf.append("width:"+slice.getWidth()+",");
    	sbf.append("height:"+slice.getHeight());
    	sbf.append("}");
        return sbf.toString();
    }
    
    public Rectangle getRectangle() {
    	return this.slice;
    }
}
