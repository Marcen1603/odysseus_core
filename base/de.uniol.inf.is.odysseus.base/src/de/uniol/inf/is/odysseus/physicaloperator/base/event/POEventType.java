package de.uniol.inf.is.odysseus.physicaloperator.base.event;

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
public enum POEventType {
	OpenInit, OpenDone,
	PostPriorisation,
	ProcessInit, ProcessDone, ProcessInitNeg, ProcessDoneNeg,
	PushInit, PushDone, PushInitNeg, PushDoneNeg,
	Done, Activated, Deactivated, Blocked, Unblocked
}
