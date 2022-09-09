package com.quanvu201120.supportfit.model

import android.os.Parcel
import android.os.Parcelable

class UserModel(
    var userId : String = "",
    var name : String = "",
    var phone : String = "",
    var email : String = "",
    var listPost : ArrayList<String> = ArrayList(),
    var listNotify : ArrayList<String> = ArrayList(),
    var listFollow : ArrayList<String> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("listPost"),
        TODO("listNotify"),
        TODO("listFollow")) {
    }

    override fun toString(): String {
        return "UserModel(userId='$userId', name='$name', phone='$phone', email='$email', listPost=$listPost, listNotify=$listNotify, listFollow=$listFollow)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
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