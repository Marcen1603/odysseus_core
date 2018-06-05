package de.uniol.inf.is.odysseus.rcp.info;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.infoservice.IInfoServiceListener;
import de.uniol.inf.is.odysseus.core.infoservice.InfoType;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionErrorDialog;

public class RCPInfoServiceListener implements IInfoServiceListener {

	private static final Logger LOG = LoggerFactory.getLogger(RCPInfoServiceListener.class);

	@Override
	public void newInfo(InfoType infoType, String message, Throwable throwable, String source) {
		if (Activator.isStarted()) {
			IStatus status = createStatus(infoType, message, throwable, source);

			if (infoType == InfoType.ERROR) {
				ExceptionErrorDialog.open(createStatus(infoType, message, throwable, source), throwable);
				// new ExceptionWindow(message, throwable);
			}

			Activator.getInstance().getLog().log(status);
		}
		switch (infoType) {
		case ERROR:
			LOG.error(message, throwable);
			break;
		case INFORMATION:
			LOG.info(message);
			break;
		case WARNING:
			LOG.warn(message, throwable);
			break;
		default:
			// do nothing
			break;
		}
	}

	private static IStatus createStatus(InfoType infoType, String message, Throwable throwable, String source) {
		if (throwable == null) {
			return new Status(toStatus(infoType), source, message);
		}

		return new Status(toStatus(infoType), source, message, throwable);
	}

	private static int toStatus(InfoType infoType) {
		switch (infoType) {
		case ERROR:
			return IStatus.ERROR;
		case INFORMATION:
			return IStatus.INFO;
		case WARNING:
			return IStatus.WARNING;
		default:
			return IStatus.INFO;
		}
	}
}
