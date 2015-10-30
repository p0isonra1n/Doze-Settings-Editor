package com.isaacparker.dozesettingseditor;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

    boolean hasRoot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    private void getSettings() {
        if(hasRoot) {
            try {
                Command command = new Command(0, "settings get global device_idle_constants") {
                    @Override
                    public void commandOutput(int id, String line) {
                        KeyValueListParser parser = new KeyValueListParser(',');
                        if ("null".equals(line)) {
                            parser.setString(line + "=0");
                        } else {
                            parser.setString(line);
                        }
                        et_inactive_to.setText(String.valueOf(parser.getLong(KEY_INACTIVE_TIMEOUT, INACTIVE_TIMEOUT)));
                        et_sensing_to.setText(String.valueOf(parser.getLong(KEY_SENSING_TIMEOUT, SENSING_TIMEOUT)));
                        et_locating_to.setText(String.valueOf(parser.getLong(KEY_LOCATING_TIMEOUT, LOCATING_TIMEOUT)));
                        et_location_accuracy.setText(String.valueOf(parser.getFloat(KEY_LOCATION_ACCURACY, LOCATION_ACCURACY)));
                        et_motion_inactive_to.setText(String.valueOf(parser.getLong(KEY_MOTION_INACTIVE_TIMEOUT, MOTION_INACTIVE_TIMEOUT)));
                        et_idle_after_inactive_to.setText(String.valueOf(parser.getLong(KEY_IDLE_AFTER_INACTIVE_TIMEOUT, IDLE_AFTER_INACTIVE_TIMEOUT)));
                        et_idle_pending_to.setText(String.valueOf(parser.getLong(KEY_IDLE_PENDING_TIMEOUT, IDLE_PENDING_TIMEOUT)));
                        et_max_idle_pending_to.setText(String.valueOf(parser.getLong(KEY_MAX_IDLE_PENDING_TIMEOUT, MAX_IDLE_PENDING_TIMEOUT)));
                        et_idle_pending_factor.setText(String.valueOf(parser.getFloat(KEY_IDLE_PENDING_FACTOR, IDLE_PENDING_FACTOR)));
                        et_idle_to.setText(String.valueOf(parser.getLong(KEY_IDLE_TIMEOUT, IDLE_TIMEOUT)));
                        et_max_idle_to.setText(String.valueOf(parser.getLong(KEY_MAX_IDLE_TIMEOUT, MAX_IDLE_TIMEOUT)));
                        et_idle_factor.setText(String.valueOf(parser.getFloat(KEY_IDLE_FACTOR, IDLE_FACTOR)));
                        et_min_time_to_alarm.setText(String.valueOf(parser.getLong(KEY_MIN_TIME_TO_ALARM, MIN_TIME_TO_ALARM)));
                        et_max_temp_app_whitelist_duration.setText(String.valueOf(parser.getLong(KEY_MAX_TEMP_APP_WHITELIST_DURATION, MAX_TEMP_APP_WHITELIST_DURATION)));
                        et_mms_temp_app_whitelist_duration.setText(String.valueOf(parser.getLong(KEY_MMS_TEMP_APP_WHITELIST_DURATION, MMS_TEMP_APP_WHITELIST_DURATION)));
                        et_sms_temp_app_whitelist_duration.setText(String.valueOf(parser.getLong(KEY_SMS_TEMP_APP_WHITELIST_DURATION, SMS_TEMP_APP_WHITELIST_DURATION)));


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
            et_inactive_to.setText(String.valueOf(INACTIVE_TIMEOUT));
            et_sensing_to.setText(String.valueOf(SENSING_TIMEOUT));
            et_locating_to.setText(String.valueOf(LOCATING_TIMEOUT));
            et_location_accuracy.setText(String.valueOf(LOCATION_ACCURACY));
            et_motion_inactive_to.setText(String.valueOf(MOTION_INACTIVE_TIMEOUT));
            et_idle_after_inactive_to.setText(String.valueOf(IDLE_AFTER_INACTIVE_TIMEOUT));
            et_idle_pending_to.setText(String.valueOf(IDLE_PENDING_TIMEOUT));
            et_max_idle_pending_to.setText(String.valueOf(MAX_IDLE_PENDING_TIMEOUT));
            et_idle_pending_factor.setText(String.valueOf(IDLE_PENDING_FACTOR));
            et_idle_to.setText(String.valueOf(IDLE_TIMEOUT));
            et_max_idle_to.setText(String.valueOf(MAX_IDLE_TIMEOUT));
            et_idle_factor.setText(String.valueOf(IDLE_FACTOR));
            et_min_time_to_alarm.setText(String.valueOf(MIN_TIME_TO_ALARM));
            et_max_temp_app_whitelist_duration.setText(String.valueOf(MAX_TEMP_APP_WHITELIST_DURATION));
            et_mms_temp_app_whitelist_duration.setText(String.valueOf(MMS_TEMP_APP_WHITELIST_DURATION));
            et_sms_temp_app_whitelist_duration.setText(String.valueOf(SMS_TEMP_APP_WHITELIST_DURATION));
        }
    }

    private void save(){
        StringBuilder sb = new StringBuilder();
        sb.append(KEY_INACTIVE_TIMEOUT + "=" + Long.valueOf(et_inactive_to.getText().toString()) + ",");
        sb.append(KEY_SENSING_TIMEOUT + "=" + Long.valueOf(et_sensing_to.getText().toString()) + ",");
        sb.append(KEY_LOCATING_TIMEOUT + "=" + Long.valueOf(et_locating_to.getText().toString()) + ",");
        sb.append(KEY_LOCATION_ACCURACY + "=" + Float.valueOf(et_location_accuracy.getText().toString()) + ",");
        sb.append(KEY_MOTION_INACTIVE_TIMEOUT + "=" + Long.valueOf(et_motion_inactive_to.getText().toString()) + ",");
        sb.append(KEY_IDLE_AFTER_INACTIVE_TIMEOUT + "=" + Long.valueOf(et_idle_after_inactive_to.getText().toString()) + ",");
        sb.append(KEY_IDLE_PENDING_TIMEOUT + "=" + Long.valueOf(et_idle_pending_to.getText().toString()) + ",");
        sb.append(KEY_MAX_IDLE_PENDING_TIMEOUT + "=" + Long.valueOf(et_max_idle_pending_to.getText().toString()) + ",");
        sb.append(KEY_IDLE_PENDING_FACTOR + "=" + Float.valueOf(et_idle_pending_factor.getText().toString()) + ",");
        sb.append(KEY_IDLE_TIMEOUT + "=" + Long.valueOf(et_idle_to.getText().toString()) + ",");
        sb.append(KEY_MAX_IDLE_TIMEOUT + "=" + Long.valueOf(et_max_idle_to.getText().toString()) + ",");
        sb.append(KEY_IDLE_FACTOR + "=" + Float.valueOf(et_idle_factor.getText().toString()) + ",");
        sb.append(KEY_MIN_TIME_TO_ALARM + "=" + Long.valueOf(et_min_time_to_alarm.getText().toString()) + ",");
        sb.append(KEY_MAX_TEMP_APP_WHITELIST_DURATION + "=" + Long.valueOf(et_max_temp_app_whitelist_duration.getText().toString()) + ",");
        sb.append(KEY_MMS_TEMP_APP_WHITELIST_DURATION + "=" + Long.valueOf(et_mms_temp_app_whitelist_duration.getText().toString()) + ",");
        sb.append(KEY_SMS_TEMP_APP_WHITELIST_DURATION + "=" + Long.valueOf(et_sms_temp_app_whitelist_duration.getText().toString()));

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
        if (id == R.id.action_save) {
            save();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
