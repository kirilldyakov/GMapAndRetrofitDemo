package ru.strongit.googlemaps;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.strongit.googlemaps.VisOrgAdapter.OnVisitSelectedListener;
import ru.strongit.googlemaps.model.OrganizationModel;
import ru.strongit.googlemaps.model.VisitModel;

/**
 * Класс основной активности
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, OnVisitSelectedListener {

    private GoogleMap mMap;

    RecyclerView recyclerView;
    List<VisitModel> visits;
    List<OrganizationModel> organisations;
    List<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        visits = new ArrayList<>();
        organisations = new ArrayList<>();
        markers = new ArrayList<>();
        markers = new ArrayList<>();


        recyclerView = (RecyclerView) findViewById(R.id.visits_recycle_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        retrofitGetData();

    }




    //Инициализаяция карты
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
    }

    //Обрабатывает событие нажатия на маркер карты
    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 250, null);
        for (Marker mrk : markers) {
            mrk.setIcon(BitmapDescriptorFactory.defaultMarker());
        }
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        try {
            String marker_id = (String) marker.getTag();

            VisOrgAdapter visOrgAdapter = (VisOrgAdapter) recyclerView.getAdapter();

            visOrgAdapter.selectListItem(marker_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Запускается процесс получения данных
    public void retrofitGetData() {
        VisOrgAdapter vis_org_adapter = new VisOrgAdapter(visits, organisations);
        vis_org_adapter.onVisitSelectedListener = new OnVisitSelectedListener() {
            @Override
            public void onVisitSelected(String id) {
                selectMarkersByTag(id);
            }
        };

        recyclerView.setAdapter(vis_org_adapter);

        GMapAndRetrofitApp.getApi().getOrganizationList().enqueue(new Callback<List<OrganizationModel>>() {
            @Override
            public void onResponse(Call<List<OrganizationModel>> call, Response<List<OrganizationModel>> response) {
                organisations.addAll(response.body());
                recyclerView.getAdapter().notifyDataSetChanged();
                addOrgsToMap(organisations);
            }


            @Override
            public void onFailure(Call<List<OrganizationModel>> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }

        });

        GMapAndRetrofitApp.getApi().getVisitsList().enqueue(new Callback<List<VisitModel>>() {

            @Override
            public void onResponse(Call<List<VisitModel>> call, Response<List<VisitModel>> response) {
                visits.addAll(response.body());
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<VisitModel>> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }

        });
    }

    //Отображает маркеры организаций на карте
    private void addOrgsToMap(List<OrganizationModel> orgs) {
        for (int i = 0; i < orgs.size(); i++) {
            LatLng latlng = new LatLng(orgs.get(i).getLatitude(), orgs.get(i).getLongitude());

            MarkerOptions mo = new MarkerOptions()
                    .position(latlng)
                    .title(orgs.get(i).getTitle());
            Marker marker = mMap.addMarker(mo);
            marker.setTag(orgs.get(i).getOrganizationId());
            markers.add(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 9));
        }
    }

    //Выделяет маркер на карте по указанному тэгу
    public void selectMarkersByTag(String tag) {
        for (Marker marker : markers) {
            if (marker.getTag().toString().equals(tag)) {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                marker.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 9));
            } else {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker());
                marker.hideInfoWindow();
            }
        }
    }

    //Колбэк от адаптера на событие выделения объекта в списке
    @Override
    public void onVisitSelected(String id) {
        selectMarkersByTag(id);
    }


}
