package home.com.epd42;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;


public class LongRunningService extends Service {

    private static final String TAG = "LongRunningService"; //debug

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable(){

            @Override
            public void run() {
                Log.i(TAG, "run: executed at "+ new Date().toString());
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 00);

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent i = new Intent(this,AlarmReceiver.class);
        i.setAction("startAlarm");
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);

        return super.onStartCommand(intent, flags, startId);
    }
}
