/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.Iterator;

import org.jvyamlb.nodes.Node;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface Constructor {
    boolean checkData();
    Object getData();
    Iterator eachDocument();
    Iterator iterator();
    Object constructDocument(final Node node);
    Object constructObject(final Node node);
    Object constructPrimitive(final Node node);
    Object constructScalar(final Node node);
    Object constructPrivateType(final Node node);
    Object constructSequence(final Node node);
    Object constructMapping(final Node node);
    Object constructPairs(final Node node);
    void doRecursionFix(Node node, Object obj);
    void addFixer(Node node, RecursiveFixer fixer);
    interface RecursiveFixer {
        void replace(final Node node, final Object real);
    }
    interface YamlConstructor {
        Object call(final Constructor self, final Node node);
    }

    interface YamlMultiConstructor {
        Object call(final Constructor self, final String pref, final Node node);
    }
}// Constructor
