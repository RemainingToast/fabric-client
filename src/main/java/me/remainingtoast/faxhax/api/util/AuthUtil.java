package me.remainingtoast.faxhax.api.util;

import me.remainingtoast.faxhax.FaxHax;
import oshi.SystemInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AuthUtil {

    private static final List<String> LICENSES = new ArrayList<>();

    public static void initializeAuth(){
        try {
            String URL = "https://pastebin.com/raw/sErXi000";
            java.net.URL pastebin = new URL(URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(pastebin.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                LICENSES.add(inputLine);
            }
        } catch (Exception e) {
            FaxHax.LOGGER.fatal("Failed to initialize AuthUtil!");
        }
    }

    public static String getHardwareUUID() {
        return new SystemInfo().getHardware().getComputerSystem().getHardwareUUID();
    }

    public static boolean isLicensedHardwareUUID(String hardwareUUID) {
        return LICENSES.contains(hardwareUUID);
    }

    public static boolean isLicensed() {
        return isLicensedHardwareUUID(getHardwareUUID());
    }
}
