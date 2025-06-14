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

package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.xs.datatypes.XSFloat;

/**
 * Represent the schema type "float"
 *
 * @xerces.internal
 *
 * @author Neeraj Bajaj, Sun Microsystems, inc.
 * @author Sandy Gao, IBM
 *
 * @version $Id: FloatDV.java,v 1.7 2010-11-01 04:39:47 joehw Exp $
 */
public class FloatDV extends TypeValidator {

    public short getAllowedFacets(){
        return ( XSSimpleTypeDecl.FACET_PATTERN | XSSimpleTypeDecl.FACET_WHITESPACE | XSSimpleTypeDecl.FACET_ENUMERATION |XSSimpleTypeDecl.FACET_MAXINCLUSIVE |XSSimpleTypeDecl.FACET_MININCLUSIVE | XSSimpleTypeDecl.FACET_MAXEXCLUSIVE  | XSSimpleTypeDecl.FACET_MINEXCLUSIVE  );
    }//getAllowedFacets()

    //convert a String to Float form, we have to take care of cases specified in spec like INF, -INF and NaN
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try{
            return new XFloat(content);
        } catch (NumberFormatException ex){
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "float"});
        }
    }//getActualValue()

    // Can't call Float#compareTo method, because it's introduced in jdk 1.2
    public int compare(Object value1, Object value2){
        return ((XFloat)value1).compareTo((XFloat)value2);
    }//compare()

    //distinguishes between identity and equality for float datatype
    //0.0 is equal but not identical to -0.0
    public boolean isIdentical (Object value1, Object value2) {
        if (value2 instanceof XFloat) {
            return ((XFloat)value1).isIdentical((XFloat)value2);
        }
        return false;
    }//isIdentical()

    private static final class XFloat implements XSFloat {

        private final float value;
        public XFloat(String s) throws NumberFormatException {
            if (DoubleDV.isPossibleFP(s)) {
                value = Float.parseFloat(s);
            }
            else if ( s.equals("INF") ) {
                value = Float.POSITIVE_INFINITY;
            }
            else if ( s.equals("-INF") ) {
                value = Float.NEGATIVE_INFINITY;
            }
            else if ( s.equals("NaN") ) {
                value = Float.NaN;
            }
            else {
                throw new NumberFormatException(s);
            }
        }

        public boolean equals(Object val) {
            if (val == this)
                return true;

            if (!(val instanceof XFloat))
                return false;
            XFloat oval = (XFloat)val;

            // NOTE: we don't distinguish 0.0 from -0.0
            if (value == oval.value)
                return true;

            if (value != value && oval.value != oval.value)
                return true;

            return false;
        }

        public int hashCode() {
            // This check is necessary because floatToIntBits(+0) != floatToIntBits(-0)
            return (value == 0f) ? 0 : Float.floatToIntBits(value);
        }

        // NOTE: 0.0 is equal but not identical to -0.0
        public boolean isIdentical (XFloat val) {
            if (val == this) {
                return true;
            }

            if (value == val.value) {
                return (value != 0.0f ||
                    (Float.floatToIntBits(value) == Float.floatToIntBits(val.value)));
            }

            if (value != value && val.value != val.value)
                return true;

            return false;
        }

        private int compareTo(XFloat val) {
            float oval = val.value;

            // this < other
            if (value < oval)
                return -1;
            // this > other
            if (value > oval)
                return 1;
            // this == other
            // NOTE: we don't distinguish 0.0 from -0.0
            if (value == oval)
                return 0;

            // one of the 2 values or both is/are NaN(s)

            if (value != value) {
                // this = NaN = other
                if (oval != oval)
                    return 0;
                // this is NaN <> other
                return INDETERMINATE;
            }

            // other is NaN <> this
            return INDETERMINATE;
        }

        private String canonical;
        public synchronized String toString() {
            if (canonical == null) {
                if (value == Float.POSITIVE_INFINITY)
                    canonical = "INF";
                else if (value == Float.NEGATIVE_INFINITY)
                    canonical = "-INF";
                else if (value != value)
                    canonical = "NaN";
                // NOTE: we don't distinguish 0.0 from -0.0
                else if (value == 0)
                    canonical = "0.0E1";
                else {
                    // REVISIT: use the java algorithm for now, because we
                    // don't know what to output for 1.1f (which is no
                    // actually 1.1)
                    canonical = Float.toString(value);
                    // if it contains 'E', then it should be a valid schema
                    // canonical representation
                    if (canonical.indexOf('E') == -1) {
                        int len = canonical.length();
                        // at most 3 longer: E, -, 9
                        char[] chars = new char[len+3];
                        canonical.getChars(0, len, chars, 0);
                        // expected decimal point position
                        int edp = chars[0] == '-' ? 2 : 1;
                        // for non-zero integer part
                        if (value >= 1 || value <= -1) {
                            // decimal point position
                            int dp = canonical.indexOf('.');
                            // move the digits: ddd.d --> d.ddd
                            for (int i = dp; i > edp; i--) {
                                chars[i] = chars[i-1];
                            }
                            chars[edp] = '.';
                            // trim trailing zeros: d00.0 --> d.000 --> d.
                            while (chars[len-1] == '0')
                                len--;
                            // add the last zero if necessary: d. --> d.0
                            if (chars[len-1] == '.')
                                len++;
                            // append E: d.dd --> d.ddE
                            chars[len++] = 'E';
                            // how far we shifted the decimal point
                            int shift = dp - edp;
                            // append the exponent --> d.ddEd
                            // the exponent is at most 7
                            chars[len++] = (char)(shift + '0');
                        }
                        else {
                            // non-zero digit point
                            int nzp = edp + 1;
                            // skip zeros: 0.003
                            while (chars[nzp] == '0')
                                nzp++;
                            // put the first non-zero digit to the left of '.'
                            chars[edp-1] = chars[nzp];
                            chars[edp] = '.';
                            // move other digits (non-zero) to the right of '.'
                            for (int i = nzp+1, j = edp+1; i < len; i++, j++)
                                chars[j] = chars[i];
                            // adjust the length
                            len -= nzp - edp;
                            // append 0 if nessary: 0.03 --> 3. --> 3.0
                            if (len == edp + 1)
                                chars[len++] = '0';
                            // append E-: d.dd --> d.ddE-
                            chars[len++] = 'E';
                            chars[len++] = '-';
                            // how far we shifted the decimal point
                            int shift = nzp - edp;
                            // append the exponent --> d.ddEd
                            // the exponent is at most 3
                            chars[len++] = (char)(shift + '0');
                        }
                        canonical = new String(chars, 0, len);
                    }
                }
            }
            return canonical;
        }

        public float getValue() {
            return value;
        }
    }
} // class FloatDV
