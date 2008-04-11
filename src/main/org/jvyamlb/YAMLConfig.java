/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface YAMLConfig {
    YAMLConfig indent(final int indent);
    int indent();
    YAMLConfig useHeader(final boolean useHeader);
    boolean useHeader();
    YAMLConfig useVersion(final boolean useVersion);
    boolean useVersion();
    YAMLConfig version(final String version);
    String version();
    YAMLConfig explicitStart(final boolean expStart);
    boolean explicitStart();
    YAMLConfig explicitEnd(final boolean expEnd);
    boolean explicitEnd();
    YAMLConfig anchorFormat(final String format);
    String anchorFormat();
    YAMLConfig explicitTypes(final boolean expTypes);
    boolean explicitTypes();
    YAMLConfig canonical(final boolean canonical);
    boolean canonical();
    YAMLConfig bestWidth(final int bestWidth);
    int bestWidth();
    YAMLConfig useBlock(final boolean useBlock);
    boolean useBlock();
    YAMLConfig useFlow(final boolean useFlow);
    boolean useFlow();
    YAMLConfig usePlain(final boolean usePlain);
    boolean usePlain();
    YAMLConfig useSingle(final boolean useSingle);
    boolean useSingle();
    YAMLConfig useDouble(final boolean useDouble);
    boolean useDouble();
}// YAMLConfig
