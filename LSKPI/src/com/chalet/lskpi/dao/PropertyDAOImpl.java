package com.chalet.lskpi.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.chalet.lskpi.mapper.UserInfoRowMapper;
import com.chalet.lskpi.model.Property;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.utils.DataBean;

@Repository("propertyDAO")
public class PropertyDAOImpl implements PropertyDAO {

    @Autowired
    @Qualifier("dataBean")
    private DataBean dataBean;
    
    public void insert(final Property property) throws Exception {
        String insertSQL = "insert into tbl_property values(null,?,?)";
        dataBean.getJdbcTemplate().update(insertSQL, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, property.getPropertyName());
                ps.setString(2, property.getPropertyValue());
            }
        });
    }

    public void update(Property property) throws Exception {
        StringBuffer sql = new StringBuffer("update tbl_property set ");
        sql.append("property_value=?");
        sql.append(" where property_name=? ");
        dataBean.getJdbcTemplate().update(sql.toString(), property.getPropertyValue(),property.getPropertyName());
    }

    public String getPropertyValueByName(String propertyName) throws Exception {
        String sql = "select property_value from tbl_property where property_name = ?";
        return dataBean.getJdbcTemplate().queryForObject(sql, new Object[]{propertyName}, String.class);
    }

    public void insert(final List<Property> properties) throws Exception {
        String insertSQL = "insert into tbl_property values(null,?,?)";
        dataBean.getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
            
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, properties.get(i).getPropertyName());
                ps.setString(2, properties.get(i).getPropertyValue());
            }
            
            @Override
            public int getBatchSize() {
                return properties.size();
            }
        });
    }

}
