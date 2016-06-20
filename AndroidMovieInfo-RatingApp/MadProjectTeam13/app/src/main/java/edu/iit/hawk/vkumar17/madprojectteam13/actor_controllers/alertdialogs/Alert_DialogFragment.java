package edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.alertdialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import edu.iit.hawk.vkumar17.madprojectteam13.R;

public class Alert_DialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context)

                .setTitle(context.getString(R.string.error_title))
                .setMessage(context.getString(R.string.error_message))

                .setNegativeButton("EXIT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                getActivity().finish();
                            }
                        });

        return builder.create();
    }

}