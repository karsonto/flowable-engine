update ACT_GE_PROPERTY set VALUE_ = '7.0.1.1' where NAME_ = 'common.schema.version';


alter table ACT_RU_EVENT_SUBSCR add SCOPE_DEFINITION_KEY_ NVARCHAR2(255);

