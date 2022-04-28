package org.kevink.dubbok.serialize.kryo.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The kryo factory learned from dubbo source code.
 */
public abstract class AbstractKryoFactory implements KryoFactory {

    private final Set<Class<?>> registrations =
            new LinkedHashSet<>();

    private volatile boolean kryoCreated;

    private boolean registrationRequired;

    private boolean references = true;

    /**
     * Only supposed to be called at startup time.
     */
    public void registerClass(Class<?> clazz) {
        if (kryoCreated) throw new IllegalStateException(
                "Cannot register class after creating kryo instance.");
        registrations.add(clazz);
    }

    /**
     * Only supposed to be called at startup time.
     */
    public void setReferences(boolean references) {
        if (kryoCreated) throw new IllegalStateException(
                "Cannot set references after creating kryo instance.");
        this.references = references;
    }

    /**
     * Only supposed to be called at startup time.
     */
    public void setRegistrationRequired(boolean registrationRequired) {
        if (kryoCreated) throw new IllegalStateException(
                "Cannot set registrationRequired after creating kryo instance.");
        this.registrationRequired = registrationRequired;
    }

    @Override
    public Kryo create() {
        if (!kryoCreated) kryoCreated = true;

        Kryo kryo = new Kryo();
        kryo.setReferences(references);
        kryo.setRegistrationRequired(registrationRequired);

        // TODO common classes to register

        for (Class<?> clazz : registrations) {
            kryo.register(clazz);
        }

        return kryo;
    }

    public abstract Kryo getKryo();

    public abstract void releaseKryo(Kryo kryo);

}
