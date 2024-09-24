package com.hackafun.foodliner;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hackafun.foodliner.interfaces.Api;
import com.hackafun.foodliner.models.GenerateFoodResponse;
import com.hackafun.foodliner.utils.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 102;

    private Bitmap imageBitmapStored;

    private ImageView imageView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Button buttonCapturePhoto = findViewById(R.id.buttonCapturePhoto);
        Button buttonUploadPhoto = findViewById(R.id.buttonUploadPhoto);
        Button buttonUseImage = findViewById(R.id.buttonUploadToServer);

        // Handle button click to capture photo
        buttonCapturePhoto.setOnClickListener(view -> {
            openCamera();
        });

        // Handle button click to upload photo from gallery
        buttonUploadPhoto.setOnClickListener(view -> {
            openGallery();
        });

        buttonUseImage.setOnClickListener(view -> {
            if (imageBitmapStored == null){

            } else {
                useImage(imageBitmapStored);
            }
        });
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_CAPTURE_CODE);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    // Assuming this method is part of your activity or a suitable class
    private void useImage(Bitmap imageBitmapStored) {
        try {
            // Step 1: Convert Bitmap to File
            File file = bitmapToFile(imageBitmapStored);

            // Step 2: Prepare the file part for Retrofit
            MultipartBody.Part imagePart = prepareFilePart(file);

            // Step 3: Call the API with the image file using RetrofitClient
            Api apiService = RetrofitClient.getInstance().getApi();
            Call<GenerateFoodResponse> call = apiService.generateFood(imagePart);

            call.enqueue(new Callback<GenerateFoodResponse>() {
                @Override
                public void onResponse(Call<GenerateFoodResponse> call, Response<GenerateFoodResponse> response) {
                    if (response.isSuccessful()) {
                        // Handle successful response
                        GenerateFoodResponse generateFoodResponse = response.body();
                        /* Do something with the response (e.g., display data) */
                    } else {
                        // Handle failure
                        System.out.println("Upload failed. Response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<GenerateFoodResponse> call, Throwable t) {
                    // Handle error
                    t.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }
    private File bitmapToFile(Bitmap bitmap) throws IOException {
        // Create a file in the cache directory
        File file = new File(getCacheDir(), "image_to_upload.jpg");
        file.createNewFile();

        // Convert bitmap to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();

        // Write the byte array to the file
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bitmapData);
        fileOutputStream.flush();
        fileOutputStream.close();

        return file;
    }

    private MultipartBody.Part prepareFilePart(File file) {
        // Create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("image", file.getName(), requestFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_CODE && resultCode == RESULT_OK) {
            // Get the captured image from the camera
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Set the captured image to ImageView
            imageView.setImageBitmap(imageBitmap);

            imageBitmapStored = (Bitmap) extras.get("data");

            // Save image to MediaStore
            saveImageToMediaStore(imageBitmap);

        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the selected image from the gallery
            Uri selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                try {
                    Bitmap selectedImageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                    imageView.setImageBitmap(selectedImageBitmap);
                    imageBitmapStored = selectedImageBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveImageToMediaStore(Bitmap bitmap) {
        OutputStream outputStream = null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "captured_image_" + System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/YourApp");

        Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        try {
            if (imageUri != null) {
                outputStream = getContentResolver().openOutputStream(imageUri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
