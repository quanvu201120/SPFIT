package com.quanvu201120.supportfit.model



class ResultApiModel(
    var multicast_id : Long,
    var success: Int,
    var failure: Int,
    var canonical_ids : Int,
    var results : List<MessageId>
) {
    override fun toString(): String {
        return "ResultApiModel(multicast_id=$multicast_id, success=$success, failure=$failure, canonical_ids=$canonical_ids, results=$results)"
    }
}