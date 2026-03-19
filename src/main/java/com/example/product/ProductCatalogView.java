package com.example.product;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.tutorial.backend.data.DataIntegrityViolationException;
import org.vaadin.tutorial.backend.data.OptimisticLockingFailureException;
import org.vaadin.tutorial.backend.product.ProductCatalogItem;
import org.vaadin.tutorial.backend.product.ProductCatalogService;
import org.vaadin.tutorial.backend.product.ProductCategoryService;
import org.vaadin.tutorial.backend.product.ProductFilter;
import org.vaadin.tutorial.backend.product.ProductId;
import org.vaadin.tutorial.backend.product.ProductSortProperty;
import org.vaadin.tutorial.backend.util.VaadinUtils;

import java.util.Optional;

@Route("")
@PageTitle("Product Catalog")
class ProductCatalogView extends HorizontalLayout implements HasUrlParameter<Long> {

    private final ProductCatalogService service;
    private final Grid<ProductCatalogItem> grid;
    private final ProductFormDrawer drawer;

    ProductCatalogView(ProductCategoryService productCategoryService, ProductCatalogService service) {
        this.service = service;

        // Create components
        var searchField = new TextField();
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setValueChangeMode(ValueChangeMode.LAZY);

        grid = new Grid<>();
        grid.addColumn(ProductCatalogItem::name)
                .setHeader("Name")
                .setSortProperty(ProductSortProperty.NAME.name());
        grid.addColumn(ProductCatalogItem::price)
                .setHeader("Price")
                .setTextAlign(ColumnTextAlign.END)
                .setSortProperty(ProductSortProperty.PRICE.name());
        grid.addColumn(ProductCatalogItem::description)
                .setHeader("Description")
                .setSortProperty(ProductSortProperty.DESCRIPTION.name());
        grid.addColumn(item -> item.category().name())
                .setHeader("Category")
                .setSortProperty(ProductSortProperty.CATEGORY.name());
        grid.addColumn(ProductCatalogItem::brand)
                .setHeader("Brand")
                .setSortProperty(ProductSortProperty.BRAND.name());

        var dataProvider = DataProvider.fromFilteringCallbacks(
                        (CallbackDataProvider.FetchCallback<ProductCatalogItem, ProductFilter>) query -> service.findItems(VaadinUtils.fromVaadinQuery(query, ProductSortProperty::valueOf)).stream(),
                        (CallbackDataProvider.CountCallback<ProductCatalogItem, ProductFilter>) query -> service.countItems(VaadinUtils.fromVaadinQuery(query, ProductSortProperty::valueOf)))
                .withConfigurableFilter();
        grid.setDataProvider(dataProvider);

        drawer = new ProductFormDrawer(
                productCategoryService,
                productDetails -> {
                    var saved = service.save(productDetails);
                    grid.getDataProvider().refreshAll();
                    return saved;
                }, this::handleException);

        searchField.addValueChangeListener(e ->
                dataProvider.setFilter(new ProductFilter(searchField.getValue()))
        );

        grid.addSelectionListener(e -> e.getFirstSelectedItem()
                .map(ProductCatalogItem::productId)
                .ifPresentOrElse(
                        ProductCatalogView::showProductDetails,
                        ProductCatalogView::showProductCatalog
                ));

        var addButton = new Button("Add Product", e ->
                new AddProductDialog(
                        productCategoryService,
                        productDetails -> {
                            var saved = service.save(productDetails);
                            grid.getDataProvider().refreshAll();
                            showProductDetails(saved.getProductId());
                        },
                        this::handleException
                ).open()
        );

        // Layout view
        setSizeFull();
        setSpacing(false);

        var toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.addToStart(searchField);
        toolbar.addToEnd(addButton);

        var listLayout = new VerticalLayout(toolbar, grid);
        listLayout.setSizeFull();
        grid.setSizeFull();

        add(listLayout, drawer);
        setFlexShrink(0, drawer);
    }

    private void handleException(RuntimeException exception) {
        if (exception instanceof OptimisticLockingFailureException) {
            var notification = new Notification(
                    "Another user has edited the same product. "
                            + "Please refresh and try again.");
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.WARNING);
            notification.setDuration(3000);
            notification.open();
        } else if (exception instanceof DataIntegrityViolationException) {
            var notification = new Notification(
                    "The SKU is already in use. Please enter another one.");
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.WARNING);
            notification.setDuration(3000);
            notification.open();
        } else {
            // Delegate to Vaadin's default error handler
            throw exception;
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long productId) {
        // Update grid selection
        Optional.ofNullable(productId)
                .map(ProductId::new)
                .flatMap(service::findItemById)
                .ifPresentOrElse(grid::select, grid::deselectAll);
        // Show or hide the drawer
        drawer.setProductDetails(Optional.ofNullable(productId)
                .map(ProductId::new)
                .flatMap(service::findDetailsById)
                .orElse(null));
    }

    public static void showProductDetails(ProductId productId) {
        UI.getCurrent().navigate(ProductCatalogView.class, productId.id());
    }

    public static void showProductCatalog() {
        UI.getCurrent().navigate(ProductCatalogView.class);
    }
}