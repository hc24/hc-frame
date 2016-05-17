package com.hc24.frame.spring.exception;

/**
 * 业务层异常类
 * @author hc24
 *
 */
public class ServiceException extends RuntimeException
{
  private static final long serialVersionUID = 2940496825720577090L;

  public ServiceException()
  {
  }

  public ServiceException(String message)
  {
    super(message);
  }

  public ServiceException(Throwable cause)
  {
    super(cause);
  }

  public ServiceException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
