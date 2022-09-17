package com.quanvu201120.supportfit.model

import android.os.Parcel
import android.os.Parcelable

class CmtModel(
    var cmtId : String = "",
    var userId : String = "",
    var postId : String = "",
    var content : String = "",
    var yearCreate : Int = 0,
    var monthCreate : Int = 0,
    var dayCreate : Int = 0,
    var hourCreate : Int = 0,
    var minuteCreate : Int = 0,
    var secondsCreate : Int = 0,
    var nameUser:String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cmtId)
        parcel.writeString(userId)
        parcel.writeString(postId)
        parcel.writeString(content)
        parcel.writeInt(yearCreate)
        parcel.writeInt(monthCreate)
        parcel.writeInt(dayCreate)
        parcel.writeInt(hourCreate)
        parcel.writeInt(minuteCreate)
        parcel.writeInt(secondsCreate)
        parcel.writeString(nameUser)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "CmtModel(cmtId='$cmtId', userId='$userId', postId='$postId', content='$content', yearCreate=$yearCreate, monthCreate=$monthCreate, dayCreate=$dayCreate, hourCreate=$hourCreate, minuteCreate=$minuteCreate, secondsCreate=$secondsCreate, nameUser='$nameUser')"
    }

    companion object CREATOR : Parcelable.Creator<CmtModel> {
        override fun createFromParcel(parcel: Parcel): CmtModel {
            return CmtModel(parcel)
        }

        override fun newArray(size: Int): Array<CmtModel?> {
            return arrayOfNulls(size)
        }
    }
}