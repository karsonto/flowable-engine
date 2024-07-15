CREATE TABLE ACT_DMN_DEPLOYMENT (ID_ VARCHAR(255) NOT NULL, NAME_ VARCHAR(255), CATEGORY_ VARCHAR(255), DEPLOY_TIME_ TIMESTAMP, TENANT_ID_ VARCHAR(255), PARENT_DEPLOYMENT_ID_ VARCHAR(255), CONSTRAINT PK_ACT_DMN_DEPLOY PRIMARY KEY (ID_));

CREATE TABLE ACT_DMN_DEPLOYMENT_RESOURCE (ID_ VARCHAR(255) NOT NULL, NAME_ VARCHAR(255), DEPLOYMENT_ID_ VARCHAR(255), RESOURCE_BYTES_ BLOB, CONSTRAINT PK_ACT_DMN_DEPLOY PRIMARY KEY (ID_));

CREATE TABLE ACT_DMN_DECISION_TABLE (ID_ VARCHAR(255) NOT NULL, NAME_ VARCHAR(255), VERSION_ INTEGER, KEY_ VARCHAR(255), CATEGORY_ VARCHAR(255), DEPLOYMENT_ID_ VARCHAR(255), PARENT_DEPLOYMENT_ID_ VARCHAR(255), TENANT_ID_ VARCHAR(255), RESOURCE_NAME_ VARCHAR(255), DESCRIPTION_ VARCHAR(255), CONSTRAINT PK_ACT_DMN_DECISI PRIMARY KEY (ID_));

CREATE TABLE ACT_DMN_HI_DECISION_EXECUTION (ID_ VARCHAR(255) NOT NULL, DECISION_DEFINITION_ID_ VARCHAR(255), DEPLOYMENT_ID_ VARCHAR(255), START_TIME_ TIMESTAMP, END_TIME_ TIMESTAMP, INSTANCE_ID_ VARCHAR(255), EXECUTION_ID_ VARCHAR(255), ACTIVITY_ID_ VARCHAR(255), FAILED_ SMALLINT DEFAULT 0, TENANT_ID_ VARCHAR(255), EXECUTION_JSON_ CLOB, CONSTRAINT PK_ACT_DMN_HI_DEC PRIMARY KEY (ID_));

ALTER TABLE ACT_DMN_HI_DECISION_EXECUTION ADD SCOPE_TYPE_ VARCHAR(255);

CALL SYSPROC.ADMIN_CMD ('REORG TABLE ACT_DMN_HI_DECISION_EXECUTION');

ALTER TABLE ACT_DMN_DECISION_TABLE DROP COLUMN PARENT_DEPLOYMENT_ID_;

CALL SYSPROC.ADMIN_CMD ('REORG TABLE ACT_DMN_DECISION_TABLE');

CREATE UNIQUE INDEX ACT_IDX_DEC_TBL_UNIQ ON ACT_DMN_DECISION_TABLE(KEY_, VERSION_, TENANT_ID_);

DROP INDEX ACT_IDX_DEC_TBL_UNIQ;

RENAME ACT_DMN_DECISION_TABLE TO ACT_DMN_DECISION;

CALL SYSPROC.ADMIN_CMD ('REORG TABLE ACT_DMN_DECISION');

CREATE UNIQUE INDEX ACT_IDX_DMN_DEC_UNIQ ON ACT_DMN_DECISION(KEY_, VERSION_, TENANT_ID_);

ALTER TABLE ACT_DMN_DECISION ADD DECISION_TYPE_ VARCHAR(255);

CALL SYSPROC.ADMIN_CMD ('REORG TABLE ACT_DMN_DECISION');

CREATE INDEX ACT_IDX_DMN_INSTANCE_ID ON ACT_DMN_HI_DECISION_EXECUTION(INSTANCE_ID_);

insert into ACT_GE_PROPERTY
values ('dmn.schema.version', '7.1.0.1', 1);
