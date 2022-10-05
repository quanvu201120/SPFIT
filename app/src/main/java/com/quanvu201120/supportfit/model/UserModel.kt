package com.quanvu201120.supportfit.model

import android.os.Parcel
import android.os.Parcelable

class UserModel(
    var userId : String = "",
    var name : String = "",
    var email : String = "",
    var listPost : ArrayList<String> = ArrayList(),
    var listNotify : ArrayList<ItemListNotifi> = ArrayList(),
    var listFollow : ArrayList<ItemListFollow> = ArrayList(),
    var admin : Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("listPost"),
        TODO("listNotify"),
        TODO("listFollow"),
        parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeByte(if (admin) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "UserModel(userId='$userId', name='$name', email='$email', listPost=$listPost, listNotify=$listNotify, listFollow=$listFollow, admin=$admin)"
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}