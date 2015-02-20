package de.uniol.inf.is.odysseus.trajectory.compare.uots.graph;

import de.uniol.inf.is.odysseus.trajectory.util.IObjectLoader;

/**
 * An extension to <tt>IObjectLoader</tt> in order load a <tt>NetGraph</tt>.
 *  
 * @param <P> type of the parameter which is required to load a <tt>NetGraph</tt>
 * @param <A> type of the additional Information which is required to load a <tt>NetGraph</tt>
 * 
 * @author marcus
 */
public interface IGraphLoader<P, A> extends IObjectLoader<NetGraph, P, A> {

}
