package com.example.siguataxi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*


import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaTaxiActivity : OnMapReadyCallback,Fragment(){


    private lateinit var mMap: GoogleMap
   // lateinit var mFragmentManager: FragmentManager
     //private lateinit var mMapView: MapView
    //private lateinit var mView :View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            super.onCreate(savedInstanceState)


        return inflater.inflate(R.layout.app_bar_menu,container,false)
        }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_mapa_taxi)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //val mapFragment = supportFragmentManager
          //  .findFragmentById(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(this)


    }*/

/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = MapView(this.context).findViewById(R.id.maps)
        with(mMapView) {
            onCreate(null)
            onResume()
        }
        mMapView.getMapAsync(this)

    }
*/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
       // MapsInitializer.initialize(context)
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Agrega la camara en siguatepeque
        val siguatepeque = LatLng(14.6, -87.8333)
        mMap.addMarker(MarkerOptions().position(siguatepeque).title("Marca en Siguatepeque").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(siguatepeque))
        val zoomMapa =16

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(siguatepeque,zoomMapa.toFloat()))

    }
}
