package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import java.util.Properties;

public class IMAPMailConfiguration extends MailConfiguration {

	@Override
	public String getProviderName() {
		return "imaps";
	}

	@Override
	public int getDefaultPort() {
		return 993;
	}

	@Override
	protected void InitProperties(Properties properties) {
		properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.socketFactory.fallback", "false");
        properties.setProperty("mail.imap.host", this.getHost());
        properties.setProperty("mail.imap.port", Integer.toString(this.getPort()));
        properties.setProperty("mail.imap.socketFactory.port", Integer.toString(this.getPort()));
	}

}
