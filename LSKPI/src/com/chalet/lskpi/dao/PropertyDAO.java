package com.chalet.lskpi.dao;

import java.util.List;

import com.chalet.lskpi.model.Property;

public interface PropertyDAO {

    public void insert(Property property) throws Exception;
    public void insert(List<Property> properties) throws Exception;
    
    public void update(Property property) throws Exception;
    
    public String getPropertyValueByName(String propertyName) throws Exception;
}
