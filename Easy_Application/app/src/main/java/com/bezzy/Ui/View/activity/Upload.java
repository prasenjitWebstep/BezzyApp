package com.bezzy.Ui.View.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.bezzy.Ui.View.activity.Fragments.Live_ragment;
import com.bezzy.Ui.View.activity.Fragments.Photo_fragment;
import com.bezzy.Ui.View.activity.Fragments.Video_fragment;
import com.bezzy_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Upload extends AppCompatActivity {
    ImageView imageView;
    Button button;

    //Image request code
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        //Initializing views
//        imageView = findViewById(R.id.image_view);
//        button = findViewById(R.id.choose_image_button);
        BottomNavigationView btmnav = findViewById(R.id.navigation_upload);
        btmnav.setOnNavigationItemSelectedListener(navlistner);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_upload,new Photo_fragment()).commit();



//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
//                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
//                    requestPermissions(permissions,PERMISSION_CODE);
//                }else{
//                    pickImageFromGallery();
//                }
//            }
//        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch(item.getItemId()){
                case R.id.photo_upload:
                    fragment= new Photo_fragment();
                    break;
                case R.id.video:
                    fragment = new Video_fragment();
                    break;
                /*case R.id.live:
                    fragment = new Live_ragment();
                    break;*/
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_upload, fragment).commit();
            return true;
        }
    };




//    private void pickImageFromGallery(){
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent,IMAGE_PICK_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch(requestCode){
//            case PERMISSION_CODE:{
//                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                    pickImageFromGallery();
//                }else{
//                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
//                }
//                }
//            }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
//            imageView.setImageURI(data.getData());
//        }
//    }
}

