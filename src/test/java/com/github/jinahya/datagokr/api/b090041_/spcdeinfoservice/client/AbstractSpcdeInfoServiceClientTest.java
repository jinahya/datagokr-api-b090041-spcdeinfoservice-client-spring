package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client;

import static java.util.Objects.requireNonNull;

/**
 * An abstract base class for testing subclasses of {@link AbstractSpcdeInfoServiceClient} class.
 *
 * @param <T> subclass type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
abstract class AbstractSpcdeInfoServiceClientTest<T extends AbstractSpcdeInfoServiceClient> {

    /**
     * Creates a new instance with specified client class.
     *
     * @param clientClass the client class.
     * @see #clientClass
     */
    AbstractSpcdeInfoServiceClientTest(final Class<T> clientClass) {
        super();
        this.clientClass = requireNonNull(clientClass, "clientClass is null");
    }

    /**
     * Returns a new instance of {@link #clientClass}.
     *
     * @return a new instance of {@link #clientClass}.
     */
    protected T clientInstance() {
        try {
            return clientClass.getConstructor().newInstance();
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to instantiate " + clientClass, roe);
        }
    }

    /**
     * The clint class to test.
     */
    protected final Class<T> clientClass;
}