package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NewFilenamePunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CreateNewFileNamePunctuationAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class RelationalCreateNewFilenamePunctuationPO<R extends Tuple<? extends ITimeInterval>>extends
		AbstractPipe<R, R> {

	private IPredicate<? super R> puncPredicate;
	private RelationalExpression<? extends IMetaAttribute> filenameExpression;

	@SuppressWarnings("unchecked")
	public RelationalCreateNewFilenamePunctuationPO(CreateNewFileNamePunctuationAO ao) {
		puncPredicate = (IPredicate<? super R>) ao.getCreateNewPuncPredicate();
		filenameExpression = new RelationalExpression<IMetaAttribute>(ao.getFilenameExpression());
		filenameExpression.initVars(ao.getInputSchema());
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void process_next(R object, int port) {
		R output = object;
		if (puncPredicate.evaluate(object)){
			String filename = String.valueOf(this.filenameExpression.evaluate((Tuple) object, null, null));
			sendPunctuation(new NewFilenamePunctuation(object.getMetadata().getStart(), String.valueOf(filename)));
			
		}
		
		transfer(output);
	}

	
}
