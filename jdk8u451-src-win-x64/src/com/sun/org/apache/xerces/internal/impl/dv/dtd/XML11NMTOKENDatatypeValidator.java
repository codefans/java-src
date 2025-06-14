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

package com.sun.org.apache.xerces.internal.impl.dv.dtd;

import com.sun.org.apache.xerces.internal.impl.dv.*;
import com.sun.org.apache.xerces.internal.util.XML11Char;

/**
 * NMTOKEN datatype validator for NMTokens from XML 1.1.
 *
 * @xerces.internal
 *
 * @author Jeffrey Rodriguez, IBM
 * @author Sandy Gao, IBM
 * @author Neil Graham, IBM
 *
 */
public class XML11NMTOKENDatatypeValidator extends NMTOKENDatatypeValidator {

    // construct a NMTOKEN datatype validator
    public XML11NMTOKENDatatypeValidator() {
        super();
    }

    /**
     * Checks that "content" string is valid NMTOKEN value.
     * If invalid a Datatype validation exception is thrown.
     *
     * @param content       the string value that needs to be validated
     * @param context       the validation context
     * @throws InvalidDatatypeException if the content is
     *         invalid according to the rules for the validators
     * @see InvalidDatatypeValueException
     */
    public void validate(String content, ValidationContext context) throws InvalidDatatypeValueException {
        if (!XML11Char.isXML11ValidNmtoken(content)) {
           throw new InvalidDatatypeValueException("NMTOKENInvalid", new Object[]{content});
       }
   }

} // class XML11NMTOKENDatatypeValidator
