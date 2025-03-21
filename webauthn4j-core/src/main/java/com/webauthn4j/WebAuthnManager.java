/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webauthn4j;

import com.webauthn4j.converter.exception.DataConversionException;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.*;
import com.webauthn4j.verifier.AuthenticationDataVerifier;
import com.webauthn4j.verifier.CustomAuthenticationVerifier;
import com.webauthn4j.verifier.CustomRegistrationVerifier;
import com.webauthn4j.verifier.RegistrationDataVerifier;
import com.webauthn4j.verifier.attestation.statement.AttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.statement.androidkey.NullAndroidKeyAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.statement.androidsafetynet.NullAndroidSafetyNetAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.statement.apple.NullAppleAnonymousAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.statement.none.NoneAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.statement.packed.NullPackedAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.statement.tpm.NullTPMAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.statement.u2f.NullFIDOU2FAttestationStatementVerifier;
import com.webauthn4j.verifier.attestation.trustworthiness.certpath.CertPathTrustworthinessVerifier;
import com.webauthn4j.verifier.attestation.trustworthiness.certpath.NullCertPathTrustworthinessVerifier;
import com.webauthn4j.verifier.attestation.trustworthiness.self.NullSelfAttestationTrustworthinessVerifier;
import com.webauthn4j.verifier.attestation.trustworthiness.self.SelfAttestationTrustworthinessVerifier;
import com.webauthn4j.verifier.exception.VerificationException;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("java:S6539")
public class WebAuthnManager {

    // ~ Instance fields
    // ================================================================================================

    private final WebAuthnRegistrationManager webAuthnRegistrationManager;
    private final WebAuthnAuthenticationManager webAuthnAuthenticationManager;

    public WebAuthnManager(@NotNull List<AttestationStatementVerifier> attestationStatementVerifiers,
                           @NotNull CertPathTrustworthinessVerifier certPathTrustworthinessVerifier,
                           @NotNull SelfAttestationTrustworthinessVerifier selfAttestationTrustworthinessVerifier,
                           @NotNull List<CustomRegistrationVerifier> customRegistrationVerifiers,
                           @NotNull List<CustomAuthenticationVerifier> customAuthenticationVerifiers,
                           @NotNull ObjectConverter objectConverter) {

        this.webAuthnRegistrationManager = new WebAuthnRegistrationManager(
                attestationStatementVerifiers,
                certPathTrustworthinessVerifier,
                selfAttestationTrustworthinessVerifier,
                customRegistrationVerifiers,
                objectConverter);
        this.webAuthnAuthenticationManager = new WebAuthnAuthenticationManager(
                customAuthenticationVerifiers,
                objectConverter);
    }

    public WebAuthnManager(@NotNull List<AttestationStatementVerifier> attestationStatementVerifiers,
                           @NotNull CertPathTrustworthinessVerifier certPathTrustworthinessVerifier,
                           @NotNull SelfAttestationTrustworthinessVerifier selfAttestationTrustworthinessVerifier,
                           @NotNull List<CustomRegistrationVerifier> customRegistrationVerifiers,
                           @NotNull List<CustomAuthenticationVerifier> customAuthenticationVerifiers) {
        this(
                attestationStatementVerifiers,
                certPathTrustworthinessVerifier,
                selfAttestationTrustworthinessVerifier,
                customRegistrationVerifiers,
                customAuthenticationVerifiers,
                new ObjectConverter()
        );
    }

    public WebAuthnManager(@NotNull List<AttestationStatementVerifier> attestationStatementVerifiers,
                           @NotNull CertPathTrustworthinessVerifier certPathTrustworthinessVerifier,
                           @NotNull SelfAttestationTrustworthinessVerifier selfAttestationTrustworthinessVerifier,
                           @NotNull ObjectConverter objectConverter) {
        this(
                attestationStatementVerifiers,
                certPathTrustworthinessVerifier,
                selfAttestationTrustworthinessVerifier,
                new ArrayList<>(),
                new ArrayList<>(),
                objectConverter
        );
    }

