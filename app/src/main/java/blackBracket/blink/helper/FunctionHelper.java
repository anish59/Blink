package blackBracket.blink.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by anish on 12-09-2017.
 */

public class FunctionHelper {

    public static void showAlertDialogWithOneOpt(Context mContext, String message, final DialogOptionsSelectedListener dialogOptionsSelectedListener, String yesOption) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(yesOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (dialogOptionsSelectedListener != null)
                            dialogOptionsSelectedListener.onSelect(true);
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alert = builder.create();

        alert.show();
    }

    public interface DialogOptionsSelectedListener {
        void onSelect(boolean isYes);
    }
}
