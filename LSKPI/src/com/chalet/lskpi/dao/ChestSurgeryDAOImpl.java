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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.chalet.lskpi.mapper.ChestSurgeryMobileRowMapper;
import com.chalet.lskpi.mapper.ChestSurgeryRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomRSMDataRowMapper;
import com.chalet.lskpi.mapper.WeeklyHospitalDataRowMapper;
import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MobileCHEDailyData;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyDataOfHospital;
import com.chalet.lskpi.model.WeeklyRatioData;
import com.chalet.lskpi.utils.DataBean;
import com.chalet.lskpi.utils.DateUtils;
import com.chalet.lskpi.utils.LsAttributes;
import com.chalet.lskpi.utils.RESWeeklyRatioDataRowMapper;
import com.chalet.lskpi.utils.ReportProcessDataRowMapper;
import com.chalet.lskpi.utils.ReportProcessDetailDataRowMapper;

@Repository("chestSurgeryDAO")
public class ChestSurgeryDAOImpl implements ChestSurgeryDAO {

    private Logger logger = Logger.getLogger(ChestSurgeryDAOImpl.class);
    
    @Autowired
    @Qualifier("dataBean")
    private DataBean dataBean;
    
    public ChestSurgeryData getChestSurgeryDataByHospital(String hospitalCode) throws Exception {
        StringBuffer sql = new StringBuffer("");
        sql.append(" select cd.* ")
        .append(" , h.code as hospitalCode, h.name as hospitalName, h.dsmName, h.saleCode as salesCode, h.isChestSurgeryAssessed ")
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
        .append(" , h.code as hospitalCode, h.name as hospitalName, h.rsmRegion , h.region, h.dsmName, h.saleCode as salesCode, h.isChestSurgeryAssessed ")
        .append(" , (select distinct name from tbl_userinfo u where u.region = h.rsmRegion and u.superior = h.dsmCode and u.userCode = h.saleCode and u.level='REP') as salesName ")
        .append(" from tbl_chestSurgery_data cd, tbl_hospital h ")
        .append(" where DATE_FORMAT(cd.createdate,'%Y-%m-%d') between DATE_FORMAT(?,'%Y-%m-%d') and DATE_FORMAT(?,'%Y-%m-%d') ")
        .append(" and cd.hospitalCode = h.code ")
        .append(" order by cd.createdate desc");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{new Timestamp(createdatebegin.getTime()),new Timestamp(createdateend.getTime())},new ChestSurgeryRowMapper());
    }

    public ChestSurgeryData getChestSurgeryDataByHospitalAndDate(String hospitalCode, Date createdate) throws Exception {
        StringBuffer sql = new StringBuffer("");
        sql.append(" select cd.* ")
        .append(" , h.code as hospitalCode, h.name as hospitalName, h.dsmName, h.saleCode as salesCode, h.isChestSurgeryAssessed ")
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
        .append(" , h.code as hospitalCode, h.name as hospitalName, h.dsmName, h.saleCode as salesCode, h.isChestSurgeryAssessed ")
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

    @Cacheable(value="getDailyCHEData4CountoryMobile")
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

    @Cacheable(value="getDailyCHEData4DSMMobile")
    public List<MobileCHEDailyData> getDailyCHEData4DSMMobile(String region) throws Exception {
        StringBuffer mobileCHEDailySQL = new StringBuffer();
        
        Date date = DateUtils.populateParamDate(new Date());
        Timestamp startDate = new Timestamp(date.getTime());
        Timestamp endDate = new Timestamp(new Date(date.getTime() + 1* 24 * 60 * 60 * 1000).getTime());
        
        mobileCHEDailySQL.append("select ui.name, ui.userCode,")
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
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

    @Cacheable(value="getDailyCHEData4RSMMobile")
    public List<MobileCHEDailyData> getDailyCHEData4RSMMobile(String regionCenter) throws Exception {
        StringBuffer mobileCHEDailySQL = new StringBuffer();
        
        Date date = DateUtils.populateParamDate(new Date());
        Timestamp startDate = new Timestamp(date.getTime());
        Timestamp endDate = new Timestamp(new Date(date.getTime() + 1* 24 * 60 * 60 * 1000).getTime());
        
        mobileCHEDailySQL.append("select ui.region as name, ui.userCode,")
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
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

    @Cacheable(value="getDailyCHEData4RSDMobile")
    public List<MobileCHEDailyData> getDailyCHEData4RSDMobile() throws Exception {
        StringBuffer mobileCHEDailySQL = new StringBuffer();
        
        Date date = DateUtils.populateParamDate(new Date());
        Timestamp startDate = new Timestamp(date.getTime());
        Timestamp endDate = new Timestamp(new Date(date.getTime() + 1* 24 * 60 * 60 * 1000).getTime());
        
        mobileCHEDailySQL.append("select ( select distinct property_value from tbl_property where property_name = ui.regionCenter ) as name, ui.userCode,")
        .append(" ( select count(1) from tbl_hospital h where h.region = ui.regionCenter and h.isChestSurgeryAssessed='1' ) hosNum, ")
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
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

    @Cacheable(value="getChildDailyCHEData4DSMMobile")
    public List<MobileCHEDailyData> getChildDailyCHEData4DSMMobile(String dsmCode) throws Exception {
        StringBuffer mobileCHEDailySQL = new StringBuffer();
        
        Date date = DateUtils.populateParamDate(new Date());
        Timestamp startDate = new Timestamp(date.getTime());
        Timestamp endDate = new Timestamp(new Date(date.getTime() + 1* 24 * 60 * 60 * 1000).getTime());
        
        mobileCHEDailySQL.append("select ui.name, ui.userCode,")
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
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
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
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

	@Override
	public ReportProcessData getSalesSelfReportProcessData(String telephone) throws Exception {
	    StringBuffer sb = new StringBuffer("");
	    sb.append("select count(1) as hosNum, ")
	    .append("( select IFNULL(sum(inNum),0) as validInNum from ( ")
	    .append("		select least(count(1),3) as inNum, h1.code as hosCode, h1.dsmCode, h1.rsmRegion ")
	    .append("		from tbl_chestSurgery_data cd, tbl_hospital h1 ")
	    .append("		where cd.hospitalCode = h1.code ")
	    .append("		and cd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
	    .append("		and h1.isChestSurgeryAssessed='1' ")
	    .append("		group by cd.hospitalCode ")
	    .append("	) inNumTemp, tbl_hos_user hu ")
	    .append("	where u.region = inNumTemp.rsmRegion ")
	    .append("	and hu.hosCode = inNumTemp.hosCode ")
	    .append("	and hu.userCode = u.userCode ")
	    .append("	and u.superior = inNumTemp.dsmCode ")
	    .append(") as validInNum ")
	    .append("from tbl_userinfo u, tbl_hospital h, tbl_hos_user hu ")
	    .append("where h.dsmCode = u.superior ")
	    .append("and h.rsmRegion = u.region ")
	    .append("and h.code = hu.hosCode ")
	    .append("and hu.userCode = u.userCode ")
	    .append("and h.isChestSurgeryAssessed='1' ")
	    .append("and telephone = ? ");
	    
	    Date startDate = DateUtils.getTheBeginDateOfCurrentWeek();
	    return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{new Timestamp(startDate.getTime()),new Timestamp(startDate.getTime()),telephone}, new ReportProcessDataRowMapper());
	}

	public List<ReportProcessDataDetail> getSalesSelfReportProcessDetailData(String telephone) throws Exception {
	    StringBuffer sb = new StringBuffer("");
	    sb.append("select h.name as hospitalName, ")
	    .append("( select IFNULL( ")
	    .append("       ( select count(1) ")
	    .append("       from tbl_chestSurgery_data cd ")
	    .append("       where cd.hospitalCode = h.code ")
	    .append("       and cd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
	    .append("       group by cd.hospitalCode ")
	    .append("   ),0) ) as inNum, ")
	    .append("( select distinct ui.name from tbl_userinfo ui where ui.userCode = h.saleCode and ui.superior = h.dsmCode and ui.region = h.rsmRegion and ui.level='REP' ) as salesName, ")
	    .append(" h.isChestSurgeryAssessed as isAssessed ")
	    .append("from tbl_userinfo u, tbl_hospital h, tbl_hos_user hu ")
	    .append("where hu.userCode = u.userCode ")
	    .append("and hu.hosCode = h.code ")
	    .append("and h.dsmCode = u.superior ")
	    .append("and h.rsmRegion = u.region ")
	    .append("and telephone = ? ");
	    
	    Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
	    return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessDetailDataRowMapper());
	}
	
    public List<ReportProcessDataDetail> getDSMSelfReportProcessDetailData(String telephone) throws Exception {
        StringBuffer sb = new StringBuffer("");
        sb.append("select h.name as hospitalName, ")
        .append("( select IFNULL( ")
        .append("       ( select count(1) ")
        .append("       from tbl_chestSurgery_data cd ")
        .append("       where cd.hospitalCode = h.code ")
        .append("       and cd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
        .append("       group by cd.hospitalCode ")
        .append("   ),0) ) as inNum, ")
        .append("( select distinct ui.name from tbl_userinfo ui where ui.userCode = h.saleCode and ui.superior = h.dsmCode and ui.region = h.rsmRegion and ui.level='REP' ) as salesName, ")
        .append(" h.isChestSurgeryAssessed as isAssessed ")
        .append("from tbl_userinfo u, tbl_hospital h ")
        .append("where h.dsmCode = u.userCode ")
        .append("and h.rsmRegion = u.region ")
        .append("and telephone = ? ");
        
        Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessDetailDataRowMapper());
    }
	
	@Override
	public ReportProcessData getDSMSelfReportProcessData(String telephone) throws Exception {
	    StringBuffer sb = new StringBuffer("");
	    sb.append("select count(1) as hosNum, ")
	    .append("( select IFNULL(sum(inNum),0) as validInNum from ( ")
	    .append("		select least(count(1),3) as inNum, h1.dsmCode, h1.rsmRegion ")
	    .append("		from tbl_chestSurgery_data cd, tbl_hospital h1 ")
	    .append("		where cd.hospitalCode = h1.code ")
	    .append("		and cd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
	    .append("		and h1.isChestSurgeryAssessed='1' ")
	    .append("		group by cd.hospitalCode ")
	    .append("	) inNumTemp ")
	    .append("	where u.region = inNumTemp.rsmRegion ")
	    .append("	and u.userCode = inNumTemp.dsmCode ")
	    .append(") as validInNum ")
	    .append("from tbl_userinfo u, tbl_hospital h ")
	    .append("where h.dsmCode = u.userCode ")
	    .append("and h.rsmRegion = u.region ")
	    .append("and h.isChestSurgeryAssessed='1' ")
	    .append("and telephone = ? ");
	    
	    Date startDate = DateUtils.getTheBeginDateOfCurrentWeek();
	    return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{new Timestamp(startDate.getTime()),new Timestamp(startDate.getTime()),telephone}, new ReportProcessDataRowMapper());
	}
	
	public List<ReportProcessDataDetail> getRSMSelfReportProcessDetailData(String telephone) throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append("select h.name as hospitalName, ")
		.append("( select IFNULL( ")
		.append("       ( select count(1) ")
		.append("       from tbl_chestSurgery_data cd ")
		.append("       where cd.hospitalCode = h.code ")
		.append("       and cd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
		.append("       group by cd.hospitalCode ")
		.append("   ),0) ) as inNum, ")
		.append("( select distinct ui.name from tbl_userinfo ui where ui.userCode = h.saleCode and ui.superior = h.dsmCode and ui.region = h.rsmRegion and ui.level='REP' ) as salesName, ")
		.append(" h.isChestSurgeryAssessed as isAssessed ")
		.append("from tbl_userinfo u, tbl_hospital h ")
		.append("where h.rsmRegion = u.region ")
		.append("and h.isChestSurgeryAssessed = '1' ")
		.append("and telephone = ? ");
		
		Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
		return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessDetailDataRowMapper());
	}
	
	@Override
	public ReportProcessData getRSMSelfReportProcessData(String telephone) throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append("select count(1) as hosNum, ")
		.append("( select IFNULL(sum(inNum),0) as validInNum from ( ")
		.append("		select least(count(1),3) as inNum, h1.dsmCode, h1.rsmRegion ")
		.append("		from tbl_chestSurgery_data cd, tbl_hospital h1 ")
		.append("		where cd.hospitalCode = h1.code ")
		.append("		and cd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
		.append("		and h1.isChestSurgeryAssessed='1' ")
		.append("		group by cd.hospitalCode ")
		.append("	) inNumTemp ")
		.append("	where u.region = inNumTemp.rsmRegion ")
		.append(") as validInNum ")
		.append("from tbl_userinfo u, tbl_hospital h ")
		.append("where h.rsmRegion = u.region ")
		.append("and h.isChestSurgeryAssessed='1' ")
		.append("and telephone = ? ");
		
		Date startDate = DateUtils.getTheBeginDateOfCurrentWeek();
		return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{new Timestamp(startDate.getTime()),new Timestamp(startDate.getTime()),telephone}, new ReportProcessDataRowMapper());
	}
	

    public WeeklyRatioData getHospitalWeeklyData4Mobile(String hospitalCode) throws Exception {
        StringBuffer mobileWeeklySQL = new StringBuffer();
        mobileWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
        .append(" , '' as userCode ")
        .append(" , lastweekdata.hospitalName as name ")
        .append(" from ( ")
        .append("   select hospitalCode, hospitalName, ")
        .append(LsAttributes.SQL_HOSPITAL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_CHE)
        .append("   where hospitalCode=? ")
        .append(") lastweekdata, ")
        .append("( ")
        .append("   select hospitalCode, hospitalName, ")
        .append(LsAttributes.SQL_HOSPITAL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_CHE)
        .append("   where hospitalCode=? ")
        .append(") last2weekdata ");
        return dataBean.getJdbcTemplate().queryForObject(mobileWeeklySQL.toString(),new Object[]{hospitalCode,hospitalCode},new RESWeeklyRatioDataRowMapper());
    }
    
    @Override
    public WeeklyRatioData getLowerWeeklyData4REPMobile(UserInfo currentUser,String lowerUserCode)
            throws Exception {
        StringBuffer mobileWeeklySQL = new StringBuffer();
        mobileWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
        .append(" , lastweekdata.saleCode as userCode ")
        .append(" , IFNULL((select u.name from tbl_userinfo u where u.userCode = lastweekdata.saleCode and u.superior = lastweekdata.dsmCode and u.level='REP'),'vacant') as name ")
        .append(" from ( ")
        .append("   select h.dsmCode, h.saleCode, ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_CHE)
        .append("   group by h.dsmCode, h.saleCode ")
        .append(") lastweekdata, ")
        .append("( ")
        .append("   select h.dsmCode, h.saleCode, ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_CHE)
        .append("   group by h.dsmCode, h.saleCode ")
        .append(") last2weekdata ")
        .append("where lastweekdata.dsmCode = last2weekdata.dsmCode ")
        .append("and lastweekdata.saleCode = last2weekdata.saleCode ")
        .append("and lastweekdata.saleCode = ?")
        .append("and lastweekdata.dsmCode = ?");
        return dataBean.getJdbcTemplate().queryForObject(mobileWeeklySQL.toString(),new Object[]{lowerUserCode,currentUser.getUserCode()},new RESWeeklyRatioDataRowMapper());
    }

    @Override
    public WeeklyRatioData getLowerWeeklyData4DSMMobile(UserInfo currentUser,String lowerUserCode)
            throws Exception {
        StringBuffer mobileWeeklySQL = new StringBuffer();
        mobileWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
        .append(" , lastweekdata.dsmCode as userCode ")
        .append(" , IFNULL((select u.name from tbl_userinfo u where u.userCode = lastweekdata.dsmCode and u.region = lastweekdata.rsmRegion and u.level='DSM' ),'vacant') as name ")
        .append(" from ( ")
        .append("   select h.dsmCode, h.rsmRegion, ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_CHE)
        .append("   group by h.rsmRegion, h.dsmCode ")
        .append(") lastweekdata, ")
        .append("( ")
        .append("   select h.dsmCode, h.rsmRegion, ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_CHE)
        .append("   group by h.rsmRegion, h.dsmCode ")
        .append(") last2weekdata ")
        .append("where lastweekdata.dsmCode = last2weekdata.dsmCode ")
        .append("and lastweekdata.rsmRegion = last2weekdata.rsmRegion ")
        .append("and lastweekdata.dsmCode = ?")
        .append("and lastweekdata.rsmRegion = ?");
        return dataBean.getJdbcTemplate().queryForObject(mobileWeeklySQL.toString(),new Object[]{lowerUserCode,currentUser.getRegion()},new RESWeeklyRatioDataRowMapper());
    }

    @Override
    public WeeklyRatioData getLowerWeeklyData4RSMMobile(UserInfo currentUser,String lowerUserCode)
            throws Exception {
        StringBuffer mobileWeeklySQL = new StringBuffer();
        mobileWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
        .append(" , lastweekdata.rsmRegion as userCode ")
        .append(" , IFNULL((select u.name from tbl_userinfo u where u.level='RSM' and u.region = lastweekdata.rsmRegion ),'vacant') as name ")
        .append(" from ( ")
        .append("   select h.region, h.rsmRegion, ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_CHE)
        .append("   group by h.region, h.rsmRegion ")
        .append(") lastweekdata, ")
        .append("( ")
        .append("   select h.region, h.rsmRegion, ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_CHE)
        .append("   group by h.region, h.rsmRegion ")
        .append(") last2weekdata ")
        .append("where lastweekdata.region = last2weekdata.region ")
        .append("and lastweekdata.rsmRegion = last2weekdata.rsmRegion ")
        .append("and lastweekdata.rsmRegion = (select region from tbl_userinfo where userCode=?)");
        return dataBean.getJdbcTemplate().queryForObject(mobileWeeklySQL.toString(),new Object[]{lowerUserCode},new RESWeeklyRatioDataRowMapper());
    }

    public List<WeeklyRatioData> getWeeklyData4DSMMobile(String telephone) throws Exception {
        StringBuffer mobileWeeklySQL = new StringBuffer();
        mobileWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
        .append(" , lastweekdata.dsmCode as userCode , lastweekdata.rsmRegion ")
        .append(" , IFNULL((select u.name from tbl_userinfo u where u.userCode = lastweekdata.dsmCode and u.region = lastweekdata.rsmRegion and u.level='DSM' ),'vacant') as name ")
        .append(" from ( ")
        .append("   select h.dsmCode, h.rsmRegion, ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_CHE)
        .append("   group by h.rsmRegion, h.dsmCode ")
        .append(") lastweekdata, ")
        .append("( ")
        .append("   select h.dsmCode, h.rsmRegion, ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_CHE)
        .append("   group by h.rsmRegion, h.dsmCode ")
        .append(") last2weekdata ")
        .append("where lastweekdata.dsmCode = last2weekdata.dsmCode ")
        .append("and lastweekdata.rsmRegion = last2weekdata.rsmRegion ")
        .append("and lastweekdata.rsmRegion = (select region from tbl_userinfo where telephone=?)");
        return dataBean.getJdbcTemplate().query(mobileWeeklySQL.toString(),new Object[]{telephone},new RESWeeklyRatioDataRowMapper());
    }

    public List<WeeklyRatioData> getWeeklyData4RSMMobile(String telephone) throws Exception {
        StringBuffer mobileWeeklySQL = new StringBuffer();
        mobileWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
        .append(" , lastweekdata.rsmRegion as userCode, lastweekdata.region ")
        .append(" , IFNULL((select u.name from tbl_userinfo u where u.level='RSM' and u.region = lastweekdata.rsmRegion ),'vacant') as name ")
        .append(" from ( ")
        .append("   select h.rsmRegion, h.region, ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_CHE)
        .append("   group by h.rsmRegion")
        .append(") lastweekdata, ")
        .append("( ")
        .append("   select h.rsmRegion, h.region, ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_CHE)
        .append("   group by h.rsmRegion")
        .append(") last2weekdata ")
        .append("where lastweekdata.rsmRegion = last2weekdata.rsmRegion ")
        .append("and lastweekdata.region = (select regionCenter from tbl_userinfo where telephone=?)");
       return dataBean.getJdbcTemplate().query(mobileWeeklySQL.toString(),new Object[]{telephone},new RESWeeklyRatioDataRowMapper());
    }

    @Cacheable(value="getWeeklyData4RSDMobile")
    public List<WeeklyRatioData> getWeeklyData4RSDMobile() throws Exception {
        StringBuffer mobileWeeklySQL = new StringBuffer();
        mobileWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
        .append(" , lastweekdata.region as userCode")
        .append(" , (select distinct property_value from tbl_property where property_name=lastweekdata.region ) as name")
        .append(" from ( ")
        .append("   select h.region, ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_CHE)
        .append("   group by h.region")
        .append(") lastweekdata, ")
        .append("( ")
        .append("   select h.region , ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_CHE)
        .append("   group by h.region")
        .append(") last2weekdata ")
        .append("where lastweekdata.region = last2weekdata.region order by lastweekdata.region");
       return dataBean.getJdbcTemplate().query(mobileWeeklySQL.toString(),new RESWeeklyRatioDataRowMapper());
    }
    
    public WeeklyRatioData getHospitalWeeklyData4Mobile() throws Exception {
        StringBuffer mobileWeeklySQL = new StringBuffer();
        mobileWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
        .append(" , '' as userCode")
        .append(" , '' as name ")
        .append(" from ( ")
        .append("   select ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_CHE)
        .append(") lastweekdata, ")
        .append("( ")
        .append("   select ")
        .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_CHE)
        .append(") last2weekdata ");
        return dataBean.getJdbcTemplate().queryForObject(mobileWeeklySQL.toString(),new RESWeeklyRatioDataRowMapper());
    }
    

    @Override
    public void generateWeeklyDataOfHospital() throws Exception {
        Date lastweekDay = DateUtils.getGenerateWeeklyReportDate();
        this.generateWeeklyDataOfHospital(lastweekDay);
    }
    
    @Override
    public int removeOldWeeklyData(String duration) throws Exception{
        String sql = "delete from tbl_chestSurgery_data_weekly where duration=?";
        return dataBean.getJdbcTemplate().update(sql, new Object[] { duration });
    }
    
    @Override
    public void generateWeeklyDataOfHospital(Date refreshDate) throws Exception {
        Timestamp lastweekDay = new Timestamp(refreshDate.getTime());
        StringBuffer sb = new StringBuffer();
        
        sb.append("insert into tbl_chestSurgery_data_weekly ")
            .append("select ")
            .append("null,")
            .append(" CONCAT(DATE_FORMAT(DATE_SUB(?, Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT(?,'%Y.%m.%d')) as duration, ")
            .append("h.name, ")
            .append("h.code, ")
            .append("cd_data.innum, ")
            .append("cd_data.pnum, ")
            .append("cd_data.risknum, ")
            .append("cd_data.whnum, ")
            .append("cd_data.lsnum, ")
            .append("cd_data.averageDose, ")
            .append("cd_data.omgRate, ")
            .append("cd_data.tmgRate, ")
            .append("cd_data.thmgRate, ")
            .append("cd_data.fmgRate, ")
            .append("cd_data.smgRate, ")
            .append("cd_data.emgRate, ")
            .append("now(),  ")
            .append("Year(DATE_SUB(?, Interval 6 day)),  ")
            .append("Month(DATE_SUB(?, Interval 6 day))  ")
            .append("from ( ")
            .append("   SELECT ")
            .append("   h.code, ")
            .append("   count_hos.inNum, ")
            .append("   (sum(cd.pnum)/count_hos.inNum)*5 as pnum, ")
            .append("   (sum(cd.risknum)/count_hos.inNum)*5 as risknum, ")
            .append("   (sum(cd.whnum)/count_hos.inNum)*5 as whnum, ")
            .append("   (sum(cd.lsnum)/count_hos.inNum)*5 as lsnum, ")
            .append("   IFNULL( ")
            .append("       sum( ")
            .append("           ( ( 1*IFNULL(cd.oqd,0) + 2*1*IFNULL(cd.tqd,0) + 1*3*IFNULL(cd.otid,0) + 2*2*IFNULL(cd.tbid,0) + 2*3*IFNULL(cd.ttid,0) + 3*2*IFNULL(cd.thbid,0) + 4*2*IFNULL(cd.fbid,0) ) / 100 )* IFNULL(cd.lsnum,0) ")
            .append("       ) / IFNULL(sum(cd.lsnum),0) ,0 ) averageDose, ")
            .append("   IFNULL( sum(IFNULL(cd.oqd,0)*cd.lsnum/100)/sum(cd.lsnum),0 ) omgRate, ")
            .append("   IFNULL( sum(IFNULL(cd.tqd,0)*cd.lsnum/100)/sum(cd.lsnum),0 ) tmgRate, ")
            .append("   IFNULL( sum(IFNULL(cd.otid,0)*cd.lsnum/100)/sum(cd.lsnum),0 ) thmgRate, ")
            .append("   IFNULL( sum(IFNULL(cd.tbid,0)*cd.lsnum/100)/sum(cd.lsnum),0 ) fmgRate, ")
            .append("   IFNULL( sum((IFNULL(cd.ttid,0)*cd.lsnum + IFNULL(cd.thbid,0)*cd.lsnum)/100)/sum(cd.lsnum),0 ) smgRate, ")
            .append("   IFNULL( sum(IFNULL(cd.fbid,0)*cd.lsnum/100)/sum(cd.lsnum),0 ) emgRate ")
            .append("   FROM tbl_chestSurgery_data cd, tbl_hospital h, ")
            .append("   (   ")
            .append("       select count(1) as inNum, h.code ")
            .append("       from tbl_chestSurgery_data cd, tbl_hospital h ")
            .append("       WHERE cd.createdate between DATE_SUB(?, Interval 6 day) and DATE_ADD(?, Interval 1 day) ")
            .append("       and cd.hospitalCode = h.code ")
            .append("       and h.isChestSurgeryAssessed='1' ")
            .append("       GROUP BY h.code ")
            .append("   ) count_hos ")
            .append("   WHERE cd.createdate between DATE_SUB(?, Interval 6 day) and DATE_ADD(?, Interval 1 day) ")
            .append("   and cd.hospitalCode = h.code ")
            .append("   and h.code = count_hos.code")
            .append("   and h.isChestSurgeryAssessed='1' ")
            .append("   GROUP BY h.code ")
            .append(") cd_data ")
            .append("right join tbl_hospital h on cd_data.code = h.code ")
            .append("where h.isChestSurgeryAssessed='1'");
        int result = dataBean.getJdbcTemplate().update(sb.toString(), new Object[]{lastweekDay,lastweekDay,lastweekDay,lastweekDay,lastweekDay,lastweekDay,lastweekDay,lastweekDay});
        logger.info(String.format("finish to generate the chest surgery weekly data, the result is %s", result));
    }
    
    public int getLastWeeklyData() throws Exception {
        Timestamp lastThursDay = new Timestamp(DateUtils.getGenerateWeeklyReportDate().getTime());
        StringBuffer sb = new StringBuffer();
        sb.append(" select count(1) from tbl_chestSurgery_data_weekly where duration = CONCAT(DATE_FORMAT(DATE_SUB(?, Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT(?,'%Y.%m.%d'))");
        return dataBean.getJdbcTemplate().queryForInt(sb.toString(), lastThursDay,lastThursDay);
    }

    public List<WeeklyDataOfHospital> getWeeklyDataOfHospital(Date refreshDate) throws Exception {
        Timestamp lastweekDay = new Timestamp(refreshDate.getTime());
        StringBuffer sb = new StringBuffer();
        sb.append(LsAttributes.SQL_HOSPITAL_WEEKLY_DATA_SELECTION)
          .append(" from tbl_chestSurgery_data_weekly ")
          .append(" where duration = CONCAT(DATE_FORMAT(DATE_SUB(?, Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT(?,'%Y.%m.%d'))");
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{lastweekDay,lastweekDay}, new WeeklyHospitalDataRowMapper());
    }

}
