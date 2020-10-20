package com.davidqf.gachabot;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reference {

    public final static String TOKEN = getStringSetting("token");
    public final static String COMMAND = "?";
    public final static long OWNER_ID = getLongSetting("owner_id");
    public final static String ATTACK_CODEPOINTS = "U+1f3f9";
    public final static List<String> SWAP_CODEPOINTS = new ArrayList<>(Arrays.asList("U+31U+20e3", "U+32U+20e3", "U+33U+20e3", "U+34U+20e3", "U+35U+20e3"));
    public final static String WAIT_CODEPOINTS = "U+1f634";
    public final static String ACCEPT_CODEPOINTS = "U+2705";
    public final static String DECLINE_CODEPOINTS = "U+274C";

    private static String getStringSetting(String tag) {
        try {
            BufferedReader br = Files.newBufferedReader(Paths.get("settings.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(tag + "=")) {
                    return line.substring(tag.length() + 1);
                }
            }
            System.out.println(tag + " tag could not be found in settings.txt");
        } catch (IOException exception) {
            System.out.println("settings.txt could not be found");
        }
        System.exit(0);
        return null;
    }

    private static long getLongSetting(String tag) {
        String val = getStringSetting(tag);
        try {
            return Long.parseLong(val);
        } catch (NumberFormatException exception) {
            System.out.println(tag + " tag in settings.txt is not valid");
        }
        System.exit(0);
        return 0;
    }
}
