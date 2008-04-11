/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.List;

import org.jvyamlb.nodes.Node;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface Resolver {
    void descendResolver(final Node currentNode, final Object currentIndex);
    void ascendResolver();
    boolean checkResolverPrefix(final int depth, final List path, final Class kind, final Node currentNode, final Object currentIndex);
    String resolve(final Class kind, final ByteList value, final boolean[] implicit);
}// Resolver
