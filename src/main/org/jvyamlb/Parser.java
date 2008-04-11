/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.Iterator;

import org.jvyamlb.events.Event;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface Parser {
    boolean checkEvent(final Class[] choices);
    Event peekEvent();
    Event getEvent();
    Iterator eachEvent();
    Iterator iterator();
    void parseStream();
    Event parseStreamNext();
}// Parser
