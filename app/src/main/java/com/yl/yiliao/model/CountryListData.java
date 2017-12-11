package com.yl.yiliao.model;


import java.io.Serializable;
import java.util.List;

public class CountryListData extends BaseModel<CountryListData> implements Serializable{

    private static final long serialVersionUID = -3029997135929786902L;
    private List<CountryItem> CountryRegion;

    public List<CountryItem> getCountryRegion() {
        return CountryRegion;
    }

    public void setCountryRegion(List<CountryItem> countryRegion) {
        CountryRegion = countryRegion;
    }
}
