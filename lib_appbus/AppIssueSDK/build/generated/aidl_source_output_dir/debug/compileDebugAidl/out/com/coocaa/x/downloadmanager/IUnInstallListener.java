/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/dunn/project/github/lib_appbus/lib_appbus/AppIssueSDK/src/main/aidl/com/coocaa/x/downloadmanager/IUnInstallListener.aidl
 */
package com.coocaa.x.downloadmanager;
public interface IUnInstallListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.coocaa.x.downloadmanager.IUnInstallListener
{
private static final java.lang.String DESCRIPTOR = "com.coocaa.x.downloadmanager.IUnInstallListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.coocaa.x.downloadmanager.IUnInstallListener interface,
 * generating a proxy if needed.
 */
public static com.coocaa.x.downloadmanager.IUnInstallListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.coocaa.x.downloadmanager.IUnInstallListener))) {
return ((com.coocaa.x.downloadmanager.IUnInstallListener)iin);
}
return new com.coocaa.x.downloadmanager.IUnInstallListener.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
java.lang.String descriptor = DESCRIPTOR;
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(descriptor);
return true;
}
case TRANSACTION_onUnInstallStart:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
this.onUnInstallStart(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onUnInstallEnd:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
java.lang.String _arg2;
_arg2 = data.readString();
this.onUnInstallEnd(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.coocaa.x.downloadmanager.IUnInstallListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void onUnInstallStart(java.lang.String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_onUnInstallStart, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onUnInstallEnd(java.lang.String packageName, boolean success, java.lang.String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeInt(((success)?(1):(0)));
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_onUnInstallEnd, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onUnInstallStart = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onUnInstallEnd = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void onUnInstallStart(java.lang.String packageName) throws android.os.RemoteException;
public void onUnInstallEnd(java.lang.String packageName, boolean success, java.lang.String msg) throws android.os.RemoteException;
}