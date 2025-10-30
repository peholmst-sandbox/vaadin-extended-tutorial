package com.example.ui;

import com.example.service.StoreService;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route
@PageTitle("Retail Store Network")
public class MainView extends SplitLayout implements AfterNavigationObserver {

    private final StoreService storeService;
    private final StoreDashboard storeDashboard;
    private final StoreList storeList;
    private final StoreMap storeMap;

    public MainView(StoreService storeService) {
        this.storeService = storeService;

        // Create components
        storeDashboard = new StoreDashboard();
        storeList = new StoreList();
        storeMap = new StoreMap();

        // Setup layout
        var primaryPanel = new SplitLayout(Orientation.VERTICAL);
        primaryPanel.addToPrimary(storeDashboard);
        primaryPanel.addToSecondary(storeList);
        primaryPanel.setSplitterPosition(50.0);

        setOrientation(Orientation.HORIZONTAL);
        addToPrimary(primaryPanel);
        addToSecondary(storeMap);
        setSplitterPosition(50);

        setSizeFull();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        var stores = storeService.findStores();
        storeDashboard.setStores(stores);
        storeList.setStores(stores);
        storeMap.setStores(stores);
    }
}
