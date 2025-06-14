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
 * BranchHandle is returned by specialized InstructionList.append() whenever a BranchInstruction is appended. This is
 * useful when the target of this instruction is not known at time of creation and must be set later via setTarget().
 *
 * @see InstructionHandle
 * @see Instruction
 * @see InstructionList
 */
public final class BranchHandle extends InstructionHandle {

    /**
     * Factory method.
     */
    static BranchHandle getBranchHandle(final BranchInstruction i) {
        return new BranchHandle(i);
    }

    // This is also a cache in case the InstructionHandle#swapInstruction() method is used
    // See BCEL-273
    private BranchInstruction bi; // An alias in fact, but saves lots of casts

    private BranchHandle(final BranchInstruction i) {
        super(i);
        bi = i;
    }

    /*
     * Override InstructionHandle methods: delegate to branch instruction. Through this overriding all access to the private
     * i_position field should be prevented.
     */
    @Override
    public int getPosition() {
        return bi.getPosition();
    }

    /**
     * @return target of instruction.
     */
    public InstructionHandle getTarget() {
        return bi.getTarget();
    }

    /**
     * Set new contents. Old instruction is disposed and may not be used anymore.
     */
    @Override // This is only done in order to apply the additional type check; could be merged with super impl.
    public void setInstruction(final Instruction i) { // TODO could be package-protected?
        super.setInstruction(i);
        if (!(i instanceof BranchInstruction)) {
            throw new ClassGenException("Assigning " + i + " to branch handle which is not a branch instruction");
        }
        bi = (BranchInstruction) i;
    }

    @Override
    void setPosition(final int pos) {
        // Original code: i_position = bi.position = pos;
        bi.setPosition(pos);
        super.setPosition(pos);
    }

    /**
     * Pass new target to instruction.
     */
    public void setTarget(final InstructionHandle ih) {
        bi.setTarget(ih);
    }

    @Override
    protected int updatePosition(final int offset, final int maxOffset) {
        final int x = bi.updatePosition(offset, maxOffset);
        super.setPosition(bi.getPosition());
        return x;
    }

    /**
     * Update target of instruction.
     */
    public void updateTarget(final InstructionHandle oldIh, final InstructionHandle newIh) {
        bi.updateTarget(oldIh, newIh);
    }
}
