drop table tbl_userinfo;
create table tbl_userinfo(
	id				int NOT NULL primary key auto_increment,
	name			varchar(255),
	telephone		varchar(20),
	etmsCode		varchar(20),
	userCode		varchar(20),
	BU				varchar(20),
	regionCenter	varchar(20),
	region			varchar(20),
	teamCode		varchar(20),
	team			varchar(200),
	level			varchar(20),
	superior		varchar(20),
	createdate 		datetime,
	modifydate		datetime
);

drop table tbl_hospital;
create table tbl_hospital(
	id				    int NOT NULL primary key auto_increment,
	name			    varchar(100),
	city			    varchar(20),
	province		    varchar(20),
	region			    varchar(20),
	rsmRegion		    varchar(20),
	level			    varchar(10),
	code			    varchar(20),
	dsmCode             varchar(20),
	dsmName             varchar(255),
	saleName            varchar(200),
	dragonType 		    varchar(20),
	isResAssessed	    varchar(2),
	isPedAssessed	    varchar(2),
	saleCode            varchar(20),
	isMonthlyAssessed   varchar(2)
);

drop table tbl_respirology_data;
create table tbl_respirology_data(
	id				int NOT NULL primary key auto_increment,
	createdate		datetime,
	hospitalName	varchar(100),
	pnum			int,
	aenum			int,
	whnum			int,
	lsnum			int,
	etmsCode		varchar(20),
	operatorName	varchar(20),
	region			varchar(20),
	rsmRegion		varchar(20),
	oqd				DECIMAL(11,2),
	tqd				DECIMAL(11,2),
	otid			DECIMAL(11,2),
	tbid			DECIMAL(11,2),
	ttid			DECIMAL(11,2),
	thbid			DECIMAL(11,2),
	fbid			DECIMAL(11,2),
	recipeType		varchar(20),
	updatedate		datetime,
	dsmCode         varchar(20)
);

drop table tbl_pediatrics_data;
create table tbl_pediatrics_data(
	id				int NOT NULL primary key auto_increment,
	createdate		datetime,
	hospitalName	varchar(100),
	pnum			int,
	whnum			int,
	lsnum			int,
	etmsCode		varchar(20),
	operatorName	varchar(20),
	region			varchar(20),
	rsmRegion		varchar(20),
	hqd				DECIMAL(11,2),
	hbid			DECIMAL(11,2),
	oqd			    DECIMAL(11,2),
	obid			DECIMAL(11,2),
	tqd			    DECIMAL(11,2),
	tbid			DECIMAL(11,2),
	recipeType		varchar(20),
	updatedate		datetime,
    dsmCode         varchar(20)
);

drop table tbl_pediatrics_data_weekly;
create table tbl_pediatrics_data_weekly(
	id				int NOT NULL primary key auto_increment,
	duration		varchar(30),
	hospitalName	varchar(100),
	hospitalCode	varchar(20),
	innum			int,
	pnum			DECIMAL(11,6),
	whnum			DECIMAL(11,6),
	lsnum			DECIMAL(11,6),
	averageDose		DECIMAL(11,6),
	hmgRate			DECIMAL(11,6),
	omgRate			DECIMAL(11,6),
	tmgRate			DECIMAL(11,6),
	fmgRate			DECIMAL(11,6),
	saleCode		varchar(20),
    dsmCode         varchar(20),
	rsmRegion		varchar(20),
	region			varchar(20),
	updatedate		datetime
);
drop table tbl_respirology_data_weekly;
create table tbl_respirology_data_weekly(
	id				int NOT NULL primary key auto_increment,
	duration		varchar(30),
	hospitalName	varchar(100),
	hospitalCode	varchar(20),
	innum			int,
	pnum			DECIMAL(11,6),
	aenum			DECIMAL(11,6),
	whnum			DECIMAL(11,6),
	lsnum			DECIMAL(11,6),
	averageDose		DECIMAL(11,6),
	omgRate			DECIMAL(11,6),
	tmgRate			DECIMAL(11,6),
	thmgRate		DECIMAL(11,6),
	fmgRate			DECIMAL(11,6),
	smgRate			DECIMAL(11,6),
	emgRate			DECIMAL(11,6),
	saleCode		varchar(20),
    dsmCode         varchar(20),
	rsmRegion		varchar(20),
	region			varchar(20),
	updatedate		datetime
);

