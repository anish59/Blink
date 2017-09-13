package blackBracket.blink.helper;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import blackBracket.blink.MainActivity;
import blackBracket.blink.R;

/**
 * Created by anish on 12-09-2017.
 */

public class AppAlarmHelper extends BroadcastReceiver {
    private static final String ACTION_1 = "Shut Off";
    private static AlarmManager alarmManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context, intent);
    }

    private void showNotification(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(/*Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | */Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);


        int alarmId = intent.getIntExtra(IntentConstants.INTENT_ALARM_ID, 0);
        Intent action1Intent = new Intent(context, NotificationActionService.class)
                .setAction(ACTION_1);
        action1Intent.putExtra(IntentConstants.INTENT_ALARM_ID, alarmId);
        PendingIntent action1PendingIntent = PendingIntent.getService(context, 0, action1Intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Blink!, its blink time..!!  *_* ")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(notificationPendingIntent)
                .addAction(0, "Action 1", action1PendingIntent);
    }


    public void setBlinkNotification(Context context, int alarmID, int interval) {
        Calendar calendar = Calendar.getInstance();
        cancelAlarm(context, alarmID);

        Intent intent = new Intent(context, AppAlarmHelper.class);
        intent.putExtra(IntentConstants.INTENT_ALARM_ID, alarmID);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmMgr = getAlarmManager(context);
        /*
        long millis = minutes * 60 * 1000;
        * */
        switch (interval) {
            case 5:
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), TimeUnit.MINUTES.toMillis(5), alarmIntent);
                break;
            case 10:
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), TimeUnit.MINUTES.toMillis(10), alarmIntent);
                break;
            case 15:
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), TimeUnit.MINUTES.toMillis(15), alarmIntent);
                break;
            case 20:
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), TimeUnit.MINUTES.toMillis(20), alarmIntent);
                break;

        }

        initBootAlarm(context);
    }

    public void cancelAlarm(Context context, int requestCode) {

        Intent intent = new Intent(context, AppAlarmHelper.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        AlarmManager alarmManager = getAlarmManager(context);
        alarmManager.cancel(pendingIntent);

        initBootAlarm(context);
    }

    private void initBootAlarm(Context context) {
        try {
            ComponentName receiver = new ComponentName(context, AppAlarmHelper.class);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AlarmManager getAlarmManager(Context context) {

        if (this.alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return alarmManager;
    }


    public static class NotificationActionService extends IntentService {

        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String action = intent.getAction();
            System.out.println("Received notification action: " + action);
            if (ACTION_1.equals(action)) {
                PrefsUtil.setBlinkStatus(getApplicationContext(), false);
                AppAlarmHelper appAlarmHelper = new AppAlarmHelper();
                int cancelId = intent.getIntExtra(IntentConstants.INTENT_ALARM_ID, 0);
                if (cancelId != 0) {
                    appAlarmHelper.cancelAlarm(getApplicationContext(), cancelId);
                    Toast.makeText(this, "Blinking stop..!", Toast.LENGTH_SHORT).show();
                }
                // TODO: handle action 1.
                // If you want to cancel the notification: NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
            }
        }
    }
}
