package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;

import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.george.memoshareapp.R;
import com.amap.api.services.core.PoiItem;
import com.george.memoshareapp.beans.Post;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapLocationActivity extends AppCompatActivity {

    private static final String TAG = "MapLocationActivity";
    private AMap aMap;
    private MapView mapView;
    private EditText searchText;
    private ListView nearbyPlacesList;
    private LatLng currentLocation;
    private Marker marker;
    private Button myLocationButton;

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;
    private PoiSearch poiSearch;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        AMapLocationClient.setApiKey("b73d5e0ad525991966aed0de9c8cecc5");
        AMapLocationClient.updatePrivacyShow(this, true, true);
        AMapLocationClient.updatePrivacyAgree(this, true);
        try {
            locationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView = (MapView) findViewById(R.id.map_view_01);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        searchText = findViewById(R.id.search_text);
        nearbyPlacesList = findViewById(R.id.nearby_places_list);
        myLocationButton = findViewById(R.id.my_location_button);

        initLocation();
        initSearch();
        initMyLocationButton();

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPlace(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null) {
                    marker.setPosition(latLng);
                } else {
                    marker = aMap.addMarker(new MarkerOptions().position(latLng));
                }
                searchNearbyPlaces(latLng);
            }
        });
    }

    private void initMyLocationButton() {
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker != null) {
                    marker.remove();
                    marker = null;
                }
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                searchNearbyPlaces(currentLocation);
            }
        });
    }

    private void initLocation() {
        try {
            locationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        locationOption = new AMapLocationClientOption();

        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setOnceLocation(true);

        locationClient.setLocationOption(locationOption);
        locationClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        currentLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        aMap.setMyLocationEnabled(true);
                        aMap.setMyLocationEnabled(true);
                        // 搜索周边地点
                        searchNearbyPlaces(currentLocation)  ;
                    }
                }
            }
        });

        locationClient.startLocation();
    }


    private void initSearch() {
        try {
            poiSearch = new PoiSearch(this, null);
        } catch (AMapException e) {
            e.printStackTrace();
        }
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int resultCode) {
                if (resultCode == AMapException.CODE_AMAP_SUCCESS && poiResult != null) {
                    List<PoiItem> poiItems = poiResult.getPois();
                    String title = poiItems.get(0).getTitle();
                    if (marker != null)
                        marker.setTitle(title);
                    updateNearbyPlacesList(poiItems);
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
            }
        });
    }

    private void searchPlace(String keyword) {
        PoiSearch.Query query = new PoiSearch.Query(keyword, "", "");
        query.setPageSize(10);
        query.setPageNum(1);
        poiSearch.setQuery(query);
        poiSearch.searchPOIAsyn();
    }

    private void searchNearbyPlaces(LatLng latLng) {
        PoiSearch.Query query = new PoiSearch.Query("", "", "");
        query.setPageSize(10);
        query.setPageNum(1);
        poiSearch.setQuery(query);

        PoiSearch.SearchBound searchBound = new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude, latLng.longitude), 15000);
        poiSearch.setBound(searchBound);
        poiSearch.searchPOIAsyn();
    }

    private void updateNearbyPlacesList(List<PoiItem> poiItems) {
        ArrayAdapter<PoiItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, poiItems);
        nearbyPlacesList.setAdapter(adapter);
        nearbyPlacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiItem poiItem = poiItems.get(position);
                LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                if (marker != null) {
                    marker.setPosition(latLng);
                    marker.setTitle(poiItem.getTitle());
                } else {
                    marker = aMap.addMarker(new MarkerOptions().position(latLng).title(poiItem.getTitle())); // Modify this line
                }
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });
    }


    private Map<String, String> getAddressFromLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Map<String, String> cityMap = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String province = address.getAdminArea();
                String city = address.getLocality();
                String addressLine = address.getAddressLine(0);
                String subLocality = address.getSubLocality();
                cityMap = new HashMap<>();
                cityMap.put("province", province);
                cityMap.put("city", city);
                cityMap.put("addressLine", addressLine);
                cityMap.put("subLocality", subLocality);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityMap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationClient != null) {
            locationClient.onDestroy();
        }
    }

    public void finish(View view) {
        finish();
    }

    public void confirm(View view) {
        if (marker != null) {
            LatLng position = marker.getPosition();
            saveLocation(position);
            finish();
        } else {
            saveLocation(currentLocation);
            finish();
        }
    }

    private void saveLocation(LatLng position) {
        Map<String, String> address = getAddressFromLocation(position);
        String city = address.get("city");
        String province = address.get("province");
        String addressLine = address.get("addressLine");
        String subLocality = address.get("subLocality");
        Post post = new Post();
        post.setLatitude(position.latitude);
        post.setLongitude(position.longitude);
        if (marker != null && marker.getTitle() != null) {
            post.setLocation(province + city + subLocality + marker.getTitle());
        } else {
            post.setLocation( addressLine);
        }
        Intent intent = new Intent();
        intent.putExtra("publishContent", post);
        setResult(ReleaseActivity.MAP_INFORMATION_SUCCESS, intent);
        Log.d(TAG, "saveLocation: "+post.getLocation()+" "+post.getLatitude()+" "+post.getLongitude());
    }
}





