package cz.quinix.condroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import cz.quinix.condroid.R;
import cz.quinix.condroid.abstracts.CondroidActivity;
import cz.quinix.condroid.database.DataProvider;
import cz.quinix.condroid.model.Annotation;
import cz.quinix.condroid.ui.listeners.MakeFavoritedListener;
import cz.quinix.condroid.ui.listeners.SetReminderListener;
import cz.quinix.condroid.ui.listeners.ShareProgramListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ShowAnnotation extends CondroidActivity {

    private Annotation annotation;
    private static DateFormat todayFormat = new SimpleDateFormat("HH:mm");
    private static DateFormat dayFormat = new SimpleDateFormat(
            "EE dd.MM. HH:mm", new Locale("cs", "CZ"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.annotation = (Annotation) this.getIntent().getSerializableExtra(
                "annotation");
        this.setContentView(R.layout.annotation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) this.findViewById(R.id.annot_title);
        title.setText(this.annotation.getTitle());

        TextView author = (TextView) this.findViewById(R.id.annot_author);
        author.setText(this.annotation.getAuthor());

        TextView pid = (TextView) this.findViewById(R.id.annot_pid);
        pid.setText("" + this.annotation.getPid());

        String date = "";
        if (annotation.getStartTime() != null
                && annotation.getEndTime() != null) {
            date = formatDate(annotation.getStartTime()) + " - "
                    + todayFormat.format(annotation.getEndTime());
        }

        TextView line = (TextView) this.findViewById(R.id.annot_line);
        line.setText(", " + DataProvider.getInstance(getApplicationContext())
                .getProgramLine(this.annotation.getLid()).getName());
        if (this.annotation.getLocation() != null && !this.annotation.getLocation().trim().equals("")) {
            TextView location = (TextView) this.findViewById(R.id.annot_location);
            location.setText(this.annotation.getLocation());
        } else {
            findViewById(R.id.lLocation).setVisibility(View.GONE);
        }

        TextView info = (TextView) this.findViewById(R.id.annot_time);
        if (date != "") {

            info.setText(date);

        } else {
            findViewById(R.id.lDate).setVisibility(View.GONE);
        }
        ((TextView) this.findViewById(R.id.annot_type)).setText(", " + this
                .getTextualTypes());
        TextView text = (TextView) this.findViewById(R.id.annot_text);
        text.setText(this.annotation.getAnnotation());
        text.setMovementMethod(new ScrollingMovementMethod());
        ((ImageView) this.findViewById(R.id.iProgramIcon))
                .setImageResource(annotation.getProgramIcon());


    }

    private CharSequence getTextualTypes() {
        String ret = getTextualType(annotation.getType());
        String[] aT = annotation.getAdditionalTypes();
        for (int i = 0; i < aT.length; i++) {
            if (aT[i].length() > 0)
                ret += ", " + getTextualType(aT[i]);
        }
        return ret;
    }

    private String getTextualType(String type) {
        if (type.equalsIgnoreCase("P"))
            return getString(R.string.lecture);

        if (type.equalsIgnoreCase("B"))
            return getString(R.string.discussion);

        if (type.equalsIgnoreCase("C"))
            return getString(R.string.theatre);

        if (type.equalsIgnoreCase("D"))
            return getString(R.string.document);

        if (type.equalsIgnoreCase("F"))
            return getString(R.string.projection);

        if (type.equalsIgnoreCase("G"))
            return getString(R.string.game);

        if (type.equalsIgnoreCase("H"))
            return getString(R.string.music);

        if (type.equalsIgnoreCase("Q"))
            return getString(R.string.quiz);

        if (type.equalsIgnoreCase("W"))
            return getString(R.string.workshop);

        return "";
    }

    private String formatDate(Date date) {
        Calendar today = Calendar.getInstance(new Locale("cs", "CZ"));
        today.setTime(new Date());

        Calendar compared = Calendar.getInstance();
        compared.setTime(date);

        if (compared.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && compared.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && compared.get(Calendar.DAY_OF_MONTH) == today
                .get(Calendar.DAY_OF_MONTH)) {
            // its today
            return todayFormat.format(date);
        } else {
            return dayFormat.format(date);
        }

    }

    public Annotation getAnnotation() {
        return annotation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = this.getSupportMenuInflater();
        mi.inflate(R.menu.show_annotation, menu);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.mShare).getActionProvider();
        mShareActionProvider.setShareIntent(new ShareProgramListener(this).getShareActionIntent(this.annotation));

        if (DataProvider.getInstance(this).getFavorited().contains(Integer.valueOf(annotation.getPid()))) {
            menu.findItem(R.id.mFavorite).setIcon(R.drawable.ic_action_star_active);
        }

        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ProgramActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.mReminder:
                new SetReminderListener(this).invoke(this.annotation);
                return true;
            case R.id.mFavorite:
                if (new MakeFavoritedListener(this).invoke(this.annotation)) {
                    item.setIcon(R.drawable.ic_action_star_active);
                } else {
                    item.setIcon(R.drawable.ic_action_star);
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}