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

import java.io.DataInput;
import java.io.IOException;

import com.sun.org.apache.bcel.internal.Const;

/**
 * Represents a parameter annotation that is represented in the class file but is not provided to the JVM.
 *
 * @since 6.0
 */
public class RuntimeInvisibleParameterAnnotations extends ParameterAnnotations {

    /**
     * @param nameIndex Index pointing to the name <em>Code</em>
     * @param length Content length in bytes
     * @param input Input stream
     * @param constantPool Array of constants
     * @throws IOException Thrown when an I/O exception of some sort has occurred.
     */
    public RuntimeInvisibleParameterAnnotations(final int nameIndex, final int length, final DataInput input, final ConstantPool constantPool)
        throws IOException {
        super(Const.ATTR_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS, nameIndex, length, input, constantPool);
    }
}
