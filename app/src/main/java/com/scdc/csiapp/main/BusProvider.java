package com.scdc.csiapp.main;

import com.squareup.otto.Bus;

/**
 * Created by Pantearz07 on 16/11/2559.
 */

public class BusProvider {
    private static Bus bus = new Bus();

    public static Bus getInstance() {
        return bus;
    }
}
