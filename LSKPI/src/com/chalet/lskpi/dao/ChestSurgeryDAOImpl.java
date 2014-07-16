package com.chalet.lskpi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.chalet.lskpi.mapper.ChestSurgeryMobileRowMapper;
import com.chalet.lskpi.mapper.ChestSurgeryRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomRSMDataRowMapper;
import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MobileCHEDailyData;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.utils.DataBean;
import com.chalet.lskpi.utils.DateUtils;
import com.chalet.lskpi.utils.LsAttributes;

@Repository("chestSurgeryDAO")
public class ChestSurgeryDAOImpl implements ChestSurgeryDAO {

    private Logger logger = Logger.getLogger(ChestSurgeryDAOImpl.class);
    
    @Autowired
    @Qualifier("dataBean")
    private DataBean dataBean;
    
    public ChestSurgeryData getChestSurgeryDataByHospital(String hospitalCode) throws Exception {
        StringBuffer sql = new StringBuffer("");
        sql.append(" select cd.* ")
        .append(" , h.code as hospitalCode, h.name as hospitalName, h.dsmName, h.saleCode as salesCode")
        .append(" , (select name from tbl_userinfo u where u.region = h.rsmRegion and u.superior = h.dsmCode and u.userCode = h.saleCode and u.level='REP') as salesName ")
        .append(" , h.region, h.rsmRegion ")
        .append(" from tbl_chestSurgery_data cd, tbl_hospital h ")
        .append(" where cd.hospitalCode=? ")
        .append(" and DATE_FORMAT(cd.createdate,'%Y-%m-%d') = curdate() ")
        .append(" and cd.hospitalCode = h.code");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{hospitalCode}, new ChestSurgeryRowMapper());
    }

    public List<ChestSurgeryData> getChestSurgeryDataByDate(Date createdatebegin, Date createdateend) throws Exception {
        StringBuffer sql = new StringBuffer("");
        sql.append(" select cd.* ")
        .append(" , h.code as hospitalCode, h.name as hospitalName, h.dsmName, h.saleCode as salesCode ")
        .append(" , (select name from tbl_userinfo u where u.region = h.rsmRegion and u.superior = h.dsmCode and u.userCode = h.saleCode and u.level='REP') as salesName ")
        .append(" , h.region, h.rsmRegion ")
        .append(" from tbl_chestSurgery_data cd, tbl_hospital h ")
        .append(" where cd.createdate between ? and ? ")
        .append(" and cd.hospitalCode = h.code ")
        .append(" order by cd.createdate desc");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{new Timestamp(createdatebegin.getTime()),new Timestamp(createdateend.getTime())},new ChestSurgeryRowMapper());
    }

    public ChestSurgeryData getChestSurgeryDataByHospitalAndDate(String hospitalCode, Date createdate) throws Exception {
        StringBuffer sql = new StringBuffer("");
        sql.append(" select cd.* ")
        .append(" , h.code as hospitalCode, h.name as hospitalName, h.dsmName, h.saleCode as salesCode ")
        .append(" , (select name from tbl_userinfo u where u.region = h.rsmRegion and u.superior = h.dsmCode and u.userCode = h.saleCode and u.level='REP') as salesName ")
        .append(" , h.region, h.rsmRegion ")
        .append(" from tbl_chestSurgery_data cd, tbl_hospital h ")
        .append(" where cd.hospitalCode = ? ")
        .append(" and DATE_FORMAT(cd.createdate,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') ")
        .append(" and cd.hospitalCode = h.code ")
        .append(" order by cd.createdate desc");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{hospitalCode,new Timestamp(createdate.getTime())}, new ChestSurgeryRowMapper());
    }

    public ChestSurgeryData getChestSurgeryDataById(int id) throws Exception {
        StringBuffer sql = new StringBuffer("");
        sql.append(" select cd.* ")
        .append(" , h.code as hospitalCode, h.name as hospitalName, h.dsmName, h.saleCode as salesCode ")
        .append(" , (select name from tbl_userinfo u where u.region = h.rsmRegion and u.superior = h.dsmCode and u.userCode = h.saleCode and u.level='REP') as salesName ")
        .append(" , h.region, h.rsmRegion ")
        .append(" from tbl_chestSurgery_data cd, tbl_hospital h ")
        .append(" where cd.id = ? ")
        .append(" and cd.hospitalCode = h.code ");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{id}, new ChestSurgeryRowMapper());
    }

    public void insert(final ChestSurgeryData chestSurgeryData, UserInfo operator, Hospital hospital) throws Exception {
        logger.info(">>ChestSurgeryDAOImpl insert");
        
        final String sql = "insert into tbl_chestSurgery_data values(null,NOW(),?,?,?,?,?,?,?,?,?,?,?,?,NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, chestSurgeryData.getHospitalCode());
                ps.setInt(2, chestSurgeryData.getPnum());
                ps.setInt(3, chestSurgeryData.getRisknum());
                ps.setInt(4, chestSurgeryData.getWhnum());
                ps.setInt(5, chestSurgeryData.getLsnum());
                ps.setDouble(6, chestSurgeryData.getOqd());
                ps.setDouble(7, chestSurgeryData.getTqd());
                ps.setDouble(8, chestSurgeryData.getOtid());
                ps.setDouble(9, chestSurgeryData.getTbid());
                ps.setDouble(10, chestSurgeryData.getTtid());
                ps.setDouble(11, chestSurgeryData.getThbid());
                ps.setDouble(12, chestSurgeryData.getFbid());
                return ps;
            }
        }, keyHolder);
        logger.info("insert chest surgery returned id is "+keyHolder.getKey().intValue());
    }

    public void insert(final ChestSurgeryData chestSurgeryData, String dsmCode) throws Exception {
        logger.info(">>ChestSurgeryDAOImpl insert - upload daily data");
        
        final String sql = "insert into tbl_chestSurgery_data values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setTimestamp(1, new Timestamp(chestSurgeryData.getCreatedate().getTime()));
                ps.setString(2, chestSurgeryData.getHospitalCode());
                ps.setInt(3, chestSurgeryData.getPnum());
                ps.setInt(4, chestSurgeryData.getRisknum());
                ps.setInt(5, chestSurgeryData.getWhnum());
                ps.setInt(6, chestSurgeryData.getLsnum());
                ps.setDouble(7, chestSurgeryData.getOqd());
                ps.setDouble(8, chestSurgeryData.getTqd());
                ps.setDouble(9, chestSurgeryData.getOtid());
                ps.setDouble(10, chestSurgeryData.getTbid());
                ps.setDouble(11, chestSurgeryData.getTtid());
                ps.setDouble(12, chestSurgeryData.getThbid());
                ps.setDouble(13, chestSurgeryData.getFbid());
                return ps;
            }
        }, keyHolder);
        logger.info("upload daily data, returned id is "+keyHolder.getKey().intValue());
    }

    public void update(ChestSurgeryData chestSurgeryData) throws Exception {
        StringBuffer sql = new StringBuffer("update tbl_chestSurgery_data set ");
        sql.append("updatedate=NOW()");
        sql.append(", pnum=? ");
        sql.append(", risknum=? ");
        sql.append(", whnum=? ");
        sql.append(", lsnum=? ");
        sql.append(", oqd=? ");
        sql.append(", tqd=? ");
        sql.append(", otid=? ");
        sql.append(", tbid=? ");
        sql.append(", ttid=? ");
        sql.append(", thbid=? ");
        sql.append(", fbid=? ");
        sql.append(" where id=? ");
        
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(chestSurgeryData.getPnum());
        paramList.add(chestSurgeryData.getRisknum());
        paramList.add(chestSurgeryData.getWhnum());
        paramList.add(chestSurgeryData.getLsnum());
        paramList.add(chestSurgeryData.getOqd());
        paramList.add(chestSurgeryData.getTqd());
        paramList.add(chestSurgeryData.getOtid());
        paramList.add(chestSurgeryData.getTbid());
        paramList.add(chestSurgeryData.getTtid());
        paramList.add(chestSurgeryData.getThbid());
        paramList.add(chestSurgeryData.getFbid());
        paramList.add(chestSurgeryData.getDataId());
        
        dataBean.getJdbcTemplate().update(sql.toString(), paramList.toArray());
    }

    public MobileCHEDailyData getDailyCHEData4CountoryMobile() throws Exception {
        StringBuffer mobileCHEDailySQL = new StringBuffer();
        
        Date date = DateUtils.populateParamDate(new Date());
        Timestamp startDate = new Timestamp(date.getTime());
        Timestamp endDate = new Timestamp(new Date(date.getTime() + 1* 24 * 60 * 60 * 1000).getTime());
        
        mobileCHEDailySQL.append("select '全国' as name,null as userCode,")
            .append(" '' as regionCenterCN, ")
            .append(" ( select count(1) from tbl_hospital h where h.isChestSurgeryAssessed='1' ) hosNum,")
            .append(LsAttributes.SQL_DAILYREPORT_SELECTION_CHE)
            .append(" from ( ")
            .append("   select cd.* from tbl_chestSurgery_data cd, tbl_hospital h ")
            .append("   where cd.hospitalCode = h.code ")
            .append("   and cd.createdate between ? and ? ")
            .append("   and h.isChestSurgeryAssessed='1' ")
            .append(" ) cd ");
        return dataBean.getJdbcTemplate().queryForObject(mobileCHEDailySQL.toString(), new Object[]{startDate,endDate},new ChestSurgeryMobileRowMapper());
    }

    public List<MobileCHEDailyData> getDailyCHEData4DSMMobile(String region) throws Exception {
        StringBuffer mobileCHEDailySQL = new StringBuffer();
        
        Date date = DateUtils.populateParamDate(new Date());
        Timestamp startDate = new Timestamp(date.getTime());
        Timestamp endDate = new Timestamp(new Date(date.getTime() + 1* 24 * 60 * 60 * 1000).getTime());
        
        mobileCHEDailySQL.append("select ui.name, ui.userCode,")
        .append(" (select property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h where h.dsmCode = ui.userCode and h.rsmRegion = ui.region and h.isChestSurgeryAssessed='1' ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_CHE)
        .append(" from ( ")
        .append(" select u.name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_CHE)
        .append(" from tbl_userinfo u, tbl_chestSurgery_data cd, tbl_hospital h1 ")
        .append(" where cd.hospitalCode = h1.code ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and h1.dsmCode = u.userCode ")
        .append(" and cd.createdate between ? and ? ")
        .append(" and h1.isChestSurgeryAssessed='1' ")
        .append(" and u.level='DSM' ")
        .append(" and u.region = ? ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='DSM' ")
        .append(" and ui.region = ?");
        return dataBean.getJdbcTemplate().query(mobileCHEDailySQL.toString(), new Object[]{startDate,endDate,region,region},new ChestSurgeryMobileRowMapper());
    }

    public List<MobileCHEDailyData> getDailyCHEData4RSMMobile(String regionCenter) throws Exception {
        StringBuffer mobileCHEDailySQL = new StringBuffer();
        
        Date date = DateUtils.populateParamDate(new Date());
        Timestamp startDate = new Timestamp(date.getTime());
        Timestamp endDate = new Timestamp(new Date(date.getTime() + 1* 24 * 60 * 60 * 1000).getTime());
        
        mobileCHEDailySQL.append("select ui.region as name, ui.userCode,")
        .append(" (select property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h where h.rsmRegion = ui.region and h.isChestSurgeryAssessed='1' ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_CHE)
        .append(" from ( ")
        .append(" select u.region as name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_CHE)
        .append(" from tbl_userinfo u, tbl_chestSurgery_data cd, tbl_hospital h1 ")
        .append(" where cd.hospitalCode = h1.code ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and cd.createdate between ? and ? ")
        .append(" and h1.isChestSurgeryAssessed='1' ")
        .append(" and u.level='RSM' ")
        .append(" and u.regionCenter = ? ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='RSM' ")
        .append(" and ui.regionCenter = ?")
        .append(" order by ui.region ");
        return dataBean.getJdbcTemplate().query(mobileCHEDailySQL.toString(), new Object[]{startDate,endDate,regionCenter,regionCenter},new ChestSurgeryMobileRowMapper());
    }

    public List<MobileCHEDailyData> getDailyCHEData4RSDMobile() throws Exception {
        StringBuffer mobileCHEDailySQL = new StringBuffer();
        
        Date date = DateUtils.populateParamDate(new Date());
        Timestamp startDate = new Timestamp(date.getTime());
        Timestamp endDate = new Timestamp(new Date(date.getTime() + 1* 24 * 60 * 60 * 1000).getTime());
        
        mobileCHEDailySQL.append("select ( select distinct property_value from tbl_property where property_name = ui.regionCenter ) as name, ui.userCode,")
        .append(" ( select count(1) from tbl_hospital h where h.region = ui.regionCenter and h.isChestSurgeryAssessed='1' ) hosNum, ")
        .append(" (select property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_CHE)
        .append(" from ( ")
        .append(" select u.regionCenter as name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_CHE)
        .append(" from tbl_userinfo u, tbl_chestSurgery_data cd, tbl_hospital h1 ")
        .append(" where cd.hospitalCode = h1.code ")
        .append(" and h1.region = u.regionCenter ")
        .append(" and cd.createdate between ? and ? ")
        .append(" and h1.isChestSurgeryAssessed='1' ")
        .append(" and u.level='RSD' ")
        .append(" group by u.regionCenter ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='RSD' ")
        .append(" order by ui.regionCenter ");
        return dataBean.getJdbcTemplate().query(mobileCHEDailySQL.toString(), new Object[]{startDate,endDate}, new ChestSurgeryMobileRowMapper());
    }

    public List<MobileCHEDailyData> getChildDailyCHEData4DSMMobile(String dsmCode) throws Exception {
        StringBuffer mobileCHEDailySQL = new StringBuffer();
        
        Date date = DateUtils.populateParamDate(new Date());
        Timestamp startDate = new Timestamp(date.getTime());
        Timestamp endDate = new Timestamp(new Date(date.getTime() + 1* 24 * 60 * 60 * 1000).getTime());
        
        mobileCHEDailySQL.append("select ui.name, ui.userCode,")
        .append(" (select property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h where h.saleCode = ui.userCode and h.rsmRegion = ui.region and h.dsmCode = ui.superior and h.isChestSurgeryAssessed='1' ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_CHE)
        .append(" from ( ")
        .append(" select u.name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_CHE)
        .append(" from tbl_userinfo u, tbl_chestSurgery_data cd, tbl_hospital h1 ")
        .append(" where cd.hospitalCode = h1.code ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and h1.dsmCode = u.superior ")
        .append(" and h1.saleCode = u.userCode ")
        .append(" and cd.createdate between ? and ? ")
        .append(" and h1.isChestSurgeryAssessed='1' ")
        .append(" and u.level='REP' ")
        .append(" and u.superior = ? ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='REP' ")
        .append(" and ui.superior = ?");
        return dataBean.getJdbcTemplate().query(mobileCHEDailySQL.toString(), new Object[]{startDate,endDate,dsmCode,dsmCode},new ChestSurgeryMobileRowMapper());
    }

    public TopAndBottomRSMData getTopAndBottomRSMData() throws Exception {
        StringBuffer sb = new StringBuffer();
        Date date = DateUtils.populateParamDate(new Date());
        Timestamp startDate = new Timestamp(date.getTime());
        Timestamp endDate = new Timestamp(new Date(date.getTime() + 1* 24 * 60 * 60 * 1000).getTime());
        
        sb.append("select inRateMinT.inRateMin, ")
            .append(" inRateMinT.inRateMinUser, ")
            .append(" inRateMaxT.inRateMax, ")
            .append(" inRateMaxT.inRateMaxUser, ")
            .append(" whRateMinT.whRateMin, ")
            .append(" whRateMinT.whRateMinUser, ")
            .append(" whRateMaxT.whRateMax, ")
            .append(" whRateMaxT.whRateMaxUser, ")
            .append(" averageDoseMinT.averageDoseMin, ")
            .append(" averageDoseMinT.averageDoseMinUser, ")
            .append(" averageDoseMaxT.averageDoseMax, ")
            .append(" averageDoseMaxT.averageDoseMaxUser ")
            .append(" from ") 
            .append(" ( select (inNumTemp.inNum/hosNumTemp.hosNum) as inRateMin,hosNumTemp.name as inRateMinUser ") 
            .append("   from ( ") 
            .append("       select IFNULL(count(1),0) as hosNum, h.rsmRegion, u.name ") 
            .append("       from tbl_hospital h, tbl_userinfo u ") 
            .append("       where h.rsmRegion = u.region ") 
            .append("       and h.isChestSurgeryAssessed='1' ") 
            .append("       and u.level='RSM' ") 
            .append("       group by u.region ") 
            .append("   ) hosNumTemp, ") 
            .append("       ( ") 
            .append("       select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from (")
            .append("           select IFNULL(count(1),0) as inNum, h.rsmRegion ")
            .append("           from tbl_chestSurgery_data cd, tbl_hospital h ")
            .append("           where cd.hospitalCode = h.code  ")
            .append("           and cd.createdate between ? and ? ")
            .append("           and h.isChestSurgeryAssessed='1' ")
            .append("           group by h.rsmRegion ")
            .append("       ) inNum1 right join tbl_userinfo u on u.region = inNum1.rsmRegion ")
            .append("       where u.level='RSM' ")
            .append("   ) inNumTemp")
            .append("   where hosNumTemp.rsmRegion = inNumTemp.rsmRegion ")
            .append("   order by inNumTemp.inNum/hosNumTemp.hosNum ")
            .append("   limit 1 ")
            .append(") inRateMinT,")
            .append("(  select (inNumTemp.inNum/hosNumTemp.hosNum) as inRateMax,hosNumTemp.name as inRateMaxUser ")
            .append("   from ( ")
            .append("       select IFNULL(count(1),0) as hosNum, h.rsmRegion, u.name ")
            .append("       from tbl_hospital h, tbl_userinfo u ")
            .append("       where h.rsmRegion = u.region ")
            .append("       and h.isChestSurgeryAssessed='1' ")
            .append("       and u.level='RSM' ")
            .append("       group by u.region ")
            .append("   ) hosNumTemp, ")
            .append("   ( select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from ( ")
            .append("           select IFNULL(count(1),0) as inNum, h.rsmRegion ")
            .append("           from tbl_chestSurgery_data cd, tbl_hospital h ")
            .append("           where cd.hospitalCode = h.code ")
            .append("           and cd.createdate between ? and ? ")
            .append("           and h.isChestSurgeryAssessed='1' ")
            .append("           group by h.rsmRegion ")
            .append("       ) inNum1 right join tbl_userinfo u on u.region = inNum1.rsmRegion ")
            .append("       where u.level='RSM' ")
            .append("   ) inNumTemp ")
            .append("   where hosNumTemp.rsmRegion = inNumTemp.rsmRegion ")
            .append("   order by inNumTemp.inNum/hosNumTemp.hosNum desc ")
            .append("   limit 1 ")
            .append(") inRateMaxT, ")
            .append("(  select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMin,pNumTemp.name as whRateMinUser ")
            .append("   from ( ")
            .append("           select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from ( ")
            .append("               select IFNULL(sum(cd.pnum),0) as pNum, h.rsmRegion ")
            .append("               from tbl_chestSurgery_data cd, tbl_hospital h ")
            .append("               where cd.hospitalCode = h.code ")
            .append("               and cd.createdate between ? and ?  ")
            .append("               and h.isChestSurgeryAssessed='1' ")
            .append("               group by h.rsmRegion ")
            .append("           ) pNum1 right join tbl_userinfo u on u.region = pNum1.rsmRegion ")
            .append("           where u.level='RSM' ")
            .append("       ) pNumTemp, ")
            .append("       ( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
            .append("           select IFNULL(sum(cd.lsnum),0) as lsNum, h.rsmRegion ")
            .append("           from tbl_chestSurgery_data cd, tbl_hospital h ")
            .append("           where cd.hospitalCode = h.code ")
            .append("           and cd.createdate between ? and ?  ")
            .append("           and h.isChestSurgeryAssessed='1' ")
            .append("           group by h.rsmRegion ")
            .append("           ) lsNum1 right join tbl_userinfo u on u.region = lsNum1.rsmRegion ")
            .append("           where u.level='RSM' ")
            .append("       ) lsNumTemp")
            .append("       where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
            .append("       order by lsNumTemp.lsNum/pNumTemp.pNum ")
            .append("       limit 1 ")
            .append(") whRateMinT,")
            .append("(  select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMax,pNumTemp.name as whRateMaxUser ")
            .append("   from ( ")
            .append("           select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from (")
            .append("               select IFNULL(sum(cd.pnum),0) as pNum, h.rsmRegion ")
            .append("               from tbl_chestSurgery_data cd, tbl_hospital h ")
            .append("               where cd.hospitalCode = h.code ")
            .append("               and cd.createdate between ? and ? ")
            .append("               and h.isChestSurgeryAssessed='1' ")
            .append("               group by h.rsmRegion ")
            .append("           ) pNum1 right join tbl_userinfo u on u.region = pNum1.rsmRegion ")
            .append("           where u.level='RSM' ")
            .append("       ) pNumTemp, ")
            .append("       ( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
            .append("               select IFNULL(sum(cd.lsnum),0) as lsNum, h.rsmRegion ")
            .append("               from tbl_chestSurgery_data cd, tbl_hospital h ")
            .append("               where cd.hospitalCode = h.code ")
            .append("               and cd.createdate between ? and ? ")
            .append("               and h.isChestSurgeryAssessed='1' ")
            .append("               group by h.rsmRegion ")
            .append("           ) lsNum1 right join tbl_userinfo u on u.region = lsNum1.rsmRegion ")
            .append("           where u.level='RSM' ")
            .append("       ) lsNumTemp ")
            .append("       where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
            .append("       order by lsNumTemp.lsNum/pNumTemp.pNum desc ")
            .append("       limit 1 ")
            .append(") whRateMaxT,")
            .append("( ")
            .append("   select IFNULL(av1.averageDose,0) as averageDoseMin, u.name as averageDoseMinUser from ")
            .append("       ( ")
            .append("           select IFNULL( sum( ( ( 1*IFNULL(cd.oqd,0) + 2*1*IFNULL(cd.tqd,0) + 1*3*IFNULL(cd.otid,0) + 2*2*IFNULL(cd.tbid,0) + 2*3*IFNULL(cd.ttid,0) + 3*2*IFNULL(cd.thbid,0) + 4*2*IFNULL(cd.fbid,0) ) / 100 ) * IFNULL(cd.lsnum,0) ) / IFNULL(sum(cd.lsnum),0),0 ) as averageDose, h.rsmRegion")
            .append("           from tbl_chestSurgery_data cd, tbl_hospital h ")
            .append("           where cd.hospitalCode = h.code ")
            .append("           and cd.createdate between ? and ?  ")
            .append("           and h.isChestSurgeryAssessed='1' ")
            .append("           group by h.rsmRegion ")
            .append("       ) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
            .append("       where u.level='RSM' ")
            .append("       order by av1.averageDose")
            .append("       limit 1 ")
            .append(") averageDoseMinT,")
            .append("( ")
            .append("   select IFNULL(av2.averageDose,0) as averageDoseMax, u.name as averageDoseMaxUser from ")
            .append("       ( ")
            .append("           select IFNULL( sum( ( ( 1*IFNULL(cd.oqd,0) + 2*1*IFNULL(cd.tqd,0) + 1*3*IFNULL(cd.otid,0) + 2*2*IFNULL(cd.tbid,0) + 2*3*IFNULL(cd.ttid,0) + 3*2*IFNULL(cd.thbid,0) + 4*2*IFNULL(cd.fbid,0) ) / 100 ) * IFNULL(cd.lsnum,0) ) / IFNULL(sum(cd.lsnum),0),0 ) as averageDose, h.rsmRegion")
            .append("           from tbl_chestSurgery_data cd, tbl_hospital h")
            .append("           where cd.hospitalCode = h.code ")
            .append("           and cd.createdate between ? and ?  ")
            .append("           and h.isChestSurgeryAssessed='1' ")
            .append("           group by h.rsmRegion ")
            .append("       ) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
            .append("       where u.level='RSM' ")
            .append("       order by av2.averageDose desc ")
            .append("       limit 1 ")
            .append(") averageDoseMaxT");
        return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{
            startDate,endDate,
            startDate,endDate,
            startDate,endDate,
            startDate,endDate,
            startDate,endDate,
            startDate,endDate,
            startDate,endDate,
            startDate,endDate
            },new TopAndBottomRSMDataRowMapper());
    }

    public List<MobileCHEDailyData> getDailyCHEData4RSMByRegionCenter(String regionCenter) throws Exception {
        StringBuffer mobileCHEDailySQL = new StringBuffer();
        
        Date date = DateUtils.populateParamDate(new Date());
        Timestamp startDate = new Timestamp(date.getTime());
        Timestamp endDate = new Timestamp(new Date(date.getTime() + 1* 24 * 60 * 60 * 1000).getTime());
        
        mobileCHEDailySQL.append("select ui.region as name, ui.userCode,")
        .append(" (select property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h where h.rsmRegion = ui.region and h.isChestSurgeryAssessed='1' ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_CHE)
        .append(" from ( ")
        .append(" select u.region as name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_CHE)
        .append(" from tbl_userinfo u, tbl_chestSurgery_data cd, tbl_hospital h1 ")
        .append(" where cd.hospitalCode = h1.code ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and cd.createdate between ? and ? ")
        .append(" and h1.isChestSurgeryAssessed='1' ")
        .append(" and u.level='RSM' ")
        .append(" and u.regionCenter = ? ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='RSM' ")
        .append(" and ui.regionCenter = ?")
        .append(" order by ui.region ");
        return dataBean.getJdbcTemplate().query(mobileCHEDailySQL.toString(), new Object[]{startDate,endDate,regionCenter,regionCenter},new ChestSurgeryMobileRowMapper());
    }

}
