package club.faxhax.client.api.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Taikelenn
 **/
public final class TickRateCalculator {
    private static final double TPS_NORM = 20.0;

    private static final Object UpdateLock = new Object();
    private static final List<Long> Measurements = new ArrayList<>();

    public static void reportReceivedPacket() {
        synchronized (UpdateLock) {
            Measurements.add(System.currentTimeMillis());

            if (Measurements.size() > 15) {
                // retain data from the last 15 measurements
                Measurements.remove(0);
            }
        }
    }

    public static void reset() {
        synchronized (UpdateLock) {
            Measurements.clear();
        }
    }

    private static double getTPS(int averageOfSeconds) {
        synchronized (UpdateLock) {
            if (Measurements.size() < 2) {
                return 0.0; // we can't compare yet
            }

            long currentTimestamp = Measurements.get(Measurements.size() - 1);
            long previousTimestamp = Measurements.get(Measurements.size() - averageOfSeconds);

            // on average, how long did it take for 20 ticks to execute? (ideal value: 1 second)
            double longTickTime = Math.max((currentTimestamp - previousTimestamp) / (1000.0 * (averageOfSeconds - 1)), 1.0);
            return TPS_NORM / longTickTime;
        }
    }

    public static double getAverageTPS() {
        synchronized (UpdateLock) {
            return getTPS(Measurements.size());
        }
    }

    public static int getColorForTPS() {
        double tps = getAverageTPS();
        if (tps == 0.0)
            return 0xFFFFFF; // white - tps = 0 means we have no data yet
        else if (tps >= 17.0)
            return 0x00FF00; // green
        else if (tps >= 12.0)
            return 0xFFFF00; // yellow
        else if (tps >= 8.0)
            return 0xFF7F00; // orange
        else
            return 0xFF0000; // red
    }

    private TickRateCalculator() {
        // make the class non-instantiable
    }
}
