/*
 * This file is part of Lorenz, licensed under the MIT License (MIT).
 *
 * Copyright (c) Jamie Mansfield <https://www.jamierocks.uk/>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.jamiemansfield.lorenz.model;

import me.jamiemansfield.lorenz.MappingsContainer;

/**
 * Represents a {@link BaseMapping} for a method.
 */
public class MethodMapping extends BaseMapping {

    private final ClassMapping parent;
    private final String obfuscatedSignature;

    /**
     * Constructs a new {@link MethodMapping} with the given parameters.
     *
     * @param parent The parent {@link ClassMapping}
     * @param obfuscated The obfuscated name of the method
     * @param obfuscatedSignature The obfuscated signature of the method
     * @param deobfuscated The deobfuscated name of the method
     */
    public MethodMapping(final ClassMapping parent, final String obfuscated,
            final String obfuscatedSignature, final String deobfuscated) {
        super(obfuscated, deobfuscated);
        this.parent = parent;
        this.obfuscatedSignature = obfuscatedSignature;
    }

    /**
     * Gets the obfuscated signature of the method.
     *
     * @return The obfuscated signature
     */
    public String getObfuscatedSignature() {
        return this.obfuscatedSignature;
    }

    /**
     * Gets the deobfuscated signature of the method.
     *
     * @return The deobfuscated signature
     */
    public String getDeobfuscatedSignature() {
        final String innerContent = this.obfuscatedSignature.substring(this.obfuscatedSignature.indexOf("(") + 1,
                this.obfuscatedSignature.indexOf(")"));
        final String outerContent = this.obfuscatedSignature.substring(this.obfuscatedSignature.indexOf(")") + 1);

        String modifiedType = this.obfuscatedSignature;

        for (final String type : innerContent.split(";")) {
            if (type.startsWith("L")) {
                final String newType = type.substring(1);
                if (this.getMappings().getClassMappings().containsKey(newType)) {
                    modifiedType = modifiedType.replace(newType,
                            this.getMappings().getClassMappings().get(newType).getDeobfuscatedName());
                }
            }
        }

        if (outerContent.startsWith("L")) {
            final String outerType = outerContent.substring(1, outerContent.length() - 1);
            if (this.getMappings().getClassMappings().containsKey(outerType)) {
                modifiedType = modifiedType.replace(outerType,
                        this.getMappings().getClassMappings().get(outerType).getDeobfuscatedName());
            }
        }

        return modifiedType;
    }

    @Override
    public String getFullObfuscatedName() {
        return String.format("%s/%s", this.parent.getFullObfuscatedName(), this.getObfuscatedName());
    }

    @Override
    public String getFullDeobfuscatedName() {
        return String.format("%s/%s", this.parent.getFullDeobfuscatedName(), this.getDeobfuscatedName());
    }

    @Override
    public MappingsContainer getMappings() {
        return this.parent.getMappings();
    }

}
