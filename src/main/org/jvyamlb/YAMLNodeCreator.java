/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.io.IOException;

import org.jvyamlb.nodes.Node;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface YAMLNodeCreator {
    String taguri();
    Node toYamlNode(Representer representer) throws IOException;
}// YAMLNodeCreator
