package com.quanvu201120.supportfit.model

import android.os.Parcel
import android.os.Parcelable

class NotifyModel(
    var notifyId : String = "",
    var postId : String = "",
    var userId : String = "",
    var dateCreate : ArrayList<Int> = ArrayList(),
    var status : Boolean = false
)