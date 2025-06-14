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
 * Denote that a class targets InstructionHandles within an InstructionList. Namely the following implementers:
 *
 * @see BranchHandle
 * @see LocalVariableGen
 * @see CodeExceptionGen
 */
public interface InstructionTargeter {

    // static final InstructionTargeter[] EMPTY_ARRAY = new InstructionTargeter[0];

    /**
     * Checks whether this targeter targets the specified instruction handle.
     */
    boolean containsTarget(InstructionHandle ih);

    /**
     * Replaces the target of this targeter from this old handle to the new handle.
     *
     * @param oldIh the old handle
     * @param newIh the new handle
     * @throws ClassGenException if oldIh is not targeted by this object
     */
    void updateTarget(InstructionHandle oldIh, InstructionHandle newIh) throws ClassGenException;
}