drop table tbl_web_userinfo;
create table tbl_web_userinfo(
    id              int NOT NULL primary key auto_increment,
    name            varchar(50),
    password        varchar(64),
    telephone       varchar(20),
    level           varchar(20),
    createdate      datetime,
    modifydate      datetime
);
drop table tbl_ddi_data;
create table tbl_ddi_data(
	id				int NOT NULL primary key auto_increment,
	num				DECIMAL(11,2),
	region			varchar(20),
	duration		varchar(20)
);

drop table tbl_month_data;
create table tbl_month_data(
	id				int NOT NULL primary key auto_increment,
	pedEmernum		int,
	pedroomnum		int,
	resnum			int,
	other			int,
	operatorName	varchar(20),
	operatorCode	varchar(20),
	hospitalCode    varchar(20),
	dsmCode         varchar(20),
	rsmRegion       varchar(20),
	region          varchar(20),
	createdate		datetime,
	updatedate		datetime
);

drop table tbl_hos_user;
create table tbl_hos_user(
    hosCode           varchar(20),
    userCode          varchar(20),
    isPrimary         char(1)
);

drop table tbl_emailMessage;
create table tbl_emailMessage(
    id              int NOT NULL primary key auto_increment,
    name            varchar(255),
    flag            varchar(2),
    createdate      datetime
);

ALTER  TABLE tbl_pediatrics_data ADD INDEX INDEX_PED_HOSNAME (hospitalName);
ALTER  TABLE tbl_pediatrics_data ADD INDEX INDEX_PED_CREATEDATE (createdate);

ALTER  TABLE tbl_respirology_data ADD INDEX INDEX_RES_HOSNAME (hospitalName);
ALTER  TABLE tbl_respirology_data ADD INDEX INDEX_RES_CREATEDATE (createdate);

ALTER  TABLE tbl_hospital ADD INDEX INDEX_HOSPITAL_NAME (name);
ALTER  TABLE tbl_hospital ADD INDEX INDEX_HOSPITAL_DSMNAME (dsmName);
ALTER  TABLE tbl_hospital ADD INDEX INDEX_HOSPITAL_REGION (region);
ALTER  TABLE tbl_hospital ADD INDEX INDEX_HOSPITAL_RSMREGION (rsmRegion);
ALTER  TABLE tbl_hospital ADD INDEX INDEX_HOSPITAL_SALECODE (saleCode);
ALTER  TABLE tbl_hospital ADD INDEX INDEX_HOSPITAL_PEDASSESSED (isPedAssessed);
ALTER  TABLE tbl_hospital ADD INDEX INDEX_HOSPITAL_RESASSESSED (isResAssessed);

ALTER  TABLE tbl_userinfo ADD INDEX INDEX_USERINFO_REGION (region);
ALTER  TABLE tbl_userinfo ADD INDEX INDEX_USERINFO_REGIONCENTER (regionCenter);
ALTER  TABLE tbl_userinfo ADD INDEX INDEX_USERINFO_USERCODE (userCode);
ALTER  TABLE tbl_userinfo ADD INDEX INDEX_USERINFO_LEVEL (level);
ALTER  TABLE tbl_userinfo ADD INDEX INDEX_USERINFO_TELEPHONE (telephone);

drop index INDEX_HOSPITAL_NAME on tbl_hospital;
drop index INDEX_HOSPITAL_DSMNAME on tbl_hospital;
drop index INDEX_HOSPITAL_REGION on tbl_hospital;
drop index INDEX_HOSPITAL_RSMREGION on tbl_hospital;
drop index INDEX_HOSPITAL_SALECODE on tbl_hospital;

drop index INDEX_USERINFO_REGION on tbl_userinfo;
drop index INDEX_USERINFO_REGIONCENTER on tbl_userinfo;
drop index INDEX_USERINFO_USERCODE on tbl_userinfo;

