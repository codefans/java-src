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


package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
final class LiteralExpr extends Expression {
    private final String _value;
    private final String _namespace;

    /**
     * Creates a new literal expression node.
     * @param value the literal expression content/value.
     */
    public LiteralExpr(String value) {
        _value = value;
        _namespace = null;
    }

    /**
     * Creates a new literal expression node.
     * @param value the literal expression content/value.
     * @param namespace the namespace in which the expression exists.
     */
    public LiteralExpr(String value, String namespace) {
        _value = value;
        _namespace = namespace.equals(Constants.EMPTYSTRING) ? null : namespace;
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return _type = Type.String;
    }

    public String toString() {
        return "literal-expr(" + _value + ')';
    }

    protected boolean contextDependent() {
        return false;
    }

    protected String getValue() {
        return _value;
    }

    protected String getNamespace() {
        return _namespace;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        il.append(new PUSH(cpg, _value));
    }
}
