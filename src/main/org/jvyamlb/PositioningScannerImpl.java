/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.io.InputStream;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PositioningScannerImpl extends ScannerImpl implements PositioningScanner {
    public PositioningScannerImpl(final InputStream stream) {
        super(stream);
    }

    public PositioningScannerImpl(final ByteList stream) {
        super(stream);
    }

    public PositioningScannerImpl(final String stream) {
        super(stream);
    }    

    public Position getPosition() {
        return null;
    }
}// PositioningScannerImpl
