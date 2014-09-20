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
    salenum         int, /*卖/赠泵数量*/
    asthmanum       int, /*哮喘*患者人次*/
    ltenum          int, /*处方>=8天的哮喘持续期病人次*/
    lsnum           int, /*持续期病人中推荐使用令舒的人次*/
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

/*
 * 1.每周一凌晨0点开始备份上上周的医生中间表，统计有哪些医生
 * 2.在用户通过界面关联医生销售时，本中间表要同步更新销售ID
 * 
 * */
drop table tbl_doctor_weekly;
create table tbl_doctor_weekly(
    id              int NOT NULL primary key auto_increment,
    duration        varchar(30),
    doctorId        int,
    doctorName      varchar(255),
    salesCode       varchar(20),
    hospitalCode    varchar(20),
    doctorCreateDT  datetime
);

ALTER  TABLE tbl_hos_user ADD INDEX INDEX_HOS_USER_USERCODE (userCode);

alter table tbl_hospital add column portNum int;
alter table tbl_pediatrics_data add column portNum int;

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