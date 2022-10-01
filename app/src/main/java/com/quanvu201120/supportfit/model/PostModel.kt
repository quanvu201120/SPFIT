package com.quanvu201120.supportfit.model

import android.os.Parcel
import android.os.Parcelable

class PostModel(
    var postId : String = "",
    var userId : String = "",
    var nameUser : String = "",
    var yearCreate : Int = 0,
    var monthCreate : Int = 0,
    var dayCreate : Int = 0,
    var hourCreate : Int = 0,
    var minuteCreate : Int = 0,
    var secondsCreate : Int = 0,
    var title : String = "",
    var description : String = "",
    var listCmt : ArrayList<CmtModel> = ArrayList(),
    var listUserFollow : ArrayList<String> = ArrayList(),
    var listTokenFollow : ArrayList<String> = ArrayList(),
    var image: String = "",
    var isComplete : Boolean = false,
    var isDisableCmt : Boolean = false,
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
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("listCmt"),
        TODO("listUserFollow"),
        TODO("listTokenFollow"),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()) {
    }

    override fun toString(): String {
        return "PostModel(postId='$postId', userId='$userId', nameUser='$nameUser', yearCreate=$yearCreate, monthCreate=$monthCreate, dayCreate=$dayCreate, hourCreate=$hourCreate, minuteCreate=$minuteCreate, secondsCreate=$secondsCreate, title='$title', description='$description', listCmt=$listCmt, listUserFollow=$listUserFollow, listTokenFollow=$listTokenFollow, image='$image', isComplete=$isComplete, isDisableCmt=$isDisableCmt)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(postId)
        parcel.writeString(userId)
        parcel.writeString(nameUser)
        parcel.writeInt(yearCreate)
        parcel.writeInt(monthCreate)
        parcel.writeInt(dayCreate)
        parcel.writeInt(hourCreate)
        parcel.writeInt(minuteCreate)
        parcel.writeInt(secondsCreate)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeByte(if (isComplete) 1 else 0)
        parcel.writeByte(if (isDisableCmt) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostModel> {
        override fun createFromParcel(parcel: Parcel): PostModel {
            return PostModel(parcel)
        }

        override fun newArray(size: Int): Array<PostModel?> {
            return arrayOfNulls(size)
        }
    }
}