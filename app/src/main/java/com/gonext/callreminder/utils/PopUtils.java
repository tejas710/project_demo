package com.gonext.callreminder.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gonext.callreminder.R;
import com.gonext.callreminder.interfaces.RemindInSelection;
import com.gonext.callreminder.utils.view.CustomTextView;


/**
 * Created by Lenovo.
 */

public class PopUtils {
    static String afterCallType = "";
    /**
     * Method to show alert dialog with two/single button
     *
     * @param context               context
     * @param message               Message of dialog
     * @param posButtonName         Name of positive button
     * @param nagButtonName         Name of negative button
     * @param onPositiveButtonClick call back method of positive button
     * @param onNegativeButtonClick call back method of negative butoon
     */
    public static void showCustomTwoButtonAlertDialog(final Context context, String title, String message, String posButtonName,
                                                      String nagButtonName, boolean isOutSideCancelable,
                                                      DialogInterface.OnClickListener onPositiveButtonClick,
                                                      DialogInterface.OnClickListener onNegativeButtonClick) {
        showCustomTwoButtonAlertDialog(context, title, message, posButtonName, nagButtonName, false, isOutSideCancelable, onPositiveButtonClick, onNegativeButtonClick);

    }

    /**
     * Method to show alert dialog with single  positive  button
     *
     * @param context               context
     * @param message               Message of dialog
     * @param posButtonName         Name of positive button
     * @param onPositiveButtonClick call back method of positive button
     */
    public static void showCustomTwoButtonAlertDialog(final Context context, String title, String message, String posButtonName,
                                                      boolean isOutSideCancelable, DialogInterface.OnClickListener onPositiveButtonClick) {
        showCustomTwoButtonAlertDialog(context, title, message, posButtonName, "", false, isOutSideCancelable, onPositiveButtonClick, null);

    }

    /**
     * Method to show alert dialog with two/single button
     *
     * @param context               context
     * @param message               Message of dialog
     * @param posButtonName         Name of positive button
     * @param nagButtonName         Name of negative button
     * @param onPositiveButtonClick call back method of positive button
     * @param onNegativeButtonClick call back method of negative butoon
     */
    public static void showCustomTwoButtonAlertDialog(final Context context, String title, String message, String posButtonName,
                                                      String nagButtonName, boolean changeButtonColor, boolean isOutSideCancelable,
                                                      DialogInterface.OnClickListener onPositiveButtonClick,
                                                      DialogInterface.OnClickListener onNegativeButtonClick) {
        try {
            if (context != null) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context, R.style.DialogTheme);
                }
                builder.setMessage(message);
                if (TextUtils.isEmpty(title)) {
                    builder.setTitle(context.getResources().getString(R.string.app_name));
                } else {
                    builder.setTitle(title);
                }

                if (onPositiveButtonClick != null) {
                    builder.setPositiveButton(posButtonName, onPositiveButtonClick);
                }
                if (onNegativeButtonClick != null) {
                    builder.setNegativeButton(nagButtonName, onNegativeButtonClick);
                }