ALTER  TABLE tbl_month_data ADD column countMonth varchar(7);
update tbl_month_data set countMonth = CONCAT(year(createdate),'-',month(createdate));
update tbl_month_data set countMonth = '2014-01' where countMonth = '2014-1';
update tbl_month_data set countMonth = '2014-02' where countMonth = '2014-2';
update tbl_month_data set countMonth = '2014-03' where countMonth = '2014-3';
update tbl_month_data set countMonth = '2014-04' where countMonth = '2014-4';
update tbl_month_data set countMonth = '2014-05' where countMonth = '2014-5';

/**
insert into tbl_userinfo values(null,'徐玉韩','13511111111',null,'South GRA','REP','13511111112',null,now(),now());
insert into tbl_userinfo values(null,'李维志','13522222222',null,'South GRA','REP','13511111113',null,now(),now());
--123456 e10adc3949ba59abbe56e057f20f883e
insert into tbl_web_userinfo values (null,'测试管理员','e10adc3949ba59abbe56e057f20f883e','13511111111','admin',now(),now());
*/

drop table tbl_doctor;
create table tbl_doctor(
    id              int NOT NULL primary key auto_increment,
    name            varchar(255),
    code            varchar(20),
    hospitalCode    varchar(20),
    salesCode       varchar(20),
    createdate      datetime,
    modifydate      datetime
);

drop table tbl_doctor_history;
create table tbl_doctor_history(
    id              int NOT NULL primary key auto_increment,
    drName          varchar(255),
    drCode          varchar(20),
    doctorId        int,
    hospitalCode    varchar(20),
    salesCode       varchar(20),
    reason          varchar(2000),
    createdate      datetime,
    modifydate      datetime
);

drop table tbl_home_data;
create table tbl_home_data(
    id              int NOT NULL primary key auto_increment,
    doctorId        int NOT NULL,
    salenum         int, /*每周新病人人次*/
    asthmanum       int, /*哮喘*患者人次*/
    ltenum          int, /*处方>=8天的哮喘维持期病人次*/
    lsnum           int, /*维持期病人中推荐使用令舒的人次*/
    efnum           int, /*8<=DOT<15天，病人次*/
    ftnum           int, /*15<=DOT<30天，病人次*/
    lttnum          int, /*DOT>=30天,病人次*/
    createdate      datetime,
    updatedate      datetime
);
ALTER TABLE tbl_home_data add column hospitalCode varchar(20);
ALTER  TABLE tbl_home_data ADD INDEX INDEX_HOME_DOCTORID (doctorId);
ALTER  TABLE tbl_home_data ADD INDEX INDEX_HOME_CREATEDATE (createdate);
ALTER  TABLE tbl_doctor ADD INDEX INDEX_DOCTOR_HOSPITALCODE (hospitalCode);

update tbl_home_data hd, tbl_doctor d 
set hd.hospitalCode = d.hospitalCode 
where d.id = hd.doctorId;

update tbl_home_data hd, tbl_doctor_history dh 
set hd.hospitalCode = dh.hospitalCode 
where dh.doctorId = hd.doctorId;

drop table tbl_chestSurgery_data;
create table tbl_chestSurgery_data(
    id              int NOT NULL primary key auto_increment,
    createdate      datetime,
    hospitalCode    varchar(100),
    pnum            int,
    risknum         int,
    whnum           int,
    lsnum           int,
    oqd             DECIMAL(11,2),
    tqd             DECIMAL(11,2),
    otid            DECIMAL(11,2),
    tbid            DECIMAL(11,2),
    ttid            DECIMAL(11,2),
    thbid           DECIMAL(11,2),
    fbid            DECIMAL(11,2),
    updatedate      datetime
);

alter table tbl_hospital add column isChestSurgeryAssessed varchar(2);
ALTER  TABLE tbl_hospital ADD INDEX INDEX_HOSPITAL_CHEASSESSED (isChestSurgeryAssessed);

ALTER TABLE tbl_respirology_data_weekly ADD INDEX INDEX_RES_WEEKLY_DURATION (duration);
ALTER TABLE tbl_respirology_data_weekly ADD INDEX INDEX_RES_WEEKLY_REGION (region);
ALTER TABLE tbl_pediatrics_data_weekly ADD INDEX INDEX_PED_WEEKLY_DURATION (duration);
ALTER TABLE tbl_pediatrics_data_weekly ADD INDEX INDEX_PED_WEEKLY_REGION (region);


