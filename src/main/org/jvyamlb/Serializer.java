/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.io.IOException;

import org.jvyamlb.nodes.Node;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface Serializer {
    void open() throws IOException;
    void close() throws IOException;
    void serialize(final Node node) throws IOException;
}// Serializer
