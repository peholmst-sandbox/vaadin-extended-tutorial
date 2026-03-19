package com.example.product;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.jspecify.annotations.Nullable;
import org.vaadin.tutorial.backend.financial.Money;
import org.vaadin.tutorial.backend.product.ProductCategory;
import org.vaadin.tutorial.backend.product.ProductCategoryService;
import org.vaadin.tutorial.backend.product.ProductDetails;
import org.vaadin.tutorial.backend.product.SKU;

import java.util.Optional;

import static org.vaadin.tutorial.backend.util.VaadinUtils.flatMapOrNull;
import static org.vaadin.tutorial.backend.util.VaadinUtils.mapOrDefault;
import static org.vaadin.tutorial.backend.util.VaadinUtils.mapOrNull;

class ProductForm extends Composite<FormLayout> {

    private final Binder<ProductDetails> binder;

    ProductForm(ProductCategoryService productCategoryService) {
        // Create components
        var nameField = new TextField("Name");
        var descriptionField = new TextArea("Description");
        var categoryField = new ComboBox<>("Category", productCategoryService.findAll());
        categoryField.setItemLabelGenerator(ProductCategory::name);
        var brandField = new TextField("Brand");
        var skuField = new TextField("SKU");
        var releaseDateField = new DatePicker("Release Date");
        var priceField = new BigDecimalField("Price");
        var discountField = new BigDecimalField("Discount");

        // Layout form
        var layout = getContent();
        layout.add(nameField);
        layout.add(descriptionField);
        layout.add(categoryField);
        layout.add(brandField);
        layout.add(skuField);
        layout.add(releaseDateField);
        layout.add(priceField);
        layout.add(discountField);

        // Bind fields
        binder = new Binder<>();
        binder.forField(nameField).asRequired("Enter name")
                .bind(ProductDetails::getName, ProductDetails::setName);
        binder.forField(descriptionField).asRequired("Enter description")
                .bind(ProductDetails::getDescription, ProductDetails::setDescription);
        binder.forField(categoryField).asRequired("Enter category")
                .withConverter(mapOrNull(ProductCategory::productCategoryId), flatMapOrNull(productCategoryService::findById))
                .bind(ProductDetails::getCategory, ProductDetails::setCategory);
        binder.forField(brandField)
                .bind(ProductDetails::getBrand, ProductDetails::setBrand);
        binder.forField(skuField).asRequired("Enter SKU")
                .withConverter(mapOrDefault(SKU::new, "", null), mapOrDefault(SKU::value, null, ""))
                .bind(ProductDetails::getSku, ProductDetails::setSku);
        binder.forField(releaseDateField)
                .bind(ProductDetails::getReleaseDate, ProductDetails::setReleaseDate);
        binder.forField(priceField).asRequired("Enter price")
                .withConverter(mapOrNull(Money::new), mapOrNull(Money::amount))
                .bind(ProductDetails::getPrice, ProductDetails::setPrice);
        binder.forField(discountField)
                .withConverter(mapOrNull(Money::new), mapOrNull(Money::amount))
                .bind(ProductDetails::getDiscount, ProductDetails::setDiscount);
    }

    public void setFormDataObject(@Nullable ProductDetails productDetails) {
        binder.setBean(productDetails);
    }

    public Optional<ProductDetails> getFormDataObject() {
        if (binder.getBean() == null) {
            throw new IllegalStateException("No form data object");
        }
        if (binder.validate().isOk()) {
            return Optional.of(binder.getBean());
        } else {
            return Optional.empty();
        }
    }
}
