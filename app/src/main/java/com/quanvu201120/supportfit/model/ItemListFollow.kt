package com.quanvu201120.supportfit.model

import android.os.Parcel
import android.os.Parcelable

class ItemListFollow(var notifiId : String = "", var idPost : String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(notifiId)
        parcel.writeString(idPost)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "ItemListFollow(notifiId='$notifiId', idPost='$idPost')"
    }

    companion object CREATOR : Parcelable.Creator<ItemListFollow> {
        override fun createFromParcel(parcel: Parcel): ItemListFollow {
            return ItemListFollow(parcel)
        }

        override fun newArray(size: Int): Array<ItemListFollow?> {
            return arrayOfNulls(size)
        }
    }
}