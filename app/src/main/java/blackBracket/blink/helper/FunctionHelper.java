package blackBracket.blink.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import blackBracket.blink.R;

/**
 * Created by anish on 12-09-2017.
 */

public class FunctionHelper {

    public static void setPermission(final Context context, @NonNull String[] permissions, PermissionListener permissionListener) {

        if (permissions.length == 0 && permissionListener != null) {
            return;
        }
        TedPermission.with(context)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(permissions)
                .check();
    }

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
