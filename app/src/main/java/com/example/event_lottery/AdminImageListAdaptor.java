package com.example.event_lottery;

import com.google.firebase.storage.StorageReference;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;


public class AdminImageListAdaptor extends ArrayAdapter<StorageReference> {
    private final Context context;
    private final List<StorageReference> imageReferences;

    public AdminImageListAdaptor(@NonNull Context context, @NonNull List<StorageReference> imageReferences) {
        super(context, 0, imageReferences);
        this.context = context;
        this.imageReferences = imageReferences;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_list_image_item, parent, false);
        }

        StorageReference imageRef = imageReferences.get(position);


        ImageView imageView = convertView.findViewById(R.id.image_view);
        Button deleteButton = convertView.findViewById(R.id.delete_button);

        // loading the image from the StorageReference into the ImageView using Glide
        imageRef.getDownloadUrl().addOnSuccessListener(uri ->
                Glide.with(context).load(uri).into(imageView)
        ).addOnFailureListener(e ->
                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
        );

        // setting the delete button functionality
        deleteButton.setOnClickListener(v -> {
            if (context instanceof AdminImageManagementActivity) {
                ((AdminImageManagementActivity) context).deleteImage(imageRef);
            } else {
                Toast.makeText(context, "Error deleting image", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
