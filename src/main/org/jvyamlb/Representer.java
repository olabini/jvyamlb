/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import org.jvyamlb.nodes.Node;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface Representer {
    void represent(final Object data) throws IOException;
    Node scalar(final String tag, final ByteList value, char style) throws IOException;
    Node seq(final String tag, final List sequence, final boolean flowStyle) throws IOException;
    Node map(final String tag, final Map mapping, final boolean flowStyle) throws IOException;
}// Representer
