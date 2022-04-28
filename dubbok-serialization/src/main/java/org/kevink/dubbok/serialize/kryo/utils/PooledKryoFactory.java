package org.kevink.dubbok.serialize.kryo.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoPool;

import java.lang.ref.SoftReference;

public class PooledKryoFactory extends AbstractKryoFactory {

    /**
     * Use {@link SoftReference}s for pooled {@link Kryo} instances.
     */
    private final KryoPool kryoPool =
            new KryoPool.Builder(this).softReferences().build();

    @Override
    public Kryo getKryo() {
        return kryoPool.borrow();
    }

    @Override
    public void releaseKryo(Kryo kryo) {
        kryoPool.release(kryo);
    }

}
