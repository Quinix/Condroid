package cz.quinix.condroid.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockListActivity;
import com.google.inject.Inject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.quinix.condroid.R;
import cz.quinix.condroid.database.DataProvider;
import cz.quinix.condroid.model.Reminder;
import cz.quinix.condroid.service.ReminderManager;
import cz.quinix.condroid.ui.activities.MainActivity;

public class ReminderList extends RoboSherlockListActivity {

	List<Reminder> data;

	@Inject
	DataProvider provider;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		data = provider.getReminderList();
		this.setListAdapter(new CustomAdapter(this, data));
		this.registerForContextMenu(this.getListView());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		if (v instanceof ListView) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			Reminder an = (Reminder) ((ListView) v).getItemAtPosition(info.position);
			menu.setHeaderTitle(an.annotation.getTitle());
			String[] menuItems = getResources().getStringArray(R.array.reminderContext);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		Reminder an = (Reminder) this.getListView().getItemAtPosition(info.position);
		switch (menuItemIndex) {
			case 0:
				provider.removeReminder(an.annotation.getPid());
				ReminderManager.updateAlarmManager(this);
				this.data.clear();
				this.data.addAll(provider.getReminderList());
				((CustomAdapter) this.getListView().getAdapter()).notifyDataSetChanged();
				break;
			default:
				break;
		}
		return true;
	}

	private class CustomAdapter extends ArrayAdapter<Reminder> {

		public CustomAdapter(Context context, List<Reminder> objects) {
			super(context, android.R.layout.simple_list_item_1, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Reminder r = this.getItem(position);

			View v = convertView;
			if (v == null) {
				v = ((LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.reminders_list, null);
			}
			TextView title = (TextView) v.findViewById(R.id.tReminderListTitle);
			TextView date = (TextView) v.findViewById(R.id.tReminderListDate);
			TextView remind = (TextView) v.findViewById(R.id.tReminderListRemind);

			if (r != null) {
				DateFormat todayFormat = new SimpleDateFormat("HH:mm");
				DateFormat dayFormat = new SimpleDateFormat(
						"EE dd.MM. HH:mm", new Locale("cs", "CZ"));
				title.setText(r.annotation.getTitle());
				Date today = new Date();
				Date st = r.annotation.getStart();
				try {
					if ((st.getYear() == today.getYear() && st.getMonth() == today.getMonth() && st.getDate() == today.getDate())) {
						date.setText("dnes, " + todayFormat.format(r.annotation.getStart()));
					} else {
						date.setText(dayFormat.format(r.annotation.getStart()));
					}
				} catch (NullPointerException ignored) {

				}

				remind.setText(r.reminder + " min před");
				return v;
			}

			return super.getView(position, convertView, parent);    //To change body of overridden methods use File | Settings | File Templates.
		}
	}
}