package com.hc24.frame.util;

import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 唯一ID生成器，生成24位递增的唯一字符串
 *
 * @author 6tail
 *
 */
public class UUID implements java.io.Serializable{

  private static final long serialVersionUID = -4415279469780082174L;
  private final int _time;
  private final int _machine;
  private final int _inc;
  private static AtomicInteger _nextInc = new AtomicInteger((new Random()).nextInt());
  private static final int _genmachine;

  /**
   * 获取唯一ID
   *
   * @return 唯一ID，24位递增的唯一字符串
   */
  public static String next(){
    return new UUID().toHexString();
  }

  private UUID(){
    _time = (int)(System.currentTimeMillis()/1000);
    _machine = _genmachine;
    _inc = _nextInc.getAndIncrement();
  }

  private String toHexString(){
    final StringBuilder buf = new StringBuilder(24);
    for(final byte b:toByteArray()){
      buf.append(String.format("%02x",b&0xff));
    }
    return buf.toString();
  }

  private byte[] toByteArray(){
    byte b[] = new byte[12];
    ByteBuffer bb = ByteBuffer.wrap(b);
    bb.putInt(_time);
    bb.putInt(_machine);
    bb.putInt(_inc);
    return b;
  }
  static{
    try{
      int machinePiece;
      try{
        StringBuilder sb = new StringBuilder();
        java.util.Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements()){
          NetworkInterface ni = e.nextElement();
          sb.append(ni.toString());
        }
        machinePiece = sb.toString().hashCode()<<16;
      }catch(Throwable e){
        machinePiece = (new Random().nextInt())<<16;
      }
      final int processPiece;
      int processId = new java.util.Random().nextInt();
      try{
        processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
      }catch(Throwable t){
      }
      ClassLoader loader = UUID.class.getClassLoader();
      int loaderId = loader!=null?System.identityHashCode(loader):0;
      StringBuilder sb = new StringBuilder();
      sb.append(Integer.toHexString(processId));
      sb.append(Integer.toHexString(loaderId));
      processPiece = sb.toString().hashCode()&0xFFFF;
      _genmachine = machinePiece|processPiece;
    }catch(Exception e){
      throw new RuntimeException(e);
    }
  }
  
  public static void main(String[] args) {
	System.out.println(UUID.next());
}
}
