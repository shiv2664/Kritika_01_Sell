package com.shivam.kritika_01_sell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.firestore.v1.DocumentTransform;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Upload_New_Product extends AppCompatActivity {


    Spinner gender_spinner, product_spinner, product_name_spinner;
    ProgressBar progressBar;
    ImageView image_profile, image_profile2, image_profile3, image_profile4,ColourChooser;
    CardView ColourCard1,ColourCard2,ColourCard3,ColourCard4,ColourCard5,ColourCard6;
    MaterialEditText Product_Name_EditText,Brand_EditText,Colour_EditText, ShopAddressEditText,
            CityNameEditText,SizeEditText,PrizeEditText,LocationEditText,PdDescriptionEditText,ShopNameEdiText;
    FloatingActionButton floatingActionButton;
    TextView ShopLocationMAP;
    Toolbar toolbar;

    FirebaseUser fuser;

    StorageReference storageReference;
    StorageReference fileReference;
    StorageReference fileReference2;
    StorageReference fileReference3;
    StorageReference fileReference4;
    StorageReference ThumbStorageReference;


    private StorageTask uploadTask;
    private StorageTask uploadTask2;
    private StorageTask uploadTask3;
    private StorageTask uploadTask4;
    private FirebaseFirestore db;
    private DocumentReference ProductRef;

    private FusedLocationProviderClient mLocationClient;

    private static final int MAPS_ACTIVITY_REQEST=1001;
    private static final int SECOND_ACTIVITY_REQUEST_CODE=1002;
    private static final int PICK_IMAGE_REQUEST = 10;
    private static final int PICK_IMAGE_REQUEST2 = 20;
    private static final int PICK_IMAGE_REQUEST3 = 30;
    private static final int PICK_IMAGE_REQUEST4 = 40;
    public static final int GPS_REQUEST_CODE = 9003;
    private int Storage_Permission_Req = 1;
    public static final int PERMISSION_REQUEST_CODE = 9001;
    private static final int PLAY_SERVICES_ERROR_CODE = 9002;
    Boolean T1=false,T2=false,T3=false,T4=false,T5=false;
    Uri ImageUri;
    Uri ImageUri2;
    Uri ImageUri3;
    Uri ImageUri4;
    Uri resultUri;
    Uri resultUri2;
    Uri resultUri3;
    Uri resultUri4;
    Bitmap bitmap;
    Bitmap bitmap2;
    Bitmap bitmap3;
    Bitmap bitmap4;
    Bitmap resized;
    Bitmap resized2;
    Bitmap resized3;
    Bitmap resized4;
    String TAG = "MyTag";
    private static String ProductID;
    String Uid;
    String ProductName;
    String CityName;
    String Colour;
    String Size;
    String Brand;
    String Prize;
    String ShopAddress;
    String GenderSpinner;
    String CategorySpinner;
    String PdDescription;
    String ShopName;
    String ProductSpinner;
    String Locality;
    String SubLocality;
    String MyLat;
    String MyLong;
    String LatBack="0.0";
    String LongBack="0.0";
    String Colour1="";
    String Colour2="";
    String Colour3="";
    String Colour4="";
    String Colour5="";
    String Colour6="";

    Double LatitudeBack=0.0;
    Double LongitudeBack=0.0;
    private String CityNameBack;
    private String ShopAddressBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__new__product);

        mLocationClient = new FusedLocationProviderClient(this);

        toolbar = findViewById(R.id.myToolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload");

        image_profile = findViewById(R.id.profile_image);
        image_profile2 = findViewById(R.id.profile_image2);
        image_profile3 = findViewById(R.id.profile_image3);
        image_profile4 = findViewById(R.id.profile_image4);
        //ColourChooser=findViewById(R.id.ColourChooser);


        ColourCard1=findViewById(R.id.ColourCard1);
        ColourCard2=findViewById(R.id.ColourCard2);
        ColourCard3=findViewById(R.id.ColourCard3);
        ColourCard4=findViewById(R.id.ColourCard4);
        ColourCard5=findViewById(R.id.ColourCard5);
        ColourCard6=findViewById(R.id.ColourCard6);



        ShopLocationMAP=findViewById(R.id.ShopLocationMAP);

        Product_Name_EditText = findViewById(R.id.Product_Name);
        Brand_EditText = findViewById(R.id.Brand);
        ShopAddressEditText = findViewById(R.id.ShopAddress);
        CityNameEditText = findViewById(R.id.CityName);
        SizeEditText=findViewById(R.id.Size);
        PrizeEditText=findViewById(R.id.Prize);
        ShopNameEdiText=findViewById(R.id.ShopName);
        LocationEditText=findViewById(R.id.Location);
        PdDescriptionEditText=findViewById(R.id.PdDescription);

        progressBar=findViewById(R.id.progressBar);

        floatingActionButton = findViewById(R.id.saveBtn);

        gender_spinner = findViewById(R.id.Gender_Spinner);
        product_spinner = findViewById(R.id.Product_Spinner);
        product_name_spinner = findViewById(R.id.Product_Name_Spinner);

        CityNameEditText.setText(CityNameBack);
        ShopAddressEditText.setText(ShopAddressBack);




        InitAdapters();
        ImageOnclick();
        CardOnClick();
        CardOnLongClick();
        SetVisibilities();



        ShopLocationMAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent intent = new Intent(Upload_New_Product.this, MapsActivity.class);
                        startActivity(intent);


                //Intent intent = new Intent(Upload_New_Product.this, MapsActivity.class);
                //startActivityForResult(intent, MAPS_ACTIVITY_REQUEST);

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getCurrentLocation();


                /*
                if ((!Colour6.equals("") &&!Colour5.equals("") &&!Colour4.equals("")&&!Colour3.equals("")&&!Colour2.equals("")&&!Colour1.equals(""))){

                    Colour=Colour1+","+Colour2+","+Colour3+","+Colour4+","+Colour5+","+Colour6;
                }else if (Colour6.equals("") && Colour5.equals("") && Colour4.equals("")&&Colour3.equals("")&&Colour2.equals("")){

                    Colour=Colour1;
                }else if (Colour6.equals("") && Colour5.equals("") && Colour4.equals("")&&Colour3.equals("")){

                    Colour=Colour1+","+Colour2;
                }else if (Colour6.equals("") && Colour5.equals("") && Colour4.equals("")){

                    Colour=Colour1+","+Colour2+","+Colour3;
                }else if (Colour6.equals("") && Colour5.equals("")){

                    Colour=Colour1+","+Colour2+","+Colour3+","+Colour4;
                }else if (Colour6.equals("")) {

                    Colour = Colour1 + "," + Colour2 + "," + Colour3 + "," + Colour4 + "+" + Colour5;
                }

                Log.d(TAG,"Colour is  :  "+Colour);

                 */



                 initGoogleMap();


                // geoCoder(28.630597, 77.218978);
                //  UploadingImage();
                //  UploadingThumbnailImage();
               // UploadDetails();
            }
        });


        fuser = FirebaseAuth.getInstance().getCurrentUser();

        assert fuser != null;
        Uid = fuser.getUid();

        db = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("Product Images").child(Uid);
        ThumbStorageReference = FirebaseStorage.getInstance().getReference("Thumbnail Images").child(Uid);

    }



    private void InitAdapters() {


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(adapter);

        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // GenderTextView.setText(parent.getItemAtPosition(position).toString());
                GenderSpinner = parent.getItemAtPosition(position).toString();

                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "Men":
                        ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeMen, android.R.layout.simple_spinner_item);
                        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        product_spinner.setAdapter(arrayAdapter2);

                        CategorySpinner = parent.getItemAtPosition(position).toString();

                        product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String selectedItem2 = parent.getItemAtPosition(position).toString();
                                switch (selectedItem2) {
                                    case "Shoes": {
                                        ArrayAdapter<CharSequence> arrayAdapter3 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeMenShoes, android.R.layout.simple_spinner_item);
                                        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter3);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();
                                        break;
                                    }
                                    case "Clothing": {
                                        ArrayAdapter<CharSequence> arrayAdapter3 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeMenClothing, android.R.layout.simple_spinner_item);
                                        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter3);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();
                                        break;
                                    }
                                    case "Accessories": {
                                        ArrayAdapter<CharSequence> arrayAdapter3 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeMenAccessories, android.R.layout.simple_spinner_item);
                                        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter3);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();

                                        break;
                                    }
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        break;
                    case "Women":
                        ArrayAdapter<CharSequence> arrayAdapter3 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeWomen, android.R.layout.simple_spinner_item);
                        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        product_spinner.setAdapter(arrayAdapter3);

                        CategorySpinner = parent.getItemAtPosition(position).toString();

                        product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String selectedItem3 = parent.getItemAtPosition(position).toString();
                                switch (selectedItem3) {
                                    case "Shoes": {
                                        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeWomenShoes, android.R.layout.simple_spinner_item);
                                        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter4);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();
                                        break;
                                    }
                                    case "Clothing": {
                                        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeWomenClothing, android.R.layout.simple_spinner_item);
                                        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter4);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();
                                        break;
                                    }
                                    case "Indian and Fusion Wear": {
                                        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeWomenIndianEthnic, android.R.layout.simple_spinner_item);
                                        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter4);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();
                                        break;
                                    }
                                    case "Accessories": {
                                        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeWomenAccessories, android.R.layout.simple_spinner_item);
                                        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter4);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();
                                        break;
                                    }
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        break;
                    case "Kids":

                        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeKids, android.R.layout.simple_spinner_item);
                        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        product_spinner.setAdapter(arrayAdapter4);

                        CategorySpinner = parent.getItemAtPosition(position).toString();

                        product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String selectedItem4 = parent.getItemAtPosition(position).toString();

                                switch (selectedItem4) {
                                    case "Boys Clothing": {
                                        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeBoysClothing, android.R.layout.simple_spinner_item);
                                        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter4);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();
                                        break;
                                    }
                                    case "Girls Clothing": {
                                        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeGirlsClothing, android.R.layout.simple_spinner_item);
                                        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter4);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();
                                        break;
                                    }
                                    case "Boys Footwear": {
                                        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeBoysFootwear, android.R.layout.simple_spinner_item);
                                        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter4);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();
                                        break;
                                    }
                                    case "Girls Footwear": {
                                        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeGirlsFootwear, android.R.layout.simple_spinner_item);
                                        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter4);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();
                                        break;
                                    }
                                    case "Infants": {
                                        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeInfants, android.R.layout.simple_spinner_item);
                                        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter4);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();
                                        break;
                                    }
                                    case "Kids Accessories": {
                                        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeKidsAccessories, android.R.layout.simple_spinner_item);
                                        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        product_name_spinner.setAdapter(arrayAdapter4);
                                        ProductSpinner = parent.getItemAtPosition(position).toString();
                                        break;
                                    }
                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }

        });


    }

    private void ImageOnclick() {

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Upload_New_Product.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openFileChooser();
                } else {
                    requestStoragePermission();
                }
            }
        });

        image_profile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Upload_New_Product.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openFileChooser2();
                } else {
                    requestStoragePermission();
                }

            }
        });

        image_profile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Upload_New_Product.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openFileChooser3();
                } else {
                    requestStoragePermission();
                }
            }
        });

        image_profile4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Upload_New_Product.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openFileChooser4();
                } else {
                    requestStoragePermission();
                }

            }
        });


    }

    private void SetVisibilities() {

        if (!Colour1.equals("")){
            ColourCard2.setVisibility(View.VISIBLE);
        }
        if (!Colour1.equals("")&&!Colour2.equals("")){
            ColourCard3.setVisibility(View.VISIBLE);
        }
        if (!Colour1.equals("")&&!Colour2.equals("")&&!Colour3.equals("")){
            ColourCard4.setVisibility(View.VISIBLE);
        }
        if (!Colour1.equals("")&&!Colour2.equals("")&&!Colour3.equals("")&&!Colour4.equals("")){
            ColourCard5.setVisibility(View.VISIBLE);
        }
        if (!Colour1.equals("")&&!Colour2.equals("")&&!Colour3.equals("")&&!Colour4.equals("")&&!Colour5.equals("")){
            ColourCard6.setVisibility(View.VISIBLE);
        }
    }

    private void CardOnClick() {

        ColourCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColorPickerDialogBuilder
                        .with(Upload_New_Product.this)
                        .setTitle("Choose color")
                        .initialColor(Color.parseColor("#ffffff"))
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                //  Toast.makeText(Upload_New_Product.this,"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            //    Toast.makeText(Upload_New_Product.this,
                            //            "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            //    Log.d(TAG,"Hex Value : "+selectedColor);
                                // root.setBackgroundColor(getResources().getColor(color.white));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                // ColourImg1.setBackground(selectedColor);
                            //    Log.d(TAG,"Selected Colour : "+selectedColor);

                              //  Colour1= Integer.toHexString(selectedColor);
                                Colour1=String.valueOf(selectedColor);
                                Log.d(TAG,"Selected Colour : "+Colour1);
                                ColourCard1.setCardBackgroundColor(selectedColor);
                                SetVisibilities();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();

            }
        });

        ColourCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColorPickerDialogBuilder
                        .with(Upload_New_Product.this)
                        .setTitle("Choose color")
                        .initialColor(Color.parseColor("#ffffff"))
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                //  Toast.makeText(Upload_New_Product.this,"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            //    Toast.makeText(Upload_New_Product.this,
                            //            "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            //    Log.d(TAG,"Hex Value : "+selectedColor);
                                // root.setBackgroundColor(getResources().getColor(color.white));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                // ColourImg1.setBackground(selectedColor);
                            //    Log.d(TAG,"Selected Colour : "+selectedColor);
                                ColourCard2.setCardBackgroundColor(selectedColor);
                                Colour2=String.valueOf(selectedColor);
                                SetVisibilities();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();

            }
        });

        ColourCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColorPickerDialogBuilder
                        .with(Upload_New_Product.this)
                        .setTitle("Choose color")
                        .initialColor(Color.parseColor("#ffffff"))
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                //  Toast.makeText(Upload_New_Product.this,"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            //    Toast.makeText(Upload_New_Product.this,
                            //            "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            //    Log.d(TAG,"Hex Value : "+selectedColor);
                                // root.setBackgroundColor(getResources().getColor(color.white));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                // ColourImg1.setBackground(selectedColor);
                             //   Log.d(TAG,"Selected Colour : "+selectedColor);
                                ColourCard3.setCardBackgroundColor(selectedColor);
                                Colour3=String.valueOf(selectedColor);
                                SetVisibilities();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();

            }
        });

        ColourCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColorPickerDialogBuilder
                        .with(Upload_New_Product.this)
                        .setTitle("Choose color")
                        .initialColor(Color.parseColor("#ffffff"))
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                //  Toast.makeText(Upload_New_Product.this,"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            //    Toast.makeText(Upload_New_Product.this,
                            //            "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            //    Log.d(TAG,"Hex Value : "+selectedColor);
                                // root.setBackgroundColor(getResources().getColor(color.white));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                // ColourImg1.setBackground(selectedColor);
                             //   Log.d(TAG,"Selected Colour : "+selectedColor);
                                ColourCard4.setCardBackgroundColor(selectedColor);
                                Colour4=String.valueOf(selectedColor);
                                SetVisibilities();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();

            }
        });

        ColourCard5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColorPickerDialogBuilder
                        .with(Upload_New_Product.this)
                        .setTitle("Choose color")
                        .initialColor(Color.parseColor("#ffffff"))
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                //  Toast.makeText(Upload_New_Product.this,"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                              //  Toast.makeText(Upload_New_Product.this,
                              //          "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                              //  Log.d(TAG,"Hex Value : "+selectedColor);
                                // root.setBackgroundColor(getResources().getColor(color.white));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                // ColourImg1.setBackground(selectedColor);
                              //  Log.d(TAG,"Selected Colour : "+selectedColor);
                                ColourCard5.setCardBackgroundColor(selectedColor);
                                Colour5=String.valueOf(selectedColor);
                                SetVisibilities();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();

            }
        });

        ColourCard6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColorPickerDialogBuilder
                        .with(Upload_New_Product.this)
                        .setTitle("Choose color")
                        .initialColor(Color.parseColor("#ffffff"))
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                //  Toast.makeText(Upload_New_Product.this,"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                               // Toast.makeText(Upload_New_Product.this,
                               //         "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                                // root.setBackgroundColor(getResources().getColor(color.white));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                // ColourImg1.setBackground(selectedColor);
                                ColourCard6.setCardBackgroundColor(selectedColor);
                                Colour6=String.valueOf(selectedColor);
                                SetVisibilities();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();

            }
        });


    }

    private void CardOnLongClick(){
        ColourCard1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Upload_New_Product.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure selected colour will be removed.")
                        .setPositiveButton("OK", ((dialogInterface, i) -> {
                            if (Colour2.equals("")&&Colour3.equals("")&&Colour4.equals("")&&Colour5.equals("")&&Colour6.equals("")){
                                Colour1="";
                                ColourCard1.setCardBackgroundColor(Color.parseColor("#e6e6e6"));
                                ColourCard2.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(Upload_New_Product.this, "Can't remove the selected colour", Toast.LENGTH_SHORT).show();
                            }
                        }))
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }})
                        .setCancelable(true)
                        .show();

                return true;
            }
        });

        ColourCard2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Upload_New_Product.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure selected colour will be removed.")
                        .setPositiveButton("OK", ((dialogInterface, i) -> {
                            if (Colour3.equals("")&&Colour4.equals("")&&Colour5.equals("")&&Colour6.equals("")){
                                Colour2="";
                                ColourCard2.setCardBackgroundColor(Color.parseColor("#e6e6e6"));
                                ColourCard3.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(Upload_New_Product.this, "Can't remove the selected colour", Toast.LENGTH_SHORT).show();
                            }
                        }))
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }})
                        .setCancelable(true)
                        .show();

                return true;
            }
        });

        ColourCard3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Upload_New_Product.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure selected colour will be removed.")
                        .setPositiveButton("OK", ((dialogInterface, i) -> {
                            if (Colour4.equals("")&&Colour5.equals("")&&Colour6.equals("")){
                                Colour3="";
                                ColourCard3.setCardBackgroundColor(Color.parseColor("#e6e6e6"));
                                ColourCard4.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(Upload_New_Product.this, "Can't remove the selected colour", Toast.LENGTH_SHORT).show();
                            }
                        }))
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }})
                        .setCancelable(true)
                        .show();

                return true;
            }
        });

        ColourCard4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Upload_New_Product.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure selected colour will be removed.")
                        .setPositiveButton("OK", ((dialogInterface, i) -> {
                            if (Colour5.equals("")&&Colour6.equals("")){
                                Colour4="";
                                ColourCard4.setCardBackgroundColor(Color.parseColor("#e6e6e6"));
                                ColourCard5.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(Upload_New_Product.this, "Can't remove the selected colour", Toast.LENGTH_SHORT).show();
                            }
                        }))
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }})
                        .setCancelable(true)
                        .show();

                return true;
            }
        });

        ColourCard5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Upload_New_Product.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure selected colour will be removed.")
                        .setPositiveButton("OK", ((dialogInterface, i) -> {
                            if (Colour6.equals("")){
                                Colour5="";
                                ColourCard5.setCardBackgroundColor(Color.parseColor("#e6e6e6"));
                                ColourCard6.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(Upload_New_Product.this, "Can't remove the selected colour", Toast.LENGTH_SHORT).show();
                            }
                        }))
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }})
                        .setCancelable(true)
                        .show();

                return true;
            }
        });

        ColourCard6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Upload_New_Product.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure selected colour will be removed.")
                        .setPositiveButton("OK", ((dialogInterface, i) -> {
                                Colour6="";
                                ColourCard6.setCardBackgroundColor(Color.parseColor("#e6e6e6"));
                               // ColourCard6.setVisibility(View.GONE);

                        }))
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }})
                        .setCancelable(true)
                        .show();

                return true;
            }
        });


    }

    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);


    }

    private void openFileChooser2() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST2);

    }

    private void openFileChooser3() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST3);
    }

    private void openFileChooser4() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST4);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();

            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), ImageUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Use older version
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            setImage_profile();
        }


        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK && data != null) {
            ImageUri2 = data.getData();

            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    bitmap2 = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), ImageUri2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Use older version
                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageUri2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            setImage_profile2();
        }


        if (requestCode == PICK_IMAGE_REQUEST3 && resultCode == RESULT_OK && data != null) {
            ImageUri3 = data.getData();

            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    bitmap3 = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), ImageUri3));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Use older version
                try {
                    bitmap3 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageUri3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            setImage_profile3();
        }


        if (requestCode == PICK_IMAGE_REQUEST4 && resultCode == RESULT_OK && data != null) {
            ImageUri4 = data.getData();

            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    bitmap4 = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), ImageUri4));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Use older version
                try {
                    bitmap4 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageUri4);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            setImage_profile4();

        }

        /*
        if (requestCode == MAPS_ACTIVITY_REQUEST) {
            if(resultCode == RESULT_OK) {

                Intent intent=getIntent();
                LatitudeBack= intent.getDoubleExtra("Latitude",0.0);
                LongitudeBack=intent.getDoubleExtra("Longitude",0.0);

                Log.d(TAG,"Location Back : "+LatitudeBack+","+LongitudeBack);
                LocationEditText.setText(LatitudeBack+","+LongitudeBack);
            }
        }

         */




        if (requestCode == GPS_REQUEST_CODE) {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (providerEnabled) {
                Toast.makeText(this, "GPS is enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "GPS not enabled. Unable to show user location", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void UploadingImage() {



        ProductName = Product_Name_EditText.getText().toString();
        CityName = CityNameEditText.getText().toString();
        ShopAddress=ShopAddressEditText.getText().toString();
     //   Colour= Colour_EditText.getText().toString();
        Brand=Brand_EditText.getText().toString();
        Prize=PrizeEditText.getText().toString();
        Size=SizeEditText.getText().toString().trim();

        if (bitmap != null && bitmap2 != null && bitmap3 != null && bitmap4 != null && !ProductName.equals("") &&
                !Brand.equals("") && !ShopAddress.equals("") && !CityName.equals("")&& !Size.equals("")&&!Prize.equals("")) {
            resized = Bitmap.createScaledBitmap(bitmap, 800, 1000, true);
            resized2 = Bitmap.createScaledBitmap(bitmap2, 800, 1000, true);
            resized3 = Bitmap.createScaledBitmap(bitmap3, 800, 1000, true);
            resized4 = Bitmap.createScaledBitmap(bitmap4, 800, 1000, true);
            // image_profile.setImageBitmap(resized);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
            ByteArrayOutputStream baos4 = new ByteArrayOutputStream();

            resized.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            resized2.compress(Bitmap.CompressFormat.JPEG, 80, baos2);
            resized3.compress(Bitmap.CompressFormat.JPEG, 80, baos3);
            resized4.compress(Bitmap.CompressFormat.JPEG, 80, baos4);

            byte[] uploadbaos = baos.toByteArray();
            byte[] uploadbaos2 = baos2.toByteArray();
            byte[] uploadbaos3 = baos3.toByteArray();
            byte[] uploadbaos4 = baos4.toByteArray();


            fuser = FirebaseAuth.getInstance().getCurrentUser();
            ProductName = Objects.requireNonNull(Product_Name_EditText.getText()).toString();
            CityName = Objects.requireNonNull(CityNameEditText.getText()).toString();

            ProductRef = db.collection("Sellers").document(CityName).collection(CityName).document(ProductID);

            fileReference = storageReference.child(ProductID).child( "1"  + ProductID + ".jpg");
            fileReference2 = storageReference.child(ProductID).child("2" + ProductID + ".jpg");
            fileReference3 = storageReference.child(ProductID).child("3" + ProductID + ".jpg");
            fileReference4 = storageReference.child(ProductID).child("4" + ProductID + ".jpg");

            uploadTask = fileReference.putBytes(uploadbaos);
            uploadTask2 = fileReference2.putBytes(uploadbaos2);
            uploadTask3 = fileReference3.putBytes(uploadbaos3);
            uploadTask4 = fileReference4.putBytes(uploadbaos4);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    } else if (task.isSuccessful()) {


                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadUri = task.getResult();
                        assert downloadUri != null;
                        String mUri = downloadUri.toString();


                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);

                        ProductRef.set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                T1=true;
                                CheckStatus();
                            }
                        });


                        //pd.dismiss();
                    } else {
                        Toast.makeText(Upload_New_Product.this, "Failed!", Toast.LENGTH_SHORT).show();
                        //pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Upload_New_Product.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    //pd.dismiss();
                }
            });



            uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    } else if (task.isSuccessful()) {


                    }

                    return fileReference2.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadUri = task.getResult();
                        assert downloadUri != null;
                        String mUri = downloadUri.toString();

                        ProductName = Product_Name_EditText.getText().toString();


                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL2", mUri);

                        ProductRef.set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                T2=true;
                                CheckStatus();
                            }
                        });


                        //pd.dismiss();
                    } else {
                        Toast.makeText(Upload_New_Product.this, "Failed!", Toast.LENGTH_SHORT).show();
                        //pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Upload_New_Product.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    //pd.dismiss();
                }
            });


            uploadTask3.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    } else if (task.isSuccessful()) {

                    }

                    return fileReference3.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        ProductName = Product_Name_EditText.getText().toString();


                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL3", mUri);

                        ProductRef.set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                T3=true;
                                CheckStatus();
                            }
                        });


                        //pd.dismiss();
                    } else {
                        Toast.makeText(Upload_New_Product.this, "Failed!", Toast.LENGTH_SHORT).show();
                        //pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Upload_New_Product.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    //pd.dismiss();
                }
            });


            uploadTask4.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    } else if (task.isSuccessful()) {


                    }

                    return fileReference4.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadUri = task.getResult();
                        assert downloadUri != null;
                        String mUri = downloadUri.toString();


                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL4", mUri);

                        ProductRef.set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                T4=true;
                                CheckStatus();
                            }
                        });


                        //pd.dismiss();
                    } else {
                        Toast.makeText(Upload_New_Product.this, "Failed!", Toast.LENGTH_SHORT).show();
                        //pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Upload_New_Product.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    //pd.dismiss();
                }
            });

        } else {
            Toast.makeText(this, "Please Upload the required images", Toast.LENGTH_SHORT).show();

        }


    }

    /*
    public void uploadFileAtIndex(int i,Bitmap bitmapUp) {

        StorageTask arrayUpload;
        ByteArrayOutputStream baosArray = new ByteArrayOutputStream();
        Bitmap bitresized;
        bitresized = Bitmap.createScaledBitmap(bitmapUp, 800, 800, true);
        bitresized.compress(Bitmap.CompressFormat.JPEG, 70, baosArray);
        fileReference = storageReference.child(ProductName).child(i + ProductName + ".jpg");


        byte[] uploadbaosarray = baosArray.toByteArray();
        arrayUpload = fileReference.putBytes(uploadbaosarray);

        arrayUpload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                } else if (task.isSuccessful()) {
                    Toast.makeText(Upload_New_Product.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                }

                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    assert downloadUri != null;
                    String mUri = downloadUri.toString();

                    ProductName = Product_Name_EditText.getText().toString();
                    ProductRef = db.collection("Sellers").document(CityName).collection(Uid).document(ProductName);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("imageURL" + i, mUri);
                    ProductRef.set(map, SetOptions.merge());
                } else {
                    Toast.makeText(Upload_New_Product.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Upload_New_Product.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

     */



    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(Upload_New_Product.this, new String[]
                {Manifest.permission.READ_EXTERNAL_STORAGE}, Storage_Permission_Req);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Storage_Permission_Req) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Upload_New_Product.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                openFileChooser();
            } else {
                Toast.makeText(Upload_New_Product.this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void UploadingThumbnailImage() {

        ProductName = Product_Name_EditText.getText().toString();
        CityName = CityNameEditText.getText().toString();
        ShopAddress=ShopAddressEditText.getText().toString();
      //  Colour= Colour_EditText.getText().toString();
        Brand=Brand_EditText.getText().toString();
        Prize=PrizeEditText.getText().toString();
        Size=SizeEditText.getText().toString().trim();

        if (bitmap != null && bitmap2 != null && bitmap3 != null && bitmap4 != null && !ProductName.equals("") &&
                !Brand.equals("") && !ShopAddress.equals("") && !CityName.equals("")&& !Size.equals("")&&!Prize.equals("")) {

            resized = Bitmap.createScaledBitmap(bitmap, 400, 500, true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] uploadbaos = baos.toByteArray();


            fuser = FirebaseAuth.getInstance().getCurrentUser();
            ProductName = Objects.requireNonNull(Product_Name_EditText.getText()).toString();
            CityName = Objects.requireNonNull(CityNameEditText.getText()).toString();
            //reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

            final StorageReference fileReference = ThumbStorageReference.child(ProductID + ".jpg");

            uploadTask = fileReference.putBytes(uploadbaos);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    } else if (task.isSuccessful()) {

                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                      ProductRef = db.collection("Sellers").document(CityName).collection(CityName).document(ProductID);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("ThumbnailUrl", "" + mUri);

                        ProductRef.set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                T5=true;
                                CheckStatus();
                            }
                        });

                        //pd.dismiss();
                    } else {
                        Toast.makeText(Upload_New_Product.this, "Failed!", Toast.LENGTH_SHORT).show();
                        //pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Upload_New_Product.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    //pd.dismiss();
                }
            });

        }

    }

    private void UploadDetails() {

        ProductName = Product_Name_EditText.getText().toString();
        CityName = CityNameEditText.getText().toString();
        ShopAddress = ShopAddressEditText.getText().toString();
      //  Colour = Colour_EditText.getText().toString();
        Brand = Brand_EditText.getText().toString();
        Size = SizeEditText.getText().toString().trim();
        ShopName = ShopNameEdiText.getText().toString();
        Prize = PrizeEditText.getText().toString();
        PdDescription = PdDescriptionEditText.getText().toString();


        /*
        if (Colour6.equals("")){
            Colour=Colour1+","+Colour2+","+Colour3+","+Colour4+"+"+Colour5;
        }if (Colour6.equals("") && Colour5.equals("")){
            Colour=Colour1+","+Colour2+","+Colour3+","+Colour4;
        }if (Colour6.equals("") && Colour5.equals("") && Colour4.equals("")){
            Colour=Colour1+","+Colour2+","+Colour3;
        }if (Colour6.equals("") && Colour5.equals("") && Colour4.equals("")&&Colour3.equals("")){
            Colour=Colour1+","+Colour2;
        }if (Colour6.equals("") && Colour5.equals("") && Colour4.equals("")&&Colour3.equals("")&&Colour2.equals("")){
            Colour=Colour1;
        }if ((!Colour6.equals("") &&!Colour5.equals("") &&!Colour4.equals("")&&!Colour3.equals("")&&!Colour2.equals("")&&!Colour1.equals(""))){
            Colour=Colour1+","+Colour2+","+Colour3+","+Colour4+"+"+Colour5+","+Colour6;
        }

         */

        if ((!Colour6.equals("") && !Colour5.equals("") &&!Colour4.equals("")&&!Colour3.equals("")&&!Colour2.equals("")&&!Colour1.equals(""))){

            Colour=Colour1+","+Colour2+","+Colour3+","+Colour4+","+Colour5+","+Colour6;
        }else if (Colour6.equals("") && Colour5.equals("") && Colour4.equals("")&&Colour3.equals("")&&Colour2.equals("")){

            Colour=Colour1;
        }else if (Colour6.equals("") && Colour5.equals("") && Colour4.equals("")&&Colour3.equals("")){

            Colour=Colour1+","+Colour2;
        }else if (Colour6.equals("") && Colour5.equals("") && Colour4.equals("")){

            Colour=Colour1+","+Colour2+","+Colour3;
        }else if (Colour6.equals("") && Colour5.equals("")){

            Colour=Colour1+","+Colour2+","+Colour3+","+Colour4;
        }else if (Colour6.equals("")) {

            Colour = Colour1 + "," + Colour2 + "," + Colour3 + "," + Colour4 + "," + Colour5;
        }

        if (bitmap != null && bitmap2 != null && bitmap3 != null && bitmap4 != null && !ProductName.equals("") &&
                !Brand.equals("")&& !ShopAddress.equals("") && !CityName.equals("")&& !Size.equals("")&&!Prize.equals("")) {

            progressBar.setVisibility(View.VISIBLE);

            ProductID =db.collection("Sellers").document(CityName).collection(CityName).document().getId();
            ProductRef = db.collection("Sellers").document(CityName).collection(CityName).document(ProductID);


            HashMap<String, Object> map = new HashMap<>();
            map.put("City_Name", CityName);
            map.put("Product_Name", ProductName);
            map.put("Colour", Colour);
            map.put("ShopAddrs", ShopAddress);
            map.put("Brand", Brand);
            map.put("Sizes", Size);
            map.put("Prize","₹"+Prize);
            map.put("UID",Uid);
            map.put("MyLat",MyLat);
            map.put("MyLong",MyLong);
            map.put("Gender", GenderSpinner);
            map.put("Category", CategorySpinner);
            map.put("ShopName",ShopName);
            map.put("Product", ProductSpinner);
            map.put("PdDescription",PdDescription);

            ProductRef.set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    UploadingImage();
                    UploadingThumbnailImage();
                }
            });

        }else {
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
        }
    }

    private void setImage_profile() {

        resized = Bitmap.createScaledBitmap(bitmap, 800, 1000, true);
        image_profile.setImageBitmap(resized);

    }

    private void setImage_profile2() {

        resized2 = Bitmap.createScaledBitmap(bitmap2, 800, 1000, true);
        image_profile2.setImageBitmap(resized2);

    }

    private void setImage_profile3() {

        resized3 = Bitmap.createScaledBitmap(bitmap3, 800, 1000, true);
        image_profile3.setImageBitmap(resized3);

    }

    private void setImage_profile4() {

        resized4 = Bitmap.createScaledBitmap(bitmap4, 800, 1000, true);
        image_profile4.setImageBitmap(resized4);

    }

    private void geoCoder(double LAt, double LOng) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        try {

            //28.628170, 77.208960
            //28.630597, 77.218978

         //   double delhi_LONG = 77.218978;
         //   double delhi_LAT = 28.630597;
            List<Address> addressList = geocoder.getFromLocation(LAt, LOng, 1);

            if (addressList.size() > 0) {
                Address address = addressList.get(0);

                Toast.makeText(this, address.getLocality(), Toast.LENGTH_SHORT).show();
                if (CityName.equals("")){
                    CityNameEditText.setText(address.getLocality());
                }

                if (ShopAddress.equals("")){
                    ShopAddressEditText.setText(address.getSubLocality());
                }


                String hey=address.getLocale().getCountry();
                Log.d(TAG, "geoLocate: Locality: " + address.getLocality() + " " + address.getSubLocality());
            }

            for (Address address : addressList) {
                Log.d(TAG, "geoLocate: Address: " + address.getAddressLine(address.getMaxAddressLineIndex()));
            }


        } catch (IOException e) {


        }

    }

    private void initGoogleMap() {

        if (isServicesOk()) {
            if (isGPSEnabled()) {
                if (checkLocationPermission()) {
                   // getCurrentLocation();
                    UploadDetails();




                } else {
                    requestLocationPermission();
                }
            } else {
                Toast.makeText(this, "Gps Not Enabled", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Services not okay", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isServicesOk() {

        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();

        int result = googleApi.isGooglePlayServicesAvailable(this);

        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApi.isUserResolvableError(result)) {
            Dialog dialog = googleApi.getErrorDialog(this, result, PLAY_SERVICES_ERROR_CODE, task ->
                    Toast.makeText(this, "Dialog is cancelled by User", Toast.LENGTH_SHORT).show());
            dialog.show();
        } else {
            Toast.makeText(this, "Play services are required by this application", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    private boolean checkLocationPermission() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private boolean isGPSEnabled() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (providerEnabled) {
            return true;
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permissions")
                    .setMessage("GPS is required for accessing the Shop location. Please enable GPS.")
                    .setPositiveButton("OK", ((dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, GPS_REQUEST_CODE);
                    }))
                    .setCancelable(false)
                    .show();

        }
        return false;
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                if (task.isSuccessful())
                {
                    Location location = task.getResult();

                    assert location != null;
                    Log.d(TAG,String.valueOf(location.getLatitude())+ location.getLongitude());

                    //if (MyLat.equals("") && MyLong.equals("")) {

                        MyLat = String.valueOf(location.getLatitude());
                        MyLong = String.valueOf(location.getLongitude());
                        LocationEditText.setText(MyLat+","+MyLong);
                        geoCoder(location.getLatitude(),location.getLongitude());
                  //  }
                }
                else
                    {
                    Log.d(TAG, "getCurrentLocation: Error: " + task.getException().getMessage());
                    Toast.makeText(Upload_New_Product.this, "Can't get Location", Toast.LENGTH_SHORT).show();
                    }


            }
        });

    }

    private void CheckStatus() {

        if (T1&&T2&&T3&&T4&&T5){
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Upload Successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

     //   MenuInflater inflater = getMenuInflater();
     //   inflater.inflate(R.menu.menu1, menu);


        return super.onCreateOptionsMenu(menu);


    }


    @Override
    protected void onResume() {
        super.onResume();


        SharedPreferences sp = getSharedPreferences("LoginInfos", MODE_PRIVATE);

        LatBack=sp.getString("GETLAT","0.0000000000");
        LongBack=sp.getString("GETLONG","0.0000000000");
        CityNameBack=sp.getString("Locality","");
        ShopAddressBack=sp.getString("SubLocality","");

        CityNameEditText.setText(CityNameBack);
        ShopAddressEditText.setText(ShopAddressBack);


        Log.d(TAG,"OnResume : "+LatBack+" , "+LongBack);

       if (LatBack.equals("null")&&LongBack.equals("null"))
       {
           LatitudeBack=0.0;
           LongitudeBack=0.0;
       }else if(LatBack!=null && LongBack!=null)
       {
           LatitudeBack=Double.parseDouble(LatBack);
           LongitudeBack=Double.parseDouble(LongBack);

       }

       MyLat=String.valueOf(LatitudeBack);
       MyLong=String.valueOf(LongitudeBack);


        Log.d(TAG,"Location Back : "+LatitudeBack+","+LongitudeBack+"MyLat and MyLong : "+MyLat+" , "+MyLong);
        LocationEditText.setText(LatitudeBack+","+LongitudeBack);




    }
}


   /*
        Intent intent=getIntent();
        LatitudeBack= intent.getDoubleExtra("Latitude",0.0);
        LongitudeBack=intent.getDoubleExtra("Longitude",0.0);

        Log.d(TAG,"Location Back : "+LatitudeBack+","+LongitudeBack);
        LocationEditText.setText(LatitudeBack+","+LongitudeBack);

         */

