package com.picfil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import Catalano.Imaging.Concurrent.Filters.Blur;
import Catalano.Imaging.Concurrent.Filters.Closing;
import Catalano.Imaging.Concurrent.Filters.Desaturation;
import Catalano.Imaging.Concurrent.Filters.Dilatation;
import Catalano.Imaging.Concurrent.Filters.Erosion;
import Catalano.Imaging.Concurrent.Filters.Grayscale;
import Catalano.Imaging.Concurrent.Filters.Sharpen;
import Catalano.Imaging.FastBitmap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_RESPONSE_CODE = 4783;
    private static final int PERMISSION_CAMERA_CODE = 3717;
    private static final int FILE_PICK_CODE = 2315;
    private static final int PERMISSION_STORAGE_CODE = 4215;
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.imageView)
    ImageView imageView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        ListView effects = (ListView) findViewById(R.id.listView1);

        ArrayList<String> effectsList = new ArrayList<>();
        effectsList.add("Blur");
        effectsList.add("Closing");
        effectsList.add("Desaturation");
        effectsList.add("Dilatation");
        effectsList.add("Erosion");
        effectsList.add("Grayscale");
        effectsList.add("Sharpen");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                effectsList);

        effects.setAdapter(arrayAdapter);

        effects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                Toast.makeText(MainActivity.this, textView.getText().toString(), Toast.LENGTH_SHORT).show();

                FilterTheImage task = new FilterTheImage();
                task.execute(textView.getText().toString());

            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CAMERA)) {

                Toast.makeText(this, "in here", Toast.LENGTH_SHORT).show();
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Permissions needed")
                        .content("Permissions needed for using the camera")
                        .positiveText("OK")
                        .show();
            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_CAMERA_CODE);
            }
        }
    }

    private void sharpenIt(Bitmap bitmap) {
        FastBitmap image = new FastBitmap(bitmap);
        Sharpen sharpen = new Sharpen();
        sharpen.applyInPlace(image);
        bitmap = image.toBitmap();
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(finalBitmap);
            }
        });
    }

    private void grayScaleIt(Bitmap bitmap) {
        FastBitmap image = new FastBitmap(bitmap);
        Grayscale grayscale = new Grayscale();
        grayscale.applyInPlace(image);
        bitmap = image.toBitmap();
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(finalBitmap);
            }
        });

        bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

    }

    private void erodeIt(Bitmap bitmap) {
        FastBitmap image = new FastBitmap(bitmap);
        Erosion erosion = new Erosion();
        erosion.applyInPlace(image);
        bitmap = image.toBitmap();
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(finalBitmap);
            }
        });
    }

    private void dilateIt(Bitmap bitmap) {
        FastBitmap image = new FastBitmap(bitmap);
        Dilatation dilatation = new Dilatation();
        dilatation.applyInPlace(image);
        bitmap = image.toBitmap();
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(finalBitmap);
            }
        });
    }

    private void desaturatedIt(Bitmap bitmap) {
        FastBitmap image = new FastBitmap(bitmap);
        Desaturation desaturation = new Desaturation();
        desaturation.applyInPlace(image);
        bitmap = image.toBitmap();
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(finalBitmap);
            }
        });
    }

    private void closingIt(Bitmap bitmap) {
        FastBitmap image = new FastBitmap(bitmap);
        Closing closing = new Closing();
        closing.applyInPlace(image);
        bitmap = image.toBitmap();
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(finalBitmap);
            }
        });
    }

    private void blurIt(Bitmap bitmap) {
        FastBitmap image = new FastBitmap(bitmap);
        Blur blur = new Blur();
        blur.applyInPlace(image);
        bitmap = image.toBitmap();
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(finalBitmap);
            }
        });
    }

    private void saveBitmap(String filename) {
        String extStorageDirectory = getFilesDir().getAbsolutePath();//Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;

        File file = new File(filename + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, filename + ".png");
            Log.e("file exist", "" + file + ",Bitmap= " + filename);
        }
        try {
            // make a new bitmap from your file
            Bitmap bitmap = BitmapFactory.decodeFile(file.getName());

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCamera() {
        new MaterialCamera(this)
                .stillShot()
                .start(CAMERA_RESPONSE_CODE);
    }

    @OnClick(R.id.saveToIntern)
    public void saveClick() {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(getFilesDir().getAbsolutePath());
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
/* Write bitmap to file using JPEG or PNG and 80% quality hint for JPEG. */
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);


        String fileName = "saved" + new Date().getTime();
        saveBitmap(fileName);
    }

    @OnClick(R.id.importFromIntent)
    public void importClick() {
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            filePicker();
        } else {
            requestPermission();
        }
    }

    private boolean grantedPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        Toast.makeText(this, "code: " + permissionCheck, Toast.LENGTH_SHORT).show();
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA_RESPONSE_CODE:
                if (resultCode == RESULT_OK) {
                    imageView.setImageURI(data.getData());
                    bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                } else if (data != null) {
                    Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;

            case FILE_PICK_CODE:
                if (resultCode == RESULT_OK) {
                    imageView.setImageURI(data.getData());
                    bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(this, "Okey :( no camera for you", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_CAMERA_CODE);
                }
                break;
            case PERMISSION_STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_STORAGE_CODE);
                }
                break;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_STORAGE_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void filePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, FILE_PICK_CODE);
    }

    private class FilterTheImage extends AsyncTask<String, Void, Void> {

        private MaterialDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new MaterialDialog.Builder(MainActivity.this)
                    .title("Filtering image!")
                    .content("Please wait ...")

                    .progress(true, 0)
                    .show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(String... params) {

            for (String str : params) {
                Log.d(TAG, "Async param: " + str);
            }

            switch (params[0]) {
                case "Blur":
                    blurIt(bitmap);
                    break;
                case "Closing":
                    closingIt(bitmap);
                    break;
                case "Desaturation":
                    desaturatedIt(bitmap);
                    break;
                case "Dilatation":
                    dilateIt(bitmap);
                    break;
                case "Erosion":
                    erodeIt(bitmap);
                    break;
                case "Grayscale":
                    grayScaleIt(bitmap);
                    break;
                case "Sharpen":
                    sharpenIt(bitmap);
                    break;
            }

            return null;
        }
    }

}
