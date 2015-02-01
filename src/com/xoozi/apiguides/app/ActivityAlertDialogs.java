package com.xoozi.apiguides.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivityAlertDialogs extends ActivityBase{

    private static final int DIALOG_YES_NO_MESSAGE = 1;
    private static final int DIALOG_YES_NO_LONG_MESSAGE = 2;
    private static final int DIALOG_LIST = 3;
    private static final int DIALOG_PROGRESS = 4;
    private static final int DIALOG_SINGLE_CHOICE = 5;
    private static final int DIALOG_MULTIPLE_CHOICE = 6;
    private static final int DIALOG_TEXT_ENTRY = 7;
    private static final int DIALOG_MULTIPLE_CHOICE_CURSOR = 8;
    private static final int DIALOG_YES_NO_ULTRA_LONG_MESSAGE = 9;
    private static final int DIALOG_YES_NO_OLD_SCHOOL_MESSAGE = 10;
    private static final int DIALOG_YES_NO_HOLO_LIGHT_MESSAGE = 11;

    private static final int MAX_PROGRESS = 100;

    private ProgressDialog  _progressDialog;
    private Handler         _progressHandler;
    private int             _progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alert_dialogs);

        _initWork();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {

        switch(id){
            case DIALOG_YES_NO_MESSAGE:
            return new AlertDialog.Builder(this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(R.string.alert_dialog_two_buttons_title)
                .setPositiveButton(R.string.alert_dialog_ok, 
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).setNegativeButton(R.string.alert_dialog_cancel,
                            new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).create();

            case DIALOG_YES_NO_LONG_MESSAGE:
            return new AlertDialog.Builder(this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(R.string.alert_dialog_two_buttons_title)
                .setMessage(R.string.alert_dialog_two_buttons2_msg)
                .setPositiveButton(R.string.alert_dialog_ok, 
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).setNegativeButton(R.string.alert_dialog_cancel,
                            new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).setNeutralButton(R.string.alert_dialog_something,
                            new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).create();

            case DIALOG_LIST:
            return new AlertDialog.Builder(this)
                .setTitle(R.string.select_dialog)
                .setItems(R.array.select_dialog_items,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0,
                                int pos) {
                            String[] array = getResources()
                                .getStringArray(R.array.select_dialog_items);
                            new AlertDialog.Builder(ActivityAlertDialogs.this)
                                .setMessage("Selected "+array[pos]+" at "+pos)
                                .show();
                        }
                }).create();

            case DIALOG_PROGRESS:
            _progressDialog = new ProgressDialog(ActivityAlertDialogs.this);
            _progressDialog.setIconAttribute(android.R.attr.alertDialogIcon);
            _progressDialog.setTitle(R.string.select_dialog);
            _progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            _progressDialog.setMax(MAX_PROGRESS);
            _progressDialog.setButton(ProgressDialog.BUTTON_POSITIVE,
                    getResources().getString(R.string.alert_dialog_hide),
                    new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
            });
            _progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE,
                    getResources().getString(R.string.alert_dialog_cancel),
                    new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
            });
            return _progressDialog;

            case DIALOG_SINGLE_CHOICE:
            return new AlertDialog.Builder(this)
                .setTitle(R.string.select_dialog)
                .setSingleChoiceItems(R.array.select_dialog_items2, 0,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0,
                                int pos) {
                            String[] array = getResources()
                                .getStringArray(R.array.select_dialog_items2);
                            new AlertDialog.Builder(ActivityAlertDialogs.this)
                                .setMessage("Choice "+array[pos]+" at "+pos)
                                .show();
                        }
                }).create();

            case DIALOG_MULTIPLE_CHOICE:
            return new AlertDialog.Builder(this)
                .setIconAttribute(R.drawable.ic_popup_reminder)
                .setTitle(R.string.alert_dialog_multi_choice)
                .setMultiChoiceItems(getResources().getStringArray(
                                    R.array.select_dialog_items3),
                        new boolean[]{false, true, false, true, false, false, false},
                        new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0,
                                    int arg1, boolean arg2) {

                            }
                })
                .setPositiveButton(R.string.alert_dialog_ok, 
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).setNegativeButton(R.string.alert_dialog_cancel,
                            new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                })
                .create();

            case DIALOG_TEXT_ENTRY:
            LayoutInflater inflater = LayoutInflater.from(this);
            View   customView = inflater.inflate(R.layout.alert_dialog_text_entry, null);
            return new AlertDialog.Builder(this)
                .setIconAttribute(R.drawable.ic_popup_reminder)
                .setTitle(R.string.alert_dialog_text_entry)
                .setView(customView)
                .setPositiveButton(R.string.alert_dialog_ok, 
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).setNegativeButton(R.string.alert_dialog_cancel,
                            new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                })
                .create();

            case DIALOG_MULTIPLE_CHOICE_CURSOR:
            String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.SEND_TO_VOICEMAIL
            };
            Cursor cursor = managedQuery(ContactsContract.Contacts.CONTENT_URI,
                    projection, null, null, null);
            return new AlertDialog.Builder(this)
                .setIconAttribute(R.drawable.ic_popup_reminder)
                .setTitle(R.string.alert_dialog_multi_choice_cursor)
                .setMultiChoiceItems(cursor,
                ContactsContract.Contacts.SEND_TO_VOICEMAIL,
                ContactsContract.Contacts.DISPLAY_NAME,
                        new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0,
                                    int arg1, boolean arg2) {

                            }
                })
                .create();

            case DIALOG_YES_NO_ULTRA_LONG_MESSAGE:
            return new AlertDialog.Builder(this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(R.string.alert_dialog_two_buttons_title)
                .setMessage(R.string.alert_dialog_two_buttons2ultra_msg)
                .setPositiveButton(R.string.alert_dialog_ok, 
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).setNegativeButton(R.string.alert_dialog_cancel,
                            new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).setNeutralButton(R.string.alert_dialog_something,
                            new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).create();

            case DIALOG_YES_NO_OLD_SCHOOL_MESSAGE:
            return new AlertDialog.Builder(this, AlertDialog.THEME_TRADITIONAL)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(R.string.alert_dialog_two_buttons_title)
                .setPositiveButton(R.string.alert_dialog_ok, 
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).setNegativeButton(R.string.alert_dialog_cancel,
                            new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).create();

            case DIALOG_YES_NO_HOLO_LIGHT_MESSAGE:
            return new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(R.string.alert_dialog_two_buttons_title)
                .setPositiveButton(R.string.alert_dialog_ok, 
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).setNegativeButton(R.string.alert_dialog_cancel,
                            new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0,
                                int arg1) {

                        }
                }).create();


        }

        return null;
    }

    @SuppressWarnings("deprecation")
    private void _initWork(){
        
        findViewById(R.id.two_buttons).setOnClickListener(
                new OnClickListener() {
                    @Override
               public void onClick(View arg0) {
                        showDialog(DIALOG_YES_NO_MESSAGE);
                    }
                });

        findViewById(R.id.two_buttons2).setOnClickListener(
                new OnClickListener() {
                    @Override
               public void onClick(View arg0) {
                        showDialog(DIALOG_YES_NO_LONG_MESSAGE);
                    }
                });
        findViewById(R.id.select_button).setOnClickListener(
                new OnClickListener() {
                    @Override
               public void onClick(View arg0) {
                        showDialog(DIALOG_LIST);
                    }
                });

        findViewById(R.id.progress_button).setOnClickListener(
                new OnClickListener() {
                    @Override
               public void onClick(View arg0) {
                        showDialog(DIALOG_PROGRESS);
                        _progress = 0;
                        _progressDialog.setProgress(0);
                        _progressHandler.sendEmptyMessage(0);
                    }
                });

        findViewById(R.id.radio_button).setOnClickListener(
                new OnClickListener() {
                    @Override
               public void onClick(View arg0) {
                        showDialog(DIALOG_SINGLE_CHOICE);
                    }
                });

        findViewById(R.id.checkbox_button).setOnClickListener(
                new OnClickListener() {
                    @Override
               public void onClick(View arg0) {
                        showDialog(DIALOG_MULTIPLE_CHOICE);
                    }
                });

        findViewById(R.id.text_entry_button).setOnClickListener(
                new OnClickListener() {
                    @Override
               public void onClick(View arg0) {
                        showDialog(DIALOG_TEXT_ENTRY);
                    }
                });

        findViewById(R.id.checkbox_button2).setOnClickListener(
                new OnClickListener() {
                    @Override
               public void onClick(View arg0) {
                        showDialog(DIALOG_MULTIPLE_CHOICE_CURSOR);
                    }
                });

        findViewById(R.id.two_buttons2ultra).setOnClickListener(
                new OnClickListener() {
                    @Override
               public void onClick(View arg0) {
                        showDialog(DIALOG_YES_NO_ULTRA_LONG_MESSAGE);
                    }
                });

        findViewById(R.id.two_buttons_old_school).setOnClickListener(
                new OnClickListener() {
                    @Override
               public void onClick(View arg0) {
                        showDialog(DIALOG_YES_NO_OLD_SCHOOL_MESSAGE);
                    }
                });

        findViewById(R.id.two_buttons_holo_light).setOnClickListener(
                new OnClickListener() {
                    @Override
               public void onClick(View arg0) {
                        showDialog(DIALOG_YES_NO_HOLO_LIGHT_MESSAGE);
                    }
                });

        _progressHandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                
                if(MAX_PROGRESS <= _progress){
                    _progressDialog.dismiss();
                }else{
                    _progress++;
                    _progressDialog.setProgress(_progress);
                    _progressHandler.sendEmptyMessageDelayed(0, 100);
                }
            }

        };
    }

}
