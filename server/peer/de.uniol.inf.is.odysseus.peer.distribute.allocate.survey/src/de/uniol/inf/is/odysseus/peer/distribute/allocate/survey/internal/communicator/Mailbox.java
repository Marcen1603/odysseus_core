package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.internal.communicator;

import java.util.List;

import net.jxta.id.ID;

import com.google.common.collect.Lists;

public class Mailbox<T> {
	private final ID sharedQueryId;
	private final List<T> mails;
	private final long createTime;
	
	public Mailbox(ID sharedQueryId) {
		mails = Lists.newArrayList();
		createTime = System.currentTimeMillis();
		this.sharedQueryId = sharedQueryId;
	}
	
	public ID getSharedQueryId() {
		return sharedQueryId;
	}

	public List<T> getMails() {
		return mails;
	}

	public long getCreateTime() {
		return createTime;
	}
}
