package test.utils;

import static org.junit.Assert.assertNull;

import java.lang.ref.WeakReference;
import java.util.function.BooleanSupplier;

/**
 * Utility methods for executing garbage collection during tests.
 */
public class GcTester {
    private static final int DEFAULT_GC_ATTEMPT_COUNT = 10;
    private static final int DEFAULT_SLEEP_MS = 100;
    
    private GcTester() {}

    /**
     * Asserts that a weak references is collected by garbage collection.
     * 
     * Uses default values for sleep and retry count.
     *
     * @param message for failed assertion.
     * @param weakReference reference to assert on.
     */
    public static void assertCollectable(String message, WeakReference<?> weakReference) {
        assertCollectable(message, weakReference, DEFAULT_GC_ATTEMPT_COUNT, DEFAULT_SLEEP_MS);
    }

    /**
     * Attempt to get a weak reference collected by garbage collection.
     * 
     * Uses default values for sleep and retry count.
     * 
     * @param weakReference reference to test for collection.
     */
    public static void attemptGC(WeakReference<?> weakReference) {
        attemptGC(weakReference, DEFAULT_GC_ATTEMPT_COUNT, DEFAULT_SLEEP_MS);
    }

    /**
     * Attempt to get some reaction from garbage collection.
     * 
     * Uses default values for sleep and retry count.
     * 
     * @param isCollected should return true if expected action happens, otherwise false.
     */
    public static void attemptGC(BooleanSupplier isCollected) {
        attemptGC(isCollected, DEFAULT_GC_ATTEMPT_COUNT, DEFAULT_SLEEP_MS);
    }
    
    
    public static void assertCollectable(WeakReference<?> weakReference) {
        assertCollectable(null, weakReference, DEFAULT_GC_ATTEMPT_COUNT, DEFAULT_SLEEP_MS);
    }

    public static void assertCollectable(String message, WeakReference<?> weakReference, int attemptCount) {
        assertCollectable(message, weakReference, attemptCount, DEFAULT_SLEEP_MS);
    }
    
    public static void assertCollectable(String message, WeakReference<?> weakReference, int attemptCount, int sleepMs) {
        attemptGC(weakReference, attemptCount, sleepMs);
        assertNull(message, weakReference.get());
    }

    public static void attemptGC(WeakReference<?> weakReference, int attemptCount) {
        attemptGC(weakReference, attemptCount, DEFAULT_SLEEP_MS);
    }

    public static void attemptGC(WeakReference<?> weakReference, int attemptCount, int sleepMs) {
        attemptGC(() -> weakReference.get() == null, attemptCount, sleepMs);
    }

    public static void attemptGC(BooleanSupplier isCollected, int attemptCount) {
        attemptGC(isCollected, attemptCount, DEFAULT_SLEEP_MS);
    }

    public static void attemptGC(BooleanSupplier isCollected, int attemptCount, int sleepMs) {
        int counter = 0;

        System.gc();
        System.runFinalization();

        while (!isCollected.getAsBoolean() && counter < attemptCount) {
            SleepUtil.sleep(sleepMs);
            counter = counter + 1;
            System.gc();
            System.runFinalization();
        }
    }
}
