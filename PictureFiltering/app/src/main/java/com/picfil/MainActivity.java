package com.picfil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Catalano.Imaging.Concurrent.Filters.Blur;
import Catalano.Imaging.Concurrent.Filters.Emboss;
import Catalano.Imaging.Concurrent.Filters.Grayscale;
import Catalano.Imaging.FastBitmap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_RESPONSE_CODE = 4783;
    private static final int PERMISSION_CAMERA_CODE = 3717;
    private static final int FILE_PICK_CODE = 2315;
    private static final int PERMISSION_STORAGE_CODE = 4215;

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
        ListView effects = (ListView)findViewById(R.id.listView1);
        ArrayList<String> effectsList = new ArrayList<String>();
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
                effectsList );

        effects.setAdapter(arrayAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
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
                Toast.makeText(this, "in here 2", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_CAMERA_CODE);
            }
        }
    }

    private void BlurIt(Bitmap bitmap) {
        FastBitmap image = new FastBitmap(bitmap);
        Grayscale blur  = new Grayscale();
        blur.applyInPlace(image);
        bitmap = image.toBitmap();
        imageView.setImageBitmap(bitmap);
        bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
    }
    private void startCamera() {
        new MaterialCamera(this)
                .stillShot()
                .start(CAMERA_RESPONSE_CODE);
    }

    @OnClick(R.id.saveToIntern)
    public void test() {
        BlurIt(bitmap);
    }
    @OnClick(R.id.importFromIntent)
    public void importClick(){
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
                    bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

                } else if (data != null) {
                    Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;

            case FILE_PICK_CODE:
                if(resultCode == RESULT_OK){
                    imageView.setImageURI(data.getData());
                    bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
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
}
