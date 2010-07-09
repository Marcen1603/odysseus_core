/**
 * 
 */
package de.uniol.inf.is.odysseus.parser.pql.benchmark;

public class BatchItem {
	public BatchItem(int size, long wait) {
		this.size = size;
		this.wait = wait;
	}
	public final int size;
	public final long wait;
}