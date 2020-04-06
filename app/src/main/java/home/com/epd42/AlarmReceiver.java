package home.com.epd42;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver"; //debug

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Log.d(TAG, "run: executed AlarmReceiver BOOT_COMPLETED : " + new Date().toString());
        }

        if ("startAlarm".equals(intent.getAction())){
            Log.d(TAG, "run: executed AlarmReceiver : " + new Date().toString());
//            Intent home = new Intent(context, MainActivity.class);
//            home.putExtra("RELOAD",1);
//            home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(home);
        }

        Log.d(TAG, "run: executed AlarmReceiver : " + new Date().toString());
        intent = new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
