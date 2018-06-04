package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import java.util.Properties;

public class POP3MailConfiguration extends MailConfiguration {

	private static final String PROVIDER_NAME = "pop3";
	//private static final int DEFAULT_PORT = 110;
	private static final int DEFAULT_PORT_SECURED = 995;

	@Override
	public String getProviderName() {
		return PROVIDER_NAME;
	}

	@Override
	public int getDefaultPort() {
		return DEFAULT_PORT_SECURED;
	}

	@Override
	protected void InitProperties(Properties properties) {
		properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
        properties.setProperty("mail.pop3.host", this.getHost());
        properties.setProperty("mail.pop3.port", Integer.toString(this.getPort()));
        properties.setProperty("mail.pop3.socketFactory.port", Integer.toString(this.getPort()));
	}

}
