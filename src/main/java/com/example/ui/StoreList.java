package com.example.ui;

import com.example.service.Store;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;

import java.util.List;

class StoreList extends Composite<Grid<Store>> {

    StoreList() {
        var grid = getContent();

        // Configure grid
        grid.addColumn(Store::storeName).setHeader("Store").setSortable(true);
        grid.addColumn(Store::manager).setHeader("Manager").setSortable(true);
        grid.addColumn(Store::revenue).setHeader("Revenue").setSortable(true);
        grid.addColumn(Store::employeeCount).setHeader("Employee Count").setSortable(true);
        grid.addColumn(Store::city).setHeader("City").setSortable(true);
        grid.addColumn(store -> store.performanceRating().getDisplayName()).setHeader("Performance Rating").setSortable(true);
        grid.setSizeFull();
    }

    public void setStores(List<Store> items) {
        getContent().setItems(items);
    }
}
