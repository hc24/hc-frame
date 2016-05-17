package com.hc24.frame.spring.dao.jdbc;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


public class BaseJdbcDao extends NamedParameterJdbcDaoSupport
{
  private BaseJdbcTemplate baseJdbcTemplate;

  protected void initTemplateConfig()
  {
    this.baseJdbcTemplate = new BaseJdbcTemplate(getJdbcTemplate());
  }

  public BaseJdbcTemplate getBaseJdbcTemplate() {
    return this.baseJdbcTemplate;
  }

  public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate()
  {
    return this.baseJdbcTemplate;
  }
}