/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\project\\github\\lib_appbus\\lib_appbus\\AppBusSdk\\src\\main\\aidl\\com\\coocaa\\appbus\\traffic\\NotifyAidl.aidl
 */
package com.coocaa.appbus.traffic;
public interface NotifyAidl extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.coocaa.appbus.traffic.NotifyAidl
{
private static final java.lang.String DESCRIPTOR = "com.coocaa.appbus.traffic.NotifyAidl";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.coocaa.appbus.traffic.NotifyAidl interface,
 * generating a proxy if needed.
 */
public static com.coocaa.appbus.traffic.NotifyAidl asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.coocaa.appbus.traffic.NotifyAidl))) {
return ((com.coocaa.appbus.traffic.NotifyAidl)iin);
}
return new com.coocaa.appbus.traffic.NotifyAidl.Stub.Proxy(obj);
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
case TRANSACTION_notify:
{
data.enforceInterface(descriptor);
java.util.List<com.coocaa.appbus.traffic.AppInfoBean> _arg0;
_arg0 = data.createTypedArrayList(com.coocaa.appbus.traffic.AppInfoBean.CREATOR);
this.notify(_arg0);
reply.writeNoException();
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.coocaa.appbus.traffic.NotifyAidl
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
@Override public void notify(java.util.List<com.coocaa.appbus.traffic.AppInfoBean> appInfoList) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedList(appInfoList);
mRemote.transact(Stub.TRANSACTION_notify, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_notify = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void notify(java.util.List<com.coocaa.appbus.traffic.AppInfoBean> appInfoList) throws android.os.RemoteException;
}
