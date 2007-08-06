package mg.dynaquest.queryexecution.event;

/**
 * @author  Marco Grawunder
 */
public enum POEventType {
	CloseInit, NextInit, OpenInit, ReadInit, WriteInit,
	CloseDone, NextDone, OpenDone, ReadDone, WriteDone,
	Exeception, OutOfMemoryException,
	BurstyBlockSizeUnexpected, BurstyDelivery, DeferredDelivery, EmptyResultSet,
	NumberOfObjectRead, NumberOfObjectsWritten, ProcessingFinished, SlowDelivery, 
	InitTimeout, ReadTimeout, WriteTimeout,
	POElementBufferMaxCapacityReached}
