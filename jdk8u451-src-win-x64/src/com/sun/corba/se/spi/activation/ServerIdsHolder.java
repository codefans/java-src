package com.sun.corba.se.spi.activation;


/**
* com/sun/corba/se/spi/activation/ServerIdsHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/jenkins/workspace/8-2-build-windows-x64-cygwin-sans-NAS/jdk8u451/1832/corba/src/share/classes/com/sun/corba/se/spi/activation/activation.idl
* Saturday, April 5, 2025 3:19:39 AM UTC
*/

public final class ServerIdsHolder implements org.omg.CORBA.portable.Streamable
{
  public int value[] = null;

  public ServerIdsHolder ()
  {
  }

  public ServerIdsHolder (int[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = com.sun.corba.se.spi.activation.ServerIdsHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    com.sun.corba.se.spi.activation.ServerIdsHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return com.sun.corba.se.spi.activation.ServerIdsHelper.type ();
  }

}
