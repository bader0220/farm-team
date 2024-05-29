package me.smartfarm.data.repositories;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class StorageRepository {
    private static StorageRepository instance;
    private FirebaseStorage storage;
    public StorageReference farmsFolder;

    private StorageRepository(String folderName) {
        storage = FirebaseStorage.getInstance();
        farmsFolder = storage.getReference().child(folderName);
    }

    public static StorageRepository getInstance(String folderName) {
        if (instance == null) {
            instance = new StorageRepository(folderName);
        }
        return instance;
    }

    public Task<String> uploadImage(Uri imageUri, String userId) {
        StorageReference imageRef = farmsFolder.child(userId + System.currentTimeMillis()).child(UUID.randomUUID().toString());

        return imageRef.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Return the download URL
                    return imageRef.getDownloadUrl();
                })
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {
                            return downloadUri.toString();
                        } else {
                            throw new Exception("Download URL is null");
                        }
                    } else {
                        throw task.getException();
                    }
                });
    }

    public  Task<List<String>> uploadImages(Set<Uri> imageUris, String bookTitle) {

        List<Task<Uri>> uploadTasks = new ArrayList<>();

        for (Uri imageUri : imageUris) {
            StorageReference imageRef = farmsFolder.child(bookTitle + System.currentTimeMillis()).child(UUID.randomUUID().toString());
            uploadTasks.add(imageRef.putFile(imageUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Return the download URL
                return imageRef.getDownloadUrl();
            }));
        }
        // Combine all tasks into a single task
        return Tasks.whenAllSuccess(uploadTasks);
    }
}
