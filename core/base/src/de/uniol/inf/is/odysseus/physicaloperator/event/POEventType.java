package de.uniol.inf.is.odysseus.physicaloperator.event;

import de.uniol.inf.is.odysseus.event.IEventType;

/**
 * @author  Marco Grawunder
 */
//public enum POEventType {
//	CloseInit, NextInit, OpenInit, ReadInit, WriteInit,
//	CloseDone, NextDone, OpenDone, ReadDone, WriteDone,
//	Done,PushInit, PushDone,
//	ProcessInit, ProcessDone,
//	Exception, OutOfMemoryException,
//	BurstyBlockSizeUnexpected, BurstyDelivery, DeferredDelivery, EmptyResultSet,
//	NumberOfObjectRead, NumberOfObjectsWritten, ProcessingFinished, SlowDelivery, 
//	InitTimeout, ReadTimeout, WriteTimeout,
//	POElementBufferMaxCapacityReached,
//	PutElementToBuffer, GetElementFromBuffer}
public enum POEventType implements IEventType{
	OpenInit, OpenDone,
	PostPriorisation,
	ProcessInit, ProcessDone, ProcessInitNeg, ProcessDoneNeg,
	PushInit, PushDone, PushInitNeg, PushDoneNeg,
	ProcessPunctuationDone,
	Done, Activated, Deactivated, Blocked, Unblocked, PushListInit, PushListDone, CloseInit, CloseDone
}
