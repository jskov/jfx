package test.utils;

/**
 * Utility methods for sleeping during tests.
 */
public class SleepUtil {
    private SleepUtil() {}
    
    public static void sleep(long msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException ex) {
            System.err.println("InterruptedException occurred during Thread.sleep()");
            Thread.currentThread().interrupt();
        }
    }
}
