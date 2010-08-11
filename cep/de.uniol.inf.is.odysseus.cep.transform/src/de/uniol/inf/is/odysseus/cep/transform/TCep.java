package de.uniol.inf.is.odysseus.cep.transform;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.cep.CepAO;
import de.uniol.inf.is.odysseus.cep.epa.CepOperator;
import de.uniol.inf.is.odysseus.cep.epa.eventgeneration.IComplexEventFactory;
import de.uniol.inf.is.odysseus.cep.epa.eventgeneration.relational.RelationalCreator;
import de.uniol.inf.is.odysseus.cep.epa.eventreading.relational.RelationalReader;
import de.uniol.inf.is.odysseus.intervalapproach.TIInputStreamSyncArea;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TCep extends AbstractTransformationRule<CepAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void transform(CepAO cepAO,
			TransformationConfiguration transformConfig) {
		Map<Integer, RelationalReader> rMap = new HashMap<Integer, RelationalReader>();
		for (LogicalSubscription s : cepAO.getSubscribedToSource()) {
			rMap.put(s.getSinkInPort(), new RelationalReader(s.getSchema(),
					cepAO.getInputTypeName(s.getSinkInPort())));
		}
		IComplexEventFactory complexEventFactory = new RelationalCreator();
		CepOperator cepPO = null;
		try {
			cepPO = new CepOperator(cepAO.getStateMachine(), rMap,
					complexEventFactory, false, new TIInputStreamSyncArea(cepAO
							.getSubscribedToSource().size()),
					new TITransferArea(cepAO.getSubscribedToSource().size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		cepPO.setOutputSchema(cepAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig
				.getTransformationHelper().replace(cepAO, cepPO);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(cepAO);
	}

	@Override
	public boolean isExecutable(CepAO operator,
			TransformationConfiguration transformConfig) {
		if (transformConfig.getDataType().equals("relational")) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "CepAO --> CepOperator (Relational)";
	}

}
