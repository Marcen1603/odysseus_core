package de.offis.gui.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Possible meta types for operators.
 * 
 * @author Alexander Funk
 *
 */
public enum OperatorMetaTypes implements IsSerializable{
    AGGREGATE, 
    DIFFERENCE,
    COMPLEX,
    EXISTENCE,
    INTERSECTION,
    JOIN,
    MAP,
    PROJECT,
    RENAME, 
    SELECT,
    SORT,
    SPLIT,
    TOP,
    UNION,
    WINDOW,
    INPUT,
    OUTPUT;
}