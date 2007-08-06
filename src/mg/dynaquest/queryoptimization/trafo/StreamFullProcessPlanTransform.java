package mg.dynaquest.queryoptimization.trafo;

import mg.dynaquest.queryoptimization.trafo.rules.StreamingJoinTransformationRule;
import mg.dynaquest.queryoptimization.trafo.rules.WindowPOTransformationRule;

public class StreamFullProcessPlanTransform extends FullProcessPlanTransform {

	public StreamFullProcessPlanTransform(){
		// Regel für den Join überschreiben
		trafos.put("mg.dynaquest.queryexecution.po.algebra.JoinPO", new StreamingJoinTransformationRule());
		trafos.put("mg.dynaquest.queryexecution.po.streaming.algebra.WindowPO", new WindowPOTransformationRule());
	}
}
