package navatrana.projecttakenote;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Vamsee on 8/26/2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MessageDialog extends android.support.v4.app.DialogFragment {

    private ContactDialog dialFrag;
    private ContactUpdateDialog updateDialog;
    String name = "";
    String number = "";
    String message = "";
    String position = "";
    public static MessageDialog newInstance(String... params) {
        Bundle args = new Bundle();
        args.putString("ACTION", params[0]);
        args.putString("NAME", params[1]);
        args.putString("NUMBER", params[2]);
        args.putString("MESSAGE", params[3]);
        args.putString("POSITION", params[4]);
        MessageDialog fragment = new MessageDialog();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String action = getArguments().getString("ACTION");
        name = getArguments().getString("NAME");
        number = getArguments().getString("NUMBER");
        message = getArguments().getString("MESSAGE");
        position = getArguments().getString("POSITION");
        builder.setTitle(name);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.contact_dialog, null);
        builder.setView(view)

                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editText = (EditText) view.findViewById(R.id.project_name);
                        message = editText.getText().toString();
                        if (action.equals("ADD")){
                            dialFrag.onDialogPositiveClick(MessageDialog.this, name, number, message);
                        }
                        else if (action.equals("UPDATE")){
                            updateDialog.onDialogPositiveClickUpdate(MessageDialog.this, name, number, message, position);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (action.equals("ADD")){
                            dialFrag.onDialogNegativeClick(MessageDialog.this);
                        }
                        else if (action.equals("UPDATE")){
                            updateDialog.onDialogNegativeClick(MessageDialog.this);
                        }

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dialFrag = (ContactDialog)context;
        updateDialog = (ContactUpdateDialog)context;
    }
}
