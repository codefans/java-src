package org.omg.PortableServer.CurrentPackage;


/**
* org/omg/PortableServer/CurrentPackage/NoContext.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/jenkins/workspace/8-2-build-windows-x64-cygwin-sans-NAS/jdk8u451/1832/corba/src/share/classes/org/omg/PortableServer/poa.idl
* Saturday, April 5, 2025 3:19:40 AM UTC
*/

public final class NoContext extends org.omg.CORBA.UserException
{

  public NoContext ()
  {
    super(NoContextHelper.id());
  } // ctor


  public NoContext (String $reason)
  {
    super(NoContextHelper.id() + "  " + $reason);
  } // ctor

} // class NoContext
