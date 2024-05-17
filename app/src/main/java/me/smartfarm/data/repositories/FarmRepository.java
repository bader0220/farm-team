package me.smartfarm.data.repositories;

import java.util.HashMap;

public class FarmRepository extends AbstractRepository {


    public void getFarms(HashMap<String, Object> filters, Callback callback) {
        filters.put("collection", "farms");
      //  getAll(filters, callback);
    }
}
