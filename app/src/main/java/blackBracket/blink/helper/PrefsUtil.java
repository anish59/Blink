package blackBracket.blink.helper;

import android.content.Context;

/**
 * Created by Anish on 18/5/16.
 */
public class PrefsUtil {


    private static String blinkStatus = "blinkStatus";
    private static String interval = "interval";


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
}
