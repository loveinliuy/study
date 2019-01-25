package shibo.study.serialization.kryo.factory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangshibo
 */
public abstract class AbstractKryoFactory implements KryoFactory {

    private final Set<Class<?>> registrations = new LinkedHashSet<>();

    private boolean registrationRequired;

    private volatile boolean kryoCreated;

    public void registerClass(Class<?> clazz) {
        if (kryoCreated) {
            throw new IllegalStateException("Can not register class after created kryo intance");
        }
        registrations.add(clazz);
    }

    public void setRegistrationRequired(boolean registrationRequired) {
        this.registrationRequired = registrationRequired;
    }

    public Kryo create() {
        if (!kryoCreated) {
            kryoCreated = true;
        }
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(registrationRequired);
        registerCommonClass(kryo);
        for (Class<?> clazz : registrations) {
            kryo.register(clazz);
        }
        return kryo;
    }

    private static void registerCommonClass(Kryo kryo) {
        kryo.register(HashMap.class);
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.register(HashSet.class);
        kryo.register(TreeSet.class);
        kryo.register(Hashtable.class);
        kryo.register(Date.class);
        kryo.register(Calendar.class);
        kryo.register(ConcurrentHashMap.class);
        kryo.register(SimpleDateFormat.class);
        kryo.register(GregorianCalendar.class);
        kryo.register(Vector.class);
        kryo.register(BitSet.class);
        kryo.register(StringBuffer.class);
        kryo.register(StringBuilder.class);
        kryo.register(Object.class);
        kryo.register(Object[].class);
        kryo.register(String[].class);
        kryo.register(byte[].class);
        kryo.register(char[].class);
        kryo.register(int[].class);
        kryo.register(float[].class);
        kryo.register(double[].class);
    }

    public abstract Kryo getKryo();

    public abstract void returnKryo(Kryo kryo);
}