    public WebAuthnManager(@NotNull List<AttestationStatementVerifier> attestationStatementVerifiers,
                           @NotNull CertPathTrustworthinessVerifier certPathTrustworthinessVerifier,
                           @NotNull SelfAttestationTrustworthinessVerifier selfAttestationTrustworthinessVerifier) {
        this(
                attestationStatementVerifiers,
                certPathTrustworthinessVerifier,
                selfAttestationTrustworthinessVerifier,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    // ~ Factory methods
    // ========================================================================================================

    /**
     * Creates {@link WebAuthnManager} with non strict configuration
     *
     * @return configured {@link WebAuthnManager}
     */
    public static @NotNull WebAuthnManager createNonStrictWebAuthnManager() {
        ObjectConverter objectConverter = new ObjectConverter();
        return createNonStrictWebAuthnManager(objectConverter);
    }

    /**
     * Creates {@link WebAuthnManager} with non strict configuration
     *
     * @param objectConverter ObjectConverter
     * @return configured {@link WebAuthnManager}
     */
    public static @NotNull WebAuthnManager createNonStrictWebAuthnManager(@NotNull ObjectConverter objectConverter) {
        return new WebAuthnManager(
                Arrays.asList(
                        new NoneAttestationStatementVerifier(),
                        new NullFIDOU2FAttestationStatementVerifier(),
                        new NullPackedAttestationStatementVerifier(),
                        new NullTPMAttestationStatementVerifier(),
                        new NullAndroidKeyAttestationStatementVerifier(),
                        new NullAndroidSafetyNetAttestationStatementVerifier(),
                        new NullAppleAnonymousAttestationStatementVerifier()
                ),
                new NullCertPathTrustworthinessVerifier(),
                new NullSelfAttestationTrustworthinessVerifier(),
                objectConverter
        );
    }

    public @NotNull RegistrationData parseRegistrationResponseJSON(@NotNull String registrationResponseJSON){
        return this.webAuthnRegistrationManager.parse(registrationResponseJSON);
    }

    public @NotNull RegistrationData parseRegistrationResponseJSON(@NotNull InputStream registrationResponseJSON){
        return this.webAuthnRegistrationManager.parse(registrationResponseJSON);
    }

    @SuppressWarnings("squid:S1130")
    public @NotNull RegistrationData parse(@NotNull RegistrationRequest registrationRequest) throws DataConversionException {
        return this.webAuthnRegistrationManager.parse(registrationRequest);
    }

    public @NotNull RegistrationData verifyRegistrationResponseJSON(@NotNull String registrationResponseJSON, @NotNull RegistrationParameters registrationParameters) throws DataConversionException, VerificationException {
        return this.webAuthnRegistrationManager.verify(registrationResponseJSON, registrationParameters);
    }

    public @NotNull RegistrationData verifyRegistrationResponseJSON(@NotNull InputStream registrationResponseJSON, @NotNull RegistrationParameters registrationParameters) throws DataConversionException, VerificationException {
        return this.webAuthnRegistrationManager.verify(registrationResponseJSON, registrationParameters);
    }

    @SuppressWarnings("squid:S1130")
    public @NotNull RegistrationData verify(@NotNull RegistrationRequest registrationRequest, @NotNull RegistrationParameters registrationParameters) throws DataConversionException, VerificationException {
        return this.webAuthnRegistrationManager.verify(registrationRequest, registrationParameters);
    }

    /**
     * @deprecated renamed to 'verify`
     */
    @Deprecated
    public @NotNull RegistrationData validate(@NotNull RegistrationRequest registrationRequest, @NotNull RegistrationParameters registrationParameters) throws DataConversionException, VerificationException {
        return verify(registrationRequest, registrationParameters);
    }

    @SuppressWarnings("squid:S1130")
    public @NotNull RegistrationData verify(@NotNull RegistrationData registrationData, @NotNull RegistrationParameters registrationParameters) throws VerificationException {
        return this.webAuthnRegistrationManager.verify(registrationData, registrationParameters);
    }

    /**
     * @deprecated renamed to 'verify`
     */
    @Deprecated
    public @NotNull RegistrationData validate(@NotNull RegistrationData registrationData, @NotNull RegistrationParameters registrationParameters) throws VerificationException {
        return verify(registrationData, registrationParameters);
    }

    public @NotNull AuthenticationData parseAuthenticationResponseJSON(@NotNull String authenticationResponseJSON) throws DataConversionException {
        return this.webAuthnAuthenticationManager.parse(authenticationResponseJSON);
    }

    public @NotNull AuthenticationData parseAuthenticationResponseJSON(@NotNull InputStream authenticationResponseJSON) throws DataConversionException {
        return this.webAuthnAuthenticationManager.parse(authenticationResponseJSON);
    }

    @SuppressWarnings("squid:S1130")
    public @NotNull AuthenticationData parse(@NotNull AuthenticationRequest authenticationRequest) throws DataConversionException {
        return this.webAuthnAuthenticationManager.parse(authenticationRequest);
    }

    public @NotNull AuthenticationData verifyAuthenticationResponseJSON(@NotNull String authenticationResponseJSON, @NotNull AuthenticationParameters authenticationParameters) throws DataConversionException, VerificationException {
        return this.webAuthnAuthenticationManager.verify(authenticationResponseJSON, authenticationParameters);
    }

    public @NotNull AuthenticationData verifyAuthenticationResponseJSON(@NotNull InputStream authenticationResponseJSON, @NotNull AuthenticationParameters authenticationParameters) throws DataConversionException, VerificationException {
        return this.webAuthnAuthenticationManager.verify(authenticationResponseJSON, authenticationParameters);
    }

    @SuppressWarnings("squid:S1130")
    public @NotNull AuthenticationData verify(@NotNull AuthenticationRequest authenticationRequest, @NotNull AuthenticationParameters authenticationParameters) throws DataConversionException, VerificationException {
        return this.webAuthnAuthenticationManager.verify(authenticationRequest, authenticationParameters);
    }

    /**
     * @deprecated renamed to 'verify`
     */
    @Deprecated
    public @NotNull AuthenticationData validate(@NotNull AuthenticationRequest authenticationRequest, @NotNull AuthenticationParameters authenticationParameters) throws DataConversionException, VerificationException {
        return verify(authenticationRequest, authenticationParameters);
    }

    @SuppressWarnings("squid:S1130")
    public @NotNull AuthenticationData verify(@NotNull AuthenticationData authenticationData, @NotNull AuthenticationParameters authenticationParameters) throws VerificationException {
        return this.webAuthnAuthenticationManager.verify(authenticationData, authenticationParameters);
    }

    /**
     * @deprecated renamed to 'verify`
     */
    @Deprecated
    public @NotNull AuthenticationData validate(@NotNull AuthenticationData authenticationData, @NotNull AuthenticationParameters authenticationParameters) throws VerificationException {
        return verify(authenticationData, authenticationParameters);
    }

    public @NotNull RegistrationDataVerifier getRegistrationDataVerifier() {
        return this.webAuthnRegistrationManager.getRegistrationDataVerifier();
    }

    public @NotNull AuthenticationDataVerifier getAuthenticationDataVerifier() {
        return this.webAuthnAuthenticationManager.getAuthenticationDataVerifier();
    }
}
