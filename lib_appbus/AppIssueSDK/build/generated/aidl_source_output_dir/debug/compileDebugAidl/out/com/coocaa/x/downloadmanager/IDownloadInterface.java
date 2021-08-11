/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\project\\github\\lib_appbus\\lib_appbus\\AppIssueSDK\\src\\main\\aidl\\com\\coocaa\\x\\downloadmanager\\IDownloadInterface.aidl
 */
package com.coocaa.x.downloadmanager;
public interface IDownloadInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.coocaa.x.downloadmanager.IDownloadInterface
{
private static final java.lang.String DESCRIPTOR = "com.coocaa.x.downloadmanager.IDownloadInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.coocaa.x.downloadmanager.IDownloadInterface interface,
 * generating a proxy if needed.
 */
public static com.coocaa.x.downloadmanager.IDownloadInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.coocaa.x.downloadmanager.IDownloadInterface))) {
return ((com.coocaa.x.downloadmanager.IDownloadInterface)iin);
}
return new com.coocaa.x.downloadmanager.IDownloadInterface.Stub.Proxy(obj);
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
case TRANSACTION_startDownload:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.startDownload(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_pauseDownload:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.pauseDownload(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_cancleDownload:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.cancleDownload(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getDonwloadInfo:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
com.coocaa.x.downloadmanager.DownloadInfo _result = this.getDonwloadInfo(_arg0);
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
case TRANSACTION_install:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
boolean _result = this.install(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_unInstall:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.unInstall(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_addDownloadListener:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
com.coocaa.x.downloadmanager.IDownloadListener _arg1;
_arg1 = com.coocaa.x.downloadmanager.IDownloadListener.Stub.asInterface(data.readStrongBinder());
this.addDownloadListener(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_removeDownloadListener:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
com.coocaa.x.downloadmanager.IDownloadListener _arg1;
_arg1 = com.coocaa.x.downloadmanager.IDownloadListener.Stub.asInterface(data.readStrongBinder());
this.removeDownloadListener(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_addInstallListener:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
com.coocaa.x.downloadmanager.IInstallListener _arg1;
_arg1 = com.coocaa.x.downloadmanager.IInstallListener.Stub.asInterface(data.readStrongBinder());
this.addInstallListener(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_removeInstallListener:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
com.coocaa.x.downloadmanager.IInstallListener _arg1;
_arg1 = com.coocaa.x.downloadmanager.IInstallListener.Stub.asInterface(data.readStrongBinder());
this.removeInstallListener(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_addUnInstallListener:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
com.coocaa.x.downloadmanager.IUnInstallListener _arg1;
_arg1 = com.coocaa.x.downloadmanager.IUnInstallListener.Stub.asInterface(data.readStrongBinder());
this.addUnInstallListener(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_removeUnInstallListener:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
com.coocaa.x.downloadmanager.IUnInstallListener _arg1;
_arg1 = com.coocaa.x.downloadmanager.IUnInstallListener.Stub.asInterface(data.readStrongBinder());
this.removeUnInstallListener(_arg0, _arg1);
reply.writeNoException();
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.coocaa.x.downloadmanager.IDownloadInterface
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
@Override public boolean startDownload(java.lang.String pkg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
mRemote.transact(Stub.TRANSACTION_startDownload, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean pauseDownload(java.lang.String pkg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
mRemote.transact(Stub.TRANSACTION_pauseDownload, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean cancleDownload(java.lang.String pkg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
mRemote.transact(Stub.TRANSACTION_cancleDownload, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public com.coocaa.x.downloadmanager.DownloadInfo getDonwloadInfo(java.lang.String pkg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.coocaa.x.downloadmanager.DownloadInfo _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
mRemote.transact(Stub.TRANSACTION_getDonwloadInfo, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.coocaa.x.downloadmanager.DownloadInfo.CREATOR.createFromParcel(_reply);
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
@Override public boolean install(java.lang.String pkg, java.lang.String path) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
_data.writeString(path);
mRemote.transact(Stub.TRANSACTION_install, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean unInstall(java.lang.String pkg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
mRemote.transact(Stub.TRANSACTION_unInstall, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void addDownloadListener(java.lang.String pkg, com.coocaa.x.downloadmanager.IDownloadListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addDownloadListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void removeDownloadListener(java.lang.String pkg, com.coocaa.x.downloadmanager.IDownloadListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeDownloadListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void addInstallListener(java.lang.String pkg, com.coocaa.x.downloadmanager.IInstallListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addInstallListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void removeInstallListener(java.lang.String pkg, com.coocaa.x.downloadmanager.IInstallListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeInstallListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void addUnInstallListener(java.lang.String pkg, com.coocaa.x.downloadmanager.IUnInstallListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addUnInstallListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void removeUnInstallListener(java.lang.String pkg, com.coocaa.x.downloadmanager.IUnInstallListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkg);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeUnInstallListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_startDownload = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_pauseDownload = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_cancleDownload = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getDonwloadInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_install = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_unInstall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_addDownloadListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_removeDownloadListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_addInstallListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_removeInstallListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_addUnInstallListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_removeUnInstallListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
}
public boolean startDownload(java.lang.String pkg) throws android.os.RemoteException;
public boolean pauseDownload(java.lang.String pkg) throws android.os.RemoteException;
public boolean cancleDownload(java.lang.String pkg) throws android.os.RemoteException;
public com.coocaa.x.downloadmanager.DownloadInfo getDonwloadInfo(java.lang.String pkg) throws android.os.RemoteException;
public boolean install(java.lang.String pkg, java.lang.String path) throws android.os.RemoteException;
public boolean unInstall(java.lang.String pkg) throws android.os.RemoteException;
public void addDownloadListener(java.lang.String pkg, com.coocaa.x.downloadmanager.IDownloadListener listener) throws android.os.RemoteException;
public void removeDownloadListener(java.lang.String pkg, com.coocaa.x.downloadmanager.IDownloadListener listener) throws android.os.RemoteException;
public void addInstallListener(java.lang.String pkg, com.coocaa.x.downloadmanager.IInstallListener listener) throws android.os.RemoteException;
public void removeInstallListener(java.lang.String pkg, com.coocaa.x.downloadmanager.IInstallListener listener) throws android.os.RemoteException;
public void addUnInstallListener(java.lang.String pkg, com.coocaa.x.downloadmanager.IUnInstallListener listener) throws android.os.RemoteException;
public void removeUnInstallListener(java.lang.String pkg, com.coocaa.x.downloadmanager.IUnInstallListener listener) throws android.os.RemoteException;
}
