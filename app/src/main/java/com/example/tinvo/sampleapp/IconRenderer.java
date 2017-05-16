package com.example.tinvo.sampleapp;

import android.content.Context;
import android.graphics.drawable.Icon;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by TinVo on 5/15/2017.
 */

public class IconRenderer extends DefaultClusterRenderer<MyItem> {
    private IconGenerator iconFactory;

    public IconRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
        iconFactory = new IconGenerator(context);
    }

    @Override
    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
        iconFactory.setStyle(IconGenerator.STYLE_GREEN);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(item.getTitle())));
        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
