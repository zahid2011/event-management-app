package com.example.event_lottery;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdminImageManagementActivity extends AppCompatActivity {
    private static final String TAG = "AdminImageManagement";
    private FirebaseStorage storage;
    private List<StorageReference> imageReferences;
    private AdminImageListAdaptor adapter;
    private ListView listView;
    private boolean testMode;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_image_list);

        testMode = getIntent().getBooleanExtra("TEST_MODE", false);

        imageReferences = new ArrayList<>();
        adapter = new AdminImageListAdaptor(this, imageReferences);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Firebase Storage
        storage = FirebaseStorage.getInstance();
        fetchAllImages();
    }

    /**
     * Fetches all images from Firebase Storage, including those in the root folder and subfolders.
     */
    private void fetchAllImages() {
        StorageReference rootRef = storage.getReference();

        rootRef.listAll()
                .addOnSuccessListener(listResult -> {
                    // fetching the images in the root folder
                    imageReferences.addAll(listResult.getItems());
                    adapter.notifyDataSetChanged();

                    // fetching the images from subfolders
                    fetchImagesFromFolders(listResult);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching images", e);
                    Toast.makeText(this, "Error fetching images", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Fetches images from all subfolders in Firebase Storage.
     *
     * @param listResult The ListResult containing references to subfolders.
     */
    private void fetchImagesFromFolders(ListResult listResult) {
        for (StorageReference folder : listResult.getPrefixes()) {
            folder.listAll().addOnSuccessListener(folderResult -> {
                imageReferences.addAll(folderResult.getItems());
                adapter.notifyDataSetChanged();
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Error fetching images from folder", e);
            });
        }
    }

    /**
     * Deletes the specified image from Firebase Storage after user confirmation.
     *
     * @param imageRef The StorageReference pointing to the image to be deleted.
     */
    public void deleteImage(StorageReference imageRef) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    imageRef.delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
                                imageReferences.remove(imageRef);
                                adapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error deleting image", e);
                                Toast.makeText(this, "Error deleting image", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}