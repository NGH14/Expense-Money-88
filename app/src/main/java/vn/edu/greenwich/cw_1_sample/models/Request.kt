package vn.edu.greenwich.cw_1_sample.models

import java.io.Serializable

class Request : Serializable {
    var id: Long
    var content: String?
    var date: String?
    var time: String?
    var type: String?
    var residentId: Long

    constructor() {
        id = -1
        content = null
        date = null
        time = null
        type = null
        residentId = -1
    }

    constructor(
        id: Long,
        content: String?,
        date: String?,
        time: String?,
        type: String?,
        residentId: Long
    ) {
        this.id = id
        this.content = content
        this.date = date
        this.time = time
        this.type = type
        this.residentId = residentId
    }

    val isEmpty: Boolean
        get() {
            return -1L == id &&
                null == content &&
                null == date &&
                null == time &&
                null == type &&
                -1L == residentId
        }

    override fun toString(): String = "[$type][$date] $content"
}
