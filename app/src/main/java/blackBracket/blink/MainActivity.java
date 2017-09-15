package blackBracket.blink;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.gun0912.tedpermission.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

import blackBracket.blink.dialog.SettingTimeDialog;
import blackBracket.blink.helper.AppAlarmHelper;
import blackBracket.blink.helper.AppConstants;
import blackBracket.blink.helper.FunctionHelper;
import blackBracket.blink.helper.PrefsUtil;
import blackBracket.blink.widgets.CTextView;
import io.ghyeok.stickyswitch.widget.StickySwitch;

public class MainActivity extends AppCompatActivity {

    private StickySwitch stickyswitch;
    private RelativeLayout layoutRelative;
    private Context context;
    private blackBracket.blink.widgets.CTextView txtSay;
    private android.widget.ImageView imgBoy;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        init();
        initListener();
        initAds();
    }

    private void initAds() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("03BE0FD0160FB62F72A0AA8DD56866F2")
                .build();
        mAdView.loadAd(adRequest);
    }

    private void initListener() {
        stickyswitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
                int colorFrom = context.getResources().getColor(R.color.white);
                int colorTo = getResources().getColor(R.color.green);
                if (stickyswitch.getDirection() == StickySwitch.Direction.RIGHT) {
//                    changeBgColor(colorFrom, colorTo); //not todo: not required but don't delete
                    onStartBlinking();
                } else {
//                    changeBgColor(colorTo, colorFrom);//not todo: not required but don't delete
                    onStopBlinking();
                }
            }

        });
    }

    private void onStartBlinking() {
        new SettingTimeDialog(context, new SettingTimeDialog.SettingTimeListener() {
            @Override
            public void onOkClicked(int selectedInterval) {
                Toast.makeText(context, "SelectedInterval=" + selectedInterval, Toast.LENGTH_SHORT).show();
                PrefsUtil.setBlinkStatus(context, true);
                PrefsUtil.setInterval(context, selectedInterval);
                txtSay.setText(String.format(context.getString(R.string.blink_just_sit_back_and_work), String.valueOf(PrefsUtil.getBlinkInterval(context))));
                AppAlarmHelper appAlarmHelper = new AppAlarmHelper();
                appAlarmHelper.setBlinkNotification(context, AppConstants.ALARM_ID, selectedInterval);
            }

            @Override
            public void onCancelClicked() {
                stickyswitch.setDirection(StickySwitch.Direction.LEFT, true);
                PrefsUtil.setBlinkStatus(context, false);
            }
        });
        imgBoy.setBackgroundResource(R.drawable.boy_working);
        txtSay.setText(String.format(context.getString(R.string.blink_just_sit_back_and_work), String.valueOf(PrefsUtil.getBlinkInterval(context))));
    }

    private void onStopBlinking() {
        imgBoy.setBackgroundResource(R.drawable.boy_relax);
        txtSay.setText(context.getString(R.string.relax_txt));
        PrefsUtil.setBlinkStatus(context, false);
        AppAlarmHelper appAlarmHelper = new AppAlarmHelper();
        appAlarmHelper.cancelAlarm(context, AppConstants.ALARM_ID);
    }

    private void init() {
        setContentView(R.layout.activity_main);
        imgBoy = (ImageView) findViewById(R.id.imgBoy);
        txtSay = (CTextView) findViewById(R.id.txtSay);
        mAdView = (AdView) findViewById(R.id.adView);
        layoutRelative = (RelativeLayout) findViewById(R.id.layoutRelative);
        stickyswitch = (StickySwitch) findViewById(R.id.stickySwitch);


        if (PrefsUtil.getBlinkStatus(context)) {
            stickyswitch.setDirection(StickySwitch.Direction.RIGHT, false);
            txtSay.setText(String.format(context.getString(R.string.blink_just_sit_back_and_work), String.valueOf(PrefsUtil.getBlinkInterval(context))));
        } else {
            stickyswitch.setDirection(StickySwitch.Direction.LEFT, false);
            imgBoy.setBackgroundResource(R.drawable.boy_relax);
            txtSay.setText(context.getString(R.string.relax_txt));
        }

        FunctionHelper.setPermission(context,
                new String[]{Manifest.permission.VIBRATE, Manifest.permission.INTERNET, Manifest.permission.WAKE_LOCK, Manifest.permission.RECEIVE_BOOT_COMPLETED}
                , new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        //nothing now
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        //nothing now
                    }
                });

    }


    private void changeBgColor(int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                layoutRelative.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
