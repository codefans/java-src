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

package com.sun.org.apache.xml.internal.utils.res;

import java.util.ListResourceBundle;

/**
 * This is an interface for error messages.  This class is misnamed,
 * and should be called XalanResourceBundle, or some such.
 * @xsl.usage internal
 */
abstract public class XResourceBundleBase extends ListResourceBundle
{

  /**
   * Get the error string associated with the error code
   *
   * @param errorCode Error code
   *
   * @return error string associated with the given error code
   */
  abstract public String getMessageKey(int errorCode);

  /**
   * Get the warning string associated with the error code
   *
   * @param errorCode Error code
   *
   * @return warning string associated with the given error code
   */
  abstract public String getWarningKey(int errorCode);
}
