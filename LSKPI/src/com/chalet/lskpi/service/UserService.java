package com.chalet.lskpi.service;

import java.util.List;

import com.chalet.lskpi.model.DDIData;
import com.chalet.lskpi.model.HospitalUserRefer;
import com.chalet.lskpi.model.UserCode;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WebUserInfo;

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午5:30:35
 * 类说明
 */

public interface UserService {

	public UserInfo getUserInfoByTel(String telephone) throws Exception;
	public UserInfo getUserInfoByUserCode(String userCode) throws Exception;
	public UserInfo getUserInfoByRegion(String region) throws Exception;
	public UserInfo getUserInfoByRegionCenter(String regionCenter) throws Exception;
	public List<UserInfo> getUserInfoByLevel(String level) throws Exception;
	public void insert(List<UserInfo> userInfos) throws Exception;
	public void insertBMUsers(List<UserInfo> userInfos) throws Exception;
	public void delete() throws Exception;
	public void deleteDDI() throws Exception;
	public void deleteBMUsers() throws Exception;
	
	public WebUserInfo getWebUser(String telephone, String password) throws Exception;
	
	public void insertDDI(List<DDIData> ddiDatas) throws Exception;
	
	public List<UserInfo> getLowerUserOfCurrentUser(UserInfo currentUser,String department) throws Exception;
	
	public List<UserInfo> getLowerUserOfCurrentUser4MonthData(UserInfo currentUser) throws Exception;
	
	public void deleteHosUsers() throws Exception;
	public void insertHosUsers(List<HospitalUserRefer> hosUsers) throws Exception;
	
	public void updateUserCodes(List<UserCode> userCodes) throws Exception;
	
	public List<UserInfo> getSalesOfCurrentUser(UserInfo currentUser ) throws Exception;
	
	public List<String> getAllRegionName() throws Exception;
	
	public List<String> getAllRSMRegion() throws Exception;
	
	public List<String> getAllDSMName() throws Exception;
	
}
