package blackBracket.blink.helper;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
        String action;
        action = intent.getAction();
        if (!PrefsUtil.getBlinkStatus(context)) {
            return; //means if blink status is false then it will show no notification.
        }
        if (action != null && intent.getBooleanExtra(IntentConstants.INTENT_IS_ACTION, false)) {
            if (intent.getBooleanExtra(IntentConstants.INTENT_ACTION_IS_SHUT_OFF, false)) {
                cancelAlarm(context, AppConstants.ALARM_ID);
                PrefsUtil.setBlinkStatus(context, false);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(AppConstants.NotificationBuilderId);
                if (PrefsUtil.getActivityVisibility(context)) {
                    Intent refreshingAppIntent = new Intent(context, MainActivity.class);
                    refreshingAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(refreshingAppIntent);
                }
            }
        } else {
            showNotification(context);
        }
    }

    private void showNotification(Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.blink_its_time_to_blink))
                .setContentText(context.getString(R.string.blink))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        noBuilder.setDefaults(Notification.DEFAULT_ALL);
//        noBuilder.setDefaults(Notification.DEFAULT_SOUND);//to make sound
        noBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000});//to vibrate
        noBuilder.setLights(Color.WHITE, 1000, 500);
//        noBuilder.setPriority(Notification.P);

        //Maybe intent
        Intent shutOff = new Intent(context, AppAlarmHelper.class);
        shutOff.putExtra(IntentConstants.INTENT_IS_ACTION, true);
        shutOff.putExtra(IntentConstants.INTENT_ACTION_IS_SHUT_OFF, true);
        shutOff.setAction(AppConstants.ACTION_SHUT_OFF);
        PendingIntent pendingIntentShutOff = PendingIntent.getBroadcast(context, AppConstants.NotificationActionID, shutOff, PendingIntent.FLAG_UPDATE_CURRENT);
        noBuilder.addAction(0, "Shut off", pendingIntentShutOff);


        int m = AppConstants.NotificationBuilderId;//for keeping same notification,not adding any else on its top
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(m, noBuilder.build());
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
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP
                        , calendar.getTimeInMillis() + TimeUnit.MINUTES.toMillis(5)
                        , TimeUnit.MINUTES.toMillis(5), alarmIntent);
                break;
            case 10:
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP
                        , calendar.getTimeInMillis() + TimeUnit.MINUTES.toMillis(10)
                        , TimeUnit.MINUTES.toMillis(10), alarmIntent);
                break;
            case 15:
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP
                        , calendar.getTimeInMillis() + TimeUnit.MINUTES.toMillis(15)
                        , TimeUnit.MINUTES.toMillis(15), alarmIntent);
                break;
            case 20:
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP
                        , calendar.getTimeInMillis() + TimeUnit.MINUTES.toMillis(20)
                        , TimeUnit.MINUTES.toMillis(20), alarmIntent);
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
