package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;

import net.jxta.pipe.OutputPipe;
import net.jxta.protocol.PipeAdvertisement;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public abstract class OutputPipeResolver extends StoppableThread {
	
	private static final int TIMEOUT_MILLIS = 2000;
	private final PipeAdvertisement pipeAdvertisement;
	
	public OutputPipeResolver( PipeAdvertisement pipeAdvertisement ) {
		this.pipeAdvertisement = Preconditions.checkNotNull(pipeAdvertisement, "Pipe advertisement for resolving outputpipe must not be null!");
		
		setDaemon(true);
		setName("Outputpipe resolver");
	}
	
	@Override
	public void run() {
		OutputPipe outputPipe = null;
		while( isRunning() ) {
			try {
				outputPipe = P2PNewPlugIn.getPipeService().createOutputPipe(pipeAdvertisement, TIMEOUT_MILLIS);
				outputPipeResolved(outputPipe);
				stopRunning();
			} catch (IOException ex) {
				// used to regulary check if thread should be stopped
			}
		}
		
		if( outputPipe == null ) {
			outputPipeFailed();
		}
	}
	
	public abstract void outputPipeResolved( OutputPipe outputPipe );
	public abstract void outputPipeFailed();
}
