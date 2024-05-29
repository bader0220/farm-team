package me.smartfarm.data.repositories;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import me.smartfarm.R;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.User;

public class AuthenticationRepository {
    private final FirebaseAuth mAuth;

    private final Context context;

    public AuthenticationRepository(Context context) {
        mAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public <T> void login(String email, String password, Class<T> toActivity) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.please_wait));
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(authResultTask -> {
                    if (authResultTask.isSuccessful()) {
                        UserRepository userRepository = new UserRepository(context);
                        userRepository.getDocumentById(authResultTask.getResult().getUser().getUid(), User.class, new OnLoadingComplete<User>() {
                            @Override
                            public void onLoaded(User user) {
                                progressDialog.dismiss();
                                Util.setCurrentUser(user);
                                Intent intent = new Intent(context, toActivity);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(context, context.getResources().getString(R.string.invalid_credintials), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void signup(String email, String password, FirebaseTask<Task<AuthResult>> firebaseTask) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> authResultTask) {
                firebaseTask.onComplete(authResultTask);
            }
        });
    }

}
