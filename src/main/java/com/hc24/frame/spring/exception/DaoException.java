package com.hc24.frame.spring.exception;

import org.springframework.dao.DataAccessException;

/**
 * 数据访问层异常类
 * @author hc24
 *
 */
public class DaoException extends DataAccessException
{
  private static final long serialVersionUID = 2940496825720577090L;

  public DaoException()
  {
	  super("",null);
  }

  public DaoException(String message)
  {
    super(message);
  }

  public DaoException(Throwable cause)
  {
    super("",cause);
  }

  public DaoException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
