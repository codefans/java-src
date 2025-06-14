/*
 * Copyright (c) 2007, 2025, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import java.util.ListResourceBundle;

/**
 * @author Morten Jorgensen
 */
public class ErrorMessages_zh_TW extends ListResourceBundle {

/*
 * XSLTC compile-time error messages.
 *
 * General notes to translators and definitions:
 *
 *   1) XSLTC is the name of the product.  It is an acronym for "XSLT Compiler".
 *      XSLT is an acronym for "XML Stylesheet Language: Transformations".
 *
 *   2) A stylesheet is a description of how to transform an input XML document
 *      into a resultant XML document (or HTML document or text).  The
 *      stylesheet itself is described in the form of an XML document.
 *
 *   3) A template is a component of a stylesheet that is used to match a
 *      particular portion of an input document and specifies the form of the
 *      corresponding portion of the output document.
 *
 *   4) An axis is a particular "dimension" in a tree representation of an XML
 *      document; the nodes in the tree are divided along different axes.
 *      Traversing the "child" axis, for instance, means that the program
 *      would visit each child of a particular node; traversing the "descendant"
 *      axis means that the program would visit the child nodes of a particular
 *      node, their children, and so on until the leaf nodes of the tree are
 *      reached.
 *
 *   5) An iterator is an object that traverses nodes in a tree along a
 *      particular axis, one at a time.
 *
 *   6) An element is a mark-up tag in an XML document; an attribute is a
 *      modifier on the tag.  For example, in <elem attr='val' attr2='val2'>
 *      "elem" is an element name, "attr" and "attr2" are attribute names with
 *      the values "val" and "val2", respectively.
 *
 *   7) A namespace declaration is a special attribute that is used to associate
 *      a prefix with a URI (the namespace).  The meanings of element names and
 *      attribute names that use that prefix are defined with respect to that
 *      namespace.
 *
 *   8) DOM is an acronym for Document Object Model.  It is a tree
 *      representation of an XML document.
 *
 *      SAX is an acronym for the Simple API for XML processing.  It is an API
 *      used inform an XML processor (in this case XSLTC) of the structure and
 *      content of an XML document.
 *
 *      Input to the stylesheet processor can come from an XML parser in the
 *      form of a DOM tree or through the SAX API.
 *
 *   9) DTD is a document type declaration.  It is a way of specifying the
 *      grammar for an XML file, the names and types of elements, attributes,
 *      etc.
 *
 *  10) XPath is a specification that describes a notation for identifying
 *      nodes in a tree-structured representation of an XML document.  An
 *      instance of that notation is referred to as an XPath expression.
 *
 *  11) Translet is an invented term that refers to the class file that contains
 *      the compiled form of a stylesheet.
 */

