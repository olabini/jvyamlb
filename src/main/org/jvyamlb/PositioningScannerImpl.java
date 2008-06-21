/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.io.InputStream;

import org.jruby.util.ByteList;
import org.jvyamlb.tokens.PositionedStreamEndToken;
import org.jvyamlb.tokens.PositionedStreamStartToken;
import org.jvyamlb.tokens.StreamStartToken;
import org.jvyamlb.tokens.StreamEndToken;

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
        return new Position(0, 0, 0, 0);
    }

    protected StreamStartToken getStreamStart() {
        return new PositionedStreamStartToken(getPosition());
    }

    protected StreamEndToken getStreamEnd() {
        return new PositionedStreamEndToken(getPosition());
    }
}// PositioningScannerImpl
