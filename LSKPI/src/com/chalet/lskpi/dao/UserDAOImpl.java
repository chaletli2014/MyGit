package com.chalet.lskpi.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.chalet.lskpi.mapper.UserInfoRowMapper;
import com.chalet.lskpi.model.DDIData;
import com.chalet.lskpi.model.HospitalUserRefer;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WebUserInfo;
import com.chalet.lskpi.utils.DataBean;
import com.chalet.lskpi.utils.LsAttributes;

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午5:19:10
 * 类说明
 */

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

	@Autowired
	@Qualifier("dataBean")
	private DataBean dataBean;
	
	private Logger logger = Logger.getLogger(UserDAOImpl.class);
	
	@Override
	public List<UserInfo> getLowerUserOfCurrentUser(UserInfo currentUser, String department) throws Exception {
		String sql = "";
		Object[] params = null;
		switch(currentUser.getLevel()){
			case LsAttributes.USER_LEVEL_DSM:
				if( LsAttributes.DEPARTMENT_RES.equalsIgnoreCase(department) ){
					sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level='REP' and superior = ? and userCode in (select distinct rdw.saleCode from tbl_respirology_data_weekly rdw, tbl_hospital h where rdw.hospitalCode = h.code and h.isResAssessed='1' ) and userCode !='"+LsAttributes.VACANT_USER_CODE+"'";
				}else{
					sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level='REP' and superior = ? and userCode in (select distinct saleCode from tbl_pediatrics_data_weekly) and userCode !='"+LsAttributes.VACANT_USER_CODE+"'";
				}
				params = new Object[]{currentUser.getUserCode()};
				break;
			case LsAttributes.USER_LEVEL_RSM:
				sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level='DSM' and region = ? and userCode !='"+LsAttributes.VACANT_USER_CODE+"'";
				params = new Object[]{currentUser.getRegion()};
				break;
			case LsAttributes.USER_LEVEL_RSD:
				sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level='RSM' and regionCenter = ? and userCode !='"+LsAttributes.VACANT_USER_CODE+"'";
				params = new Object[]{currentUser.getRegionCenter()};
				break;
			case LsAttributes.USER_LEVEL_BM:
				sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level='RSM' and userCode !='"+LsAttributes.VACANT_USER_CODE+"'";
				params = new Object[]{};
				break;
		}
	    return dataBean.getJdbcTemplate().query(sql, params, new UserInfoRowMapper());
	}
	
	@Override
	public List<UserInfo> getLowerUserOfCurrentUser4MonthData(UserInfo currentUser) throws Exception {
		String sql = "";
		Object[] params = null;
		switch(currentUser.getLevel()){
		case LsAttributes.USER_LEVEL_RSM:
			sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level='DSM' and region = ? and userCode !='"+LsAttributes.VACANT_USER_CODE+"'";
			params = new Object[]{currentUser.getRegion()};
			break;
		case LsAttributes.USER_LEVEL_RSD:
			sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level='RSM' and regionCenter = ? and userCode !='"+LsAttributes.VACANT_USER_CODE+"'";
			params = new Object[]{currentUser.getRegionCenter()};
			break;
		case LsAttributes.USER_LEVEL_BM:
			sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level='RSD'";
			params = new Object[]{};
			break;
		}
		return dataBean.getJdbcTemplate().query(sql, params, new UserInfoRowMapper());
	}
	
	@Override
	public UserInfo getUserInfoByTel(String telephone) throws Exception {
		UserInfo userInfo = new UserInfo();
		String sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where telephone = ?";
		userInfo = dataBean.getJdbcTemplate().queryForObject(sql, new Object[]{telephone}, new UserInfoRowMapper());
		return userInfo;
	}
	
	@Override
	public UserInfo getUserInfoByUserCode(String userCode) throws Exception {
	    UserInfo userInfo = new UserInfo();
	    String sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where userCode = ?";
	    logger.info("get user info by user code - " + userCode);
	    userInfo = dataBean.getJdbcTemplate().queryForObject(sql, new Object[]{userCode}, new UserInfoRowMapper());
	    return userInfo;
	}
	
	@Override
	public UserInfo getUserInfoByRegion(String region) throws Exception {
		UserInfo userInfo = new UserInfo();
		String sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where region = ? and level='RSM' ";
		logger.info("get user info by region - " + region);
		userInfo = dataBean.getJdbcTemplate().queryForObject(sql, new Object[]{region}, new UserInfoRowMapper());
		return userInfo;
	}
	
	@Override
	public UserInfo getUserInfoByRegionCenter(String regionCenter) throws Exception {
		UserInfo userInfo = new UserInfo();
		String sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where regionCenter = ? and level='RSD' ";
		logger.info(String.format("get user info by regionCenter - %s", regionCenter));
		userInfo = dataBean.getJdbcTemplate().queryForObject(sql, new Object[]{regionCenter}, new UserInfoRowMapper());
		return userInfo;
	}
	
	public List<UserInfo> getUserInfoByLevel(String level) throws Exception {
		String sql = "";
		if( null != level && LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(level) ){
			sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level not in ('REP','DSM','RSM','RSD')";
		}else{
			sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level = '"+level+"' order by regionCenter, region";
		}
		return dataBean.getJdbcTemplate().query(sql, new UserInfoRowMapper());
	}
	
	@Override
	public void insert(final List<UserInfo> userInfos) throws Exception {
		String insertSQL = "insert into tbl_userinfo(id,name,telephone,etmsCode,userCode,BU,regionCenter,region,teamCode,team,level,superior,createdate,modifydate,email) values(null,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?)";
		dataBean.getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, userInfos.get(i).getName());
				ps.setString(2, userInfos.get(i).getTelephone());
				ps.setString(3, userInfos.get(i).getEtmsCode());
				ps.setString(4, userInfos.get(i).getUserCode());
				ps.setString(5, userInfos.get(i).getBu());
				ps.setString(6, userInfos.get(i).getRegionCenter());
				ps.setString(7, userInfos.get(i).getRegion());
				ps.setString(8, userInfos.get(i).getTeamCode());
				ps.setString(9, userInfos.get(i).getTeam());
				ps.setString(10, userInfos.get(i).getLevel());
				ps.setString(11, userInfos.get(i).getSuperior());
				ps.setString(12, userInfos.get(i).getEmail());
			}
			
			@Override
			public int getBatchSize() {
				return userInfos.size();
			}
		});
	}
	
	@Override
	public void insert(final UserInfo userInfo) throws Exception {
		String insertSQL = "insert into tbl_userinfo(id,name,telephone,etmsCode,userCode,BU,regionCenter,region,teamCode,team,level,superior,createdate,modifydate,email) values(null,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?)";
		dataBean.getJdbcTemplate().update(insertSQL, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userInfo.getName());
				ps.setString(2, userInfo.getTelephone());
				ps.setString(3, userInfo.getEtmsCode());
				ps.setString(4, userInfo.getUserCode());
				ps.setString(5, userInfo.getBu());
				ps.setString(6, userInfo.getRegionCenter());
				ps.setString(7, userInfo.getRegion());
				ps.setString(8, userInfo.getTeamCode());
				ps.setString(9, userInfo.getTeam());
				ps.setString(10, userInfo.getLevel());
				ps.setString(11, userInfo.getSuperior());
				ps.setString(12, userInfo.getEmail());
			}
		});
	}
	
	@Override
	public void insertDDI(final List<DDIData> ddiDatas) throws Exception {
		String insertSQL = "insert into tbl_ddi_data values(null,?,?,?)";
		dataBean.getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setDouble(1, ddiDatas.get(i).getNum());
				ps.setString(2, ddiDatas.get(i).getRegion());
				ps.setString(3, ddiDatas.get(i).getDuration());
			}
			@Override
			public int getBatchSize() {
				return ddiDatas.size();
			}
		});
	}

	@Override
	public void delete() throws Exception {
		dataBean.getJdbcTemplate().update("delete from tbl_userinfo");
	}

    public void deleteDDI() throws Exception {
        dataBean.getJdbcTemplate().update("delete from tbl_ddi_data");
    }
    
    @Override
    public void deleteBMUsers() throws Exception {
        dataBean.getJdbcTemplate().update("delete from tbl_userinfo where level not in('RSD','RSM','DSM','REP')");
    }
	
    public WebUserInfo getWebUser(String telephone, String password) throws Exception {
        WebUserInfo userInfo = new WebUserInfo();
        String sql = "select * from tbl_web_userinfo where telephone = ? and password = ?";
        userInfo = dataBean.getJdbcTemplate().queryForObject(sql, new Object[]{telephone, password}, new WebUserInfoRowMapper());
        return userInfo;
    }
    
    class WebUserInfoRowMapper implements RowMapper<WebUserInfo>{
        @Override
        public WebUserInfo mapRow(ResultSet rs, int i) throws SQLException {
            WebUserInfo dbUser = new WebUserInfo();
            dbUser.setId(rs.getInt("id"));
            dbUser.setName(rs.getString("name"));
            dbUser.setTelephone(rs.getString("telephone"));
            dbUser.setLevel(rs.getString("level"));
            dbUser.setCreatedate(rs.getDate("createdate"));
            return dbUser;
        }
        
    }

    public void deleteHosUsers() throws Exception {
        dataBean.getJdbcTemplate().update("delete from tbl_hos_user");
    }

    public void insertHosUsers(final List<HospitalUserRefer> hosUsers) throws Exception {
        String insertSQL = "insert into tbl_hos_user(hosCode,userCode,isPrimary) values(?,?,?)";
        dataBean.getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
            
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, hosUsers.get(i).getHospitalCode());
                ps.setString(2, hosUsers.get(i).getUserCode());
                ps.setString(3, hosUsers.get(i).getIsPrimary());
            }
            
            @Override
            public int getBatchSize() {
                return hosUsers.size();
            }
        });
    }

    public List<UserInfo> getSalesOfCurrentUser(UserInfo currentUser) throws Exception {
        String sql = "";
        Object[] params = null;
        switch(currentUser.getLevel()){
            case LsAttributes.USER_LEVEL_DSM:
                sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level='REP' and superior = ? and userCode !='"+LsAttributes.VACANT_USER_CODE+"'";
                params = new Object[]{currentUser.getUserCode()};
                break;
            case LsAttributes.USER_LEVEL_RSM:
                sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level='REP' and region = ? and userCode !='"+LsAttributes.VACANT_USER_CODE+"'";
                params = new Object[]{currentUser.getRegion()};
                break;
            case LsAttributes.USER_LEVEL_RSD:
                sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level='REP' and regionCenter = ? and userCode !='"+LsAttributes.VACANT_USER_CODE+"'";
                params = new Object[]{currentUser.getRegionCenter()};
                break;
            case LsAttributes.USER_LEVEL_BM:
                sql = "select *, (select distinct property_value from tbl_property where property_name=regionCenter) as regionCenterCN from tbl_userinfo where level='REP' and userCode !='"+LsAttributes.VACANT_USER_CODE+"'";
                params = new Object[]{};
                break;
        }
        return dataBean.getJdbcTemplate().query(sql, params, new UserInfoRowMapper());
    }

    public List<String> getAllRegionName() throws Exception {
        return dataBean.getJdbcTemplate().queryForList("select distinct regionCenter from tbl_userinfo where regionCenter is not null", String.class);
    }
    
    public List<String> getAllRSMRegion() throws Exception {
        return dataBean.getJdbcTemplate().queryForList("select distinct region from tbl_userinfo where region is not null order by region ", String.class);
    }
    
    public List<String> getAllDSMName() throws Exception {
    	return dataBean.getJdbcTemplate().queryForList("select distinct concat(region,'-',userCode,'-',name) from tbl_userinfo where level = 'DSM' ", String.class);
    }
}
