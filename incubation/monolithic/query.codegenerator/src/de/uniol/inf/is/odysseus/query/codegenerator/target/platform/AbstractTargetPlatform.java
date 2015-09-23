package de.uniol.inf.is.odysseus.query.codegenerator.target.platform;

import java.util.concurrent.BlockingQueue;



import de.uniol.inf.is.odysseus.query.codegenerator.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.enums.UpdateMessageStatusType;

public abstract class AbstractTargetPlatform implements ITargetPlatform{
	
	private BlockingQueue<ProgressBarUpdate> progressBarQueue;
	private String targetPlatformName = "";
	
	public AbstractTargetPlatform(String targetPlatformName) {
		this.setTargetPlatformName(targetPlatformName);
	}
	
	@Override
	public BlockingQueue<ProgressBarUpdate> getProgressBarQueue() {
		return progressBarQueue;
	}
	
	@Override
	public void setProgressBarQueue(BlockingQueue<ProgressBarUpdate> progressBarQueue) {
		this.progressBarQueue = progressBarQueue;
	}

	@Override
	public String getTargetPlatformName() {
		return targetPlatformName;
	}

	@Override
	public void setTargetPlatformName(String targetPlatformName) {
		this.targetPlatformName = targetPlatformName;
	}
	
	@Override
	public void updateProgressBar(int value, String text, UpdateMessageStatusType statusType){
		if(progressBarQueue!= null){
			try {
				progressBarQueue.put(new ProgressBarUpdate(value, text,statusType));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
}
