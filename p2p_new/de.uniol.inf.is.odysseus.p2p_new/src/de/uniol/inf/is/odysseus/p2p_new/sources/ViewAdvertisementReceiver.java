package de.uniol.inf.is.odysseus.p2p_new.sources;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.service.DataDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;

public class ViewAdvertisementReceiver implements IAdvertisementListener {

	@Override
	public void advertisementOccured(IAdvertisementManager sender, Advertisement adv) {
		
		final ViewAdvertisement viewAdvertisement = (ViewAdvertisement)adv;
		final JxtaReceiverAO receiverOperator = new JxtaReceiverAO();
			
		receiverOperator.setPipeID(viewAdvertisement.getPipeID().toString());
		receiverOperator.setOutputSchema(viewAdvertisement.getOutputSchema());
		receiverOperator.setSchema(viewAdvertisement.getOutputSchema().getAttributes());
		receiverOperator.setName(viewAdvertisement.getViewName() + "_Receive" );
		receiverOperator.setDestinationName("local");
		
		final RenameAO renameNoOp = new RenameAO();
		renameNoOp.setDestinationName("local");
		renameNoOp.setNoOp(true);
		
		receiverOperator.subscribeSink(renameNoOp, 0, 0, receiverOperator.getOutputSchema());
		renameNoOp.initialize();
	
		DataDictionaryService.get().setView(viewAdvertisement.getViewName(), renameNoOp, SessionManagementService.getActiveSession());
	}

	@Override
	public boolean isSelected(Advertisement advertisement) {
		if( advertisement instanceof ViewAdvertisement ) {
			return !DataDictionaryService.get().containsViewOrStream(((ViewAdvertisement)advertisement).getViewName(), SessionManagementService.getActiveSession());
		}
		return false;
	}

}