drop table tbl_property;
create table tbl_property(
    id                      int NOT NULL primary key auto_increment,
    property_name           varchar(2000),
    property_value          varchar(2000)
);

alter table tbl_hospital add column isTop100 varchar(2);

drop table tbl_chestSurgery_data_weekly;
create table tbl_chestSurgery_data_weekly(
    id              int NOT NULL primary key auto_increment,
    duration        varchar(30),
    hospitalName    varchar(100),
    hospitalCode    varchar(20),
    innum           int,
    pnum            DECIMAL(11,6),
    risknum         DECIMAL(11,6),
    whnum           DECIMAL(11,6),
    lsnum           DECIMAL(11,6),
    averageDose     DECIMAL(11,6),
    omgRate         DECIMAL(11,6),
    tmgRate         DECIMAL(11,6),
    thmgRate        DECIMAL(11,6),
    fmgRate         DECIMAL(11,6),
    smgRate         DECIMAL(11,6),
    emgRate         DECIMAL(11,6),
    updatedate      datetime,
    date_YYYY       int,
    date_MM         int
);

ALTER  TABLE tbl_respirology_data_weekly ADD column date_YYYY int;
ALTER  TABLE tbl_respirology_data_weekly ADD column date_MM int;

update tbl_respirology_data_weekly 
set date_YYYY = year(left(duration,10));
update tbl_respirology_data_weekly 
set date_MM = month(left(duration,10));

ALTER TABLE tbl_respirology_data_weekly DROP COLUMN date_YYYYMM;


ALTER TABLE tbl_respirology_data_weekly ADD INDEX INDEX_RES_WEEKLY_YYYY(date_YYYY);
ALTER TABLE tbl_respirology_data_weekly ADD INDEX INDEX_RES_WEEKLY_MM(date_MM);

drop table tbl_hospital_data_weekly;
create table tbl_hospital_data_weekly(
    id              int NOT NULL primary key auto_increment,
    duration        varchar(30),
    hospitalCode    varchar(20),
    updatedate      datetime,
    pedPNum         DECIMAL(11,6) default 0,
    pedLsNum        DECIMAL(11,6) default 0,
    pedAverageDose  DECIMAL(11,6) default 0,
    resPNum         DECIMAL(11,6) default 0,
    resLsNum        DECIMAL(11,6) default 0,
    resAverageDose  DECIMAL(11,6) default 0,
    chePNum         DECIMAL(11,6) default 0,
    cheLsNum        DECIMAL(11,6) default 0,
    cheAverageDose  DECIMAL(11,6) default 0
);
ALTER TABLE tbl_hospital_data_weekly ADD INDEX INDEX_HOSPITAL_WEEKLY_CODE(hospitalCode);
ALTER TABLE tbl_hospital_data_weekly ADD INDEX INDEX_HOSPITAL_WEEKLY_DURATION(duration);

ALTER  TABLE tbl_hos_user ADD INDEX INDEX_HOS_USER_USERCODE (userCode);

alter table tbl_hospital add column portNum int;
alter table tbl_pediatrics_data add column portNum int;

/*
 * 1.每周一凌晨0点开始备份上上周的医生中间表，统计有哪些医生
 * 2.在用户通过界面关联医生销售时，本中间表要同步更新销售ID
 * 
 * */
drop table tbl_doctor_weekly;
create table tbl_doctor_weekly(
    id              int NOT NULL primary key auto_increment,
    duration        varchar(255),
    code            varchar(20),
    hospitalCode    varchar(20),
    salesCode       varchar(20),
    createdate      datetime,
    modifydate      datetime
);

alter table tbl_home_data add column duration varchar(30);

update tbl_home_data 
set duration = concat( DATE_FORMAT( ADDDATE(createdate,-WEEKDAY(createdate)),'%Y.%m.%d'),'-', DATE_FORMAT(ADDDATE(createdate,6-WEEKDAY(createdate)),'%Y.%m.%d'));

ALTER  TABLE tbl_doctor_weekly ADD INDEX INDEX_DOCTOR_WEEKLY_DURATION (duration);
ALTER  TABLE tbl_home_data ADD INDEX INDEX_HOME_DURATION (duration);

