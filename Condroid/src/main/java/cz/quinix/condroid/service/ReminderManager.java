package cz.quinix.condroid.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import cz.quinix.condroid.database.DataProvider;
import cz.quinix.condroid.model.Reminder;
import roboguice.RoboGuice;

public class ReminderManager {

	public static void updateAlarmManager(Context context) {
		Log.d("Condroid", "Setting up alarm service");
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		DataProvider dp = RoboGuice.getInjector(context).getInstance(DataProvider.class);

		//find closest event
		Reminder closest = dp.getNextReminder();
		if (closest != null) {
			if (closest.annotation == null) {
				Toast.makeText(context, "Systémová chyba, vymažte data programu.", Toast.LENGTH_LONG).show();
				return;
			}
			if (closest.annotation.getStart() == null) {
				dp.removeReminder(closest.annotation.getPid());
				return;
			}

			PendingIntent pi = PendingIntent.getService(context, 0, new Intent(context, ReminderTask.class), 0);
			long time = closest.annotation.getStart().getTime() - (closest.reminder * 60 * 1000);

			am.set(AlarmManager.RTC_WAKEUP, time, pi);
			Log.d("Condroid", "Alarm will run in " + new Date(time));
		} else {
			Log.d("Condroid", "No next alarm");
		}
	}
}
