package ru.strongit.googlemaps;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.strongit.googlemaps.model.OrganizationModel;
import ru.strongit.googlemaps.model.VisitModel;

/**
 * Интерфейс Api для общения с интернет сервером
 */
public interface GMapAndRetrofitApi {
    @GET("/getVisitsListTest")
    Call<List<VisitModel>> getVisitsList();

    @GET("/getOrganizationListTest")
    Call<List<OrganizationModel>> getOrganizationList();


}
