package me.smartfarm.data.repositories;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.HashMap;

public class FarmRepository extends AbstractRepository {
    @SuppressLint("StaticFieldLeak")
    private static FarmRepository instance;

    public synchronized static FarmRepository getInstance(Context context) {
        if (instance == null) {
            instance = new FarmRepository(context);
        }
        return instance;
    }

    private FarmRepository(Context context) {
        super("farms", context);
    }


//    public void getFarms(HashMap<String, Object> filters, Callback callback) {
//        filters.put("collection", "farms");
//      //  getAll(filters, callback);
//    }
}
