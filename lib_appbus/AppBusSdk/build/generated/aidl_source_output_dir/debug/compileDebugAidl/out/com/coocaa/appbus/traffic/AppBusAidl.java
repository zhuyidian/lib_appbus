/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\project\\github\\lib_appbus\\lib_appbus\\AppBusSdk\\src\\main\\aidl\\com\\coocaa\\appbus\\traffic\\AppBusAidl.aidl
 */
package com.coocaa.appbus.traffic;
public interface AppBusAidl extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.coocaa.appbus.traffic.AppBusAidl
{
private static final java.lang.String DESCRIPTOR = "com.coocaa.appbus.traffic.AppBusAidl";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.coocaa.appbus.traffic.AppBusAidl interface,
 * generating a proxy if needed.
 */
public static com.coocaa.appbus.traffic.AppBusAidl asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.coocaa.appbus.traffic.AppBusAidl))) {
return ((com.coocaa.appbus.traffic.AppBusAidl)iin);
}
return new com.coocaa.appbus.traffic.AppBusAidl.Stub.Proxy(obj);
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
case TRANSACTION_run:
{
data.enforceInterface(descriptor);
com.coocaa.appbus.traffic.Request _arg0;
if ((0!=data.readInt())) {
_arg0 = com.coocaa.appbus.traffic.Request.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
com.coocaa.appbus.traffic.Response _result = this.run(_arg0);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_register:
{
data.enforceInterface(descriptor);
com.coocaa.appbus.traffic.AppBusCallback _arg0;
_arg0 = com.coocaa.appbus.traffic.AppBusCallback.Stub.asInterface(data.readStrongBinder());
int _arg1;
_arg1 = data.readInt();
this.register(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_unregister:
{
data.enforceInterface(descriptor);
com.coocaa.appbus.traffic.AppBusCallback _arg0;
_arg0 = com.coocaa.appbus.traffic.AppBusCallback.Stub.asInterface(data.readStrongBinder());
this.unregister(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getAppInfo:
{
data.enforceInterface(descriptor);
java.util.List<com.coocaa.appbus.traffic.AppInfoBean> _result = this.getAppInfo();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.coocaa.appbus.traffic.AppBusAidl
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
@Override public com.coocaa.appbus.traffic.Response run(com.coocaa.appbus.traffic.Request request) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.coocaa.appbus.traffic.Response _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((request!=null)) {
_data.writeInt(1);
request.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_run, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.coocaa.appbus.traffic.Response.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void register(com.coocaa.appbus.traffic.AppBusCallback cb, int pid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
_data.writeInt(pid);
mRemote.transact(Stub.TRANSACTION_register, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregister(com.coocaa.appbus.traffic.AppBusCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregister, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.util.List<com.coocaa.appbus.traffic.AppInfoBean> getAppInfo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.coocaa.appbus.traffic.AppInfoBean> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAppInfo, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.coocaa.appbus.traffic.AppInfoBean.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_run = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_register = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_unregister = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getAppInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public com.coocaa.appbus.traffic.Response run(com.coocaa.appbus.traffic.Request request) throws android.os.RemoteException;
public void register(com.coocaa.appbus.traffic.AppBusCallback cb, int pid) throws android.os.RemoteException;
public void unregister(com.coocaa.appbus.traffic.AppBusCallback cb) throws android.os.RemoteException;
public java.util.List<com.coocaa.appbus.traffic.AppInfoBean> getAppInfo() throws android.os.RemoteException;
}