    // These message should be read from a locale-specific resource bundle
    /** Get the lookup table for error messages.
     *
     * @return The message lookup table.
     */
    public Object[][] getContents()
    {
      return new Object[][] {
        {ErrorMsg.MULTIPLE_STYLESHEET_ERR,
        "\u76F8\u540C\u6A94\u6848\u4E2D\u5B9A\u7FA9\u4E86\u8D85\u904E\u4E00\u500B\u6A23\u5F0F\u8868\u3002"},

        /*
         * Note to translators:  The substitution text is the name of a
         * template.  The same name was used on two different templates in the
         * same stylesheet.
         */
        {ErrorMsg.TEMPLATE_REDEF_ERR,
        "\u6A23\u677F ''{0}'' \u5DF2\u7D93\u5B9A\u7FA9\u5728\u6B64\u6A23\u5F0F\u8868\u4E2D\u3002"},


        /*
         * Note to translators:  The substitution text is the name of a
         * template.  A reference to the template name was encountered, but the
         * template is undefined.
         */
        {ErrorMsg.TEMPLATE_UNDEF_ERR,
        "\u6A23\u677F ''{0}'' \u672A\u5B9A\u7FA9\u5728\u6B64\u6A23\u5F0F\u8868\u4E2D\u3002"},

        /*
         * Note to translators:  The substitution text is the name of a variable
         * that was defined more than once.
         */
        {ErrorMsg.VARIABLE_REDEF_ERR,
        "\u8B8A\u6578 ''{0}'' \u5728\u76F8\u540C\u7BC4\u570D\u4E2D\u5B9A\u7FA9\u591A\u6B21\u3002"},

        /*
         * Note to translators:  The substitution text is the name of a variable
         * or parameter.  A reference to the variable or parameter was found,
         * but it was never defined.
         */
        {ErrorMsg.VARIABLE_UNDEF_ERR,
        "\u8B8A\u6578\u6216\u53C3\u6578 ''{0}'' \u672A\u5B9A\u7FA9\u3002"},

        /*
         * Note to translators:  The word "class" here refers to a Java class.
         * Processing the stylesheet required a class to be loaded, but it could
         * not be found.  The substitution text is the name of the class.
         */
        {ErrorMsg.CLASS_NOT_FOUND_ERR,
        "\u627E\u4E0D\u5230\u985E\u5225 ''{0}''\u3002"},

        /*
         * Note to translators:  The word "method" here refers to a Java method.
         * Processing the stylesheet required a reference to the method named by
         * the substitution text, but it could not be found.  "public" is the
         * Java keyword.
         */
        {ErrorMsg.METHOD_NOT_FOUND_ERR,
        "\u627E\u4E0D\u5230\u5916\u90E8\u65B9\u6CD5 ''{0}'' (\u5FC5\u9808\u70BA\u516C\u7528)\u3002"},

        /*
         * Note to translators:  The word "method" here refers to a Java method.
         * Processing the stylesheet required a reference to the method named by
         * the substitution text, but no method with the required types of
         * arguments or return type could be found.
         */
        {ErrorMsg.ARGUMENT_CONVERSION_ERR,
        "\u7121\u6CD5\u8F49\u63DB\u547C\u53EB\u65B9\u6CD5 ''{0}'' \u4E2D\u7684\u5F15\u6578/\u50B3\u56DE\u985E\u578B"},

        /*
         * Note to translators:  The file or URI named in the substitution text
         * is missing.
         */
        {ErrorMsg.FILE_NOT_FOUND_ERR,
        "\u627E\u4E0D\u5230\u6A94\u6848\u6216 URI ''{0}''\u3002"},

        /*
         * Note to translators:  This message is displayed when the URI
         * mentioned in the substitution text is not well-formed syntactically.
         */
        {ErrorMsg.INVALID_URI_ERR,
        "\u7121\u6548\u7684 URI ''{0}''\u3002"},

        /*
         * Note to translators:  The file or URI named in the substitution text
         * exists but could not be opened.
         */
        {ErrorMsg.FILE_ACCESS_ERR,
        "\u7121\u6CD5\u958B\u555F\u6A94\u6848\u6216 URI ''{0}''\u3002"},

        /*
         * Note to translators: <xsl:stylesheet> and <xsl:transform> are
         * keywords that should not be translated.
         */
        {ErrorMsg.MISSING_ROOT_ERR,
        "\u9810\u671F <xsl:stylesheet> \u6216 <xsl:transform> \u5143\u7D20\u3002"},

        /*
         * Note to translators:  The stylesheet contained a reference to a
         * namespace prefix that was undefined.  The value of the substitution
         * text is the name of the prefix.
         */
        {ErrorMsg.NAMESPACE_UNDEF_ERR,
        "\u672A\u5BA3\u544A\u547D\u540D\u7A7A\u9593\u524D\u7F6E\u78BC ''{0}''\u3002"},

        /*
         * Note to translators:  The Java function named in the stylesheet could
         * not be found.
         */
        {ErrorMsg.FUNCTION_RESOLVE_ERR,
        "\u7121\u6CD5\u89E3\u6790\u51FD\u6578 ''{0}'' \u7684\u547C\u53EB\u3002"},

        /*
         * Note to translators:  The substitution text is the name of a
         * function.  A literal string here means a constant string value.
         */
        {ErrorMsg.NEED_LITERAL_ERR,
        "''{0}'' \u7684\u5F15\u6578\u5FC5\u9808\u662F\u6587\u5B57\u5B57\u4E32\u3002"},

        /*
         * Note to translators:  This message indicates there was a syntactic
         * error in the form of an XPath expression.  The substitution text is
         * the expression.
         */
        {ErrorMsg.XPATH_PARSER_ERR,
        "\u5256\u6790 XPath \u8868\u793A\u5F0F ''{0}'' \u6642\u767C\u751F\u932F\u8AA4\u3002"},

        /*
         * Note to translators:  An element in the stylesheet requires a
         * particular attribute named by the substitution text, but that
         * attribute was not specified in the stylesheet.
         */
        {ErrorMsg.REQUIRED_ATTR_ERR,
        "\u907A\u6F0F\u5FC5\u8981\u7684\u5C6C\u6027 ''{0}''\u3002"},

        /*
         * Note to translators:  This message indicates that a character not
         * permitted in an XPath expression was encountered.  The substitution
         * text is the offending character.
         */
        {ErrorMsg.ILLEGAL_CHAR_ERR,
        "XPath \u8868\u793A\u5F0F\u4E2D\u7121\u6548\u7684\u5B57\u5143 ''{0}''\u3002"},

        /*
         * Note to translators:  A processing instruction is a mark-up item in
         * an XML document that request some behaviour of an XML processor.  The
         * form of the name of was invalid in this case, and the substitution
         * text is the name.
         */
        {ErrorMsg.ILLEGAL_PI_ERR,
        "\u8655\u7406\u6307\u793A\u7684\u7121\u6548\u540D\u7A31 ''{0}''\u3002"},

        /*
         * Note to translators:  This message is reported if the stylesheet
         * being processed attempted to construct an XML document with an
         * attribute in a place other than on an element.  The substitution text
         * specifies the name of the attribute.
         */
        {ErrorMsg.STRAY_ATTRIBUTE_ERR,
        "\u5C6C\u6027 ''{0}'' \u5728\u5143\u7D20\u4E4B\u5916\u3002"},

        /*
         * Note to translators:  An attribute that wasn't recognized was
         * specified on an element in the stylesheet.  The attribute is named
         * by the substitution
         * text.
         */
        {ErrorMsg.ILLEGAL_ATTRIBUTE_ERR,
        "\u7121\u6548\u7684\u5C6C\u6027 ''{0}''\u3002"},

        /*
         * Note to translators:  "import" and "include" are keywords that should
         * not be translated.  This messages indicates that the stylesheet
         * named in the substitution text imported or included itself either
         * directly or indirectly.
         */
        {ErrorMsg.CIRCULAR_INCLUDE_ERR,
        "\u5FAA\u74B0\u532F\u5165/\u5305\u542B\u3002\u5DF2\u7D93\u8F09\u5165\u6A23\u5F0F\u8868 ''{0}''\u3002"},

        /*
         * Note to translators:  A result-tree fragment is a portion of a
         * resulting XML document represented as a tree.  "<xsl:sort>" is a
         * keyword and should not be translated.
         */
        {ErrorMsg.RESULT_TREE_SORT_ERR,
        "\u7121\u6CD5\u6392\u5E8F Result-tree \u7247\u6BB5 (\u5FFD\u7565 <xsl:sort> \u5143\u7D20)\u3002\u5EFA\u7ACB\u7D50\u679C\u6A39\u72C0\u7D50\u69CB\u6642\uFF0C\u5FC5\u9808\u6392\u5E8F\u7BC0\u9EDE\u3002"},

        /*
         * Note to translators:  A name can be given to a particular style to be
         * used to format decimal values.  The substitution text gives the name
         * of such a style for which more than one declaration was encountered.
         */
        {ErrorMsg.SYMBOLS_REDEF_ERR,
        "\u5DF2\u7D93\u5B9A\u7FA9\u5341\u9032\u4F4D\u683C\u5F0F ''{0}''\u3002"},

        /*
         * Note to translators:  The stylesheet version named in the
         * substitution text is not supported.
         */
        {ErrorMsg.XSL_VERSION_ERR,
        "XSLTC \u4E0D\u652F\u63F4 XSL \u7248\u672C ''{0}''\u3002"},

        /*
         * Note to translators:  The definitions of one or more variables or
         * parameters depend on one another.
         */
        {ErrorMsg.CIRCULAR_VARIABLE_ERR,
        "\u5728 ''{0}'' \u4E2D\u6709\u5FAA\u74B0\u8B8A\u6578/\u53C3\u6578\u53C3\u7167\u3002"},

        /*
         * Note to translators:  The operator in an expresion with two operands was
         * not recognized.
         */
        {ErrorMsg.ILLEGAL_BINARY_OP_ERR,
        "\u4E8C\u9032\u4F4D\u8868\u793A\u5F0F\u4E0D\u660E\u7684\u904B\u7B97\u5B50\u3002"},

        /*
         * Note to translators:  This message is produced if a reference to a
         * function has too many or too few arguments.
         */
        {ErrorMsg.ILLEGAL_ARG_ERR,
        "\u51FD\u6578\u547C\u53EB\u7121\u6548\u7684\u5F15\u6578\u3002"},

        /*
         * Note to translators:  "document()" is the name of function and must
         * not be translated.  A node-set is a set of the nodes in the tree
         * representation of an XML document.
         */
        {ErrorMsg.DOCUMENT_ARG_ERR,
        "document() \u51FD\u6578\u7684\u7B2C\u4E8C\u500B\u5F15\u6578\u5FC5\u9808\u662F node-set\u3002"},

        /*
         * Note to translators:  "<xsl:when>" and "<xsl:choose>" are keywords
         * and should not be translated.  This message describes a syntax error
         * in the stylesheet.
         */
        {ErrorMsg.MISSING_WHEN_ERR,
        "\u5728 <xsl:choose> \u4E2D\u81F3\u5C11\u9700\u8981\u4E00\u500B <xsl:when> \u5143\u7D20\u3002"},

        /*
         * Note to translators:  "<xsl:otherwise>" and "<xsl:choose>" are
         * keywords and should not be translated.  This message describes a
         * syntax error in the stylesheet.
         */
        {ErrorMsg.MULTIPLE_OTHERWISE_ERR,
        "\u5728 <xsl:choose> \u4E2D\u53EA\u5141\u8A31\u4E00\u500B <xsl:otherwise> \u5143\u7D20\u3002"},

        /*
         * Note to translators:  "<xsl:otherwise>" and "<xsl:choose>" are
         * keywords and should not be translated.  This message describes a
         * syntax error in the stylesheet.
         */
        {ErrorMsg.STRAY_OTHERWISE_ERR,
        "<xsl:otherwise> \u53EA\u80FD\u5728 <xsl:choose> \u5167\u4F7F\u7528\u3002"},

        /*
         * Note to translators:  "<xsl:when>" and "<xsl:choose>" are keywords
         * and should not be translated.  This message describes a syntax error
         * in the stylesheet.
         */
        {ErrorMsg.STRAY_WHEN_ERR,
        "<xsl:when> \u53EA\u80FD\u5728 <xsl:choose> \u5167\u4F7F\u7528\u3002"},

        /*
         * Note to translators:  "<xsl:when>", "<xsl:otherwise>" and
         * "<xsl:choose>" are keywords and should not be translated.  This
         * message describes a syntax error in the stylesheet.
         */
        {ErrorMsg.WHEN_ELEMENT_ERR,
        "\u5728 <xsl:choose> \u4E2D\u53EA\u5141\u8A31 <xsl:when> \u8207 <xsl:otherwise> \u5143\u7D20\u3002"},

        /*
         * Note to translators:  "<xsl:attribute-set>" and "name" are keywords
         * that should not be translated.
         */
        {ErrorMsg.UNNAMED_ATTRIBSET_ERR,
        "<xsl:attribute-set> \u907A\u6F0F 'name' \u5C6C\u6027\u3002"},

        /*
         * Note to translators:  An element in the stylesheet contained an
         * element of a type that it was not permitted to contain.
         */
        {ErrorMsg.ILLEGAL_CHILD_ERR,
        "\u7121\u6548\u7684\u5B50\u9805\u5143\u7D20\u3002"},

        /*
         * Note to translators:  The stylesheet tried to create an element with
         * a name that was not a valid XML name.  The substitution text contains
         * the name.
         */
        {ErrorMsg.ILLEGAL_ELEM_NAME_ERR,
        "\u60A8\u7121\u6CD5\u547C\u53EB\u5143\u7D20 ''{0}''"},

        /*
         * Note to translators:  The stylesheet tried to create an attribute
         * with a name that was not a valid XML name.  The substitution text
         * contains the name.
         */
        {ErrorMsg.ILLEGAL_ATTR_NAME_ERR,
        "\u60A8\u7121\u6CD5\u547C\u53EB\u5C6C\u6027 ''{0}''"},

        /*
         * Note to translators:  The children of the outermost element of a
         * stylesheet are referred to as top-level elements.  No text should
         * occur within that outermost element unless it is within a top-level
         * element.  This message indicates that that constraint was violated.
         * "<xsl:stylesheet>" is a keyword that should not be translated.
         */
        {ErrorMsg.ILLEGAL_TEXT_NODE_ERR,
        "\u6700\u4E0A\u5C64 <xsl:stylesheet> \u5143\u7D20\u4E4B\u5916\u7684\u6587\u5B57\u8CC7\u6599\u3002"},

        /*
         * Note to translators:  JAXP is an acronym for the Java API for XML
         * Processing.  This message indicates that the XML parser provided to
         * XSLTC to process the XML input document had a configuration problem.
         */
        {ErrorMsg.SAX_PARSER_CONFIG_ERR,
        "\u672A\u6B63\u78BA\u8A2D\u5B9A JAXP \u5256\u6790\u5668"},

        /*
         * Note to translators:  The substitution text names the internal error
         * encountered.
         */
        {ErrorMsg.INTERNAL_ERR,
        "\u7121\u6CD5\u5FA9\u539F\u7684 XSLTC-internal \u932F\u8AA4: ''{0}''"},

        /*
         * Note to translators:  The stylesheet contained an element that was
         * not recognized as part of the XSL syntax.  The substitution text
         * gives the element name.
         */
        {ErrorMsg.UNSUPPORTED_XSL_ERR,
        "\u4E0D\u652F\u63F4\u7684 XSL \u5143\u7D20 ''{0}''\u3002"},

        /*
         * Note to translators:  The stylesheet referred to an extension to the
         * XSL syntax and indicated that it was defined by XSLTC, but XSTLC does
         * not recognized the particular extension named.  The substitution text
         * gives the extension name.
         */
        {ErrorMsg.UNSUPPORTED_EXT_ERR,
        "\u7121\u6CD5\u8FA8\u8B58\u7684 XSLTC \u64F4\u5145\u5957\u4EF6 ''{0}''\u3002"},

        /*
         * Note to translators:  The XML document given to XSLTC as a stylesheet
         * was not, in fact, a stylesheet.  XSLTC is able to detect that in this
         * case because the outermost element in the stylesheet has to be
         * declared with respect to the XSL namespace URI, but no declaration
         * for that namespace was seen.
         */
        {ErrorMsg.MISSING_XSLT_URI_ERR,
        "\u8F38\u5165\u6587\u4EF6\u4E0D\u662F\u6A23\u5F0F\u8868 (\u6839\u5143\u7D20\u4E2D\u672A\u5BA3\u544A XSL \u547D\u540D\u7A7A\u9593)\u3002"},

        /*
         * Note to translators:  XSLTC could not find the stylesheet document
         * with the name specified by the substitution text.
         */
        {ErrorMsg.MISSING_XSLT_TARGET_ERR,
        "\u627E\u4E0D\u5230\u6A23\u5F0F\u8868\u76EE\u6A19 ''{0}''\u3002"},

        /*
         * Note to translators:  access to the stylesheet target is denied
         */
        {ErrorMsg.ACCESSING_XSLT_TARGET_ERR,
        "\u7121\u6CD5\u8B80\u53D6\u6A23\u5F0F\u8868\u76EE\u6A19 ''{0}''\uFF0C\u56E0\u70BA accessExternalStylesheet \u5C6C\u6027\u8A2D\u5B9A\u7684\u9650\u5236\uFF0C\u6240\u4EE5\u4E0D\u5141\u8A31 ''{1}'' \u5B58\u53D6\u3002"},

        /*
         * Note to translators:  This message represents an internal error in
         * condition in XSLTC.  The substitution text is the class name in XSLTC
         * that is missing some functionality.
         */
        {ErrorMsg.NOT_IMPLEMENTED_ERR,
        "\u672A\u5BE6\u884C: ''{0}''\u3002"},

        /*
         * Note to translators:  The XML document given to XSLTC as a stylesheet
         * was not, in fact, a stylesheet.
         */
        {ErrorMsg.NOT_STYLESHEET_ERR,
        "\u8F38\u5165\u6587\u4EF6\u672A\u5305\u542B XSL \u6A23\u5F0F\u8868\u3002"},

        /*
         * Note to translators:  The element named in the substitution text was
         * encountered in the stylesheet but is not recognized.
         */
        {ErrorMsg.ELEMENT_PARSE_ERR,
        "\u7121\u6CD5\u5256\u6790\u5143\u7D20 ''{0}''"},

        /*
         * Note to translators:  "use", "<key>", "node", "node-set", "string"
         * and "number" are keywords in this context and should not be
         * translated.  This message indicates that the value of the "use"
         * attribute was not one of the permitted values.
         */
        {ErrorMsg.KEY_USE_ATTR_ERR,
        "<key> \u7684\u4F7F\u7528\u5C6C\u6027\u5FC5\u9808\u662F\u7BC0\u9EDE\u3001node-set\u3001\u5B57\u4E32\u6216\u6578\u5B57\u3002"},

        /*
         * Note to translators:  An XML document can specify the version of the
         * XML specification to which it adheres.  This message indicates that
         * the version specified for the output document was not valid.
         */
        {ErrorMsg.OUTPUT_VERSION_ERR,
        "\u8F38\u51FA XML \u6587\u4EF6\u7248\u672C\u61C9\u70BA 1.0"},

        /*
         * Note to translators:  The operator in a comparison operation was
         * not recognized.
         */
        {ErrorMsg.ILLEGAL_RELAT_OP_ERR,
        "\u95DC\u806F\u8868\u793A\u5F0F\u7684\u904B\u7B97\u5B50\u4E0D\u660E"},

        /*
         * Note to translators:  An attribute set defines as a set of XML
         * attributes that can be added to an element in the output XML document
         * as a group.  This message is reported if the name specified was not
         * used to declare an attribute set.  The substitution text is the name
         * that is in error.
         */
        {ErrorMsg.ATTRIBSET_UNDEF_ERR,
        "\u5617\u8A66\u4F7F\u7528\u4E0D\u5B58\u5728\u7684\u5C6C\u6027\u96C6 ''{0}''\u3002"},

        /*
         * Note to translators:  The term "attribute value template" is a term
         * defined by XSLT which describes the value of an attribute that is
         * determined by an XPath expression.  The message indicates that the
         * expression was syntactically incorrect; the substitution text
         * contains the expression that was in error.
         */
        {ErrorMsg.ATTR_VAL_TEMPLATE_ERR,
        "\u7121\u6CD5\u5256\u6790\u5C6C\u6027\u503C\u6A23\u677F ''{0}''\u3002"},

        /*
         * Note to translators:  ???
         */
        {ErrorMsg.UNKNOWN_SIG_TYPE_ERR,
        "\u985E\u5225 ''{0}'' \u7C3D\u7AE0\u6709\u4E0D\u660E\u7684 data-type\u3002"},

        /*
         * Note to translators:  The substitution text refers to data types.
         * The message is displayed if a value in a particular context needs to
         * be converted to type {1}, but that's not possible for a value of
         * type {0}.
         */
        {ErrorMsg.DATA_CONVERSION_ERR,
        "\u7121\u6CD5\u8F49\u63DB data-type ''{0}'' \u70BA ''{1}''\u3002"},

        /*
         * Note to translators:  "Templates" is a Java class name that should
         * not be translated.
         */
        {ErrorMsg.NO_TRANSLET_CLASS_ERR,
        "\u6B64\u6A23\u677F\u672A\u5305\u542B\u6709\u6548\u7684 translet \u985E\u5225\u5B9A\u7FA9\u3002"},

        /*
         * Note to translators:  "Templates" is a Java class name that should
         * not be translated.
         */
        {ErrorMsg.NO_MAIN_TRANSLET_ERR,
        "\u6B64\u6A23\u677F\u672A\u5305\u542B\u540D\u7A31\u70BA ''{0}'' \u7684\u985E\u5225\u3002"},

        /*
         * Note to translators:  The substitution text is the name of a class.
         */
        {ErrorMsg.TRANSLET_CLASS_ERR,
        "\u7121\u6CD5\u8F09\u5165 translet \u985E\u5225 ''{0}''\u3002"},

        {ErrorMsg.TRANSLET_OBJECT_ERR,
        "\u5DF2\u8F09\u5165 translet \u985E\u5225\uFF0C\u4F46\u7121\u6CD5\u5EFA\u7ACB translet \u57F7\u884C\u8655\u7406\u3002"},

        /*
         * Note to translators:  "ErrorListener" is a Java interface name that
         * should not be translated.  The message indicates that the user tried
         * to set an ErrorListener object on object of the class named in the
         * substitution text with "null" Java value.
         */
        {ErrorMsg.ERROR_LISTENER_NULL_ERR,
        "\u5617\u8A66\u5C07 ''{0}'' \u7684 ErrorListener \u8A2D\u5B9A\u70BA\u7A7A\u503C"},

        /*
         * Note to translators:  StreamSource, SAXSource and DOMSource are Java
         * interface names that should not be translated.
         */
        {ErrorMsg.JAXP_UNKNOWN_SOURCE_ERR,
        "XSLTC \u50C5\u652F\u63F4 StreamSource\u3001SAXSource \u8207 DOMSource"},

        /*
         * Note to translators:  "Source" is a Java class name that should not
         * be translated.  The substitution text is the name of Java method.
         */
        {ErrorMsg.JAXP_NO_SOURCE_ERR,
        "\u50B3\u9001\u81F3 ''{0}'' \u7684\u4F86\u6E90\u7269\u4EF6\u6C92\u6709\u5167\u5BB9\u3002"},

        /*
         * Note to translators:  The message indicates that XSLTC failed to
         * compile the stylesheet into a translet (class file).
         */
        {ErrorMsg.JAXP_COMPILE_ERR,
        "\u7121\u6CD5\u7DE8\u8B6F\u6A23\u5F0F\u8868"},

        /*
         * Note to translators:  "TransformerFactory" is a class name.  In this
         * context, an attribute is a property or setting of the
         * TransformerFactory object.  The substitution text is the name of the
         * unrecognised attribute.  The method used to retrieve the attribute is
         * "getAttribute", so it's not clear whether it would be best to
         * translate the term "attribute".
         */
        {ErrorMsg.JAXP_INVALID_ATTR_ERR,
        "TransformerFactory \u7121\u6CD5\u8FA8\u8B58\u5C6C\u6027 ''{0}''\u3002"},

        {ErrorMsg.JAXP_INVALID_ATTR_VALUE_ERR,
        "\u70BA ''{0}'' \u5C6C\u6027\u6307\u5B9A\u7684\u503C\u4E0D\u6B63\u78BA\u3002"},

        /*
         * Note to translators:  "setResult()" and "startDocument()" are Java
         * method names that should not be translated.
         */
        {ErrorMsg.JAXP_SET_RESULT_ERR,
        "\u547C\u53EB startDocument() \u4E4B\u524D\uFF0C\u5FC5\u9808\u5148\u547C\u53EB setResult()\u3002"},

        /*
         * Note to translators:  "Transformer" is a Java interface name that
         * should not be translated.  A Transformer object should contained a
         * reference to a translet object in order to be used for
         * transformations; this message is produced if that requirement is not
         * met.
         */
        {ErrorMsg.JAXP_NO_TRANSLET_ERR,
        "\u8F49\u63DB\u5668\u6C92\u6709\u5C01\u88DD\u7684 translet \u7269\u4EF6\u3002"},

        /*
         * Note to translators:  The XML document that results from a
         * transformation needs to be sent to an output handler object; this
         * message is produced if that requirement is not met.
         */
        {ErrorMsg.JAXP_NO_HANDLER_ERR,
        "\u8F49\u63DB\u7D50\u679C\u6C92\u6709\u5B9A\u7FA9\u7684\u8F38\u51FA\u8655\u7406\u7A0B\u5F0F\u3002"},

        /*
         * Note to translators:  "Result" is a Java interface name in this
         * context.  The substitution text is a method name.
         */
        {ErrorMsg.JAXP_NO_RESULT_ERR,
        "\u50B3\u9001\u81F3 ''{0}'' \u7684\u7D50\u679C\u7269\u4EF6\u7121\u6548\u3002"},

        /*
         * Note to translators:  "Transformer" is a Java interface name.  The
         * user's program attempted to access an unrecognized property with the
         * name specified in the substitution text.  The method used to retrieve
         * the property is "getOutputProperty", so it's not clear whether it
         * would be best to translate the term "property".
         */
        {ErrorMsg.JAXP_UNKNOWN_PROP_ERR,
        "\u5617\u8A66\u5B58\u53D6\u7121\u6548\u7684\u8F49\u63DB\u5668\u5C6C\u6027 ''{0}''\u3002"},

        /*
         * Note to translators:  SAX2DOM is the name of a Java class that should
         * not be translated.  This is an adapter in the sense that it takes a
         * DOM object and converts it to something that uses the SAX API.
         */
        {ErrorMsg.SAX2DOM_ADAPTER_ERR,
        "\u7121\u6CD5\u5EFA\u7ACB SAX2DOM \u8F49\u63A5\u5668: ''{0}''\u3002"},

        /*
         * Note to translators:  "XSLTCSource.build()" is a Java method name.
         * "systemId" is an XML term that is short for "system identification".
         */
        {ErrorMsg.XSLTC_SOURCE_ERR,
        "\u672A\u8A2D\u5B9A systemId \u800C\u547C\u53EB XSLTCSource.build()\u3002"},

        { ErrorMsg.ER_RESULT_NULL,
            "\u7D50\u679C\u4E0D\u61C9\u70BA\u7A7A\u503C"},

        /*
         * Note to translators:  This message indicates that the value argument
         * of setParameter must be a valid Java Object.
         */
        {ErrorMsg.JAXP_INVALID_SET_PARAM_VALUE,
        "\u53C3\u6578 {0} \u7684\u503C\u5FC5\u9808\u662F\u6709\u6548\u7684 Java \u7269\u4EF6"},


        {ErrorMsg.COMPILE_STDIN_ERR,
        "-i \u9078\u9805\u5FC5\u9808\u8207 -o \u9078\u9805\u4E00\u8D77\u4F7F\u7528\u3002"},


        /*
         * Note to translators:  This message contains usage information for a
         * means of invoking XSLTC from the command-line.  The message is
         * formatted for presentation in English.  The strings <output>,
         * <directory>, etc. indicate user-specified argument values, and can
         * be translated - the argument <package> refers to a Java package, so
         * it should be handled in the same way the term is handled for JDK
         * documentation.
         */
        {ErrorMsg.COMPILE_USAGE_STR,
        "\u6982\u8981\n   java com.sun.org.apache.xalan.internal.xsltc.cmdline.Compile [-o <output>]\n      [-d <directory>] [-j <jarfile>] [-p <package>]\n      [-n] [-x] [-u] [-v] [-h] { <stylesheet> | -i }\n\n\u9078\u9805\n   -o <output>    \u6307\u6D3E\u540D\u7A31 <output> \u81F3\u7522\u751F\u7684\n                  translet\u3002\u6839\u64DA\u9810\u8A2D\uFF0Ctranslet \u540D\u7A31\n                  \u884D\u751F\u81EA <stylesheet> \u540D\u7A31\u3002\u82E5\u7DE8\u8B6F\n                  \u591A\u500B\u6A23\u5F0F\u8868\uFF0C\u5C07\u5FFD\u7565\u6B64\u9078\u9805\u3002\n   -d <directory> \u6307\u5B9A translet \u7684\u76EE\u7684\u5730\u76EE\u9304\n   -j <jarfile>   \u5C01\u88DD translet \u985E\u5225\u6210\u70BA jar \u6A94\u6848\uFF0C\n                  \u540D\u7A31\u6307\u5B9A\u70BA <jarfile>\n   -p <package>   \u6307\u5B9A\u6240\u6709\u7522\u751F\u7684 translet \u985E\u5225\u7684\u5957\u88DD\u7A0B\u5F0F\n                  \u540D\u7A31\u524D\u7F6E\u78BC\u3002\n   -n             \u555F\u7528\u6A23\u677F\u5167\u5D4C (\u9810\u8A2D\u884C\u70BA\u4E00\u822C\u800C\u8A00\n                  \u8F03\u4F73)\u3002\n   -x             \u958B\u555F\u984D\u5916\u7684\u9664\u932F\u8A0A\u606F\u8F38\u51FA\n   -u             \u89E3\u8B6F <stylesheet> \u5F15\u6578\u70BA URL\n   -i             \u5F37\u5236\u7DE8\u8B6F\u5668\u5F9E stdin \u8B80\u53D6\u6A23\u5F0F\u8868\n   -v             \u5217\u5370\u7DE8\u8B6F\u5668\u7248\u672C\n   -h             \u5217\u5370\u6B64\u7528\u6CD5\u6558\u8FF0\n"},

        /*
         * Note to translators:  This message contains usage information for a
         * means of invoking XSLTC from the command-line.  The message is
         * formatted for presentation in English.  The strings <jarfile>,
         * <document>, etc. indicate user-specified argument values, and can
         * be translated - the argument <class> refers to a Java class, so it
         * should be handled in the same way the term is handled for JDK
         * documentation.
         */
        {ErrorMsg.TRANSFORM_USAGE_STR,
        "\u6982\u8981 \n   java com.sun.org.apache.xalan.internal.xsltc.cmdline.Transform [-j <jarfile>]\n      [-x] [-n <iterations>] {-u <document_url> | <document>}\n      <class> [<param1>=<value1> ...]\n\n   \u4F7F\u7528 translet <class> \u8F49\u63DB\u6307\u5B9A\u70BA <document> \n   \u7684 XML \u6587\u4EF6\u3002translet <class> \u4F4D\u65BC\n   \u4F7F\u7528\u8005\u7684\u985E\u5225\u8DEF\u5F91\uFF0C\u6216\u662F\u5728\u9078\u64C7\u6027\u6307\u5B9A\u7684 <jarfile> \u4E2D\u3002\n\u9078\u9805\n   -j <jarfile>    \u6307\u5B9A\u8F09\u5165 translet \u7684\u4F86\u6E90 jarfile\n   -x              \u958B\u555F\u984D\u5916\u7684\u9664\u932F\u8A0A\u606F\u8F38\u51FA\n   -n <iterations> \u57F7\u884C\u8F49\u63DB <iterations> \u6B21\u6578\u8207\n                   \u986F\u793A\u5206\u6790\u8CC7\u8A0A\n   -u <document_url> \u6307\u5B9A XML \u8F38\u5165\u6587\u4EF6\u70BA URL\n"},



        /*
         * Note to translators:  "<xsl:sort>", "<xsl:for-each>" and
         * "<xsl:apply-templates>" are keywords that should not be translated.
         * The message indicates that an xsl:sort element must be a child of
         * one of the other kinds of elements mentioned.
         */
        {ErrorMsg.STRAY_SORT_ERR,
        "<xsl:sort> \u53EA\u80FD\u5728 <xsl:for-each> \u6216 <xsl:apply-templates> \u4E2D\u4F7F\u7528\u3002"},

        /*
         * Note to translators:  The message indicates that the encoding
         * requested for the output document was on that requires support that
         * is not available from the Java Virtual Machine being used to execute
         * the program.
         */
        {ErrorMsg.UNSUPPORTED_ENCODING,
        "\u6B64 JVM \u4E0D\u652F\u63F4\u8F38\u51FA\u7DE8\u78BC ''{0}''\u3002"},

        /*
         * Note to translators:  The message indicates that the XPath expression
         * named in the substitution text was not well formed syntactically.
         */
        {ErrorMsg.SYNTAX_ERR,
        "''{0}'' \u4E2D\u7684\u8A9E\u6CD5\u932F\u8AA4\u3002"},

        /*
         * Note to translators:  The substitution text is the name of a Java
         * class.  The term "constructor" here is the Java term.  The message is
         * displayed if XSLTC could not find a constructor for the specified
         * class.
         */
        {ErrorMsg.CONSTRUCTOR_NOT_FOUND,
        "\u627E\u4E0D\u5230\u5916\u90E8\u5EFA\u69CB\u5B50 ''{0}''\u3002"},

        /*
         * Note to translators:  "static" is the Java keyword.  The substitution
         * text is the name of a function.  The first argument of that function
         * is not of the required type.
         */
        {ErrorMsg.NO_JAVA_FUNCT_THIS_REF,
        "\u975E\u975C\u614B Java \u51FD\u6578 ''{0}'' \u7684\u7B2C\u4E00\u500B\u5F15\u6578\u4E0D\u662F\u6709\u6548\u7684\u7269\u4EF6\u53C3\u7167\u3002"},

        /*
         * Note to translators:  An XPath expression was not of the type
         * required in a particular context.  The substitution text is the
         * expression that was in error.
         */
        {ErrorMsg.TYPE_CHECK_ERR,
        "\u6AA2\u67E5\u8868\u793A\u5F0F ''{0}'' \u7684\u985E\u578B\u6642\u767C\u751F\u932F\u8AA4\u3002"},

        /*
         * Note to translators:  An XPath expression was not of the type
         * required in a particular context.  However, the location of the
         * problematic expression is unknown.
         */
        {ErrorMsg.TYPE_CHECK_UNK_LOC_ERR,
        "\u6AA2\u67E5\u4F4D\u65BC\u4E0D\u660E\u4F4D\u7F6E\u8868\u793A\u5F0F\u7684\u985E\u578B\u6642\u767C\u751F\u932F\u8AA4\u3002"},

        /*
         * Note to translators:  The substitution text is the name of a command-
         * line option that was not recognized.
         */
        {ErrorMsg.ILLEGAL_CMDLINE_OPTION_ERR,
        "\u547D\u4EE4\u884C\u9078\u9805 ''{0}'' \u7121\u6548\u3002"},

        /*
         * Note to translators:  The substitution text is the name of a command-
         * line option.
         */
        {ErrorMsg.CMDLINE_OPT_MISSING_ARG_ERR,
        "\u547D\u4EE4\u884C\u9078\u9805 ''{0}'' \u907A\u6F0F\u5FC5\u8981\u7684\u5F15\u6578\u3002"},

        /*
         * Note to translators:  This message is used to indicate the severity
         * of another message.  The substitution text contains two error
         * messages.  The spacing before the second substitution text indents
         * it the same amount as the first in English.
         */
        {ErrorMsg.WARNING_PLUS_WRAPPED_MSG,
        "WARNING:  ''{0}''\n       :{1}"},

        /*
         * Note to translators:  This message is used to indicate the severity
         * of another message.  The substitution text is an error message.
         */
        {ErrorMsg.WARNING_MSG,
        "WARNING:  ''{0}''"},

        /*
         * Note to translators:  This message is used to indicate the severity
         * of another message.  The substitution text contains two error
         * messages.  The spacing before the second substitution text indents
         * it the same amount as the first in English.
         */
        {ErrorMsg.FATAL_ERR_PLUS_WRAPPED_MSG,
        "FATAL ERROR:  ''{0}''\n           :{1}"},

        /*
         * Note to translators:  This message is used to indicate the severity
         * of another message.  The substitution text is an error message.
         */
        {ErrorMsg.FATAL_ERR_MSG,
        "FATAL ERROR:  ''{0}''"},

        /*
         * Note to translators:  This message is used to indicate the severity
         * of another message.  The substitution text contains two error
         * messages.  The spacing before the second substitution text indents
         * it the same amount as the first in English.
         */
        {ErrorMsg.ERROR_PLUS_WRAPPED_MSG,
        "ERROR:  ''{0}''\n     :{1}"},

        /*
         * Note to translators:  This message is used to indicate the severity
         * of another message.  The substitution text is an error message.
         */
        {ErrorMsg.ERROR_MSG,
        "ERROR:  ''{0}''"},

        /*
         * Note to translators:  The substitution text is the name of a class.
         */
        {ErrorMsg.TRANSFORM_WITH_TRANSLET_STR,
        "\u4F7F\u7528 translet ''{0}'' \u8F49\u63DB"},

        /*
         * Note to translators:  The first substitution is the name of a class,
         * while the second substitution is the name of a jar file.
         */
        {ErrorMsg.TRANSFORM_WITH_JAR_STR,
        "\u4F7F\u7528\u4F86\u81EA jar \u6A94\u6848 ''{1}'' \u7684 translet ''{0}'' \u8F49\u63DB"},

        /*
         * Note to translators:  "TransformerFactory" is the name of a Java
         * interface and must not be translated.  The substitution text is
         * the name of the class that could not be instantiated.
         */
        {ErrorMsg.COULD_NOT_CREATE_TRANS_FACT,
        "\u7121\u6CD5\u5EFA\u7ACB TransformerFactory \u985E\u5225 ''{0}'' \u7684\u57F7\u884C\u8655\u7406\u3002"},

        /*
         * Note to translators:  This message is produced when the user
         * specified a name for the translet class that contains characters
         * that are not permitted in a Java class name.  The substitution
         * text "{0}" specifies the name the user requested, while "{1}"
         * specifies the name the processor used instead.
         */
        {ErrorMsg.TRANSLET_NAME_JAVA_CONFLICT,
         "\u540D\u7A31 ''{0}'' \u7121\u6CD5\u4F5C\u70BA translet \u985E\u5225\u7684\u540D\u7A31\uFF0C\u56E0\u70BA\u5B83\u5305\u542B Java \u985E\u5225\u540D\u7A31\u4E0D\u5141\u8A31\u7684\u5B57\u5143\u3002\u8ACB\u6539\u7528\u540D\u7A31 ''{1}''\u3002"},

        /*
         * Note to translators:  The following message is used as a header.
         * All the error messages are collected together and displayed beneath
         * this message.
         */
        {ErrorMsg.COMPILER_ERROR_KEY,
        "\u7DE8\u8B6F\u5668\u932F\u8AA4:"},

        /*
         * Note to translators:  The following message is used as a header.
         * All the warning messages are collected together and displayed
         * beneath this message.
         */
        {ErrorMsg.COMPILER_WARNING_KEY,
        "\u7DE8\u8B6F\u5668\u8B66\u544A:"},

        /*
         * Note to translators:  The following message is used as a header.
         * All the error messages that are produced when the stylesheet is
         * applied to an input document are collected together and displayed
         * beneath this message.  A 'translet' is the compiled form of a
         * stylesheet (see above).
         */
        {ErrorMsg.RUNTIME_ERROR_KEY,
        "Translet \u932F\u8AA4:"},

        /*
         * Note to translators:  An attribute whose value is constrained to
         * be a "QName" or a list of "QNames" had a value that was incorrect.
         * 'QName' is an XML syntactic term that must not be translated.  The
         * substitution text contains the actual value of the attribute.
         */
        {ErrorMsg.INVALID_QNAME_ERR,
        "\u503C\u5FC5\u9808\u70BA QName \u6216\u4F7F\u7528\u7A7A\u683C\u52A0\u4EE5\u5340\u9694\u7684 QNames \u6E05\u55AE\u7684\u5C6C\u6027\uFF0C\u5177\u6709\u503C ''{0}''"},

        /*
         * Note to translators:  An attribute whose value is required to
         * be an "NCName".
         * 'NCName' is an XML syntactic term that must not be translated.  The
         * substitution text contains the actual value of the attribute.
         */
        {ErrorMsg.INVALID_NCNAME_ERR,
        "\u503C\u5FC5\u9808\u70BA NCName \u7684\u5C6C\u6027\uFF0C\u5177\u6709\u503C ''{0}''"},

        /*
         * Note to translators:  An attribute with an incorrect value was
         * encountered.  The permitted value is one of the literal values
         * "xml", "html" or "text"; it is also permitted to have the form of
         * a QName that is not also an NCName.  The terms "method",
         * "xsl:output", "xml", "html" and "text" are keywords that must not
         * be translated.  The term "qname-but-not-ncname" is an XML syntactic
         * term.  The substitution text contains the actual value of the
         * attribute.
         */
        {ErrorMsg.INVALID_METHOD_IN_OUTPUT,
        "<xsl:output> \u5143\u7D20\u7684\u65B9\u6CD5\u5C6C\u6027\u5177\u6709\u503C ''{0}''\u3002\u6B64\u503C\u5FC5\u9808\u662F ''xml''\u3001''html''\u3001''text'' \u6216 qname-but-not-ncname \u5176\u4E2D\u4E4B\u4E00"},

        {ErrorMsg.JAXP_GET_FEATURE_NULL_NAME,
        "TransformerFactory.getFeature(\u5B57\u4E32\u540D\u7A31) \u4E2D\u7684\u529F\u80FD\u540D\u7A31\u4E0D\u53EF\u70BA\u7A7A\u503C\u3002"},

        {ErrorMsg.JAXP_SET_FEATURE_NULL_NAME,
        "TransformerFactory.setFeature(\u5B57\u4E32\u540D\u7A31, \u5E03\u6797\u503C) \u4E2D\u7684\u529F\u80FD\u540D\u7A31\u4E0D\u53EF\u70BA\u7A7A\u503C\u3002"},

        {ErrorMsg.JAXP_UNSUPPORTED_FEATURE,
        "\u7121\u6CD5\u5728\u6B64 TransformerFactory \u4E0A\u8A2D\u5B9A\u529F\u80FD ''{0}''\u3002"},

        {ErrorMsg.JAXP_SECUREPROCESSING_FEATURE,
        "FEATURE_SECURE_PROCESSING: \u5B89\u5168\u7BA1\u7406\u7A0B\u5F0F\u5B58\u5728\u6642\uFF0C\u7121\u6CD5\u5C07\u529F\u80FD\u8A2D\u70BA\u507D\u3002"},

        /*
         * Note to translators:  This message describes an internal error in the
         * processor.  The term "byte code" is a Java technical term for the
         * executable code in a Java method, and "try-catch-finally block"
         * refers to the Java keywords with those names.  "Outlined" is a
         * technical term internal to XSLTC and should not be translated.
         */
        {ErrorMsg.OUTLINE_ERR_TRY_CATCH,
         "\u5167\u90E8 XSLTC \u932F\u8AA4:  \u7522\u751F\u7684\u4F4D\u5143\u7D44\u78BC\u5305\u542B try-catch-finally \u5340\u584A\uFF0C\u7121\u6CD5\u52A0\u4EE5 outlined."},

        /*
         * Note to translators:  This message describes an internal error in the
         * processor.  The terms "OutlineableChunkStart" and
         * "OutlineableChunkEnd" are the names of classes internal to XSLTC and
         * should not be translated.  The message indicates that for every
         * "start" there must be a corresponding "end", and vice versa, and
         * that if one of a pair of "start" and "end" appears between another
         * pair of corresponding "start" and "end", then the other half of the
         * pair must also be between that same enclosing pair.
         */
        {ErrorMsg.OUTLINE_ERR_UNBALANCED_MARKERS,
         "\u5167\u90E8 XSLTC \u932F\u8AA4:  OutlineableChunkStart \u548C OutlineableChunkEnd \u6A19\u8A18\u5FC5\u9808\u6210\u5C0D\u51FA\u73FE\uFF0C\u4E26\u4F7F\u7528\u6B63\u78BA\u7684\u5DE2\u72C0\u7D50\u69CB\u3002"},

        /*
         * Note to translators:  This message describes an internal error in the
         * processor.  The term "byte code" is a Java technical term for the
         * executable code in a Java method.  The "method" that is being
         * referred to is a Java method in a translet that XSLTC is generating
         * in processing a stylesheet.  The "instruction" that is being
         * referred to is one of the instrutions in the Java byte code in that
         * method.  "Outlined" is a technical term internal to XSLTC and
         * should not be translated.
         */
        {ErrorMsg.OUTLINE_ERR_DELETED_TARGET,
         "\u5167\u90E8 XSLTC \u932F\u8AA4:  \u539F\u59CB\u65B9\u6CD5\u4E2D\u4ECD\u7136\u53C3\u7167\u5C6C\u65BC outlined \u4F4D\u5143\u7D44\u78BC\u5340\u584A\u4E00\u90E8\u5206\u7684\u6307\u793A\u3002"
        },


        /*
         * Note to translators:  This message describes an internal error in the
         * processor.  The "method" that is being referred to is a Java method
         * in a translet that XSLTC is generating.
         *
         */
        {ErrorMsg.OUTLINE_ERR_METHOD_TOO_BIG,
         "\u5167\u90E8 XSLTC \u932F\u8AA4:  translet \u4E2D\u7684\u65B9\u6CD5\u8D85\u904E Java \u865B\u64EC\u6A5F\u5668\u5C0D\u65BC\u65B9\u6CD5\u9577\u5EA6 64 KB \u7684\u9650\u5236\u3002\u9019\u901A\u5E38\u662F\u56E0\u70BA\u6A23\u5F0F\u8868\u4E2D\u6709\u975E\u5E38\u5927\u7684\u6A23\u677F\u3002\u8ACB\u5617\u8A66\u91CD\u65B0\u7D44\u7E54\u60A8\u7684\u6A23\u5F0F\u8868\u4EE5\u4F7F\u7528\u8F03\u5C0F\u7684\u6A23\u677F\u3002"
        },

         {ErrorMsg.DESERIALIZE_TRANSLET_ERR, "\u555F\u7528 Java \u5B89\u5168\u6642\uFF0C\u6703\u505C\u7528\u9084\u539F\u5E8F\u5217\u5316 TemplatesImpl \u7684\u652F\u63F4\u3002\u5C07 jdk.xml.enableTemplatesImplDeserialization \u7CFB\u7D71\u5C6C\u6027\u8A2D\u70BA\u771F\u5373\u53EF\u8986\u5BEB\u6B64\u8A2D\u5B9A\u3002"}

    };

    }
}
