package com.sun.corba.se.PortableActivationIDL.RepositoryPackage;

/**
* com/sun/corba/se/PortableActivationIDL/RepositoryPackage/ServerDefHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/jenkins/workspace/8-2-build-windows-x64-cygwin-sans-NAS/jdk8u451/1832/corba/src/share/classes/com/sun/corba/se/PortableActivationIDL/activation.idl
* Saturday, April 5, 2025 3:19:39 AM UTC
*/

public final class ServerDefHolder implements org.omg.CORBA.portable.Streamable
{
  public com.sun.corba.se.PortableActivationIDL.RepositoryPackage.ServerDef value = null;

  public ServerDefHolder ()
  {
  }

  public ServerDefHolder (com.sun.corba.se.PortableActivationIDL.RepositoryPackage.ServerDef initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = com.sun.corba.se.PortableActivationIDL.RepositoryPackage.ServerDefHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    com.sun.corba.se.PortableActivationIDL.RepositoryPackage.ServerDefHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return com.sun.corba.se.PortableActivationIDL.RepositoryPackage.ServerDefHelper.type ();
  }

}
