package com.example.product;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import org.vaadin.tutorial.backend.product.ProductCategoryService;
import org.vaadin.tutorial.backend.product.ProductDetails;

class AddProductDialog extends Dialog {

    private final SaveCallback saveCallback;
    private final ErrorCallback errorCallback;
    private final ProductForm form;

    AddProductDialog(ProductCategoryService productCategoryService, SaveCallback saveCallback, ErrorCallback errorCallback) {
        this.saveCallback = saveCallback;
        this.errorCallback = errorCallback;

        // Create components
        form = new ProductForm(productCategoryService);
        form.setFormDataObject(new ProductDetails());
        var saveButton = new Button("Save", e -> save());
        saveButton.addThemeVariants(ButtonVariant.PRIMARY);
        var cancelButton = new Button("Cancel", e -> close());

        // Layout dialog
        setHeaderTitle("Add Product");
        add(form);
        getFooter().add(cancelButton, saveButton);
    }

    private void save() {
        form.getFormDataObject().ifPresent(productDetails -> {
            try {
                saveCallback.save(productDetails);
                close();
            } catch (RuntimeException e) {
                errorCallback.handleException(e);
            }
        });
    }

    @FunctionalInterface
    interface SaveCallback {
        void save(ProductDetails productDetails);
    }

    @FunctionalInterface
    interface ErrorCallback {
        void handleException(RuntimeException e);
    }
}