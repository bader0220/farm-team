package me.smartfarm.data.repositories;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;

import me.smartfarm.common.Util;
import me.smartfarm.data.models.User;

public class UserRepository extends AbstractRepository {
    public UserRepository(Context context) {
        super("users", context);
    }

    public void registerUser(OnLoadingComplete<Void> onLoadingComplete) {
        StorageRepository.getInstance("users").uploadImage(Uri.parse(User.getInstance().getImage()), User.getInstance().getFirstName())
                .addOnSuccessListener(s -> {
                    User.getInstance().setImage(s);
                    collectionReference.document(User.getInstance().getId()).set(User.getInstance())
                            .addOnSuccessListener(unused -> {
                                onLoadingComplete.onLoaded(unused);
                                Util.setCurrentUser(User.getInstance());
                            });
                });
    }
}
