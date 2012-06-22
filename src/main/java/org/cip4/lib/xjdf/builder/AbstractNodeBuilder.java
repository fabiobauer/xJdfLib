/**
 * All rights reserved by
 * 
 * flyeralarm GmbH
 * Alfred-Nobel-Straße 18
 * 97080 Würzburg
 *
 * info@flyeralarm.com
 * http://www.flyeralarm.com
 */
package org.cip4.lib.xjdf.builder;

/**
 * Abstract Builder implementation where all builders are derived from.
 * @author s.meissner
 * @date 05.03.2012
 */
public abstract class AbstractNodeBuilder<T> {

	private final T node;

	/**
	 * Default constructor.
	 */
	protected AbstractNodeBuilder(T node) {
		// initialize node
		this.node = node;
	}

	/**
	 * Getter for node attribute.
	 * @return the node
	 */
	protected T getNode() {
		return node;
	}

	/**
	 * Build and return the Node.
	 * @return Node as DOM tree.
	 */
	public final T build() {
		return node;
	}
}