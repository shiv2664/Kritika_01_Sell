package com.shivam.kritika_01_sell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Upload_New_Product extends AppCompatActivity {

    Spinner gender_spinner,product_spinner,product_name_spinner;
    ImageView image_profile,image_profile2,image_profile3,image_profile4;
    MaterialEditText CityNameEdit, editText4, shopAddress;
    Button button;

    FirebaseUser fuser;

    StorageReference storageReference;
    StorageReference fileReference;
    StorageReference ThumbStorageReference;


    private StorageTask uploadTask;
    private FirebaseFirestore db;
    private DocumentReference ProductRef;


    String GenderSpinner;
    private FusedLocationProviderClient mLocationClient;
    public static final int GPS_REQUEST_CODE = 9003;
    private static final int PICK_IMAGE_REQUEST = 1;
    private int Storage_Permission_Req = 1;
    public static final int PERMISSION_REQUEST_CODE = 9001;
    private static final int PLAY_SERVICES_ERROR_CODE = 9002;
    Uri ImageUri;
    Uri resultUri;
    Bitmap bitmap;
    Bitmap resized;
    String TAG = "MyTag";
    String Uid;
    String ProductType;
    String CityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__new__product);

        image_profile = findViewById(R.id.profile_image);
        image_profile2 = findViewById(R.id.profile_image2);
        image_profile3 = findViewById(R.id.profile_image3);
        image_profile4 = findViewById(R.id.profile_image4);
        // button=findViewById(R.id.Upload);

        CityNameEdit = findViewById(R.id.CityName);
        editText4 = findViewById(R.id.ProductType);
        shopAddress = findViewById(R.id.ShopAddress);
        button=findViewById(R.id.location);

        gender_spinner=findViewById(R.id.Gender_Spinner);
        product_spinner=findViewById(R.id.Product_Spinner);
        product_name_spinner=findViewById(R.id.Product_Name_Spinner);



        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.Gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(adapter);

        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // GenderTextView.setText(parent.getItemAtPosition(position).toString());
                GenderSpinner=parent.getItemAtPosition(position).toString();

                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Men")){
                    ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeMen, android.R.layout.simple_spinner_item);
                    arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    product_spinner.setAdapter(arrayAdapter2);

                    product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            String selectedItem2 = parent.getItemAtPosition(position).toString();
                            if (selectedItem2.equals("Shoes")){
                                ArrayAdapter<CharSequence> arrayAdapter3 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeMenShoes, android.R.layout.simple_spinner_item);
                                arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                product_name_spinner.setAdapter(arrayAdapter3);
                            }
                            else if (selectedItem2.equals("Clothing")){
                                ArrayAdapter<CharSequence> arrayAdapter3 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeMenClothing, android.R.layout.simple_spinner_item);
                                arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                product_name_spinner.setAdapter(arrayAdapter3);
                            }
                            else if (selectedItem2.equals("Accessories")){
                                ArrayAdapter<CharSequence> arrayAdapter3 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeMenAccessories, android.R.layout.simple_spinner_item);
                                arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                product_name_spinner.setAdapter(arrayAdapter3);

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }else if (selectedItem.equals("Women")){
                    ArrayAdapter<CharSequence> arrayAdapter3 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeWomen, android.R.layout.simple_spinner_item);
                    arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    product_spinner.setAdapter(arrayAdapter3);

                    product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            String selectedItem3 = parent.getItemAtPosition(position).toString();
                            if (selectedItem3.equals("Shoes")){
                                ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeWomenShoes, android.R.layout.simple_spinner_item);
                                arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                product_name_spinner.setAdapter(arrayAdapter4);
                            }
                            else if (selectedItem3.equals("Clothing")){
                                ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeWomenClothing, android.R.layout.simple_spinner_item);
                                arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                product_name_spinner.setAdapter(arrayAdapter4);
                            }
                            else if (selectedItem3.equals("Indian and Fusion Wear")){
                                ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeWomenIndianEthnic, android.R.layout.simple_spinner_item);
                                arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                product_name_spinner.setAdapter(arrayAdapter4);
                            }
                            else if (selectedItem3.equals("Accessories")){
                                ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(Upload_New_Product.this, R.array.ProductTypeWomenAccessories, android.R.layout.simple_spinner_item);
                                arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                product_name_spinner.setAdapter(arrayAdapter4);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }

        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCurrentLocation();
            }
        });




        //ProductRef=db.collection("Sellers").document(CityName).collection(Uid).document(ProductType);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        assert fuser != null;
        Uid = fuser.getUid();

        db = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("Product Images").child(Uid);
        ThumbStorageReference = FirebaseStorage.getInstance().getReference("Thumbnail Images").child(Uid);

        //geoCoder();
        initGoogleMap();


        mLocationClient = new FusedLocationProviderClient(this);


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

        /*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadDetails();
                UploadingImage();
                UploadingThumbnailImage();
            }
        });

         */
    }

    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    private void UploadingImage() {

        resized = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
        // image_profile.setImageBitmap(resized);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] uploadbaos = baos.toByteArray();


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        ProductType = Objects.requireNonNull(editText4.getText()).toString();
        CityName = Objects.requireNonNull(CityNameEdit.getText()).toString();

        if (uploadbaos != null) {
            fileReference = storageReference.child(ProductType).child(ProductType + ".jpg");

            uploadTask = fileReference.putBytes(uploadbaos);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    } else if (task.isSuccessful()) {
                        Toast.makeText(Upload_New_Product.this, "Upadated Successfully", Toast.LENGTH_SHORT).show();
                        //mProgressBar.setVisibility(View.INVISIBLE);
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        // ProductRef=db.collection("Sellers").document().collection(ProductType);
                        ProductType = editText4.getText().toString();
                        ProductRef = db.collection("Sellers").document(CityName).collection(Uid).document(ProductType);
                        //reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", "" + mUri);
                        //reference.updateChildren(map);
                        ProductRef.set(map, SetOptions.merge());


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
            Toast.makeText(Upload_New_Product.this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }

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

        resized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] uploadbaos = baos.toByteArray();


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        ProductType = Objects.requireNonNull(editText4.getText()).toString();
        CityName = Objects.requireNonNull(CityNameEdit.getText()).toString();
        //reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        if (uploadbaos != null) {
            final StorageReference fileReference = ThumbStorageReference.child(ProductType + ".jpg");

            uploadTask = fileReference.putBytes(uploadbaos);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    } else if (task.isSuccessful()) {
                        //Toast.makeText(EditCredActivity.this, "Upadated Successfully", Toast.LENGTH_SHORT).show();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        ProductRef = db.collection("Sellers").document(CityName).collection(Uid).document(ProductType);
                        //reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("ThumbnailUrl", "" + mUri);
                        //reference.updateChildren(map);
                        ProductRef.set(map, SetOptions.merge());

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
            Toast.makeText(Upload_New_Product.this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void UploadDetails() {

        ProductType = Objects.requireNonNull(editText4.getText()).toString();
        CityName = Objects.requireNonNull(CityNameEdit.getText()).toString();

        ProductRef = db.collection("Sellers").document(CityName).collection(Uid).document(ProductType);

        HashMap<String, Object> map = new HashMap<>();
        map.put("City_Name", CityName);
        ProductRef.set(map, SetOptions.merge());
    }

    private void setImage_profile() {

        resized = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
        image_profile.setImageBitmap(resized);

    }

    private void geoCoder(double LAt,double LOng) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        try {

            //28.628170, 77.208960
            //28.630597, 77.218978

            double delhi_LONG = 77.218978;
            double delhi_LAT = 28.630597;
            List<Address> addressList = geocoder.getFromLocation(LAt,LOng, 3);

            if (addressList.size() > 0) {
                Address address = addressList.get(0);

                Toast.makeText(this, address.getLocality(), Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(this, "Ready to Map", Toast.LENGTH_SHORT).show();
                } else {
                    requestLocationPermission();
                }
            }
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
                    .setMessage("GPS is required for this app to work. Please enable GPS.")
                    .setPositiveButton("Yes", ((dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, GPS_REQUEST_CODE);
                    }))
                    .setCancelable(false)
                    .show();

        }
        return false;

    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null) {
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


                setImage_profile();
                //  UploadingImage();
                //  UploadingThumbnailImage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

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

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                if (task.isSuccessful()) {
                    Location location = task.getResult();

                    //gotoLocation(location.getLatitude(), location.getLongitude());
                    geoCoder(location.getLatitude(),location.getLongitude());
                } else {
                    Log.d(TAG, "getCurrentLocation: Error: " + task.getException().getMessage());
                }


            }
        });

    }
}

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

