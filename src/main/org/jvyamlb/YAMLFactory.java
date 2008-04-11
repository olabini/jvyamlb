/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

import java.io.InputStream;
import java.io.OutputStream;

import org.jruby.util.ByteList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface YAMLFactory {
    Scanner createScanner(final ByteList io);
    Scanner createScanner(final InputStream io);
    Parser createParser(final Scanner scanner);
    Parser createParser(final Scanner scanner, final YAMLConfig cfg);
    Resolver createResolver();
    Composer createComposer(final Parser parser, final Resolver resolver);
    Constructor createConstructor(final Composer composer);
    Emitter createEmitter(final OutputStream output, final YAMLConfig cfg);
    Serializer createSerializer(final Emitter emitter, final Resolver resolver, final YAMLConfig cfg);
    Representer createRepresenter(final Serializer serializer, final YAMLConfig cfg);
}// YAMLFactory
