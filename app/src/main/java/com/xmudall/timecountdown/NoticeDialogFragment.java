package com.xmudall.timecountdown;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

/**
 * Created by udall on 2017/8/26.
 */

public class NoticeDialogFragment extends DialogFragment {

    public interface NoticeDialogListener {
        void onClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mPosListener, mNegListener;

    private int messageResourceId;
    private View customView;

    public NoticeDialogFragment withMessageResourceId(int messageResourceId) {
        this.messageResourceId = messageResourceId;
        return this;
    }

    public NoticeDialogFragment withPositiveListener(NoticeDialogListener listener) {
        this.mPosListener = listener;
        return this;
    }

    public NoticeDialogFragment withNegativeListener(NoticeDialogListener listener) {
        this.mNegListener = listener;
        return this;
    }

    public NoticeDialogFragment withCustomView(View customView) {
        this.customView = customView;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (customView != null) {
            builder.setView(customView);
        }
        if (messageResourceId != 0) {
            builder.setMessage(messageResourceId);
        }
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
                if (mPosListener != null) {
                    mPosListener.onClick(NoticeDialogFragment.this);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the negative button event back to the host activity
                if (mNegListener != null) {
                    mNegListener.onClick(NoticeDialogFragment.this);
                }
            }
        });
        return builder.create();
    }

}

