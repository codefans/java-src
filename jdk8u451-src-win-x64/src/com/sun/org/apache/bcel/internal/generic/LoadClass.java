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

package com.sun.org.apache.bcel.internal.generic;

/**
 * Denotes that an instruction may start the process of loading and resolving the referenced class in the Virtual Machine.
 */
public interface LoadClass {

    /**
     * Returns the {@link ObjectType} of the referenced class or interface that may be loaded and resolved.
     *
     * @param cpg A ConstantPoolGen
     * @return object type that may be loaded or null if a primitive is referenced
     */
    ObjectType getLoadClassType(ConstantPoolGen cpg);

    /**
     * Returns the type associated with this instruction. LoadClass instances are always typed, but this type does not always refer to the type of the class or
     * interface that it possibly forces to load. For example, {@link GETFIELD} would return the type of the field and not the type of the class where the field
     * is defined. If no class is forced to be loaded, {@code null} is returned. An example for this is an {@link NEWARRAY} instruction that creates an
     * {@code int[][]}.
     *
     * @param cpg A ConstantPoolGen
     * @return the type associated with this instruction.
     * @see #getLoadClassType(ConstantPoolGen)
     */
    Type getType(ConstantPoolGen cpg);
}
