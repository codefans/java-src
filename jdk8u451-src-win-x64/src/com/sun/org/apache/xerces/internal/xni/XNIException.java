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

package com.sun.org.apache.xerces.internal.xni;

/**
 * This exception is the base exception of all XNI exceptions. It
 * can be constructed with an error message or used to wrap another
 * exception object.
 * <p>
 * <strong>Note:</strong> By extending the Java
 * <code>RuntimeException</code>, XNI handlers and components are
 * not required to catch XNI exceptions but may explicitly catch
 * them, if so desired.
 *
 * @author Andy Clark, IBM
 *
 * @version $Id: XNIException.java,v 1.6 2010-11-01 04:40:19 joehw Exp $
 */
public class XNIException
    extends RuntimeException {

    /** Serialization version. */
    static final long serialVersionUID = 9019819772686063775L;

    //
    // Data
    //

    /** The wrapped exception. */
    private Exception fException;

    //
    // Constructors
    //

    /**
     * Constructs an XNI exception with a message.
     *
     * @param message The exception message.
     */
    public XNIException(String message) {
        super(message);
    } // <init>(String)

    /**
     * Constructs an XNI exception with a wrapped exception.
     *
     * @param exception The wrapped exception.
     */
    public XNIException(Exception exception) {
        super(exception.getMessage());
        fException = exception;
    } // <init>(Exception)

    /**
     * Constructs an XNI exception with a message and wrapped exception.
     *
     * @param message The exception message.
     * @param exception The wrapped exception.
     */
    public XNIException(String message, Exception exception) {
        super(message);
        fException = exception;
    } // <init>(Exception,String)

    //
    // Public methods
    //

    /** Returns the wrapped exception. */
    public Exception getException() {
        return fException;
    } // getException():Exception

    public Throwable getCause() {
       return fException;
    }
} // class QName
