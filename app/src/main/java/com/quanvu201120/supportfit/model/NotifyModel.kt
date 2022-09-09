package com.quanvu201120.supportfit.model

import android.os.Parcel
import android.os.Parcelable

class NotifyModel(
    var notifyId : String = "",
    var postId : String = "",
    var userId : String = "",
    var dateCreate : String = "",
    var status : Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte()) {
    }

    override fun toString(): String {
        return "NotifyModel(notifyId='$notifyId', postId='$postId', userId='$userId', dateCreate='$dateCreate', status=$status)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(notifyId)
        parcel.writeString(postId)
        parcel.writeString(userId)
        parcel.writeString(dateCreate)
        parcel.writeByte(if (status) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotifyModel> {
        override fun createFromParcel(parcel: Parcel): NotifyModel {
            return NotifyModel(parcel)
        }

        override fun newArray(size: Int): Array<NotifyModel?> {
            return arrayOfNulls(size)
        }
    }
}