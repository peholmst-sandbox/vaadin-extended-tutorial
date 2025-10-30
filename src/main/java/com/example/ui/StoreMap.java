package com.example.ui;

import com.example.service.Store;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.map.configuration.Coordinate;
import com.vaadin.flow.component.map.configuration.Feature;
import com.vaadin.flow.component.map.configuration.feature.MarkerFeature;

import java.util.List;

class StoreMap extends Composite<Map> {

    public StoreMap() {
        getContent().setSizeFull();
        var map = getContent();
        map.setZoom(4);
    }

    public void setStores(List<Store> stores) {
        var features = stores.stream().map(this::createStoreFeature).toList();
        var map = getContent();
        map.getFeatureLayer().removeAllFeatures();
        features.forEach(map.getFeatureLayer()::addFeature);
        map.zoomToFit(features);
    }

    private Feature createStoreFeature(Store store) {
        var feature = new MarkerFeature(new Coordinate(store.longitude(), store.latitude()));
        feature.setText(store.storeName());
        return feature;
    }
}
