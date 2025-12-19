package com.example.product;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jspecify.annotations.Nullable;

class ProductFormDrawer extends Composite<VerticalLayout> {

    private final ProductForm form;
    private final SaveCallback saveCallback;
    private final ErrorCallback errorCallback;

    ProductFormDrawer(SaveCallback saveCallback, ErrorCallback errorCallback) {
        this.saveCallback = saveCallback;
        this.errorCallback = errorCallback;

        var header = new H2("Product Details");
        form = new ProductForm();

        var saveButton = new Button("Save", e -> save());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var layout = getContent();
        layout.add(header);
        layout.add(new Scroller(form));
        layout.add(saveButton);
        layout.setWidth("300px");
        addClassName(LumoUtility.BoxShadow.MEDIUM);
        setVisible(false);
    }

    public void setProductDetails(@Nullable ProductDetails productDetails) {
        form.setFormDataObject(productDetails);
        setVisible(productDetails != null);
    }

    private void save() {
        form.getFormDataObject().ifPresent(productDetails -> {
            try {
                var saved = saveCallback.save(productDetails);
                form.setFormDataObject(saved);
            } catch (RuntimeException e) {
                errorCallback.handleException(e);
            }
        });
    }

    @FunctionalInterface
    interface SaveCallback {
        ProductDetails save(ProductDetails productDetails);
    }

    @FunctionalInterface
    interface ErrorCallback {
        void handleException(RuntimeException e);
    }
}