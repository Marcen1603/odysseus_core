package de.uniol.inf.is.odysseus.wrapper.mail.physicaloperator.access;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;

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
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
	}

}
