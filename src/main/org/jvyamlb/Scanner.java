/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.util.Iterator;

import org.jvyamlb.tokens.Token;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface Scanner {
    boolean checkToken(final Class[] choices);
    Token peekToken();
    Token peekToken(int index);
    Token getToken();
    Iterator eachToken();
    Iterator iterator();
}// Scanner
