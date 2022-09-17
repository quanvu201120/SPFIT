package com.quanvu201120.supportfit.model

import android.os.Parcel
import android.os.Parcelable

class PostModel(
    var postId : String = "",
    var userId : String = "",
    var nameUser : String = "",
    var dateCreate : ArrayList<Int> = ArrayList(),
    var title : String = "",
    var description : String = "",
    var listCmt : ArrayList<CmtModel> = ArrayList(),
    var listUserFollow : ArrayList<String> = ArrayList(),
    var image: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("dateCreate"),
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("listCmt"),
        TODO("listUserFollow"),
        parcel.readString().toString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(postId)
        parcel.writeString(userId)
        parcel.writeString(nameUser)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "PostModel(postId='$postId', userId='$userId', nameUser='$nameUser', dateCreate=$dateCreate, title='$title', description='$description', listCmt=$listCmt, listUserFollow=$listUserFollow, image='$image')"
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