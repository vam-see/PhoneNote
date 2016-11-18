package navatrana.projecttakenote;

import android.support.v4.app.DialogFragment;

/**
 * Created by Vamsee on 8/26/2016.
 */
public interface ContactDialog {

    public void onDialogPositiveClick(DialogFragment dialog, String... params);
    public void onDialogNegativeClick(DialogFragment dialog);
}