alter table tbl_pediatrics_data_weekly add column portNum int default 0;

alter table tbl_doctor_weekly add column doctorId int;
alter table tbl_doctor_weekly add column doctorName varchar(255);

/*
 * 删除医生的待审批表
 * status -- 0-初始提交  1-删除  2-拒绝
 * */
drop table tbl_doctor_approval;
create table tbl_doctor_approval(
    id              int NOT NULL primary key auto_increment,
    drId        	int,
    deleteReason    varchar(2000),
    status			char(1),
    operatorTel		varchar(20),
    createdate      datetime,
    modifydate      datetime
);

pedEmernum		int,
pedroomnum		int,
resnum			int,
other			int,
	
alter table tbl_month_data add column ped_emer_drugstore DECIMAL(11,2);
alter table tbl_month_data add column ped_emer_wh DECIMAL(11,2);
alter table tbl_month_data add column ped_room_drugstore DECIMAL(11,2);
alter table tbl_month_data add column ped_room_drugstore_wh DECIMAL(11,2);
alter table tbl_month_data add column res_clinic DECIMAL(11,2);
alter table tbl_month_data add column res_room DECIMAL(11,2);

ALTER  TABLE tbl_month_data ADD INDEX INDEX_MONTH_COUNTMONTH (countMonth);

alter table tbl_month_data add column home_wh DECIMAL(11,2) not null default 0;

alter table tbl_hospital add column isDailyReport varchar(2) not null default 0;

alter table tbl_respirology_data add column xbknum int not null default 0;
alter table tbl_respirology_data add column xbk1num int not null default 0;
alter table tbl_respirology_data add column xbk2num int not null default 0;
alter table tbl_respirology_data add column xbk3num int not null default 0;

alter table tbl_hospital add column isRe2 varchar(2) not null default 0;

alter table tbl_respirology_data_weekly add column xbknum int not null default 0;
alter table tbl_respirology_data_weekly add column xbk1num int not null default 0;
alter table tbl_respirology_data_weekly add column xbk2num int not null default 0;
alter table tbl_respirology_data_weekly add column xbk3num int not null default 0;

ALTER  TABLE tbl_month_data ADD INDEX INDEX_MONTH_HOSPITALCODE (hospitalCode);

alter table tbl_pediatrics_data add column whbwnum int not null default 0;
alter table tbl_hospital add column isWHBW varchar(2) not null default 0;

alter table tbl_pediatrics_data_weekly add column whbwnum int not null default 0;

alter table tbl_pediatrics_data add column whdays_emerging_1 DECIMAL(11,6) not null default 0 comment '门急诊雾化天数1天占比(%)';
alter table tbl_pediatrics_data add column whdays_emerging_2 DECIMAL(11,6) not null default 0 comment '门急诊雾化天数2天占比(%)';
alter table tbl_pediatrics_data add column whdays_emerging_3 DECIMAL(11,6) not null default 0 comment '门急诊雾化天数3天占比(%)';
alter table tbl_pediatrics_data add column whdays_emerging_4 DECIMAL(11,6) not null default 0 comment '门急诊雾化天数4天占比(%)';
alter table tbl_pediatrics_data add column whdays_emerging_5 DECIMAL(11,6) not null default 0 comment '门急诊雾化天数5天占比(%)';
alter table tbl_pediatrics_data add column whdays_emerging_6 DECIMAL(11,6) not null default 0 comment '门急诊雾化天数6天占比(%)';
alter table tbl_pediatrics_data add column whdays_emerging_7 DECIMAL(11,6) not null default 0 comment '门急诊雾化天数7天及以上占比(%)';
alter table tbl_pediatrics_data add column home_wh_emerging_num1 int not null default 0 comment '门急诊赠卖泵数量';
alter table tbl_pediatrics_data add column home_wh_emerging_num2 int not null default 0 comment '门急诊带药人数';
alter table tbl_pediatrics_data add column home_wh_emerging_num3 int not null default 0 comment '门急诊平均带药天数';
alter table tbl_pediatrics_data add column home_wh_emerging_num4 int not null default 0 comment '门急诊总带药支数';