                builder.setCancelable(true);
                final AlertDialog alert = builder.create();
                alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                if (changeButtonColor) {
                    try {
                        alert.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (isOutSideCancelable) {
                    alert.setCanceledOnTouchOutside(true);
                } else {
                    alert.setCanceledOnTouchOutside(false);
                }
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to show alertDialog with items array
     *
     * @param context             context
     * @param items               Array of data which are display in dialog
     * @param title               title of the alert dialog
     * @param onItemClickListener call back method of selected item
     */
    public static void showCustomAlertDialogWithItems(Context context, CharSequence[] items,
                                                      final String title,
                                                      DialogInterface.OnClickListener onItemClickListener) {
        try {
            if (context != null) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle(title);
                builder.setItems(items, onItemClickListener);
                AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showRateAppDialog(Context context, final View.OnClickListener rateNowClickListener) {
        final Dialog dialogRateApp = new Dialog(context);
        dialogRateApp.setContentView(R.layout.dialog_rateapp);

        Window window = dialogRateApp.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        CustomTextView tvLater = (CustomTextView) dialogRateApp.findViewById(R.id.tvLater);
        CustomTextView tvNoThanx = (CustomTextView) dialogRateApp.findViewById(R.id.tvNoThanx);
        CustomTextView tvRateNow = (CustomTextView) dialogRateApp.findViewById(R.id.tvRateNow);

        tvLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRateApp.dismiss();
            }
        });

        tvNoThanx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRateApp.dismiss();
            }
        });

        tvRateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogRateApp.dismiss();
                rateNowClickListener.onClick(v);
            }
        });


        dialogRateApp.show();
    }

    public static void showDialogforPermission(Context context,String text,final View.OnClickListener allowClickListener ) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_permission);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvtextofpermission = (TextView) dialog.findViewById(R.id.tvtextofpermission);
        TextView tvskipbutton = (TextView) dialog.findViewById(R.id.tvskipbutton);
        TextView tvAllowbutton = (TextView) dialog.findViewById(R.id.tvAllowbutton);

        tvtextofpermission.setText(text);
        tvAllowbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
                allowClickListener.onClick(v);

                // finish();
            }
        });
        tvskipbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //Dialog For Show Remind Time...
    public static void showRemindInDialog(final Context context, String title, final RemindInSelection remindInSelection) {
        final Dialog dialogRemindIn = new Dialog(context);
        dialogRemindIn.setContentView(R.layout.dialog_remind_in);
        dialogRemindIn.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RadioButton rb5min = (RadioButton) dialogRemindIn.findViewById(R.id.rb5min);
        final RadioButton rb10min = (RadioButton) dialogRemindIn.findViewById(R.id.rb10min);
        final RadioButton rb15min = (RadioButton) dialogRemindIn.findViewById(R.id.rb15min);
        final RadioButton rb30min = (RadioButton) dialogRemindIn.findViewById(R.id.rb30min);
        final RadioButton rb1hour = (RadioButton) dialogRemindIn.findViewById(R.id.rb1hour);
        CustomTextView tvOk = (CustomTextView) dialogRemindIn.findViewById(R.id.tvOk);
        CustomTextView tvCancel = (CustomTextView) dialogRemindIn.findViewById(R.id.tvCancel);
        String snoozeData = title;
        if (snoozeData.equalsIgnoreCase(context.getString(R.string._5_minutes))) {
            rb5min.setChecked(true);
        } else if (snoozeData.equalsIgnoreCase(context.getString(R.string._10_minutes))) {
            rb10min.setChecked(true);
        } else if (snoozeData.equalsIgnoreCase(context.getString(R.string._15_minutes))) {
            rb15min.setChecked(true);
        } else if (snoozeData.equalsIgnoreCase(context.getString(R.string._30_minutes))) {
            rb30min.setChecked(true);
        } else if (snoozeData.equalsIgnoreCase(context.getString(R.string._1_hour))) {
            rb1hour.setChecked(true);
        }
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb5min.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string._5_minutes));
                } else if (rb10min.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string._10_minutes));
                } else if (rb15min.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string._15_minutes));
                } else if (rb30min.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string._30_minutes));
                } else if (rb1hour.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string._1_hour));
                }
                dialogRemindIn.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRemindIn.dismiss();
            }
        });
        dialogRemindIn.show();
    }

    public static void showErrorDialog(Context context, String text) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_error);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CustomTextView tvOk = (CustomTextView) dialog.findViewById(R.id.tvOk);
        CustomTextView tvMsg = (CustomTextView) dialog.findViewById(R.id.tvMsg);
        tvMsg.setText(text);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showDismissAfterCallWindow(final Context context, String text, final RemindInSelection remindInSelection) {
        final Dialog dialogDismissAfterCall = new Dialog(context);
        dialogDismissAfterCall.setContentView(R.layout.dialog_after_call_window_autodismiss);
        dialogDismissAfterCall.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RadioButton rb5sec = (RadioButton) dialogDismissAfterCall.findViewById(R.id.rb5sec);
        final RadioButton rb10sec = (RadioButton) dialogDismissAfterCall.findViewById(R.id.rb10sec);
        final RadioButton rb20sec = (RadioButton) dialogDismissAfterCall.findViewById(R.id.rb20sec);
        final RadioButton rb30sec = (RadioButton) dialogDismissAfterCall.findViewById(R.id.rb30sec);
        final RadioButton rbDonotDismiss = (RadioButton) dialogDismissAfterCall.findViewById(R.id.rbDonotDismiss);
        CustomTextView tvOk = (CustomTextView) dialogDismissAfterCall.findViewById(R.id.tvOk);
        CustomTextView tvCancel = (CustomTextView) dialogDismissAfterCall.findViewById(R.id.tvCancel);
        String snoozeData = text;
        if (snoozeData.equalsIgnoreCase(context.getString(R.string.dismiss_after_5_seconds))) {
            rb5sec.setChecked(true);
        } else if (snoozeData.equalsIgnoreCase(context.getString(R.string.dismiss_after_10_seconds))) {
            rb10sec.setChecked(true);
        } else if (snoozeData.equalsIgnoreCase(context.getString(R.string.dismiss_after_20_seconds))) {
            rb20sec.setChecked(true);
        } else if (snoozeData.equalsIgnoreCase(context.getString(R.string.dismiss_after_30_seconds))) {
            rb30sec.setChecked(true);
        } else if (snoozeData.equalsIgnoreCase(context.getString(R.string.do_not_dismiss))) {
            rbDonotDismiss.setChecked(true);
        }
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb5sec.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string.dismiss_after_5_seconds));
                } else if (rb10sec.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string.dismiss_after_10_seconds));
                } else if (rb20sec.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string.dismiss_after_20_seconds));
                } else if (rb30sec.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string.dismiss_after_30_seconds));
                } else if (rbDonotDismiss.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string.do_not_dismiss));
                }
                dialogDismissAfterCall.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDismissAfterCall.dismiss();
            }
        });
        dialogDismissAfterCall.show();
    }

    public static void showDisplayLengthCallNote(final Context context, String text, final RemindInSelection remindInSelection) {
        final Dialog dialogDisplayLength = new Dialog(context);
        dialogDisplayLength.setContentView(R.layout.dialog_callnote_length);
        dialogDisplayLength.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RadioButton rb10sec = (RadioButton) dialogDisplayLength.findViewById(R.id.rb10sec);
        final RadioButton rb20sec = (RadioButton) dialogDisplayLength.findViewById(R.id.rb20sec);
        final RadioButton rb30sec = (RadioButton) dialogDisplayLength.findViewById(R.id.rb30sec);
        final RadioButton rbCallDuration = (RadioButton) dialogDisplayLength.findViewById(R.id.rbCallDuration);
        CustomTextView tvOk = (CustomTextView) dialogDisplayLength.findViewById(R.id.tvOk);
        CustomTextView tvCancel = (CustomTextView) dialogDisplayLength.findViewById(R.id.tvCancel);
        String displayLenghtData = text;
        if (displayLenghtData.equalsIgnoreCase(context.getString(R.string.display_for_10_seconds))) {
            rb10sec.setChecked(true);
        } else if (displayLenghtData.equalsIgnoreCase(context.getString(R.string.display_for_20_seconds))) {
            rb20sec.setChecked(true);
        } else if (displayLenghtData.equalsIgnoreCase(context.getString(R.string.display_for_30_seconds))) {
            rb30sec.setChecked(true);
        } else if (displayLenghtData.equalsIgnoreCase(context.getString(R.string.duration_of_call))) {
            rbCallDuration.setChecked(true);
        }
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb10sec.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string.display_for_10_seconds));
                } else if (rb20sec.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string.display_for_20_seconds));
                } else if (rb30sec.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string.display_for_30_seconds));
                } else if (rbCallDuration.isChecked()) {
                    remindInSelection.onRemindInSelection(context.getString(R.string.duration_of_call));
                }
                dialogDisplayLength.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDisplayLength.dismiss();
            }
        });
        dialogDisplayLength.show();
    }

    public static void showDeleteDialog(Context context , String text,final View.OnClickListener yesClickListener) {
        final Dialog dialogDelete = new Dialog(context);
        dialogDelete.setContentView(R.layout.dialog_delete);
        dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView dialogTitle = (TextView) dialogDelete.findViewById(R.id.dialogTitle);
        TextView dialogText = (TextView) dialogDelete.findViewById(R.id.dialogText);
        TextView tvYes = (TextView) dialogDelete.findViewById(R.id.tvYes);
        TextView tvNO = (TextView) dialogDelete.findViewById(R.id.tvNo);
        dialogText.setText(text);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogDelete.dismiss();
                yesClickListener.onClick(v);


            }
        });
        tvNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete.dismiss();
            }
        });
        dialogDelete.show();
    }


    public static void showConfirmSms(Context context ,final View.OnClickListener uncheckedClickListener ,final View.OnClickListener cancelClicklistener) {
        final Dialog dialogConfirmSms = new Dialog(context);
        dialogConfirmSms.setContentView(R.layout.dialog_confirm_msg);
        dialogConfirmSms.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvCancel = (TextView) dialogConfirmSms.findViewById(R.id.tvCancel);
        TextView tvUnchecked = (TextView) dialogConfirmSms.findViewById(R.id.tvUnchecked);
        tvUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogConfirmSms.dismiss();
                uncheckedClickListener.onClick(v);


            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirmSms.dismiss();
                cancelClicklistener.onClick(v);
            }
        });
        dialogConfirmSms.show();
    }
    public static void showAfterCallWindowType(final Context context,String text,final RemindInSelection remindInSelection)
    {
        final Dialog dialogAfterCallWindowType = new Dialog(context);
        dialogAfterCallWindowType.setContentView(R.layout.dialog_after_call_window_type);
        dialogAfterCallWindowType.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final CheckBox cbIncoming = (CheckBox) dialogAfterCallWindowType.findViewById(R.id.cbIncoming);
        final CheckBox cbOutgoing = (CheckBox) dialogAfterCallWindowType.findViewById(R.id.cbOutgoing);
        final CheckBox cbMissed = (CheckBox) dialogAfterCallWindowType.findViewById(R.id.cbMissed);
        CustomTextView tvOk = (CustomTextView) dialogAfterCallWindowType.findViewById(R.id.tvOk);
        CustomTextView tvCancel = (CustomTextView) dialogAfterCallWindowType.findViewById(R.id.tvCancel);
        String callType = text;
        if (callType.contains(context.getString(R.string.incoming_calls))) {
            cbIncoming.setChecked(true);
        }
        if (callType.contains(context.getString(R.string.outgoing_calls))) {
            cbOutgoing.setChecked(true);
        }
        if (callType.contains(context.getString(R.string.missed_calls))) {
            cbMissed.setChecked(true);
        }
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbIncoming.isChecked()) {
                    afterCallType = context.getString(R.string.incoming_calls);
                } if (cbOutgoing.isChecked()) {
                    afterCallType = afterCallType + " " + context.getString(R.string.outgoing_calls);
                } if (cbMissed.isChecked()) {
                    afterCallType = afterCallType + " " + context.getString(R.string.missed_calls);
                }
                remindInSelection.onRemindInSelection(afterCallType);
                dialogAfterCallWindowType.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAfterCallWindowType.dismiss();
            }
        });
        dialogAfterCallWindowType.show();
    }
}
