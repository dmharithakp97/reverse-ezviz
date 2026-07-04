/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.videogo.openapi.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.videogo.openapi.annotation.Serializable;

public class EZCloudServicePackageInfo
implements Parcelable {
    @Serializable(name="id")
    private long serviceId;
    @Serializable(name="productPayType")
    private int productPayType;
    @Serializable(name="storageTimeUnit")
    private int storageTimeUnit;
    @Serializable(name="expireTime")
    private String expireTime;
    @Serializable(name="forceBinding")
    private int forceBinding;
    @Serializable(name="effectImmediately")
    private int effectImmediately;
    @Serializable(name="canFamilyDeviceNum")
    private int canFamilyDeviceNum;
    @Serializable(name="familyServiceId")
    private long familyServiceId;
    @Serializable(name="serviceTime")
    private int serviceTime;
    @Serializable(name="addFamilyDeviceNum")
    private int addFamilyDeviceNum;
    @Serializable(name="productCode")
    private int productCode;
    @Serializable(name="buyNum")
    private int buyNum;
    @Serializable(name="businessOrderNo")
    private String businessOrderNo;
    @Serializable(name="serviceTimeUnit")
    private int serviceTimeUnit;
    @Serializable(name="storageTime")
    private int storageTime;
    @Serializable(name="serviceType")
    private int serviceType;
    @Serializable(name="effectTime")
    private String effectTime;
    @Serializable(name="status")
    private int status;
    public static final Parcelable.Creator<EZCloudServicePackageInfo> CREATOR = new Parcelable.Creator<EZCloudServicePackageInfo>(){

        public EZCloudServicePackageInfo createFromParcel(Parcel in) {
            return new EZCloudServicePackageInfo(in);
        }

        public EZCloudServicePackageInfo[] newArray(int size) {
            return new EZCloudServicePackageInfo[size];
        }
    };

    public EZCloudServicePackageInfo() {
    }

    public long getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public int getProductPayType() {
        return this.productPayType;
    }

    public void setProductPayType(int productPayType) {
        this.productPayType = productPayType;
    }

    public int getStorageTimeUnit() {
        return this.storageTimeUnit;
    }

    public void setStorageTimeUnit(int storageTimeUnit) {
        this.storageTimeUnit = storageTimeUnit;
    }

    public String getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public int getForceBinding() {
        return this.forceBinding;
    }

    public void setForceBinding(int forceBinding) {
        this.forceBinding = forceBinding;
    }

    public int getEffectImmediately() {
        return this.effectImmediately;
    }

    public void setEffectImmediately(int effectImmediately) {
        this.effectImmediately = effectImmediately;
    }

    public int getCanFamilyDeviceNum() {
        return this.canFamilyDeviceNum;
    }

    public void setCanFamilyDeviceNum(int canFamilyDeviceNum) {
        this.canFamilyDeviceNum = canFamilyDeviceNum;
    }

    public long getFamilyServiceId() {
        return this.familyServiceId;
    }

    public void setFamilyServiceId(long familyServiceId) {
        this.familyServiceId = familyServiceId;
    }

    public int getServiceTime() {
        return this.serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getAddFamilyDeviceNum() {
        return this.addFamilyDeviceNum;
    }

    public void setAddFamilyDeviceNum(int addFamilyDeviceNum) {
        this.addFamilyDeviceNum = addFamilyDeviceNum;
    }

    public int getProductCode() {
        return this.productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public int getBuyNum() {
        return this.buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getBusinessOrderNo() {
        return this.businessOrderNo;
    }

    public void setBusinessOrderNo(String businessOrderNo) {
        this.businessOrderNo = businessOrderNo;
    }

    public int getServiceTimeUnit() {
        return this.serviceTimeUnit;
    }

    public void setServiceTimeUnit(int serviceTimeUnit) {
        this.serviceTimeUnit = serviceTimeUnit;
    }

    public int getStorageTime() {
        return this.storageTime;
    }

    public void setStorageTime(int storageTime) {
        this.storageTime = storageTime;
    }

    public int getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getEffectTime() {
        return this.effectTime;
    }

    public void setEffectTime(String effectTime) {
        this.effectTime = effectTime;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    protected EZCloudServicePackageInfo(Parcel in) {
        this.serviceId = in.readLong();
        this.productPayType = in.readInt();
        this.storageTimeUnit = in.readInt();
        this.expireTime = in.readString();
        this.forceBinding = in.readInt();
        this.effectImmediately = in.readInt();
        this.canFamilyDeviceNum = in.readInt();
        this.familyServiceId = in.readLong();
        this.serviceTime = in.readInt();
        this.addFamilyDeviceNum = in.readInt();
        this.productCode = in.readInt();
        this.buyNum = in.readInt();
        this.businessOrderNo = in.readString();
        this.serviceTimeUnit = in.readInt();
        this.storageTime = in.readInt();
        this.serviceType = in.readInt();
        this.effectTime = in.readString();
        this.status = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.serviceId);
        dest.writeInt(this.productPayType);
        dest.writeInt(this.storageTimeUnit);
        dest.writeString(this.expireTime);
        dest.writeInt(this.forceBinding);
        dest.writeInt(this.effectImmediately);
        dest.writeInt(this.canFamilyDeviceNum);
        dest.writeLong(this.familyServiceId);
        dest.writeInt(this.serviceTime);
        dest.writeInt(this.addFamilyDeviceNum);
        dest.writeInt(this.productCode);
        dest.writeInt(this.buyNum);
        dest.writeString(this.businessOrderNo);
        dest.writeInt(this.serviceTimeUnit);
        dest.writeInt(this.storageTime);
        dest.writeInt(this.serviceType);
        dest.writeString(this.effectTime);
        dest.writeInt(this.status);
    }

    public int describeContents() {
        return 0;
    }
}

