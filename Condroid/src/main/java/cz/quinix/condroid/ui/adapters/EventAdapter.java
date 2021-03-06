package cz.quinix.condroid.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cz.quinix.condroid.R;
import cz.quinix.condroid.model.Convention;

public class EventAdapter extends ArrayAdapter<Convention> {

	private int layout;

	private List<Convention> data;

	public EventAdapter(Context context, List<Convention> data) {
		super(context, R.layout.event_item_layout, data);
		this.layout = R.layout.event_item_layout;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		EventViewHolder holder;

		if (row == null) {
			LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
			row = inflater.inflate(layout, parent, false);

			holder = new EventViewHolder();
			holder.title = (TextView) row.findViewById(R.id.tEventTitle);
			holder.subtitle = (TextView) row.findViewById(R.id.tEventSubtitle);

			row.setTag(holder);
		} else {
			holder = (EventViewHolder) row.getTag();
		}

		Convention event = data.get(position);
		holder.title.setText(event.getName());

		String date = "";
		if (event.getDate() != null) {
			date = event.getDate();
		}
		holder.subtitle.setText(date);

		return row;
	}

	static class EventViewHolder {

		TextView title;

		TextView subtitle;
	}
}
