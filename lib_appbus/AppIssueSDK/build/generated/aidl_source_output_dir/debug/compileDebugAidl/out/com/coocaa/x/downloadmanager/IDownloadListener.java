/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/dunn/project/github/lib_appbus/lib_appbus/AppIssueSDK/src/main/aidl/com/coocaa/x/downloadmanager/IDownloadListener.aidl
 */
package com.coocaa.x.downloadmanager;
public interface IDownloadListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.coocaa.x.downloadmanager.IDownloadListener
{
private static final java.lang.String DESCRIPTOR = "com.coocaa.x.downloadmanager.IDownloadListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.coocaa.x.downloadmanager.IDownloadListener interface,
 * generating a proxy if needed.
 */
public static com.coocaa.x.downloadmanager.IDownloadListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.coocaa.x.downloadmanager.IDownloadListener))) {
return ((com.coocaa.x.downloadmanager.IDownloadListener)iin);
}
return new com.coocaa.x.downloadmanager.IDownloadListener.Stub.Proxy(obj);
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
case TRANSACTION_onStart:
{
data.enforceInterface(descriptor);
com.coocaa.x.downloadmanager.DownloadInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.coocaa.x.downloadmanager.DownloadInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onStart(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onProgress:
{
data.enforceInterface(descriptor);
com.coocaa.x.downloadmanager.DownloadInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.coocaa.x.downloadmanager.DownloadInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
int _arg1;
_arg1 = data.readInt();
this.onProgress(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onSuccess:
{
data.enforceInterface(descriptor);
com.coocaa.x.downloadmanager.DownloadInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.coocaa.x.downloadmanager.DownloadInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onSuccess(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onPaused:
{
data.enforceInterface(descriptor);
com.coocaa.x.downloadmanager.DownloadInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.coocaa.x.downloadmanager.DownloadInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onPaused(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onFailed:
{
data.enforceInterface(descriptor);
com.coocaa.x.downloadmanager.DownloadInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.coocaa.x.downloadmanager.DownloadInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.lang.String _arg1;
_arg1 = data.readString();
this.onFailed(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onCancle:
{
data.enforceInterface(descriptor);
com.coocaa.x.downloadmanager.DownloadInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.coocaa.x.downloadmanager.DownloadInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.lang.String _arg1;
_arg1 = data.readString();
this.onCancle(_arg0, _arg1);
reply.writeNoException();
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.coocaa.x.downloadmanager.IDownloadListener
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
@Override public void onStart(com.coocaa.x.downloadmanager.DownloadInfo info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onStart, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onProgress(com.coocaa.x.downloadmanager.DownloadInfo info, int progress) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(progress);
mRemote.transact(Stub.TRANSACTION_onProgress, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onSuccess(com.coocaa.x.downloadmanager.DownloadInfo info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onSuccess, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onPaused(com.coocaa.x.downloadmanager.DownloadInfo info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onPaused, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onFailed(com.coocaa.x.downloadmanager.DownloadInfo info, java.lang.String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_onFailed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onCancle(com.coocaa.x.downloadmanager.DownloadInfo info, java.lang.String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_onCancle, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onStart = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onSuccess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onPaused = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onFailed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onCancle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void onStart(com.coocaa.x.downloadmanager.DownloadInfo info) throws android.os.RemoteException;
public void onProgress(com.coocaa.x.downloadmanager.DownloadInfo info, int progress) throws android.os.RemoteException;
public void onSuccess(com.coocaa.x.downloadmanager.DownloadInfo info) throws android.os.RemoteException;
public void onPaused(com.coocaa.x.downloadmanager.DownloadInfo info) throws android.os.RemoteException;
public void onFailed(com.coocaa.x.downloadmanager.DownloadInfo info, java.lang.String msg) throws android.os.RemoteException;
public void onCancle(com.coocaa.x.downloadmanager.DownloadInfo info, java.lang.String msg) throws android.os.RemoteException;
}
