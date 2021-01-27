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
import android.widget.Toast;

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
import de.htw.berlin.steganography.steganography.image.exceptions.BitmapInaccuracyException;
import pl.droidsonroids.gif.GifImageView;

import static com.example.stegaexampleapp.UploadFileActivity.REQUEST_IMAGE_GET;

public class StegImageActivity extends AppCompatActivity {
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};

    Boolean accesingRawFile;
    ImageView rawImageView;
    ImageView encodedImageView;
    GifImageView rawGifImageView;
    GifImageView encodedGifImageView;
    Button choseRawFile;
    Button loadEncodedFileButton;
    Button computeImage;
    Button decodeImageButton;
    Button saveToStorageButton;
    TextView decodedTextView;
    EditText messageEditText;
    EditText fileName;
    Uri rawFileUri;
    Uri encodedFileUri;

    byte[] steganographyArray = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steg_image);
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);


        rawGifImageView = (GifImageView) findViewById(R.id.stegImageRawGifView);
        encodedGifImageView = (GifImageView) findViewById(R.id.stegImageEncodedGifView);

        loadEncodedFileButton = findViewById(R.id.stegImageLoadEncodedImageButtonId);
        loadEncodedFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accesingRawFile = false;
                selectImage();

            }
        });

        decodedTextView = (TextView) findViewById(R.id.stegImageDecodedTextId);

        decodeImageButton = (Button) findViewById(R.id.stegImageDecodeButtonId);
        decodeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(getUriMimType(rawFileUri).equals("png") || getUriMimType(encodedFileUri).equals("png")) {
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

        encodedImageView = (ImageView) findViewById(R.id.stegImageSteganographedImageView);

        messageEditText = (EditText) findViewById(R.id.stegImageEnterMessageEditText);

        computeImage = (Button) findViewById(R.id.stegImageComputeStegButton);
        computeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    steganographyArray =  computeSteganographyArrayFromByteArray(getUriMimType(rawFileUri), getBytesFromUri(rawFileUri), messageEditText.getText().toString().getBytes());
                    if(getUriMimType(rawFileUri).equals("png")) {
                        setImageViewFromByteArray(steganographyArray, encodedImageView);
                    }
                    if(getUriMimType(rawFileUri).equals("gif")) {
                        setImageViewFromByteArray(steganographyArray, encodedGifImageView);
                    }
                } catch (BitmapInaccuracyException e) {
                    e.printStackTrace();
                    Toast.makeText(getStegImageActivity(), "This image will not work due to Bitmap inaccuracies", Toast.LENGTH_LONG).show();
                }
            }
        });


        choseRawFile = (Button) findViewById(R.id.stegImageChoseFileButton);
        choseRawFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accesingRawFile = true;
                selectImage();

            }
        });

        fileName = (EditText) findViewById(R.id.stegImageEnterFileNameEditText);

        saveToStorageButton = (Button) findViewById(R.id.stegImageSaveToStorageButtonId);
        saveToStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getUriMimType(rawFileUri)!= null) {
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File encodedFile = new File(dir + File.separator + fileName.getText().toString()+"."+getUriMimType(rawFileUri));

                    //File encodedFile = new File(Environment.getExternalStorageDirectory() , fileName.getText().toString()+"."+getUriMimType(rawFileUri) );
                    boolean savedFile = writeToFile(encodedFile,steganographyArray);
                    Log.i("StegImageActivity writeToFile finished", "filepath: " + encodedFile.getAbsolutePath());
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if(savedFile){
                                Toast.makeText(getStegImageActivity(), "Saved File to "+ encodedFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getStegImageActivity(), "Saving File failed", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }
            }
        });
    }

    public void setImageViewFromByteArray(byte[] bytes, ImageView imageView){
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bmp);
    }

    private byte[] computeSteganographyArrayFromByteArray(String mimeType, byte[] inputData, byte[] message) throws BitmapInaccuracyException {
        try {
            if(mimeType.equals("png")) {
                Log.i("no stegano img size", String.valueOf(inputData.length));

                Steganography steganography = new ImageSteg();
                return steganography.encode(inputData, message);
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
        return null;
    }

    private byte[] getBytesFromUri(Uri uri) {
        try {
            InputStream iStream =   getContentResolver().openInputStream(uri);
            return getMyBytes(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        if(accesingRawFile){
            if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
                Bitmap thumbnail = data.getParcelableExtra("data");
                rawFileUri = data.getData();
                Log.i("raw Image Path", rawFileUri.getEncodedPath());
                Log.i("raw file type", getUriMimType(rawFileUri));
                if (getUriMimType(rawFileUri).equals("png")) {
                    rawImageView.setImageURI(rawFileUri);
                }
                if (getUriMimType(rawFileUri).equals("gif")) {
                    rawGifImageView.setImageURI(rawFileUri);
                }
            }
        }
        else{
            if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
                Bitmap thumbnail = data.getParcelableExtra("data");
                encodedFileUri = data.getData();
                Log.i("encoded Image Path", encodedFileUri.getEncodedPath());
                Log.i("encoded file type", getUriMimType(encodedFileUri));
                steganographyArray = getBytesFromUri(encodedFileUri);
                if (getUriMimType(encodedFileUri).equals("png")) {
                    encodedImageView.setImageURI(encodedFileUri);
                }
                if (getUriMimType(encodedFileUri).equals("gif")) {
                    encodedGifImageView.setImageURI(encodedFileUri);
                }
            }
        }

    }

    private void resetAllViewsAndArrays() {
        rawGifImageView.setImageResource(0);
        rawImageView.setImageResource(0);
        encodedImageView.setImageResource(0);
        rawFileUri = null;
        encodedFileUri = null;
        steganographyArray = null;
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

    private boolean writeToFile(File file,byte[] bytes){
        try {
            if( file.exists()){file.delete();}
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    public StegImageActivity getStegImageActivity(){
        return this;
    }
}