package me.smartfarm.viewmodel;
//
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.google.firebase.auth.FirebaseUser;
//
//import me.smartfarm.data.repositories.AbstractRepository;
//
//public class AuthViewModel extends ViewModel {
//    private AbstractRepository repository;
//
//    public AuthViewModel() {
//        repository = new AbstractRepository();
//    }
//
//    public LiveData<FirebaseUser> register(String email, String password) {
//        return repository.register(email, password);
//    }
//
//    public LiveData<FirebaseUser> login(String email, String password) {
//        return repository.login(email, password);
//    }
//}