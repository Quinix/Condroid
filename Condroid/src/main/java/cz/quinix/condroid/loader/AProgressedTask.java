package cz.quinix.condroid.loader;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import cz.quinix.condroid.abstracts.AListenedAsyncTask;
import cz.quinix.condroid.abstracts.ITaskListener;

/**
 * Created by Jan on 27. 4. 2014.
 */
public abstract class AProgressedTask<Progress, Result> extends AListenedAsyncTask<Progress, Result> {

    protected ProgressDialog pd;
    private SherlockFragmentActivity parent;
    protected String pdString;

    public AProgressedTask(ITaskListener listener, SherlockFragmentActivity parent) {
        super(listener);

        this.parent = parent;
    }

    @Override
    protected void onPreExecute() throws Exception {
        super.onPreExecute();
        this.showDialog();
    }

    @Override
    protected void onFinally() throws RuntimeException {
        super.onFinally();
        if(this.pd != null && this.pd.isShowing()) {
            this.pd.dismiss();
        }
    }

    protected void showDialog() {
        if (this.pd != null && this.pd.isShowing()) {
            return;
        }
        if (parent != null) {
            this.pd = new ProgressDialog(parent);
            this.pd.setMessage(pdString);
           /* if (this.pdMax > 0) {
                this.pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setMax(pdMax);
            }*/
            pd.setCancelable(true);
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.dismiss();
                    AProgressedTask.this.cancel(true);
                }
            });
            pd.show();
        }
    }
}