package com.quanvu201120.supportfit.model

import android.os.Parcel
import android.os.Parcelable

class NotifyModel(
    var notifyId : String = "",
    var postId : String = "",
    var userId : String = "",
    var yearCreate : Int = 0,
    var monthCreate : Int = 0,
    var dayCreate : Int = 0,
    var hourCreate : Int = 0,
    var minuteCreate : Int = 0,
    var secondsCreate : Int = 0,
    var status : Boolean = false,
    var content:String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(notifyId)
        parcel.writeString(postId)
        parcel.writeString(userId)
        parcel.writeInt(yearCreate)
        parcel.writeInt(monthCreate)
        parcel.writeInt(dayCreate)
        parcel.writeInt(hourCreate)
        parcel.writeInt(minuteCreate)
        parcel.writeInt(secondsCreate)
        parcel.writeByte(if (status) 1 else 0)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "NotifyModel(notifyId='$notifyId', postId='$postId', userId='$userId', yearCreate=$yearCreate, monthCreate=$monthCreate, dayCreate=$dayCreate, hourCreate=$hourCreate, minuteCreate=$minuteCreate, secondsCreate=$secondsCreate, status=$status, content='$content')"
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