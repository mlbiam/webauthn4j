module com.webauthn4j.core {
    requires java.naming;

    requires com.webauthn4j.util;

    requires org.slf4j;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.cbor;

    requires static org.jetbrains.annotations;

    exports com.webauthn4j;
    exports com.webauthn4j.anchor;
    exports com.webauthn4j.authenticator;
    exports com.webauthn4j.converter;
    exports com.webauthn4j.converter.exception;
    exports com.webauthn4j.converter.jackson;
    exports com.webauthn4j.converter.jackson.serializer.cbor;
    exports com.webauthn4j.converter.jackson.serializer.json;
    exports com.webauthn4j.converter.jackson.deserializer.cbor;
    exports com.webauthn4j.converter.jackson.deserializer.json;
    exports com.webauthn4j.converter.util;
    exports com.webauthn4j.credential;
    exports com.webauthn4j.data;
    exports com.webauthn4j.data.attestation;
    exports com.webauthn4j.data.attestation.statement;
    exports com.webauthn4j.data.attestation.authenticator;
    exports com.webauthn4j.data.client;
    exports com.webauthn4j.data.client.challenge;
    exports com.webauthn4j.data.extension;
    exports com.webauthn4j.data.extension.authenticator;
    exports com.webauthn4j.data.extension.client;
    exports com.webauthn4j.data.jws;
    exports com.webauthn4j.server;
    exports com.webauthn4j.verifier;
    exports com.webauthn4j.verifier.internal.asn1 to com.webauthn4j.appattest, com.webauthn4j.metadata.async;
    exports com.webauthn4j.verifier.attestation.statement;
    exports com.webauthn4j.verifier.attestation.statement.androidkey;
    exports com.webauthn4j.verifier.attestation.statement.androidsafetynet;
    exports com.webauthn4j.verifier.attestation.statement.apple;
    exports com.webauthn4j.verifier.attestation.statement.none;
    exports com.webauthn4j.verifier.attestation.statement.packed;
    exports com.webauthn4j.verifier.attestation.statement.tpm;
    exports com.webauthn4j.verifier.attestation.statement.u2f;
    exports com.webauthn4j.verifier.attestation.trustworthiness.certpath;
    exports com.webauthn4j.verifier.attestation.trustworthiness.self;
    exports com.webauthn4j.verifier.exception;
    exports com.webauthn4j.verifier.internal;
}
