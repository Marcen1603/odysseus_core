package de.uniol.inf.is.odysseus.p2p.splitting.sourcesplit;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PAO;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSourceAO;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

public class SourceSplit extends AbstractSplittingStrategy {

	static Logger logger = LoggerFactory.getLogger(SourceSplit.class);
	
	@Override
	public void initializeService() {
		IExecutionHandler<AbstractSplittingStrategy> handler = new SourceSplitExecutionHandler<AbstractSplittingStrategy>();
		handler.setPeer(getPeer());
		handler.setFunction(this);
		handler.setProvidedLifecycle(Lifecycle.SPLIT);
		getPeer().bindExecutionHandler(handler);
	}

	@Override
	public ArrayList<ILogicalOperator> splitPlan(ILogicalOperator plan) {
		ArrayList<ILogicalOperator> splitList = new ArrayList<ILogicalOperator>();
		logger.debug(AbstractTreeWalker.prefixWalk(plan,
				new AlgebraPlanToStringVisitor(true)));
		splitPlan(plan, AdvertisementTools.createSocketAdvertisement().toString(), splitList);
		logger.debug("Zeige Splits--------------------------");
		for (ILogicalOperator iLogicalOperator : splitList) {
			logger.debug(AbstractTreeWalker.prefixWalk(iLogicalOperator,
					new AlgebraPlanToStringVisitor(true)));
		}
		logger.debug("Zeige Splits Ende-------------------");
		return splitList;
	}

	private void splitPlan(ILogicalOperator iLogicalOperator, String current,
			List<ILogicalOperator> splitList) {
		int outputCount = iLogicalOperator.getSubscribedToSource().size();
		String temp = null;
		P2PSinkAO p2psink = new P2PSinkAO(current);
		String adv = AdvertisementTools.createSocketAdvertisement().toString();
		P2PSourceAO p2psource = new P2PSourceAO(p2psink.getAdv());
		// für den ersten Operator
		if (iLogicalOperator.getSubscriptions().isEmpty()) {
			P2PSinkAO tempAO = new P2PSinkAO(AdvertisementTools.createSocketAdvertisement()
					.toString());
			tempAO.subscribeToSource(iLogicalOperator, 0, 0,
					iLogicalOperator.getOutputSchema());
			splitList.add(tempAO);
		}
		if (!(iLogicalOperator instanceof P2PAO)) {
			// Bei Operatoren mit zwei Eingabeports
			if (outputCount == 2) {

				logger.debug("Hier kommen wir auch rein, aber wir machen hier garnichts");
				String adv2 = AdvertisementTools.createSocketAdvertisement().toString();
				// P2PSourceAO p2paccess2 = new P2PSourceAO(adv2);
				temp = adv2;
				P2PSinkAO p2psink2 = new P2PSinkAO(temp);
				// P2PSourceAO p2psource2 = new P2PSourceAO(p2psink2.getAdv());
				RestructHelper.insertOperator(p2psource, iLogicalOperator, 0,
						0, 0);
				// RestructHelper.insertOperator(p2psource2, iLogicalOperator,
				// 1, 0, 0);
				RestructHelper.insertOperator(p2psink, p2psource, 0, 0, 0);
				// RestructHelper.insertOperator(p2psink2, p2psource2,0,0,0);
			} else if (outputCount == 1) {
				// RestructHelper.insertOperator(p2psource, iLogicalOperator, 0,
				// 0, 0);
				// RestructHelper.insertOperator(p2psink, p2psource,0,0,0);
				// temp = adv;
			}
		} else if (iLogicalOperator instanceof P2PSinkAO) {
			splitList.add((AbstractLogicalOperator) iLogicalOperator);
		}
		for (int i = 0; i < outputCount; ++i) {
			if (i == 1) {
				adv = temp;
			}
			if (iLogicalOperator instanceof AccessAO) {
				return;
			}
			splitPlan(iLogicalOperator.getSubscribedToSource(i).getTarget(),
					adv, splitList);
			// Nach der Rekursion Verbindung zwischen P2PSource und P2PSinks
			// kappen
			if (iLogicalOperator instanceof P2PSourceAO) {
				((P2PSourceAO) iLogicalOperator)
						.unsubscribeFromSource(iLogicalOperator
								.getSubscribedToSource(0));
			}
		}
	}

	
	@Override
	public String getName() {
		return "SplitSource";
	}

}
