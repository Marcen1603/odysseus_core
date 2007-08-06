package mg.dynaquest.queryoptimization.trafo.rules;

import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.streaming.algebra.WindowPO;
import mg.dynaquest.queryexecution.po.streaming.algebra.WindowType;
import mg.dynaquest.queryexecution.po.streaming.base.SlidingWindowPO;
import mg.dynaquest.queryexecution.po.streaming.base.TumblingWindowPO;

public class WindowPOTransformationRule extends TransformationRule {

	@Override
	public int getNoOfTransformations() {
		return 1;
	}

	@Override
	public PlanOperator transform(SupportsCloneMe logicalPO, int no)
			throws TransformationNotApplicableExeception {
		return transformToPhysicalWindow((WindowPO) logicalPO);
	}

	private PlanOperator transformToPhysicalWindow(WindowPO windowPO) 
		throws TransformationNotApplicableExeception {
		PlanOperator po = null;
		if (windowPO.getWindowType().equals(WindowType.Sliding)){
			po = new SlidingWindowPO(windowPO);
		}
		if (windowPO.getWindowType().equals(WindowType.Tumbling)){
			po = new TumblingWindowPO(windowPO);
		}
		if (po == null){
			throw new TransformationNotApplicableExeception("Wrong Windowtype");
		}
		po.setInputPO(0, windowPO.getPhysInputPO());
		return po;		
	}

}
