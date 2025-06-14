/*
 * Copyright (c) 2007, 2025, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.sun.org.apache.xml.internal.security.algorithms.implementations;

import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;

import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithmSpi;
import com.sun.org.apache.xml.internal.security.signature.XMLSignature;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.crypto.dsig.DigestMethod;

public abstract class SignatureBaseRSA extends SignatureAlgorithmSpi {

    private static final com.sun.org.slf4j.internal.Logger LOG =
        com.sun.org.slf4j.internal.LoggerFactory.getLogger(SignatureBaseRSA.class);

    /** Field algorithm */
    private final Signature signatureAlgorithm;

    /**
     * Constructor SignatureRSA
     *
     * @throws XMLSignatureException
     */
    public SignatureBaseRSA() throws XMLSignatureException {
        this(null);
    }

    public SignatureBaseRSA(Provider provider) throws XMLSignatureException {
        String algorithmID = JCEMapper.translateURItoJCEID(this.engineGetURI());
        this.signatureAlgorithm = getSignature(provider, algorithmID);
        LOG.debug("Created SignatureRSA using {0} and provider {1}",
            algorithmID, signatureAlgorithm.getProvider());
    }

    Signature getSignature(Provider provider, String algorithmID)
        throws XMLSignatureException {
        try {
            if (provider == null) {
                String providerId = JCEMapper.getProviderId();
                if (providerId == null) {
                    return Signature.getInstance(algorithmID);

                } else {
                    return Signature.getInstance(algorithmID, providerId);
                }

            } else {
                return Signature.getInstance(algorithmID, provider);
            }

        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            Object[] exArgs = {algorithmID, ex.getLocalizedMessage()};
            throw new XMLSignatureException("algorithms.NoSuchAlgorithm", exArgs);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void engineSetParameter(AlgorithmParameterSpec params)
        throws XMLSignatureException {
        try {
            this.signatureAlgorithm.setParameter(params);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new XMLSignatureException(ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected boolean engineVerify(byte[] signature) throws XMLSignatureException {
        try {
            return this.signatureAlgorithm.verify(signature);
        } catch (SignatureException ex) {
            throw new XMLSignatureException(ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void engineInitVerify(Key publicKey) throws XMLSignatureException {
        engineInitVerify(publicKey, this.signatureAlgorithm);
    }

    /** {@inheritDoc} */
    @Override
    protected byte[] engineSign() throws XMLSignatureException {
        try {
            return this.signatureAlgorithm.sign();
        } catch (SignatureException ex) {
            throw new XMLSignatureException(ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void engineInitSign(Key privateKey, SecureRandom secureRandom)
        throws XMLSignatureException {
        engineInitSign(privateKey, secureRandom, this.signatureAlgorithm);
    }

    /** {@inheritDoc} */
    @Override
    protected void engineInitSign(Key privateKey) throws XMLSignatureException {
        engineInitSign(privateKey, (SecureRandom)null);
    }

    /** {@inheritDoc} */
    @Override
    protected void engineUpdate(byte[] input) throws XMLSignatureException {
        try {
            this.signatureAlgorithm.update(input);
        } catch (SignatureException ex) {
            throw new XMLSignatureException(ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void engineUpdate(byte input) throws XMLSignatureException {
        try {
            this.signatureAlgorithm.update(input);
        } catch (SignatureException ex) {
            throw new XMLSignatureException(ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void engineUpdate(byte[] buf, int offset, int len) throws XMLSignatureException {
        try {
            this.signatureAlgorithm.update(buf, offset, len);
        } catch (SignatureException ex) {
            throw new XMLSignatureException(ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected String engineGetJCEAlgorithmString() {
        return this.signatureAlgorithm.getAlgorithm();
    }

    /** {@inheritDoc} */
    @Override
    protected String engineGetJCEProviderName() {
        return this.signatureAlgorithm.getProvider().getName();
    }

    /** {@inheritDoc} */
    @Override
    protected void engineSetHMACOutputLength(int HMACOutputLength)
        throws XMLSignatureException {
        throw new XMLSignatureException("algorithms.HMACOutputLengthOnlyForHMAC");
    }

    /** {@inheritDoc} */
    @Override
    protected void engineInitSign(
        Key signingKey, AlgorithmParameterSpec algorithmParameterSpec
    ) throws XMLSignatureException {
        throw new XMLSignatureException("algorithms.CannotUseAlgorithmParameterSpecOnRSA");
    }

    /**
     * Class SignatureRSASHA1
     */
    public static class SignatureRSASHA1 extends SignatureBaseRSA {

        /**
         * Constructor SignatureRSASHA1
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA1() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA1(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1;
        }
    }

    /**
     * Class SignatureRSASHA224
     */
    public static class SignatureRSASHA224 extends SignatureBaseRSA {

        /**
         * Constructor SignatureRSASHA224
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA224() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA224(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA224;
        }
    }

    /**
     * Class SignatureRSASHA256
     */
    public static class SignatureRSASHA256 extends SignatureBaseRSA {

        /**
         * Constructor SignatureRSASHA256
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA256() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA256(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256;
        }
    }

    /**
     * Class SignatureRSASHA384
     */
    public static class SignatureRSASHA384 extends SignatureBaseRSA {

        /**
         * Constructor SignatureRSASHA384
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA384() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA384(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA384;
        }
    }

    /**
     * Class SignatureRSASHA512
     */
    public static class SignatureRSASHA512 extends SignatureBaseRSA {

        /**
         * Constructor SignatureRSASHA512
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA512() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA512(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA512;
        }
    }

    /**
     * Class SignatureRSARIPEMD160
     */
    public static class SignatureRSARIPEMD160 extends SignatureBaseRSA {

        /**
         * Constructor SignatureRSARIPEMD160
         *
         * @throws XMLSignatureException
         */
        public SignatureRSARIPEMD160() throws XMLSignatureException {
            super();
        }

        public SignatureRSARIPEMD160(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_RIPEMD160;
        }
    }

    /**
     * Class SignatureRSAMD5
     */
    public static class SignatureRSAMD5 extends SignatureBaseRSA {

        /**
         * Constructor SignatureRSAMD5
         *
         * @throws XMLSignatureException
         */
        public SignatureRSAMD5() throws XMLSignatureException {
            super();
        }

        public SignatureRSAMD5(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_NOT_RECOMMENDED_RSA_MD5;
        }
    }

    public abstract static class SignatureBaseRSAPSS extends SignatureBaseRSA {

        public SignatureBaseRSAPSS() throws XMLSignatureException {
            super();
        }

        public SignatureBaseRSAPSS(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        @Override
        Signature getSignature(Provider provider, String algorithmID)
            throws XMLSignatureException {
            try {
                Signature sig;
                if (provider == null) {
                    String providerId = JCEMapper.getProviderId();
                    if (providerId == null) {
                        sig = Signature.getInstance("RSASSA-PSS");
                    } else {
                        sig = Signature.getInstance("RSASSA-PSS", providerId);
                    }
                } else {
                    sig = Signature.getInstance("RSASSA-PSS", provider);
                }
                try {
                    sig.setParameter(getPSSParameterSpec());
                } catch (InvalidAlgorithmParameterException e) {
                    throw new NoSuchAlgorithmException("Should not happen", e);
                }
                return sig;
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                return super.getSignature(provider, algorithmID);
            }
        }

        abstract PSSParameterSpec getPSSParameterSpec();
    }

    /**
     * Class SignatureRSASHA1MGF1
     */
    public static class SignatureRSASHA1MGF1 extends SignatureBaseRSAPSS {

        private static final PSSParameterSpec SHA1_MGF1_PARAMS
                = new PSSParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1,
                20, PSSParameterSpec.TRAILER_FIELD_BC);

        /**
         * Constructor SignatureRSASHA1MGF1
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA1MGF1() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA1MGF1(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1_MGF1;
        }

        @Override
        public PSSParameterSpec getPSSParameterSpec() {
            return SHA1_MGF1_PARAMS;
        }
    }

    /**
     * Class SignatureRSASHA224MGF1
     */
    public static class SignatureRSASHA224MGF1 extends SignatureBaseRSAPSS {

        private static final PSSParameterSpec SHA224_MGF1_PARAMS
                = new PSSParameterSpec("SHA-224", "MGF1", MGF1ParameterSpec.SHA224,
                28, PSSParameterSpec.TRAILER_FIELD_BC);

        /**
         * Constructor SignatureRSASHA224MGF1
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA224MGF1() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA224MGF1(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA224_MGF1;
        }

        @Override
        public PSSParameterSpec getPSSParameterSpec() {
            return SHA224_MGF1_PARAMS;
        }
    }

    /**
     * Class SignatureRSASHA256MGF1
     */
    public static class SignatureRSASHA256MGF1 extends SignatureBaseRSAPSS {

        private static final PSSParameterSpec SHA256_MGF1_PARAMS
                = new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256,
                32, PSSParameterSpec.TRAILER_FIELD_BC);

        /**
         * Constructor SignatureRSASHA256MGF1
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA256MGF1() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA256MGF1(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256_MGF1;
        }

        @Override
        public PSSParameterSpec getPSSParameterSpec() {
            return SHA256_MGF1_PARAMS;
        }
    }

    /**
     * Class SignatureRSASHA384MGF1
     */
    public static class SignatureRSASHA384MGF1 extends SignatureBaseRSAPSS {

        private static final PSSParameterSpec SHA384_MGF1_PARAMS
                = new PSSParameterSpec("SHA-384", "MGF1", MGF1ParameterSpec.SHA384,
                48, PSSParameterSpec.TRAILER_FIELD_BC);

        /**
         * Constructor SignatureRSASHA384MGF1
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA384MGF1() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA384MGF1(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA384_MGF1;
        }

        @Override
        public PSSParameterSpec getPSSParameterSpec() {
            return SHA384_MGF1_PARAMS;
        }
    }

    /**
     * Class SignatureRSASHA512MGF1
     */
    public static class SignatureRSASHA512MGF1 extends SignatureBaseRSAPSS {

        private static final PSSParameterSpec SHA512_MGF1_PARAMS
                = new PSSParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512,
                64, PSSParameterSpec.TRAILER_FIELD_BC);

        /**
         * Constructor SignatureRSASHA512MGF1
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA512MGF1() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA512MGF1(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA512_MGF1;
        }

        @Override
        public PSSParameterSpec getPSSParameterSpec() {
            return SHA512_MGF1_PARAMS;
        }
    }

    /**
     * Class SignatureRSA3_SHA224MGF1
     */
    public static class SignatureRSASHA3_224MGF1 extends SignatureBaseRSAPSS {

        private static final PSSParameterSpec SHA3_224_MGF1_PARAMS
                = new PSSParameterSpec("SHA3-224", "MGF1",
                new MGF1ParameterSpec("SHA3-224"),
                28, PSSParameterSpec.TRAILER_FIELD_BC);

        /**
         * Constructor SignatureRSASHA3_224MGF1
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA3_224MGF1() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA3_224MGF1(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA3_224_MGF1;
        }

        @Override
        public PSSParameterSpec getPSSParameterSpec() {
            return SHA3_224_MGF1_PARAMS;
        }
    }

    /**
     * Class SignatureRSA3_SHA256MGF1
     */
    public static class SignatureRSASHA3_256MGF1 extends SignatureBaseRSAPSS {

        private static final PSSParameterSpec SHA3_256_MGF1_PARAMS
                = new PSSParameterSpec("SHA3-256", "MGF1",
                new MGF1ParameterSpec("SHA3-256"),
                32, PSSParameterSpec.TRAILER_FIELD_BC);

        /**
         * Constructor SignatureRSASHA3_256MGF1
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA3_256MGF1() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA3_256MGF1(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA3_256_MGF1;
        }

        @Override
        public PSSParameterSpec getPSSParameterSpec() {
            return SHA3_256_MGF1_PARAMS;
        }
    }

    /**
     * Class SignatureRSA3_SHA384MGF1
     */
    public static class SignatureRSASHA3_384MGF1 extends SignatureBaseRSAPSS {

        private static final PSSParameterSpec SHA3_384_MGF1_PARAMS
                = new PSSParameterSpec("SHA3-384", "MGF1",
                new MGF1ParameterSpec("SHA3-384"),
                48, PSSParameterSpec.TRAILER_FIELD_BC);

        /**
         * Constructor SignatureRSASHA3_384MGF1
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA3_384MGF1() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA3_384MGF1(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA3_384_MGF1;
        }

        @Override
        public PSSParameterSpec getPSSParameterSpec() {
            return SHA3_384_MGF1_PARAMS;
        }
    }

    /**
     * Class SignatureRSASHA3_512MGF1
     */
    public static class SignatureRSASHA3_512MGF1 extends SignatureBaseRSAPSS {

        private static final PSSParameterSpec SHA3_512_MGF1_PARAMS
                = new PSSParameterSpec("SHA3-512", "MGF1",
                new MGF1ParameterSpec("SHA3-512"),
                64, PSSParameterSpec.TRAILER_FIELD_BC);

        /**
         * Constructor SignatureRSASHA3_512MGF1
         *
         * @throws XMLSignatureException
         */
        public SignatureRSASHA3_512MGF1() throws XMLSignatureException {
            super();
        }

        public SignatureRSASHA3_512MGF1(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        /** {@inheritDoc} */
        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA3_512_MGF1;
        }

        @Override
        public PSSParameterSpec getPSSParameterSpec() {
            return SHA3_512_MGF1_PARAMS;
        }
    }

    public static class SignatureRSASSAPSS extends SignatureBaseRSA {
        private PSSParameterSpec pssParameterSpec;

        public enum DigestAlgorithm {
            SHA224("SHA-224", "http://www.w3.org/2001/04/xmldsig-more#sha224", 28),
            SHA256("SHA-256", DigestMethod.SHA256, 32),
            SHA384("SHA-384", "http://www.w3.org/2001/04/xmldsig-more#sha384", 48),
            SHA512("SHA-512", DigestMethod.SHA512, 64),
            SHA3_224("SHA3-224", "http://www.w3.org/2007/05/xmldsig-more#sha3-224", 28),
            SHA3_256("SHA3-256", "http://www.w3.org/2007/05/xmldsig-more#sha3-256", 32),
            SHA3_384("SHA3-384", "http://www.w3.org/2007/05/xmldsig-more#sha3-384", 48),
            SHA3_512("SHA3-512", "http://www.w3.org/2007/05/xmldsig-more#sha3-512", 64);

            private final String xmlDigestAlgorithm;
            private final String digestAlgorithm;
            private final int saltLength;

            DigestAlgorithm(String digestAlgorithm, String xmlDigestAlgorithm, int saltLength) {
                this.digestAlgorithm = digestAlgorithm;
                this.xmlDigestAlgorithm = xmlDigestAlgorithm;
                this.saltLength = saltLength;
            }

            public String getXmlDigestAlgorithm() {
                return xmlDigestAlgorithm;
            }

            public String getDigestAlgorithm() {
                return digestAlgorithm;
            }

            public int getSaltLength() {
                return saltLength;
            }

            public static DigestAlgorithm fromXmlDigestAlgorithm(String xmlDigestAlgorithm) throws XMLSignatureException {
                for (DigestAlgorithm value : DigestAlgorithm.values()) {
                    if(value.getXmlDigestAlgorithm().equals(xmlDigestAlgorithm)) {
                        return value;
                    }
                }
                throw new XMLSignatureException();
            }

            public static DigestAlgorithm fromDigestAlgorithm(String digestAlgorithm) throws XMLSignatureException {
                for (DigestAlgorithm value : DigestAlgorithm.values()) {
                    if(value.getDigestAlgorithm().equals(digestAlgorithm)) {
                        return value;
                    }
                }
                throw new XMLSignatureException();

            }

        }

        public SignatureRSASSAPSS() throws XMLSignatureException {
            super();
        }

        public SignatureRSASSAPSS(Provider provider) throws XMLSignatureException {
            super(provider);
        }

        @Override
        public String engineGetURI() {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_PSS;
        }

        @Override
        protected void engineAddContextToElement(Element element) throws XMLSignatureException {
            if (element == null) {
                throw new IllegalArgumentException("null element");
            }

            Document doc = element.getOwnerDocument();
            Element rsaPssParamsElement = doc.createElementNS(Constants.XML_DSIG_NS_MORE_07_05, "pss" + ":" + Constants._TAG_RSAPSSPARAMS);
            rsaPssParamsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:" + "pss", Constants.XML_DSIG_NS_MORE_07_05);

            Element digestMethodElement = XMLUtils.createElementInSignatureSpace(rsaPssParamsElement.getOwnerDocument(), Constants._TAG_DIGESTMETHOD);
            digestMethodElement.setAttributeNS(null, Constants._ATT_ALGORITHM, DigestAlgorithm.fromDigestAlgorithm(pssParameterSpec.getDigestAlgorithm()).getXmlDigestAlgorithm());
            XMLUtils.addReturnToElement(rsaPssParamsElement);
            rsaPssParamsElement.appendChild(digestMethodElement);
            XMLUtils.addReturnToElement(rsaPssParamsElement);

            Element saltLengthElement = rsaPssParamsElement.getOwnerDocument().createElementNS(Constants.XML_DSIG_NS_MORE_07_05, "pss" + ":" + Constants._TAG_SALTLENGTH);
            Text saltLengthText = rsaPssParamsElement.getOwnerDocument().createTextNode(String.valueOf(pssParameterSpec.getSaltLength()));
            saltLengthElement.appendChild(saltLengthText);

            rsaPssParamsElement.appendChild(saltLengthElement);
            XMLUtils.addReturnToElement(rsaPssParamsElement);

            Element trailerFieldElement = rsaPssParamsElement.getOwnerDocument().createElementNS(Constants.XML_DSIG_NS_MORE_07_05, "pss" + ":" + Constants._TAG_TRAILERFIELD);
            Text trailerFieldText = rsaPssParamsElement.getOwnerDocument().createTextNode(String.valueOf(pssParameterSpec.getTrailerField()));
            trailerFieldElement.appendChild(trailerFieldText);

            rsaPssParamsElement.appendChild(trailerFieldElement);
            XMLUtils.addReturnToElement(rsaPssParamsElement);

            XMLUtils.addReturnToElement(element);
            element.appendChild(rsaPssParamsElement);
            XMLUtils.addReturnToElement(element);
        }

        @Override
        protected void engineGetContextFromElement(Element element) throws XMLSignatureException {
            if (pssParameterSpec == null) {
                super.engineGetContextFromElement(element);
                Element rsaPssParams = XMLUtils.selectNode(element.getFirstChild(), Constants.XML_DSIG_NS_MORE_07_05, Constants._TAG_RSAPSSPARAMS, 0);
                if (rsaPssParams == null) {
                    throw new XMLSignatureException("algorithms.MissingRSAPSSParams");
                }

                Element saltLengthNode = XMLUtils.selectNode(rsaPssParams.getFirstChild(), Constants.XML_DSIG_NS_MORE_07_05, Constants._TAG_SALTLENGTH, 0);
                Element trailerFieldNode = XMLUtils.selectNode(rsaPssParams.getFirstChild(), Constants.XML_DSIG_NS_MORE_07_05, Constants._TAG_TRAILERFIELD, 0);
                int trailerField = 1;
                if (trailerFieldNode != null) {
                    try {
                        trailerField = Integer.parseInt(trailerFieldNode.getTextContent());
                    } catch (NumberFormatException ex) {
                        throw new XMLSignatureException("empty", new Object[] {"Invalid trailer field value supplied"});
                    }
                }
                String xmlAlgorithm = XMLUtils.selectDsNode(rsaPssParams.getFirstChild(), Constants._TAG_DIGESTMETHOD, 0).getAttribute(Constants._ATT_ALGORITHM);
                DigestAlgorithm digestAlgorithm = DigestAlgorithm.fromXmlDigestAlgorithm(xmlAlgorithm);
                String digestAlgorithmName = digestAlgorithm.getDigestAlgorithm();
                int saltLength = digestAlgorithm.getSaltLength();
                if (saltLengthNode != null) {
                    try {
                        saltLength = Integer.parseInt(saltLengthNode.getTextContent());
                    } catch (NumberFormatException ex) {
                        throw new XMLSignatureException("empty", new Object[] {"Invalid salt length value supplied"});
                    }
                }
                engineSetParameter(new PSSParameterSpec(digestAlgorithmName, "MGF1", new MGF1ParameterSpec(digestAlgorithmName), saltLength, trailerField));
            }
        }

        @Override
        protected void engineSetParameter(AlgorithmParameterSpec params) throws XMLSignatureException {
            pssParameterSpec = (PSSParameterSpec) params;
            super.engineSetParameter(params);
        }

    }
}
