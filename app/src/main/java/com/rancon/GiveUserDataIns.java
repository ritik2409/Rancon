package com.rancon;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaishu on 7/2/18.
 */

class GiveUserDataIns {
    List<UserData> userDataList = new ArrayList<>();
    private static final GiveUserDataIns ourInstance = new GiveUserDataIns();

    static GiveUserDataIns getInstance() {
        return ourInstance;
    }

    private GiveUserDataIns() {
    }
    public UserData finalUserDataIns (String a) {
        UserData outputUserData = null;
        for (UserData i : userDataList) {
            Log.e("GGGGGG",a);
            if (a.equals(i.cardId)) {
                outputUserData = i;

            }
        }
        return outputUserData;
    }
}
