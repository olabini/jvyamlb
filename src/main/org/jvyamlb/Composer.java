/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.Iterator;

import org.jvyamlb.nodes.Node;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface Composer {
    boolean checkNode();
    Node getNode();
    Iterator eachNode();
    Iterator iterator();
}// Composer
