package com.example.stegaexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import de.htw.berlin.steganography.apis.MediaType;
import de.htw.berlin.steganography.apis.SocialMedia;
import de.htw.berlin.steganography.apis.imgur.Imgur;
import de.htw.berlin.steganography.apis.models.Token;
import de.htw.berlin.steganography.apis.reddit.Reddit;
import de.htw.berlin.steganography.apis.utils.BlobConverterImpl;
import de.htw.berlin.steganography.OAuthMainActivity;
import de.htw.berlin.steganography.steganography.Steganography;
import de.htw.berlin.steganography.steganography.exceptions.MediaCapacityException;
import de.htw.berlin.steganography.steganography.exceptions.MediaNotFoundException;
import de.htw.berlin.steganography.steganography.exceptions.MediaReassemblingException;
import de.htw.berlin.steganography.steganography.exceptions.UnknownStegFormatException;
import de.htw.berlin.steganography.steganography.exceptions.UnsupportedMediaTypeException;
import de.htw.berlin.steganography.steganography.image.ImageSteg;

public class UploadFileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_GET = 1;
    Button uploadFileBtn;
    Button choseFileUploadButton;
    EditText enterMessage;
    EditText enterSearchKeyword;
    ImageView imageView;
    String accesToken;
    String selectedNetworkString;
    TextView selectedNetworkTextView;
    Uri fullFileUri;
    byte[] steganographyArray;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.oauth_activity_main);
        setContentView(R.layout.activity_upload_file);

        progressBar = findViewById(R.id.uploadFileProgressBar);
        progressBar.setVisibility(View.GONE);

        selectedNetworkString = getIntent().getStringExtra("selectedNetwork");

        accesToken = getIntent().getStringExtra("accesToken");

        Log.i("UploadFile accesToken:",accesToken);

        selectedNetworkTextView = (TextView) findViewById(R.id.uploadFileSocialMediaTypeId);
        selectedNetworkTextView.setText(selectedNetworkString);
        Log.i("UploadFileActivity AccesToken", accesToken);
        Log.i("UploadFileActvity selectedNetowrk", selectedNetworkString);

        imageView = (ImageView) findViewById(R.id.uploadFileImageId);

        enterMessage = (EditText) findViewById(R.id.uploadFileEnterMessageEditTextId);

        enterSearchKeyword = (EditText) findViewById(R.id.uploadFileEnterSearchKeywordEditTextId);

        choseFileUploadButton = (Button) findViewById(R.id.uploadFileChoseFileButtonId);
        choseFileUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        uploadFileBtn = (Button) findViewById(R.id.uploadFileButtonId);
        uploadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Runnable run = new Runnable() {
                    @Override
                    public void run() {

                        if (steganographyArray!=null && fullFileUri != null && selectedNetworkString != null) {

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    progressBar.setVisibility(View.VISIBLE);

                                }
                            });



                            SocialMedia socialMedia = null;
                            if(selectedNetworkString.equals("reddit")) {
                                socialMedia = new Reddit();
                            }
                            if(selectedNetworkString.equals("imgur")){
                                socialMedia = new Imgur();
                            }
                            socialMedia.setToken(new Token(accesToken, 999999999));

                            byte[] steganographedBytes = null;
                            if(getUriMimType(fullFileUri).equals("png")) {
                                Steganography imageSteg = new ImageSteg();
                                try {
                                    steganographedBytes = imageSteg.encode(steganographyArray, enterMessage.getText().toString().getBytes());
                                    Log.i("steganographedBytes size", String.valueOf(steganographedBytes.length));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (MediaNotFoundException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedMediaTypeException e) {
                                    e.printStackTrace();
                                }
                                catch (MediaReassemblingException e) {
                                    e.printStackTrace();
                                } catch (MediaCapacityException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.i("UploadFile bytes", String.valueOf(steganographedBytes.length));
                            boolean succesfullUpload =  socialMedia.postToSocialNetwork(steganographedBytes, MediaType.valueOf(getUriMimType(fullFileUri).toUpperCase()), enterSearchKeyword.getText().toString());

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    progressBar.setVisibility(View.GONE);
                                    if(succesfullUpload){
                                        Toast.makeText(getUploadFileActivity(), "Succesfully uploaded", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(getUploadFileActivity(), "Upload failed", Toast.LENGTH_LONG).show();

                                    }

                                }
                            });




                        }
                    }
                };
                Thread t = new Thread(run);
                t.start();

            }
        });


    }

    private byte[] getByteArrayFromUri(Uri uri){
        try {
            InputStream iStream =   getContentResolver().openInputStream(uri);
            byte[] inputData = getMyBytesFromInputStream(iStream);
            return inputData;
            } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getMyBytesFromInputStream(InputStream iStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = iStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            Uri fullPhotoUri = data.getData();
            fullFileUri = fullPhotoUri;
            Log.i("Image Path", fullPhotoUri.getEncodedPath());
            imageView.setImageURI(fullPhotoUri);
            steganographyArray = getByteArrayFromUri(fullPhotoUri);


        }
    }

    private String getUriMimType(Uri uri){
        if(uri!=null) {
            ContentResolver cR = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cR.getType(uri));
        }
        else{
            return "no readable file";
        }
    }

    public UploadFileActivity getUploadFileActivity(){
        return this;
    }

}