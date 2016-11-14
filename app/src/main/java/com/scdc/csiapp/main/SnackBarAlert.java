package com.scdc.csiapp.main;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Pantearz07 on 14/11/2559.
 */

public class SnackBarAlert {
    private Snackbar snackbar;
    private int duration;
    private String textAlert;
    private View view;

    public SnackBarAlert(Snackbar snackbar, View view, int duration, String textAlert) {
        this.snackbar = snackbar;
        this.view = view;
        this.duration = duration;
        this.textAlert = textAlert;
    }

    public void createSnacbar(){

        snackbar = Snackbar.make(view, textAlert, duration);
        snackbar.show();
    }
}
