package com.quanvu201120.supportfit.model

import android.os.Parcel
import android.os.Parcelable

class CmtModel(
    var cmtId : String = "",
    var userId : String = "",
    var postId : String = "",
    var content : String = "",
    var dateCreate : String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()) {
    }

    override fun toString(): String {
        return "CmtModel(cmtId='$cmtId', userId='$userId', postId='$postId', content='$content', dateCreate='$dateCreate')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cmtId)
        parcel.writeString(userId)
        parcel.writeString(postId)
        parcel.writeString(content)
        parcel.writeString(dateCreate)
    }

    override fun describeContents(): Int {
        return 0
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