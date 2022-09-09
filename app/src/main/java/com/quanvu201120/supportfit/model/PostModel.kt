package com.quanvu201120.supportfit.model

import android.os.Parcel
import android.os.Parcelable

class PostModel(
    var postId : String = "",
    var userId : String = "",
    var dateCreate : String = "",
    var title : String = "",
    var description : String = "",
    var listCmt : ArrayList<String> = ArrayList(),
    var listUserFollow : ArrayList<String> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("listCmt"),
        TODO("listUserFollow")) {
    }

    override fun toString(): String {
        return "PostModel(postId='$postId', userId='$userId', dateCreate='$dateCreate', title='$title', description='$description', listCmt=$listCmt, listUserFollow=$listUserFollow)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(postId)
        parcel.writeString(userId)
        parcel.writeString(dateCreate)
        parcel.writeString(title)
        parcel.writeString(description)
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