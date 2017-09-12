package blackBracket.blink.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import blackBracket.blink.R;
import blackBracket.blink.helper.FunctionHelper;
import blackBracket.blink.helper.PrefsUtil;

/**
 * Created by anish on 12-09-2017.
 */

public class SettingTimeDialog extends Dialog {
    private Context context;
    private android.widget.ImageView imgSub;
    private TextView txtInterval;
    private android.widget.ImageView imgAdd;
    private TextView txtInform;
    private TextView txtCancel;
    private TextView txtOK;
    private int intervals[] = {5, 10, 15, 20};
    private SettingTimeListener settingTimeListener;

    public SettingTimeDialog(@NonNull Context context, SettingTimeListener settingTimeListener) {
        super(context);
        this.context = context;
        this.settingTimeListener = settingTimeListener;
        init();
        initListeners();
        show();
    }

    private void initListeners() {
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedInterval = txtInterval.getText().toString().trim();
                settingTimeListener.onOkClicked(Integer.parseInt(selectedInterval));
                dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingTimeListener.onCancelClicked();
                dismiss();
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int prevInterval = Integer.parseInt(txtInterval.getText().toString().trim());
                int newInterval = 0;
                if (prevInterval == intervals[3]) {
                    FunctionHelper.showAlertDialogWithOneOpt(context, context.getString(R.string.max_interval_prompt), new FunctionHelper.DialogOptionsSelectedListener() {
                        @Override
                        public void onSelect(boolean isYes) {

                        }
                    }, context.getString(R.string.ok));
                } else {
                    for (int i = 0; i < intervals.length; i++) {
                        if (prevInterval == intervals[i]) {
                            newInterval = intervals[i + 1];
                            break;
                        }
                    }
                    txtInterval.setText(String.valueOf(newInterval));
                    txtInform.setText(String.format(context.getString(R.string.we_will_notify_you_to_blink)
                            , String.valueOf(newInterval)));
                }
            }
        });

        imgSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int prevInterval = Integer.parseInt(txtInterval.getText().toString().trim());
                int newInterval = 0;
                if (prevInterval == intervals[0]) {
                    FunctionHelper.showAlertDialogWithOneOpt(context, context.getString(R.string.min_interval_prompt), new FunctionHelper.DialogOptionsSelectedListener() {
                        @Override
                        public void onSelect(boolean isYes) {
                            // return;
                        }
                    }, context.getString(R.string.ok));
                } else {
                    for (int i = intervals.length - 1; i >= 0; i--) {
                        if (prevInterval == intervals[i]) {
                            newInterval = intervals[i - 1];
                            break;
                        }
                    }
                    txtInterval.setText(String.valueOf(newInterval));
                    txtInform.setText(String.format(context.getString(R.string.we_will_notify_you_to_blink)
                            , String.valueOf(newInterval)));
                }
            }
        });
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_setting_time, null, false);
        this.txtOK = (TextView) view.findViewById(R.id.txtOK);
        this.txtCancel = (TextView) view.findViewById(R.id.txtCancel);
        this.txtInform = (TextView) view.findViewById(R.id.txtInform);
        this.imgAdd = (ImageView) view.findViewById(R.id.imgAdd);
        this.txtInterval = (TextView) view.findViewById(R.id.txtInterval);
        this.imgSub = (ImageView) view.findViewById(R.id.imgSub);
        setDialogProps(view);

        txtInterval.setText(String.valueOf(PrefsUtil.getBlinkInterval(context)));
        txtInform.setText(String.format(context.getString(R.string.we_will_notify_you_to_blink)
                , String.valueOf(PrefsUtil.getBlinkInterval(context))));
    }

    private void setDialogProps(View view) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        this.setCanceledOnTouchOutside(false);
    }

    public interface SettingTimeListener {
        void onOkClicked(int selectedInterval);

        void onCancelClicked();
    }
}

