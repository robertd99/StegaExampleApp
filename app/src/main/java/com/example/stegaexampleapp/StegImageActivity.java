package com.example.stegaexampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.htw.berlin.steganography.steganography.Steganography;
import de.htw.berlin.steganography.steganography.exceptions.MediaCapacityException;
import de.htw.berlin.steganography.steganography.exceptions.MediaNotFoundException;
import de.htw.berlin.steganography.steganography.exceptions.MediaReassemblingException;
import de.htw.berlin.steganography.steganography.exceptions.UnknownStegFormatException;
import de.htw.berlin.steganography.steganography.exceptions.UnsupportedMediaTypeException;
import de.htw.berlin.steganography.steganography.image.ImageSteg;
import pl.droidsonroids.gif.GifImageView;

import static com.example.stegaexampleapp.UploadFileActivity.REQUEST_IMAGE_GET;

public class StegImageActivity extends AppCompatActivity {
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{ "android.permission.WRITE_EXTERNAL_STORAGE"};

    ImageView rawImageView;
    ImageView steganoImageView;
    Button choseFile;
    Button computeImage;
    EditText messageEditText;
    Uri fullFileUri;
    Button saveToStorageButton;
    EditText fileName;
    GifImageView rawGifImageView;

    byte[] steganographyArray = null;

    Button decodeImageButton;

    TextView decodedTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steg_image);
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);


        rawGifImageView = (GifImageView) findViewById(R.id.stegImageRawGifView);



        decodedTextView = (TextView) findViewById(R.id.stegImageDecodedTextId);

        decodeImageButton = (Button) findViewById(R.id.stegImageDecodeButtonId);
        decodeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(getUriMimType(fullFileUri).equals("png")) {
                        Steganography imageSteg = new ImageSteg();
                        byte[] decodedResult = imageSteg.decode(steganographyArray);
                        decodedTextView.setText(new String(decodedResult));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MediaNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedMediaTypeException e) {
                    e.printStackTrace();
                } catch (UnknownStegFormatException e) {
                    e.printStackTrace();
                }

            }
        });

        rawImageView = (ImageView) findViewById(R.id.stegImageRawImageView);

        steganoImageView= (ImageView) findViewById(R.id.stegImageSteganographedImageView);

        messageEditText = (EditText) findViewById(R.id.stegImageEnterMessageEditText);

        computeImage = (Button) findViewById(R.id.stegImageComputeStegButton);
        computeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                computeStegImage(fullFileUri, messageEditText.getText().toString().getBytes());
            }
        });

        choseFile = (Button) findViewById(R.id.stegImageChoseFileButton);
        choseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        fileName = (EditText) findViewById(R.id.stegImageEnterFileNameEditText);

        saveToStorageButton = (Button) findViewById(R.id.stegImageSaveToStorageButtonId);
        saveToStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getUriMimType(fullFileUri)!= null) {
                    File encodedFile = new File(Environment.getExternalStorageDirectory() + "/" + fileName.getText().toString()+"."+getUriMimType(fullFileUri) );
                    writeToFile(encodedFile,steganographyArray);


                }
            }
        });
    }

    private void computeStegImage(Uri uri, byte[] message) {
        try {
            InputStream iStream =   getContentResolver().openInputStream(uri);
            byte[] inputData = getMyBytes(iStream);
            if(getUriMimType(fullFileUri).equals("png")) {
                Log.i("no stegano img size", String.valueOf(inputData.length));

                Steganography steganography = new ImageSteg();
                steganographyArray = steganography.encode(inputData, message);
                Bitmap bmp = BitmapFactory.decodeByteArray(steganographyArray, 0, steganographyArray.length);

                steganoImageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, steganoImageView.getWidth(), steganoImageView.getHeight(), false));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (MediaNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedMediaTypeException e) {
            e.printStackTrace();
        } catch (MediaCapacityException e) {
            e.printStackTrace();
        } catch (MediaReassemblingException e) {
            e.printStackTrace();
        }
    }

    private byte[] getMyBytes(InputStream iStream) throws IOException {
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
        resetAllViewsAndArrays();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            fullFileUri = data.getData();
            Log.i("Image Path", fullFileUri.getEncodedPath());
            Log.i("file type", getUriMimType(fullFileUri));
            if(getUriMimType(fullFileUri).equals("png")){
                rawImageView.setImageURI(fullFileUri);
            }
            if(getUriMimType(fullFileUri).equals("gif")){
                rawGifImageView.setImageURI(fullFileUri);
            }

        }
    }

    private void resetAllViewsAndArrays() {
        rawGifImageView.setImageResource(0);
        rawImageView.setImageResource(0);
        steganoImageView.setImageResource(0);
        fullFileUri = null;
        steganographyArray = null;
    }

    private String getUriMimType(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cR.getType(fullFileUri));
}

    private void writeToFile(File file,byte[] bytes){
        try {
            if( file.exists()){file.delete();}
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

}