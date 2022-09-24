package com.example.pgsr_daegu_safety;

import static com.example.pgsr_daegu_safety.MainActivity.tag;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    private Geocoder geocoder;

    private WebView webView;
    private com.example.pgsr_daegu_safety.ProgressDialog progressDialog;

    private EditText address_editText;
    private ImageButton search_button;
    private ImageButton cur_posi_button;

    private GpsTracker gpsTracker;
    static String Tag;
    static String adr;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.second_main);

        geocoder = new Geocoder(this);

        if (checkLocationServicesStatus()) {
            checkRunTimePermission();
        } else {
            showDialogForLocationServiceSetting();
        }
        gpsTracker = new GpsTracker(this);


        progressDialog = new com.example.pgsr_daegu_safety.ProgressDialog(this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        webView = (WebView)findViewById(R.id.my_webView);
        address_editText = (EditText)findViewById(R.id.address_search_editText);
        search_button = (ImageButton) findViewById(R.id.search_button);
        cur_posi_button = (ImageButton)findViewById(R.id.cur_posi_button);

        adr = "https://www.google.com/maps/d/edit?hl=ko&mid=1YEjQZk8j00Fg89FfeDDQgJzTdFyLouM&ll=";
        if(tag==1) {
            adr = "https://www.google.com/maps/d/edit?hl=ko&mid=1lqSAQkHjJY0FgjpIbZo3Rbv_yQgdMe8&ll=";
        }
        else if(tag==2){
            adr = "https://www.google.com/maps/d/edit?hl=ko&mid=1WEU7f7hTzOJMvSvChRpR0SQYV0CZhLk&ll=";
        }
        else if(tag==3){
            adr = "https://www.google.com/maps/d/edit?hl=ko&mid=1UZMSV-RbqfkRQJqPzweVVF8QrWHCo-k&ll=";
        }
        else if(tag==4){
            adr = "https://www.google.com/maps/d/edit?hl=ko&mid=1YEjQZk8j00Fg89FfeDDQgJzTdFyLouM&ll=";
        }

        cur_posi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_posi();
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move_to_address(address_editText.getText().toString());
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                progressDialog.show();
            }

            //웹페이지 로딩 종료시 호출
            @Override
            public void onPageFinished(WebView view, String url){
                progressDialog.dismiss();
            }
        });

        cur_posi();
    }

    private void move_to_address(String address) {
        List<Address> list = null;
        String url = "";

        try {
            list = geocoder.getFromLocationName
                    (address, // 지역 이름
                            10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }

        if (list != null) {
            if (list.size() == 0) {
                Toast.makeText(this, "해당 주소가 없습니다.", Toast.LENGTH_SHORT);
            } else {
                // 해당되는 주소로 인텐트 날리기
                Address addr = list.get(0);
                double latitude = addr.getLatitude();
                double longitude = addr.getLongitude();

                System.out.println("////////////현재 내 위치값 : "+latitude+","+longitude);
                url = adr + latitude + "%2C" + longitude + "&z=14";
                webView.loadUrl(url);
                Toast.makeText(this, Tag, Toast.LENGTH_SHORT);
            }
        }
    }

    private void cur_posi() {
        Log.d("cur_posi", "cur_posi() starts");
        Toast.makeText(this, "cur_posi() starts", Toast.LENGTH_SHORT);


        System.out.println(gpsTracker);

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        System.out.println("latitude: " + latitude + ",    longitude: " + longitude);

        String url = adr + latitude + "%2C" + longitude + "&z=14";

        webView.loadUrl(url);

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {
            } else {


                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission() {


        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {


        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}