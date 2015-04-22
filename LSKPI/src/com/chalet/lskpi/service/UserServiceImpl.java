package com.chalet.lskpi.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.chalet.lskpi.dao.PediatricsDAO;
import com.chalet.lskpi.dao.RespirologyDAO;
import com.chalet.lskpi.dao.UserDAO;
import com.chalet.lskpi.model.DDIData;
import com.chalet.lskpi.model.HospitalUserRefer;
import com.chalet.lskpi.model.UserCode;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WebUserInfo;
import com.chalet.lskpi.utils.ChaletMD5Utils;

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午5:30:57
 * 类说明
 */

@Service("userService")
public class UserServiceImpl implements UserService {

    Logger logger = Logger.getLogger(UserServiceImpl.class);
    
	@Autowired
	@Qualifier("userDAO")
	private UserDAO userDAO;
	
	@Autowired
	@Qualifier("pediatricsDAO")
	private PediatricsDAO pediatricsDAO;
	
	@Autowired
	@Qualifier("respirologyDAO")
	private RespirologyDAO respirologyDAO;
	

	@Override
	public void deleteBMUsers() throws Exception {
		userDAO.deleteBMUsers();
	}
	
	@Override
	public UserInfo getUserInfoByTel(String telephone) throws Exception {
		return userDAO.getUserInfoByTel(telephone);
	}
	
	@Override
	public UserInfo getUserInfoByUserCode(String userCode) throws Exception {
	    return userDAO.getUserInfoByUserCode(userCode);
	}
	
	@Override
	public UserInfo getUserInfoByRegion(String region) throws Exception {
		return userDAO.getUserInfoByRegion(region);
	}
	
	@Override
	public UserInfo getUserInfoByRegionCenter(String regionCenter) throws Exception {
		return userDAO.getUserInfoByRegionCenter(regionCenter);
	}
	
	public List<UserInfo> getUserInfoByLevel(String level) throws Exception {
	    return userDAO.getUserInfoByLevel(level);
	}

	@Override
	public void insert(List<UserInfo> userInfos) throws Exception {
	    try{
	        userDAO.insert(userInfos);
	    }catch(Exception e){
	        logger.error("fail to insert the user info,",e);
	        throw new Exception("更新用户信息失败，请确保数据格式及内容正确");
	    }
	}
	
	@Override
	public void insertBMUsers(List<UserInfo> userInfos) throws Exception {
		for( UserInfo user : userInfos ){
			UserInfo dbuser = null;
			try{
				dbuser = userDAO.getUserInfoByTel(user.getTelephone());
			}catch(EmptyResultDataAccessException erd){
	            logger.info("there is no user found.");
	        } catch(Exception e){
	            logger.error(String.format("fail to get the web user info by telephone - %s", user.getTelephone()),e);
	        }
			if( null == dbuser || "".equalsIgnoreCase(dbuser.getTelephone())){
			    try{
			        userDAO.insert(user);
			    }catch(Exception e){
		            logger.error("fail to insert the user info,",e);
		            throw new Exception("更新用户信息失败，请确保数据格式及内容正确");
		        }
			}else{
				logger.info(String.format("the user is existing in DB whose telephone is %s, so no need to insert again.", user.getTelephone()));
			}
		}
	}
	
	@Override
	public void insertDDI(List<DDIData> ddiDatas) throws Exception {
		userDAO.insertDDI(ddiDatas);
	}

	@Override
	public void delete() throws Exception {
		userDAO.delete();
	}
	
	@Override
	public void deleteDDI() throws Exception {
	    userDAO.deleteDDI();
	}

    public WebUserInfo getWebUser(String telephone, String password) throws Exception {
        try{
            return userDAO.getWebUser(telephone, ChaletMD5Utils.MD5(password));
        } catch(EmptyResultDataAccessException erd){
            logger.info("there is no web user found.");
            return null;
        } catch(Exception e){
            logger.error("fail to get the web user info by telephone - " + telephone,e);
            return null;
        }
    }

	@Override
	public List<UserInfo> getLowerUserOfCurrentUser(UserInfo currentUser,String department) throws Exception {
		return userDAO.getLowerUserOfCurrentUser(currentUser,department);
	}
	
	@Override
	public List<UserInfo> getLowerUserOfCurrentUser4MonthData(UserInfo currentUser) throws Exception {
		if( !"BM".equalsIgnoreCase(currentUser.getLevel()) 
				&& !"RSD".equalsIgnoreCase(currentUser.getLevel())
				&& !"RSM".equalsIgnoreCase(currentUser.getLevel()) ){
			return null;
		}
		return userDAO.getLowerUserOfCurrentUser4MonthData(currentUser);
	}

    public void deleteHosUsers() throws Exception {
        userDAO.deleteHosUsers();
    }

    public void insertHosUsers(List<HospitalUserRefer> hosUsers) throws Exception {
        if( null != hosUsers && hosUsers.size() > 0){
            userDAO.insertHosUsers(hosUsers);
        }else{
            logger.info("the size of the list hosUsers is 0, no need to insert the data");
        }
    }

	@Override
	public void updateUserCodes(List<UserCode> userCodes) throws Exception {
		logger.info("start to update the user codes in PED");
		pediatricsDAO.updatePEDUserCodes(userCodes);
		logger.info("start to update the user codes in RES");
		respirologyDAO.updateRESUserCodes(userCodes);
	}

    public List<UserInfo> getSalesOfCurrentUser(UserInfo currentUser) throws Exception {
        return userDAO.getSalesOfCurrentUser(currentUser);
    }

    public List<String> getAllRegionName() throws Exception {
        return userDAO.getAllRegionName();
    }
    
    public List<String> getAllRSMRegion() throws Exception {
        return userDAO.getAllRSMRegion();
    }
    
    public List<String> getAllDSMName() throws Exception {
    	return userDAO.getAllDSMName();
    }
}
