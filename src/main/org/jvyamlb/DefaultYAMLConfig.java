/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.jvyamlb;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class DefaultYAMLConfig implements YAMLConfig {
    private int indent = 2;
    private boolean useHeader = false;
    private boolean useVersion = false;
    private String version = "1.1";
    private boolean expStart = true;
    private boolean expEnd = false;
    private String format = "id{0,number,000}";
    private boolean expTypes = false;
    private boolean canonical = false;
    private int bestWidth = 80;
    private boolean useBlock = false;
    private boolean useFlow = false;
    private boolean usePlain = false;
    private boolean useSingle = false;
    private boolean useDouble = false;
    
    public YAMLConfig indent(final int indent) { this.indent = indent; return this; }
    public int indent() { return this.indent; }
    public YAMLConfig useHeader(final boolean useHeader) { this.useHeader = useHeader; return this; }
    public boolean useHeader() { return this.useHeader; }
    public YAMLConfig useVersion(final boolean useVersion) { this.useVersion = useVersion; return this; }
    public boolean useVersion() { return this.useVersion; }
    public YAMLConfig version(final String version) { this.version = version; return this; }
    public String version() { return this.version; }
    public YAMLConfig explicitStart(final boolean expStart) { this.expStart = expStart; return this; }
    public boolean explicitStart() { return this.expStart; }
    public YAMLConfig explicitEnd(final boolean expEnd) { this.expEnd = expEnd; return this; }
    public boolean explicitEnd() { return this.expEnd; }
    public YAMLConfig anchorFormat(final String format) { this.format = format; return this; }
    public String anchorFormat() { return this.format; }
    public YAMLConfig explicitTypes(final boolean expTypes) { this.expTypes = expTypes; return this; }
    public boolean explicitTypes() { return this.expTypes; }
    public YAMLConfig canonical(final boolean canonical) { this.canonical = canonical; return this; }
    public boolean canonical() { return this.canonical; }
    public YAMLConfig bestWidth(final int bestWidth) { this.bestWidth = bestWidth; return this; }
    public int bestWidth() { return this.bestWidth; }
    public YAMLConfig useBlock(final boolean useBlock) { this.useBlock = useBlock; return this; }
    public boolean useBlock() { return this.useBlock; }
    public YAMLConfig useFlow(final boolean useFlow) { this.useFlow = useFlow; return this; }
    public boolean useFlow() { return this.useFlow; }
    public YAMLConfig usePlain(final boolean usePlain) { this.usePlain = usePlain; return this; }
    public boolean usePlain() { return this.usePlain; }
    public YAMLConfig useSingle(final boolean useSingle) { this.useSingle = useSingle; return this; }
    public boolean useSingle() { return this.useSingle; }
    public YAMLConfig useDouble(final boolean useDouble) { this.useDouble = useDouble; return this; }
    public boolean useDouble() { return this.useDouble; }
}// DefaultYAMLConfig
