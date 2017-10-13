package blackBracket.blink.helper;

import android.content.Context;

/**
 * Created by Anish on 18/5/16.
 */
public class PrefsUtil {


    private static String blinkStatus = "blinkStatus";
    private static String interval = "interval";
    private static String MainScreenVisible = "MainScreenVisible";


    public static void setBlinkStatus(Context context, Boolean isOn) {
        Prefs.with(context).save(blinkStatus, isOn);
    }

    public static boolean getBlinkStatus(Context context) {
        return Prefs.with(context).getBoolean(blinkStatus, false);
    }

    public static int getBlinkInterval(Context context) {
        return Prefs.with(context).getInt(interval, 10);
    }

    public static void setInterval(Context context, int blinkInterval) {
        Prefs.with(context).save(interval, blinkInterval);
    }


    public static void setActivityStatus(Context context, Boolean isVisibile) {
        Prefs.with(context).save(MainScreenVisible, isVisibile);
    }

    public static boolean getActivityVisibility(Context context) {
        return Prefs.with(context).getBoolean(MainScreenVisible, false);
    }
}
