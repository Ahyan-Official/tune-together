package com.app.tunetogether.az;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.app.tunetogether.az.model.MyItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRenderer extends DefaultClusterRenderer<MyItem> {


    // it for changing cluster color and its size
    Context context;
    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected int getColor(int clusterSize) {
        return ContextCompat.getColor(context, R.color.teal_200);
    }

    @Override
    protected boolean shouldRenderAsCluster(@NonNull Cluster<MyItem> cluster) {
        //cluster size
        return cluster.getSize() > 2;
    }







}