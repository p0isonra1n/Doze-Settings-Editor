# Doze-Settings-Editor
Requires Root

This app is a simple editor of the settings or parameters which affect the operation of Doze.

##Parameter Descriptions

These are all direct from the Doze source code.

**Inactive Timeout - inactive_to**

This is the time, after becoming inactive, at which we start looking at the motion sensor to determine if the device is being left alone. We don't do this immediately after going inactive just because we don't want to be continually running the significant motion sensor whenever the screen is off.

**Sensing Timeout - sensing_to**

If we don't receive a callback from AnyMotion in this amount of time + locating_to, we will change from STATE_SENSING to STATE_INACTIVE, and any AnyMotion callbacks while not in STATE_SENSING will be ignored.

**Locating Timeout - locating_to**

This is how long we will wait to try to get a good location fix before going in to idle mode.

**Location Accuracy - location_accuracy**

The desired maximum accuracy (in meters) we consider the location to be good enough to go on to idle. We will be trying to get an accuracy fix at least this good or until locating_to expires.

**Motion Inactive Timeout - motion_inactive_to**

This is the time, after seeing motion, that we wait after becoming inactive from that until we start looking for motion again.

**Idle After Inactive Timeout - idle_after_inactive_to**

This is the time, after the inactive timeout elapses, that we will wait looking for significant motion until we truly consider the device to be idle.

**Idle Pending Timeout - idle_pending_to**

This is the initial time, after being idle, that we will allow ourself to be back in the IDLE_PENDING state allowing the system to run normally until we return to idle.

**Max Idle Pending Timeout - max_idle_pending_to**

Maximum pending idle timeout (time spent running) we will be allowed to use.

**Idle Pending Factor - idle_pending_factor**

Scaling factor to apply to current pending idle timeout each time we cycle through that state.

**Idle Timeout - idle_to**

This is the initial time that we want to sit in the idle state before waking up again to return to pending idle and allowing normal work to run.

**Max Idle Timeout - max_idle_to**

Maximum idle duration we will be allowed to use.

**Idle Factor - idle_factor**

Scaling factor to apply to current idle timeout each time we cycle through that state.

**Min Time to Alarm - min_time_to_alarm**

This is the minimum time we will allow until the next upcoming alarm for us to actually go in to idle mode.

**Max Temp App Whitelist Duration - max_temp_app_whitelist_duration**

Max amount of time to temporarily whitelist an app when it receives a high tickle.

**MMS Temp App Whitelist Duration - mms_temp_app_whitelist_duration**

Amount of time we would like to whitelist an app that is receiving an MMS.

**SMS Temp App Whitelist Duration - sms_temp_app_whitelist_duration**

Amount of time we would like to whitelist an app that is receiving an SMS.
