package com.hc24.frame.spring.exception;

/**
 * 文件上传异常
 * @author hc24
 *
 */
public class FileUploadException extends RuntimeException
{
  private static final long serialVersionUID = 2940496825720577090L;

  public FileUploadException()
  {
  }

  public FileUploadException(String message)
  {
    super(message);
  }

  public FileUploadException(Throwable cause)
  {
    super(cause);
  }

  public FileUploadException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
