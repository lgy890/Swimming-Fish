package com.swimmingFish.Loading;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncTaskLoader extends AsyncTask<IAsyncCallback, Integer, Boolean> {

    // ===========================================================
    // Fields
    // ===========================================================

    IAsyncCallback[] _params;

    // ===========================================================
    // Inherited Methods
    // ===========================================================

    @Override
    protected Boolean doInBackground(IAsyncCallback... params) {
        this._params = params;
        int count = params.length;
        for(int i = 0; i < count; i++){
            params[i].workToDo();//Execute every callback function
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        int count = this._params.length;
        for(int i = 0; i < count; i++){
            this._params[i].onComplete();
        }
    }
}