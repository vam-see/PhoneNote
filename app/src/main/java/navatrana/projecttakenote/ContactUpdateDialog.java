package navatrana.projecttakenote;

import android.support.v4.app.DialogFragment;

/**
 * Created by Vamsee on 8/29/2016.
 */
public interface ContactUpdateDialog {
    public void onDialogPositiveClickUpdate(DialogFragment dialog, String... params);
    public void onDialogNegativeClick(DialogFragment dialog);
}
