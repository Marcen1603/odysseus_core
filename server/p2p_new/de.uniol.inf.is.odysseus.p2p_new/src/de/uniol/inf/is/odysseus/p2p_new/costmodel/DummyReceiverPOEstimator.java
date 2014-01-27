package de.uniol.inf.is.odysseus.p2p_new.costmodel;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IDataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.DummyReceiverPO;

@SuppressWarnings("rawtypes")
public class DummyReceiverPOEstimator implements IOperatorEstimator<DummyReceiverPO> {

	@Override
	public Class<DummyReceiverPO> getOperatorClass() {
		return DummyReceiverPO.class;
	}

	@Override
	public OperatorEstimation<DummyReceiverPO> estimateOperator(DummyReceiverPO instance, List<OperatorEstimation<?>> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		OperatorEstimation<DummyReceiverPO> estimation = new OperatorEstimation<DummyReceiverPO>(instance);

		estimation.setHistograms(Maps.<SDFAttribute, IHistogram>newHashMap());
		estimation.setSelectivity(1.0);
		
		IDataStream<DummyReceiverPO> stream = new DataStream<DummyReceiverPO>(instance, instance.getDataRate(), instance.getIntervalLength());
		estimation.setDataStream(stream);
		estimation.setDetailCost(new OperatorDetailCost<DummyReceiverPO>(instance, 100, 0.00005));
		
		return estimation;
	}
}
