package com.isaacparker.dozesettingseditor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    // Key names stored in the settings value.
    private static final String KEY_INACTIVE_TIMEOUT = "inactive_to";
    private static final String KEY_SENSING_TIMEOUT = "sensing_to";
    private static final String KEY_LOCATING_TIMEOUT = "locating_to";
    private static final String KEY_LOCATION_ACCURACY = "location_accuracy";
    private static final String KEY_MOTION_INACTIVE_TIMEOUT = "motion_inactive_to";
    private static final String KEY_IDLE_AFTER_INACTIVE_TIMEOUT = "idle_after_inactive_to";
    private static final String KEY_IDLE_PENDING_TIMEOUT = "idle_pending_to";
    private static final String KEY_MAX_IDLE_PENDING_TIMEOUT = "max_idle_pending_to";
    private static final String KEY_IDLE_PENDING_FACTOR = "idle_pending_factor";
    private static final String KEY_IDLE_TIMEOUT = "idle_to";
    private static final String KEY_MAX_IDLE_TIMEOUT = "max_idle_to";
    private static final String KEY_IDLE_FACTOR = "idle_factor";
    private static final String KEY_MIN_TIME_TO_ALARM = "min_time_to_alarm";
    private static final String KEY_MAX_TEMP_APP_WHITELIST_DURATION =
            "max_temp_app_whitelist_duration";
    private static final String KEY_MMS_TEMP_APP_WHITELIST_DURATION =
            "mms_temp_app_whitelist_duration";
    private static final String KEY_SMS_TEMP_APP_WHITELIST_DURATION =
            "sms_temp_app_whitelist_duration";

    final long INACTIVE_TIMEOUT = 30 * 60 * 1000L;
    final long SENSING_TIMEOUT = 4 * 60 * 1000L;
    final long LOCATING_TIMEOUT = 30 * 1000L;
    final float LOCATION_ACCURACY = 20;
    final long MOTION_INACTIVE_TIMEOUT = 10 * 60 * 1000L;
    final long IDLE_AFTER_INACTIVE_TIMEOUT = 30 * 60 * 1000L;
    final long IDLE_PENDING_TIMEOUT = 5 * 60 * 1000L;
    final long MAX_IDLE_PENDING_TIMEOUT = 10 * 60 * 1000L;
    final float IDLE_PENDING_FACTOR = 2;
    final long IDLE_TIMEOUT = 60 * 60 * 1000L;
    final long MAX_IDLE_TIMEOUT = 6 * 60 * 60 * 1000L;
    final long IDLE_FACTOR = 2;
    final long MIN_TIME_TO_ALARM = 60 * 60 * 1000L;
    final long MAX_TEMP_APP_WHITELIST_DURATION = 5 * 60 * 1000L;
    final long MMS_TEMP_APP_WHITELIST_DURATION = 60 * 1000L;
    final long SMS_TEMP_APP_WHITELIST_DURATION = 20 * 1000L;

    private static final String DESC_INACTIVE_TIMEOUT = "This is the time, after becoming inactive, at which we start looking at the motion sensor to determine if the device is being left alone. We don't do this immediately after going inactive just because we don't want to be continually running the significant motion sensor whenever the screen is off.";
    private static final String DESC_SENSING_TIMEOUT = "If we don't receive a callback from AnyMotion in this amount of time + locating_to, we will change from STATE_SENSING to STATE_INACTIVE, and any AnyMotion callbacks while not in STATE_SENSING will be ignored.";
    private static final String DESC_LOCATING_TIMEOUT = "This is how long we will wait to try to get a good location fix before going in to idle mode.";
    private static final String DESC_LOCATION_ACCURACY = "The desired maximum accuracy (in meters) we consider the location to be good enough to go on to idle. We will be trying to get an accuracy fix at least this good or until locating_to expires.";
    private static final String DESC_MOTION_INACTIVE_TIMEOUT = "This is the time, after seeing motion, that we wait after becoming inactive from that until we start looking for motion again.";
    private static final String DESC_IDLE_AFTER_INACTIVE_TIMEOUT = "This is the time, after the inactive timeout elapses, that we will wait looking for significant motion until we truly consider the device to be idle.";
    private static final String DESC_IDLE_PENDING_TIMEOUT = "This is the initial time, after being idle, that we will allow ourself to be back in the IDLE_PENDING state allowing the system to run normally until we return to idle.";
    private static final String DESC_MAX_IDLE_PENDING_TIMEOUT = "Maximum pending idle timeout (time spent running) we will be allowed to use.";
    private static final String DESC_IDLE_PENDING_FACTOR = "Scaling factor to apply to current pending idle timeout each time we cycle through that state.";
    private static final String DESC_IDLE_TIMEOUT = "This is the initial time that we want to sit in the idle state before waking up again to return to pending idle and allowing normal work to run.";
    private static final String DESC_MAX_IDLE_TIMEOUT = "Maximum idle duration we will be allowed to use.";
    private static final String DESC_IDLE_FACTOR = "Scaling factor to apply to current idle timeout each time we cycle through that state.";
    private static final String DESC_MIN_TIME_TO_ALARM = "This is the minimum time we will allow until the next upcoming alarm for us to actually go in to idle mode.";
    private static final String DESC_MAX_TEMP_APP_WHITELIST_DURATION = "Max amount of time to temporarily whitelist an app when it receives a high tickle.";
    private static final String DESC_MMS_TEMP_APP_WHITELIST_DURATION = "Amount of time we would like to whitelist an app that is receiving an MMS.";
    private static final String DESC_SMS_TEMP_APP_WHITELIST_DURATION = "Amount of time we would like to whitelist an app that is receiving an SMS.";

    EditText et_inactive_to;
    EditText et_sensing_to;
    EditText et_locating_to;
    EditText et_location_accuracy;
    EditText et_motion_inactive_to;
    EditText et_idle_after_inactive_to;
    EditText et_idle_pending_to;
    EditText et_max_idle_pending_to;
    EditText et_idle_pending_factor;
    EditText et_idle_to;
    EditText et_max_idle_to;
    EditText et_idle_factor;
    EditText et_min_time_to_alarm;
    EditText et_max_temp_app_whitelist_duration;
    EditText et_mms_temp_app_whitelist_duration;
    EditText et_sms_temp_app_whitelist_duration;

    ImageView iv_inactive_to;
    ImageView iv_sensing_to;
    ImageView iv_locating_to;
    ImageView iv_location_accuracy;
    ImageView iv_motion_inactive_to;
    ImageView iv_idle_after_inactive_to;
    ImageView iv_idle_pending_to;
    ImageView iv_max_idle_pending_to;
    ImageView iv_idle_pending_factor;
    ImageView iv_idle_to;
    ImageView iv_max_idle_to;
    ImageView iv_idle_factor;
    ImageView iv_min_time_to_alarm;
    ImageView iv_max_temp_app_whitelist_duration;
    ImageView iv_mms_temp_app_whitelist_duration;
    ImageView iv_sms_temp_app_whitelist_duration;

    boolean hasRoot = false;
    SharedPreferences sharedPref;
    int displayValueIn;
    int millisecondsInOneSecond = 1000;
    int millisecondsInOneMinute = 60 * millisecondsInOneSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        et_inactive_to = (EditText) findViewById(R.id.et_inactive_to);
        et_sensing_to = (EditText) findViewById(R.id.et_sensing_to);
        et_locating_to = (EditText) findViewById(R.id.et_locating_to);
        et_location_accuracy = (EditText) findViewById(R.id.et_location_accurary);
        et_motion_inactive_to = (EditText) findViewById(R.id.et_motion_inactive_to);
        et_idle_after_inactive_to = (EditText) findViewById(R.id.et_idle_after_inactive_to);
        et_idle_pending_to = (EditText) findViewById(R.id.et_idle_pending_to);
        et_max_idle_pending_to = (EditText) findViewById(R.id.et_max_idle_pending_to);
        et_idle_pending_factor = (EditText) findViewById(R.id.et_idle_pending_factor);
        et_idle_to = (EditText) findViewById(R.id.et_idle_to);
        et_max_idle_to = (EditText) findViewById(R.id.et_max_idle_to);
        et_idle_factor = (EditText) findViewById(R.id.et_idle_factor);
        et_min_time_to_alarm = (EditText) findViewById(R.id.et_min_time_to_alarm);
        et_max_temp_app_whitelist_duration = (EditText) findViewById(R.id.et_max_temp_app_whitelist_duration);
        et_mms_temp_app_whitelist_duration = (EditText) findViewById(R.id.et_mms_temp_app_whitelist_duration);
        et_sms_temp_app_whitelist_duration = (EditText) findViewById(R.id.et_sms_temp_app_whitelist_duration);

        iv_inactive_to = (ImageView) findViewById(R.id.iv_inactive_to);
        iv_sensing_to = (ImageView) findViewById(R.id.iv_sensing_to);
        iv_locating_to = (ImageView) findViewById(R.id.iv_locating_to);
        iv_location_accuracy = (ImageView) findViewById(R.id.iv_location_accurary);
        iv_motion_inactive_to = (ImageView) findViewById(R.id.iv_motion_inactive_to);
        iv_idle_after_inactive_to = (ImageView) findViewById(R.id.iv_idle_after_inactive_to);
        iv_idle_pending_to = (ImageView) findViewById(R.id.iv_idle_pending_to);
        iv_max_idle_pending_to = (ImageView) findViewById(R.id.iv_max_idle_pending_to);
        iv_idle_pending_factor = (ImageView) findViewById(R.id.iv_idle_pending_factor);
        iv_idle_to = (ImageView) findViewById(R.id.iv_idle_to);
        iv_max_idle_to = (ImageView) findViewById(R.id.iv_max_idle_to);
        iv_idle_factor = (ImageView) findViewById(R.id.iv_idle_factor);
        iv_min_time_to_alarm = (ImageView) findViewById(R.id.iv_min_time_to_alarm);
        iv_max_temp_app_whitelist_duration = (ImageView) findViewById(R.id.iv_max_temp_app_whitelist_duration);
        iv_mms_temp_app_whitelist_duration = (ImageView) findViewById(R.id.iv_mms_temp_app_whitelist_duration);
        iv_sms_temp_app_whitelist_duration = (ImageView) findViewById(R.id.iv_sms_temp_app_whitelist_duration);

        setInfoOnClick();


        if (RootShell.isAccessGiven()) {
            hasRoot = true;
            //getSettings();
        }else{
            hasRoot = false;
            //Toast.makeText(this, "Root access required!", Toast.LENGTH_SHORT).show();
            //finish();
        }
        getSettings();
    }

    private void setInfoOnClick() {
        iv_inactive_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_INACTIVE_TIMEOUT);
                builder.setMessage(DESC_INACTIVE_TIMEOUT + "\n\nDefault: " + String.valueOf(INACTIVE_TIMEOUT / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_sensing_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_SENSING_TIMEOUT);
                builder.setMessage(DESC_SENSING_TIMEOUT + "\n\nDefault: " + String.valueOf(SENSING_TIMEOUT / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_locating_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_LOCATING_TIMEOUT);
                builder.setMessage(DESC_LOCATING_TIMEOUT + "\n\nDefault: " + String.valueOf(LOCATING_TIMEOUT / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_location_accuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_LOCATION_ACCURACY);
                builder.setMessage(DESC_LOCATION_ACCURACY + "\n\nDefault: " + String.valueOf(LOCATION_ACCURACY + " meters"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_motion_inactive_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_MOTION_INACTIVE_TIMEOUT);
                builder.setMessage(DESC_MOTION_INACTIVE_TIMEOUT + "\n\nDefault: " + String.valueOf(MOTION_INACTIVE_TIMEOUT / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_idle_after_inactive_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_IDLE_AFTER_INACTIVE_TIMEOUT);
                builder.setMessage(DESC_IDLE_AFTER_INACTIVE_TIMEOUT + "\n\nDefault: " + String.valueOf(IDLE_AFTER_INACTIVE_TIMEOUT / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_idle_pending_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_IDLE_PENDING_TIMEOUT);
                builder.setMessage(DESC_IDLE_PENDING_TIMEOUT + "\n\nDefault: " + String.valueOf(IDLE_PENDING_TIMEOUT / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_max_idle_pending_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_MAX_IDLE_PENDING_TIMEOUT);
                builder.setMessage(DESC_MAX_IDLE_PENDING_TIMEOUT + "\n\nDefault: " + String.valueOf(MAX_IDLE_PENDING_TIMEOUT / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_idle_pending_factor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_IDLE_PENDING_FACTOR);
                builder.setMessage(DESC_IDLE_PENDING_FACTOR + "\n\nDefault: " + String.valueOf(IDLE_PENDING_FACTOR));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_idle_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_IDLE_TIMEOUT);
                builder.setMessage(DESC_IDLE_TIMEOUT + "\n\nDefault: " + String.valueOf(IDLE_TIMEOUT / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_max_idle_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_MAX_IDLE_TIMEOUT);
                builder.setMessage(DESC_MAX_IDLE_TIMEOUT + "\n\nDefault: " + String.valueOf(MAX_IDLE_TIMEOUT / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_idle_factor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_IDLE_FACTOR);
                builder.setMessage(DESC_IDLE_FACTOR + "\n\nDefault: " + String.valueOf(IDLE_FACTOR));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_min_time_to_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_MIN_TIME_TO_ALARM);
                builder.setMessage(DESC_MIN_TIME_TO_ALARM + "\n\nDefault: " + String.valueOf(MIN_TIME_TO_ALARM / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_max_temp_app_whitelist_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_MAX_TEMP_APP_WHITELIST_DURATION);
                builder.setMessage(DESC_MAX_TEMP_APP_WHITELIST_DURATION + "\n\nDefault: " + String.valueOf(MAX_TEMP_APP_WHITELIST_DURATION / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_mms_temp_app_whitelist_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_MMS_TEMP_APP_WHITELIST_DURATION);
                builder.setMessage(DESC_MMS_TEMP_APP_WHITELIST_DURATION + "\n\nDefault: " + String.valueOf(MMS_TEMP_APP_WHITELIST_DURATION / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        iv_sms_temp_app_whitelist_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int divideBy= getDisplayValueFix();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(KEY_SMS_TEMP_APP_WHITELIST_DURATION);
                builder.setMessage(DESC_SMS_TEMP_APP_WHITELIST_DURATION + "\n\nDefault: " + String.valueOf(SMS_TEMP_APP_WHITELIST_DURATION / divideBy) + ((divideBy == 1) ? " milliseconds" : " seconds"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void getSettings() {
        if(hasRoot) {
            try {
                Command command = new Command(0, "settings get global device_idle_constants") {
                    @Override
                    public void commandOutput(int id, String line) {
                        if(line.startsWith("Error")){
                            Toast.makeText(MainActivity.this, "Can not access device settings. You are now in non root mode.", Toast.LENGTH_LONG).show();
                            hasRoot = false;
                            super.commandOutput(id, line);
                        }
                        KeyValueListParser parser = new KeyValueListParser(',');
                        if ("null".equals(line)) {
                            parser.setString(line + "=0");
                        } else {
                            parser.setString(line);
                        }
                        int divideBy= getDisplayValueFix();
                        et_inactive_to.setText(String.valueOf(parser.getLong(KEY_INACTIVE_TIMEOUT, INACTIVE_TIMEOUT) / divideBy));
                        et_sensing_to.setText(String.valueOf(parser.getLong(KEY_SENSING_TIMEOUT, SENSING_TIMEOUT) / divideBy));
                        et_locating_to.setText(String.valueOf(parser.getLong(KEY_LOCATING_TIMEOUT, LOCATING_TIMEOUT) / divideBy));
                        et_location_accuracy.setText(String.valueOf(parser.getFloat(KEY_LOCATION_ACCURACY, LOCATION_ACCURACY)));
                        et_motion_inactive_to.setText(String.valueOf(parser.getLong(KEY_MOTION_INACTIVE_TIMEOUT, MOTION_INACTIVE_TIMEOUT) / divideBy));
                        et_idle_after_inactive_to.setText(String.valueOf(parser.getLong(KEY_IDLE_AFTER_INACTIVE_TIMEOUT, IDLE_AFTER_INACTIVE_TIMEOUT) / divideBy));
                        et_idle_pending_to.setText(String.valueOf(parser.getLong(KEY_IDLE_PENDING_TIMEOUT, IDLE_PENDING_TIMEOUT) / divideBy));
                        et_max_idle_pending_to.setText(String.valueOf(parser.getLong(KEY_MAX_IDLE_PENDING_TIMEOUT, MAX_IDLE_PENDING_TIMEOUT) / divideBy));
                        et_idle_pending_factor.setText(String.valueOf(parser.getFloat(KEY_IDLE_PENDING_FACTOR, IDLE_PENDING_FACTOR)));
                        et_idle_to.setText(String.valueOf(parser.getLong(KEY_IDLE_TIMEOUT, IDLE_TIMEOUT) / divideBy));
                        et_max_idle_to.setText(String.valueOf(parser.getLong(KEY_MAX_IDLE_TIMEOUT, MAX_IDLE_TIMEOUT) / divideBy));
                        et_idle_factor.setText(String.valueOf(parser.getFloat(KEY_IDLE_FACTOR, IDLE_FACTOR)));
                        et_min_time_to_alarm.setText(String.valueOf(parser.getLong(KEY_MIN_TIME_TO_ALARM, MIN_TIME_TO_ALARM) / divideBy));
                        et_max_temp_app_whitelist_duration.setText(String.valueOf(parser.getLong(KEY_MAX_TEMP_APP_WHITELIST_DURATION, MAX_TEMP_APP_WHITELIST_DURATION) / divideBy));
                        et_mms_temp_app_whitelist_duration.setText(String.valueOf(parser.getLong(KEY_MMS_TEMP_APP_WHITELIST_DURATION, MMS_TEMP_APP_WHITELIST_DURATION) / divideBy));
                        et_sms_temp_app_whitelist_duration.setText(String.valueOf(parser.getLong(KEY_SMS_TEMP_APP_WHITELIST_DURATION, SMS_TEMP_APP_WHITELIST_DURATION) / divideBy));


                        //MUST call the super method when overriding!
                        super.commandOutput(id, line);
                    }

                    @Override
                    public void commandTerminated(int id, String reason) {
                    }

                    @Override
                    public void commandCompleted(int id, int exitcode) {
                    }

                };
                RootShell.getShell(true).add(command);
            } catch (RootDeniedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            int divideBy = getDisplayValueFix();
            et_inactive_to.setText(String.valueOf(INACTIVE_TIMEOUT / divideBy));
            et_sensing_to.setText(String.valueOf(SENSING_TIMEOUT / divideBy));
            et_locating_to.setText(String.valueOf(LOCATING_TIMEOUT / divideBy));
            et_location_accuracy.setText(String.valueOf(LOCATION_ACCURACY));
            et_motion_inactive_to.setText(String.valueOf(MOTION_INACTIVE_TIMEOUT / divideBy));
            et_idle_after_inactive_to.setText(String.valueOf(IDLE_AFTER_INACTIVE_TIMEOUT / divideBy));
            et_idle_pending_to.setText(String.valueOf(IDLE_PENDING_TIMEOUT / divideBy));
            et_max_idle_pending_to.setText(String.valueOf(MAX_IDLE_PENDING_TIMEOUT / divideBy));
            et_idle_pending_factor.setText(String.valueOf(IDLE_PENDING_FACTOR));
            et_idle_to.setText(String.valueOf(IDLE_TIMEOUT / divideBy));
            et_max_idle_to.setText(String.valueOf(MAX_IDLE_TIMEOUT / divideBy));
            et_idle_factor.setText(String.valueOf(IDLE_FACTOR));
            et_min_time_to_alarm.setText(String.valueOf(MIN_TIME_TO_ALARM / divideBy));
            et_max_temp_app_whitelist_duration.setText(String.valueOf(MAX_TEMP_APP_WHITELIST_DURATION / divideBy));
            et_mms_temp_app_whitelist_duration.setText(String.valueOf(MMS_TEMP_APP_WHITELIST_DURATION / divideBy));
            et_sms_temp_app_whitelist_duration.setText(String.valueOf(SMS_TEMP_APP_WHITELIST_DURATION / divideBy));
        }
    }

    private int getDisplayValueFix() {
        displayValueIn = Integer.valueOf(sharedPref.getString("list_display_value_in", "-1"));
        int divideBy = 1;
        switch (displayValueIn){
            case -1:
                divideBy = 1;
                break;
            case 0:
                divideBy = millisecondsInOneSecond;
                break;
        }
        return divideBy;
    }

    private void save(){
        int multiplyBy = getDisplayValueFix();
        StringBuilder sb = new StringBuilder();
        sb.append(KEY_INACTIVE_TIMEOUT + "=" + Long.valueOf(et_inactive_to.getText().toString()) * multiplyBy + ",");
        sb.append(KEY_SENSING_TIMEOUT + "=" + Long.valueOf(et_sensing_to.getText().toString()) * multiplyBy + ",");
        sb.append(KEY_LOCATING_TIMEOUT + "=" + Long.valueOf(et_locating_to.getText().toString()) * multiplyBy + ",");
        sb.append(KEY_LOCATION_ACCURACY + "=" + Float.valueOf(et_location_accuracy.getText().toString()) + ",");
        sb.append(KEY_MOTION_INACTIVE_TIMEOUT + "=" + Long.valueOf(et_motion_inactive_to.getText().toString()) * multiplyBy + ",");
        sb.append(KEY_IDLE_AFTER_INACTIVE_TIMEOUT + "=" + Long.valueOf(et_idle_after_inactive_to.getText().toString()) * multiplyBy + ",");
        sb.append(KEY_IDLE_PENDING_TIMEOUT + "=" + Long.valueOf(et_idle_pending_to.getText().toString()) * multiplyBy + ",");
        sb.append(KEY_MAX_IDLE_PENDING_TIMEOUT + "=" + Long.valueOf(et_max_idle_pending_to.getText().toString()) * multiplyBy + ",");
        sb.append(KEY_IDLE_PENDING_FACTOR + "=" + Float.valueOf(et_idle_pending_factor.getText().toString()) + ",");
        sb.append(KEY_IDLE_TIMEOUT + "=" + Long.valueOf(et_idle_to.getText().toString()) * multiplyBy + ",");
        sb.append(KEY_MAX_IDLE_TIMEOUT + "=" + Long.valueOf(et_max_idle_to.getText().toString()) * multiplyBy + ",");
        sb.append(KEY_IDLE_FACTOR + "=" + Float.valueOf(et_idle_factor.getText().toString()) + ",");
        sb.append(KEY_MIN_TIME_TO_ALARM + "=" + Long.valueOf(et_min_time_to_alarm.getText().toString()) * multiplyBy + ",");
        sb.append(KEY_MAX_TEMP_APP_WHITELIST_DURATION + "=" + Long.valueOf(et_max_temp_app_whitelist_duration.getText().toString()) * multiplyBy + ",");
        sb.append(KEY_MMS_TEMP_APP_WHITELIST_DURATION + "=" + Long.valueOf(et_mms_temp_app_whitelist_duration.getText().toString()) * multiplyBy + ",");
        sb.append(KEY_SMS_TEMP_APP_WHITELIST_DURATION + "=" + Long.valueOf(et_sms_temp_app_whitelist_duration.getText().toString()) * multiplyBy);

        if(hasRoot) {
            try {
                Command command = new Command(0, "settings put global device_idle_constants " + sb.toString()) {
                    @Override
                    public void commandOutput(int id, String line) {
                        //MUST call the super method when overriding!
                        super.commandOutput(id, line);
                    }

                    @Override
                    public void commandTerminated(int id, String reason) {
                    }

                    @Override
                    public void commandCompleted(int id, int exitcode) {
                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }

                };
                RootShell.getShell(true).add(command);
            } catch (RootDeniedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            final String command = "adb shell settings put global device_idle_constants " + sb.toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("ADB Command");
            builder.setMessage(command);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Nothing
                }
            });
            builder.setNegativeButton("Copy to clipboard", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ClipboardManager manager =
                            (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    manager.setText(command);
                    Toast.makeText(MainActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void restoreDefaults(){
        StringBuilder sb = new StringBuilder();
        sb.append(KEY_INACTIVE_TIMEOUT + "=" + INACTIVE_TIMEOUT + ",");
        sb.append(KEY_SENSING_TIMEOUT + "=" + SENSING_TIMEOUT + ",");
        sb.append(KEY_LOCATING_TIMEOUT + "=" + LOCATING_TIMEOUT + ",");
        sb.append(KEY_LOCATION_ACCURACY + "=" + LOCATION_ACCURACY + ",");
        sb.append(KEY_MOTION_INACTIVE_TIMEOUT + "=" + MOTION_INACTIVE_TIMEOUT + ",");
        sb.append(KEY_IDLE_AFTER_INACTIVE_TIMEOUT + "=" + IDLE_AFTER_INACTIVE_TIMEOUT + ",");
        sb.append(KEY_IDLE_PENDING_TIMEOUT + "=" + IDLE_PENDING_TIMEOUT + ",");
        sb.append(KEY_MAX_IDLE_PENDING_TIMEOUT + "=" + MAX_IDLE_PENDING_TIMEOUT + ",");
        sb.append(KEY_IDLE_PENDING_FACTOR + "=" + IDLE_PENDING_FACTOR + ",");
        sb.append(KEY_IDLE_TIMEOUT + "=" + IDLE_TIMEOUT + ",");
        sb.append(KEY_MAX_IDLE_TIMEOUT + "=" + MAX_IDLE_TIMEOUT + ",");
        sb.append(KEY_IDLE_FACTOR + "=" + IDLE_FACTOR + ",");
        sb.append(KEY_MIN_TIME_TO_ALARM + "=" + MIN_TIME_TO_ALARM + ",");
        sb.append(KEY_MAX_TEMP_APP_WHITELIST_DURATION + "=" + MAX_TEMP_APP_WHITELIST_DURATION + ",");
        sb.append(KEY_MMS_TEMP_APP_WHITELIST_DURATION + "=" + MMS_TEMP_APP_WHITELIST_DURATION + ",");
        sb.append(KEY_SMS_TEMP_APP_WHITELIST_DURATION + "=" + SMS_TEMP_APP_WHITELIST_DURATION);

        if(hasRoot) {
            try {
                Command command = new Command(0, "settings put global device_idle_constants " + sb.toString()) {
                    @Override
                    public void commandOutput(int id, String line) {
                        //MUST call the super method when overriding!
                        super.commandOutput(id, line);
                    }

                    @Override
                    public void commandTerminated(int id, String reason) {
                    }

                    @Override
                    public void commandCompleted(int id, int exitcode) {
                        Toast.makeText(MainActivity.this, "Defaults restored", Toast.LENGTH_SHORT).show();
                    }

                };
                RootShell.getShell(true).add(command);
            } catch (RootDeniedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            final String command = "adb shell settings put global device_idle_constants " + sb.toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("ADB Command");
            builder.setMessage(command);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Nothing
                }
            });
            builder.setNegativeButton("Copy to clipboard", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ClipboardManager manager =
                            (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    manager.setText(command);
                    Toast.makeText(MainActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        //Show changes
        Toast.makeText(MainActivity.this, "Refreshing settings", Toast.LENGTH_SHORT).show();
        getSettings();
    }

    private void applyProfile(String settings){
        if(hasRoot) {
            try {
                Command command = new Command(0, "settings put global device_idle_constants " + settings) {
                    @Override
                    public void commandOutput(int id, String line) {
                        //MUST call the super method when overriding!
                        super.commandOutput(id, line);
                    }

                    @Override
                    public void commandTerminated(int id, String reason) {
                    }

                    @Override
                    public void commandCompleted(int id, int exitcode) {
                        Toast.makeText(MainActivity.this, "Defaults restored", Toast.LENGTH_SHORT).show();
                    }

                };
                RootShell.getShell(true).add(command);
            } catch (RootDeniedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            final String command = "adb shell settings put global device_idle_constants " + settings;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("ADB Command");
            builder.setMessage(command);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Nothing
                }
            });
            builder.setNegativeButton("Copy to clipboard", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ClipboardManager manager =
                            (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    manager.setText(command);
                    Toast.makeText(MainActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        //Show changes
        Toast.makeText(MainActivity.this, "Refreshing settings", Toast.LENGTH_SHORT).show();
        KeyValueListParser parser = new KeyValueListParser(',');
        parser.setString(settings);
        int divideBy= getDisplayValueFix();
        et_inactive_to.setText(String.valueOf(parser.getLong(KEY_INACTIVE_TIMEOUT, INACTIVE_TIMEOUT) / divideBy));
        et_sensing_to.setText(String.valueOf(parser.getLong(KEY_SENSING_TIMEOUT, SENSING_TIMEOUT) / divideBy));
        et_locating_to.setText(String.valueOf(parser.getLong(KEY_LOCATING_TIMEOUT, LOCATING_TIMEOUT) / divideBy));
        et_location_accuracy.setText(String.valueOf(parser.getFloat(KEY_LOCATION_ACCURACY, LOCATION_ACCURACY)));
        et_motion_inactive_to.setText(String.valueOf(parser.getLong(KEY_MOTION_INACTIVE_TIMEOUT, MOTION_INACTIVE_TIMEOUT) / divideBy));
        et_idle_after_inactive_to.setText(String.valueOf(parser.getLong(KEY_IDLE_AFTER_INACTIVE_TIMEOUT, IDLE_AFTER_INACTIVE_TIMEOUT) / divideBy));
        et_idle_pending_to.setText(String.valueOf(parser.getLong(KEY_IDLE_PENDING_TIMEOUT, IDLE_PENDING_TIMEOUT) / divideBy));
        et_max_idle_pending_to.setText(String.valueOf(parser.getLong(KEY_MAX_IDLE_PENDING_TIMEOUT, MAX_IDLE_PENDING_TIMEOUT) / divideBy));
        et_idle_pending_factor.setText(String.valueOf(parser.getFloat(KEY_IDLE_PENDING_FACTOR, IDLE_PENDING_FACTOR)));
        et_idle_to.setText(String.valueOf(parser.getLong(KEY_IDLE_TIMEOUT, IDLE_TIMEOUT) / divideBy));
        et_max_idle_to.setText(String.valueOf(parser.getLong(KEY_MAX_IDLE_TIMEOUT, MAX_IDLE_TIMEOUT) / divideBy));
        et_idle_factor.setText(String.valueOf(parser.getFloat(KEY_IDLE_FACTOR, IDLE_FACTOR)));
        et_min_time_to_alarm.setText(String.valueOf(parser.getLong(KEY_MIN_TIME_TO_ALARM, MIN_TIME_TO_ALARM) / divideBy));
        et_max_temp_app_whitelist_duration.setText(String.valueOf(parser.getLong(KEY_MAX_TEMP_APP_WHITELIST_DURATION, MAX_TEMP_APP_WHITELIST_DURATION) / divideBy));
        et_mms_temp_app_whitelist_duration.setText(String.valueOf(parser.getLong(KEY_MMS_TEMP_APP_WHITELIST_DURATION, MMS_TEMP_APP_WHITELIST_DURATION) / divideBy));
        et_sms_temp_app_whitelist_duration.setText(String.valueOf(parser.getLong(KEY_SMS_TEMP_APP_WHITELIST_DURATION, SMS_TEMP_APP_WHITELIST_DURATION) / divideBy));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_profile:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Profiles");
                builder.setItems(Profiles.ProfileListNames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        applyProfile(Profiles.ProfileList[item]);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.action_save:
                save();
                break;
            case R.id.action_restoredefault:
                restoreDefaults();
                break;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                sharedPref.registerOnSharedPreferenceChangeListener(
                        new SharedPreferences.OnSharedPreferenceChangeListener() {
                            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                                getSettings();
                            }
                        });
        }

        return super.onOptionsItemSelected(item);
    }
}
