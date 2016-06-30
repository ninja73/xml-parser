package com.parser.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Offer {

    @XmlAttribute
    private Long id;
    private String url;
    private Long price;
    private String currencyId;
    private Long categoryId;
    private String picture;
    private Boolean delivery;
    private Integer localDeliveryCost;
    private String typePrefix;
    private String vendor;
    private String vendorCode;
    private String model;
    private String description;
    private Boolean manufacturerWarranty;

    @XmlElement(name = "param")
    private List<Param> params = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean getDelivery() {
        return delivery;
    }

    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }

    public Integer getLocalDeliveryCost() {
        return localDeliveryCost;
    }

    public void setLocalDeliveryCost(Integer localDeliveryCost) {
        this.localDeliveryCost = localDeliveryCost;
    }

    public String getTypePrefix() {
        return typePrefix;
    }

    public void setTypePrefix(String typePrefix) {
        this.typePrefix = typePrefix;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getManufacturerWarranty() {
        return manufacturerWarranty;
    }

    public void setManufacturerWarranty(Boolean manufacturerWarranty) {
        this.manufacturerWarranty = manufacturerWarranty;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Offer && this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 1;
        result = prime * result + getLongHashCode(id);
        result = prime * result + getLongHashCode(price);
        result = prime * result + getLongHashCode(url);
        result = prime * result + getLongHashCode(currencyId);
        result = prime * result + getLongHashCode(categoryId);
        result = prime * result + getLongHashCode(picture);
        result = prime * result + getLongHashCode(delivery);
        result = prime * result + getLongHashCode(localDeliveryCost);
        result = prime * result + getLongHashCode(typePrefix);
        result = prime * result + getLongHashCode(vendor);
        result = prime * result + getLongHashCode(vendorCode);
        result = prime * result + getLongHashCode(model);
        result = prime * result + getLongHashCode(description);
        result = prime * result + getLongHashCode(manufacturerWarranty);
        for(Param param : params) {
            result = prime * result + param.hashCode();
        }
        return result;
    }
    private int getLongHashCode(Object value) {
        if(value == null) return 0;
        if(value instanceof Long)
            return (int)((Long)value ^ ((Long)value >>> 32));
        else if(value instanceof String || value instanceof Integer)
            return (value.hashCode());
        else if(value instanceof Boolean)
            return ((Boolean)value ? 1 : 0 );
        else return 0;
    }
}
