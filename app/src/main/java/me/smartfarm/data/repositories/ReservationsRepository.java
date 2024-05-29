package me.smartfarm.data.repositories;

import android.annotation.SuppressLint;
import android.content.Context;

public class ReservationsRepository extends AbstractRepository {

    @SuppressLint("StaticFieldLeak")
    private static ReservationsRepository instance;

    public static ReservationsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ReservationsRepository(context);
        }
        return instance;
    }

    private ReservationsRepository(Context context) {
        super("reservations", context);
    }
}