alter table tbl_pediatrics_data add column pnum_room int not null default 0 comment '当日住院人数';
alter table tbl_pediatrics_data add column whdays_room_1 DECIMAL(11,6) not null default 0 comment '病房雾化天数1天占比(%)';
alter table tbl_pediatrics_data add column whdays_room_2 DECIMAL(11,6) not null default 0 comment '病房雾化天数2天占比(%)';
alter table tbl_pediatrics_data add column whdays_room_3 DECIMAL(11,6) not null default 0 comment '病房雾化天数3天占比(%)';
alter table tbl_pediatrics_data add column whdays_room_4 DECIMAL(11,6) not null default 0 comment '病房雾化天数4天占比(%)';
alter table tbl_pediatrics_data add column whdays_room_5 DECIMAL(11,6) not null default 0 comment '病房雾化天数5天占比(%)';
alter table tbl_pediatrics_data add column whdays_room_6 DECIMAL(11,6) not null default 0 comment '病房雾化天数6天占比(%)';
alter table tbl_pediatrics_data add column whdays_room_7 DECIMAL(11,6) not null default 0 comment '病房雾化天数7天占比(%)';
alter table tbl_pediatrics_data add column whdays_room_8 DECIMAL(11,6) not null default 0 comment '病房雾化天数8天占比(%)';
alter table tbl_pediatrics_data add column whdays_room_9 DECIMAL(11,6) not null default 0 comment '病房雾化天数9天占比(%)';
alter table tbl_pediatrics_data add column whdays_room_10 DECIMAL(11,6) not null default 0 comment '病房雾化天数10天占比(%)';
alter table tbl_pediatrics_data add column home_wh_room_num1 int not null default 0 comment '病房赠卖泵数量';
alter table tbl_pediatrics_data add column home_wh_room_num2 int not null default 0 comment '病房带药人数';
alter table tbl_pediatrics_data add column home_wh_room_num3 int not null default 0 comment '病房平均带药天数';
alter table tbl_pediatrics_data add column home_wh_room_num4 int not null default 0 comment '病房总带药支数';

alter table tbl_pediatrics_data add column whnum_room int not null default 0 comment '病房雾化人数'
,add column lsnum_room int not null default 0 comment '病房雾化令舒人数'
,add column hqd_room DECIMAL(11,6) not null default 0 comment '病房0.5mgqd'
,add column hbid_room DECIMAL(11,6) not null default 0 comment '病房0.5mgbid'
,add column oqd_room DECIMAL(11,6) not null default 0 comment '病房1mgqd'
,add column obid_room DECIMAL(11,6) not null default 0 comment '病房1mgbid'
,add column tqd_room DECIMAL(11,6) not null default 0 comment '病房2mgqd'
,add column tbid_room DECIMAL(11,6) not null default 0 comment '病房2mgbid'
,add column recipeType_room varchar(20) not null default '1' comment '病房2mgbid'
,add column whbwnum_room int not null default 0 comment '病房雾化博雾人次';

ALTER  TABLE tbl_property ADD INDEX INDEX_property_name (property_name);

alter table tbl_pediatrics_data_weekly add column homeWhNum1 int not null default 0 comment '门急诊卖赠泵数量'
,add column homeWhNum2 int not null default 0 comment '门急诊平均带药人数'
,add column homeWhNum3 int not null default 0 comment '门急诊平均带药天数'
,add column homeWhNum4 int not null default 0 comment '门急诊总带药支数'
,add column homeRoomWhNum1 int not null default 0 comment '病房卖赠泵数量'
,add column homeRoomWhNum2 int not null default 0 comment '病房平均带药人数'
,add column homeRoomWhNum3 int not null default 0 comment '病房平均带药天数'
,add column homeRoomWhNum4 int not null default 0 comment '病房总带药支数'
,add column whbwnum int not null default 0 comment '门急诊雾化博雾人次'
,add column roomWhbwnum int not null default 0 comment '病房雾化博雾人次'
,add column whDaysEmerging DECIMAL(11,2) not null default 0 comment '门急诊平均天数'
,add column whDaysRoom DECIMAL(11,2) not null default 0 comment '病房平均天数';
