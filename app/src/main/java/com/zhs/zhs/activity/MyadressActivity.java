package com.zhs.zhs.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.zhs.zhs.R;
import com.zhs.zhs.utils.GPSUtil;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyadressActivity extends Baseactivity {

    @Bind(R.id.map)
    MapView mMapView;
    @Bind(R.id.adress)
    TextView adress;
    @Bind(R.id.geoButton)
    Button geoButton;


    private MapView mapView;
    private AMap aMap;
    private UiSettings mUiSettings;
    private CameraUpdate mUpdata;
    Context context;

    // 单选提示框  
    private AlertDialog alertDialog;
    String address;
    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myadress);
        ButterKnife.bind(this);
        context = this;
        setTitleTextView(getString(R.string.my_adress), null);

        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        address = prefs.getAddress();
        lat = GPSUtil.bd09_To_Gcj02(prefs.getLat(), prefs.getLon())[0];
        lon = GPSUtil.bd09_To_Gcj02(prefs.getLat(), prefs.getLon())[1];
        adress.setText(address);
        init();
        hideSoftKeyBoard();
    }

    @OnClick({R.id.geoButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.geoButton:
                showSingleAlertDialog();
                break;
        }
    }


    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(true);
            mUiSettings.setCompassEnabled(true);
            mUpdata = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(lat, lon), 15, 0, 30));//这是地理位置，就是经纬度。
            aMap.moveCamera(mUpdata);//定位的方法
            drawMarkers();
        }
    }

    public void drawMarkers() {

        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(address)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true));
        marker.showInfoWindow();// 设置默认显示一个infowinfow
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    /**
     * 初始化AMap对象
     */


    public void showSingleAlertDialog() {
        boolean hasGaode = false;
        boolean hasBaidu = false;
        if (isAvilible(context, "com.autonavi.minimap")) {
            hasGaode = true;
        }
        if (isAvilible(context, "com.baidu.BaiduMap")) {
            hasBaidu = true;
        }

        if (hasBaidu == true && hasGaode == false) {
            String[] items = {"百度地图"};
            final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);
            TextView textView = new TextView(mContext);
            textView.setText("使用以下方式找到路线");
            textView.setTextColor(getResources().getColor(R.color.base_color));
            textView.setTextSize(20);
            textView.setGravity(1);
            alertBuilder.setCustomTitle(textView);
            alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showToast("百度");
                    try {
                        Intent intent = Intent.getIntent("intent://map/direction?" +
                                "origin=latlng:" + lat + "," + lon + "|name:" +
                                "&destination=latlng:" + "," + "|name:" + address +
                                "&mode=driving&coord_type=gcj02&src=com.example.maplisttest|MapListTest#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                        startActivity(intent);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    alertDialog.dismiss();
                }
            });
            alertDialog = alertBuilder.create();
            alertDialog.show();
        }

        if (hasBaidu == false && hasGaode == true) {
            String[] items = {"高德地图"};
            final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);
            TextView textView = new TextView(mContext);
            textView.setText("使用一下方式找到路线");
            textView.setTextColor(getResources().getColor(R.color.base_color));
            textView.setTextSize(20);
            textView.setGravity(1);
            alertBuilder.setCustomTitle(textView);
            alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=行车记录仪&poiname=" + address + "&lat=" + lat + "&lon=" + lon + "&dev=0&style=2");
                        startActivity(intent);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    alertDialog.dismiss();
                }
            });
            alertDialog = alertBuilder.create();
            alertDialog.show();
        }

        if (hasBaidu == true && hasGaode == true) {
            String[] items = {"百度地图", "高德地图"};
            final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);
            TextView textView = new TextView(mContext);
            textView.setText("使用一下方式找到路线");
            textView.setTextColor(getResources().getColor(R.color.base_color));
            textView.setTextSize(20);
            textView.setGravity(1);
            alertBuilder.setCustomTitle(textView);
            alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            try {
                                Intent intent = Intent.getIntent("intent://map/direction?" +
                                        "origin=latlng:" + lat + "," + lon + "" +
                                        "&destination=latlng:" + "|name:" + address +
                                        "&mode=driving&coord_type=gcj02&src=com.example.maplisttest|MapListTest#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                                startActivity(intent);
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                            alertDialog.dismiss();
                            break;
                        case 1:
                            showToast("高德");
                            try {
                                Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=行车记录仪&poiname=" + address + "&lat=" + lat + "&lon=" + lon + "&dev=0&style=2");
                                startActivity(intent);
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                            alertDialog.dismiss();
                            break;
                    }
                }
            });
            alertDialog = alertBuilder.create();
            alertDialog.show();
        }

        if (hasBaidu == false && hasGaode == false) {
            //显示手机上所有的market商店
            Toast.makeText(context, "您尚未安装地图软件不能导航", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }


    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}