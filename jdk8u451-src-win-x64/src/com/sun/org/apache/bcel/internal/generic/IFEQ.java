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
 * IFEQ - Branch if int comparison with zero succeeds
 *
 * <PRE>
 * Stack: ..., value -&gt; ...
 * </PRE>
 */
public class IFEQ extends IfInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction. Not to be used otherwise.
     */
    IFEQ() {
    }

    public IFEQ(final InstructionHandle target) {
        super(com.sun.org.apache.bcel.internal.Const.IFEQ, target);
    }

    /**
     * Call corresponding visitor method(s). The order is: Call visitor methods of implemented interfaces first, then call
     * methods according to the class hierarchy in descending order, i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitStackConsumer(this);
        v.visitBranchInstruction(this);
        v.visitIfInstruction(this);
        v.visitIFEQ(this);
    }

    /**
     * @return negation of instruction, e.g. IFEQ.negate() == IFNE
     */
    @Override
    public IfInstruction negate() {
        return new IFNE(super.getTarget());
    }
}
