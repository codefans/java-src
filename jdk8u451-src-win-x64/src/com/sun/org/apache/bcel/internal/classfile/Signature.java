/*
 * Copyright (c) 2007, 2025, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.org.apache.bcel.internal.classfile;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.sun.org.apache.bcel.internal.Const;
import com.sun.org.apache.bcel.internal.util.Args;

/**
 * This class is derived from <em>Attribute</em> and represents a reference to a GJ attribute.
 *
 * @see Attribute
 */
public final class Signature extends Attribute {

    /**
     * Extends ByteArrayInputStream to make 'unreading' chars possible.
     */
    private static final class MyByteArrayInputStream extends ByteArrayInputStream {

        MyByteArrayInputStream(final String data) {
            super(data.getBytes(StandardCharsets.UTF_8));
        }

        String getData() {
            return new String(buf, StandardCharsets.UTF_8);
        }

        void unread() {
            if (pos > 0) {
                pos--;
            }
        }
    }

    private static boolean identStart(final int ch) {
        return ch == 'T' || ch == 'L';
    }

    // @since 6.0 is no longer final
    public static boolean isActualParameterList(final String s) {
        return s.startsWith("L") && s.endsWith(">;");
    }

    // @since 6.0 is no longer final
    public static boolean isFormalParameterList(final String s) {
        return s.startsWith("<") && s.indexOf(':') > 0;
    }

    private static void matchGJIdent(final MyByteArrayInputStream in, final StringBuilder buf) {
        int ch;
        matchIdent(in, buf);
        ch = in.read();
        if (ch == '<' || ch == '(') { // Parameterized or method
            // System.out.println("Enter <");
            buf.append((char) ch);
            matchGJIdent(in, buf);
            while ((ch = in.read()) != '>' && ch != ')') { // List of parameters
                if (ch == -1) {
                    throw new IllegalArgumentException("Illegal signature: " + in.getData() + " reaching EOF");
                }
                // System.out.println("Still no >");
                buf.append(", ");
                in.unread();
                matchGJIdent(in, buf); // Recursive call
            }
            // System.out.println("Exit >");
            buf.append((char) ch);
        } else {
            in.unread();
        }
        ch = in.read();
        if (identStart(ch)) {
            in.unread();
            matchGJIdent(in, buf);
        } else if (ch == ')') {
            in.unread();
        } else if (ch != ';') {
            throw new IllegalArgumentException("Illegal signature: " + in.getData() + " read " + (char) ch);
        }
    }

    private static void matchIdent(final MyByteArrayInputStream in, final StringBuilder buf) {
        int ch;
        if ((ch = in.read()) == -1) {
            throw new IllegalArgumentException("Illegal signature: " + in.getData() + " no ident, reaching EOF");
        }
        // System.out.println("return from ident:" + (char)ch);
        if (!identStart(ch)) {
            final StringBuilder buf2 = new StringBuilder();
            int count = 1;
            while (Character.isJavaIdentifierPart((char) ch)) {
                buf2.append((char) ch);
                count++;
                ch = in.read();
            }
            if (ch == ':') { // Ok, formal parameter
                final int skipExpected = "Ljava/lang/Object".length();
                final long skipActual = in.skip(skipExpected);
                if (skipActual != skipExpected) {
                    throw new IllegalStateException(String.format("Unexpected skip: expected=%,d, actual=%,d", skipExpected, skipActual));
                }
                buf.append(buf2);
                ch = in.read();
                in.unread();
                // System.out.println("so far:" + buf2 + ":next:" +(char)ch);
            } else {
                for (int i = 0; i < count; i++) {
                    in.unread();
                }
            }
            return;
        }
        final StringBuilder buf2 = new StringBuilder();
        ch = in.read();
        do {
            buf2.append((char) ch);
            ch = in.read();
            // System.out.println("within ident:"+ (char)ch);
        } while (ch != -1 && (Character.isJavaIdentifierPart((char) ch) || ch == '/'));
        buf.append(Utility.pathToPackage(buf2.toString()));
        // System.out.println("regular return ident:"+ (char)ch + ":" + buf2);
        if (ch != -1) {
            in.unread();
        }
    }

    public static String translate(final String s) {
        // System.out.println("Sig:" + s);
        final StringBuilder buf = new StringBuilder();
        matchGJIdent(new MyByteArrayInputStream(s), buf);
        return buf.toString();
    }

    private int signatureIndex;

    /**
     * Construct object from file stream.
     *
     * @param nameIndex Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param input Input stream
     * @param constantPool Array of constants
     * @throws IOException if an I/O error occurs.
     */
    Signature(final int nameIndex, final int length, final DataInput input, final ConstantPool constantPool) throws IOException {
        this(nameIndex, length, input.readUnsignedShort(), constantPool);
    }

    /**
     * @param nameIndex Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param signatureIndex Index in constant pool to CONSTANT_Utf8
     * @param constantPool Array of constants
     */
    public Signature(final int nameIndex, final int length, final int signatureIndex, final ConstantPool constantPool) {
        super(Const.ATTR_SIGNATURE, nameIndex, Args.require(length, 2, "Signature length attribute"), constantPool);
        this.signatureIndex = signatureIndex;
        // validate:
        Objects.requireNonNull(constantPool.getConstantUtf8(signatureIndex), "constantPool.getConstantUtf8(signatureIndex)");
    }

    /**
     * Initialize from another object. Note that both objects use the same references (shallow copy). Use clone() for a
     * physical copy.
     *
     * @param c Source to copy.
     */
    public Signature(final Signature c) {
        this(c.getNameIndex(), c.getLength(), c.getSignatureIndex(), c.getConstantPool());
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitly defined by the contents of a Java class.
     * I.e., the hierarchy of methods, fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        // System.err.println("Visiting non-standard Signature object");
        v.visitSignature(this);
    }

    /**
     * @return deep copy of this attribute
     */
    @Override
    public Attribute copy(final ConstantPool constantPool) {
        return (Attribute) clone();
    }

    /**
     * Dump source file attribute to file stream in binary format.
     *
     * @param file Output file stream
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void dump(final DataOutputStream file) throws IOException {
        super.dump(file);
        file.writeShort(signatureIndex);
    }

    /**
     * @return GJ signature.
     */
    public String getSignature() {
        return super.getConstantPool().getConstantUtf8(signatureIndex).getBytes();
    }

    /**
     * @return Index in constant pool of source file name.
     */
    public int getSignatureIndex() {
        return signatureIndex;
    }

    /**
     * @param signatureIndex the index info the constant pool of this signature
     */
    public void setSignatureIndex(final int signatureIndex) {
        this.signatureIndex = signatureIndex;
    }

    /**
     * @return String representation
     */
    @Override
    public String toString() {
        return "Signature: " + getSignature();
    }
}