// CityNameBack=sp.getString("Locality",CityName);
// CityNameEditText.setText(CityNameBack);
// ShopAddressBack=sp.getString("SubLocality",ShopAddress);

   /*
        Product_Name_EditText = findViewById(R.id.Product_Name);
        Brand_EditText = findViewById(R.id.Brand);
        Colour_EditText = findViewById(R.id.Colour);
        ShopAddressEditText = findViewById(R.id.ShopAddress);
        CityNameEditText = findViewById(R.id.CityName);

        if (bitmap != null && bitmap2 != null && bitmap3 != null && bitmap4 != null && Product_Name_EditText!=null &&
                Brand_EditText!=null&& Colour_EditText!=null && ShopAddressEditText!=null && CityNameEditText!=null&&!Prize.equals("")) {

            fuser = FirebaseAuth.getInstance().getCurrentUser();
            ProductName = Product_Name_EditText.getText().toString();
            CityName = CityNameEditText.getText().toString();

            ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
            bitmapArrayList.add(bitmap);
            bitmapArrayList.add(bitmap2);
            bitmapArrayList.add(bitmap3);
            bitmapArrayList.add(bitmap4);

            int i =0;
            for (Bitmap bitUpload : bitmapArrayList) {
                i = i + 1;
                uploadFileAtIndex(i,bitUpload);
            }
        }

         */


        /*
        if (bitmap != null && bitmap2 != null && bitmap3 != null && bitmap4 != null && Product_Name_EditText!=null &&
                Brand_EditText!=null&& Colour_EditText!=null && ShopAddressEditText!=null && CityNameEditText!=null) {

            StorageTask arrayUpload;
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            ProductName = Objects.requireNonNull(Product_Name_EditText.getText()).toString();
            CityName = Objects.requireNonNull(CityNameEditText.getText()).toString();

            // Bitmap[] bitmaps=new Bitmap[3];
            ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
            bitmapArrayList.add(bitmap);
            bitmapArrayList.add(bitmap2);
            bitmapArrayList.add(bitmap3);
            bitmapArrayList.add(bitmap4);

            Bitmap bitresized;

            for (Bitmap bitUpload : bitmapArrayList)
            {
                bitresized = Bitmap.createScaledBitmap(bitUpload, 800, 800, true);
                ByteArrayOutputStream baosArray = new ByteArrayOutputStream();
                bitresized.compress(Bitmap.CompressFormat.JPEG, 70, baosArray);
                byte[] uploadbaosarray = baosArray.toByteArray();
                i = i + 1;
                fileReference = storageReference.child(ProductName).child(i + ProductName + ".jpg");

                arrayUpload = fileReference.putBytes(uploadbaosarray);

                arrayUpload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        } else if (task.isSuccessful()) {
                            Toast.makeText(Upload_New_Product.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            //mProgressBar.setVisibility(View.INVISIBLE);
                        }

                        return fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            assert downloadUri != null;
                            String mUri = downloadUri.toString();

                            ProductName = Product_Name_EditText.getText().toString();
                            ProductRef = db.collection("Sellers").document(CityName).collection(Uid).document(ProductName);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("imageURL" + i, mUri);
                            //reference.updateChildren(map);
                            ProductRef.set(map, SetOptions.merge());

                        } else {
                            Toast.makeText(Upload_New_Product.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Upload_New_Product.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        //pd.dismiss();
                    }
                });

            }
        }

         */

 /*

    File f = new File(resultUri.getPath());
    long sizeUri = f.length()/1024;
    mProgressBar.setVisibility(View.VISIBLE);


        int quality;
        if (sizeUri <= 300)
            quality = 90;
        else if (sizeUri <= 1000)
            quality = 70;
        else if (sizeUri <= 2000)
            quality = 50;
        else if (sizeUri <= 3000)
            quality = 30;
        else
            quality= 10;

        */


    /*
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    //         .setAspectRatio(1, 1)
                    .start(this);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                if (Build.VERSION.SDK_INT >= 29) {
                    try {
                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), resultUri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Use older version
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //setImage_profile();
                resized = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
                image_profile.setImageBitmap(resized);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }


        */


//  UploadingImage();
//  UploadingThumbnailImage();



        /*
        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK && data != null)
        {
            ImageUri2 = data.getData();

            CropImage.activity(ImageUri2)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    //         .setAspectRatio(1, 1)
                    .start(this);

        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                resultUri2 = result.getUri();


                if (Build.VERSION.SDK_INT >= 29) {
                    try {
                        bitmap2 = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), resultUri2));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Use older version
                    try {
                        bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                resized2 = Bitmap.createScaledBitmap(bitmap2, 600, 600, true);
                image_profile2.setImageBitmap(resized2);
                //setImage_profile2();
                //  UploadingImage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

         */



