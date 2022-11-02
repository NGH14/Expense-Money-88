package vn.edu.greenwich.cw_1_sample.models

import java.io.Serializable
import java.util.*

class Backup(
    var date: Date,
    var deviceName: String,
    residents: ArrayList<Resident>,
    requests: ArrayList<Request>
) : Serializable {
    private var _residents: ArrayList<Resident>
    private var _requests: ArrayList<Request>

    init {
        _residents = residents
        _requests = requests
    }

    var residents: ArrayList<Resident>
        get() = _residents
        set(residents) {
            _residents = residents
        }
    var requests: ArrayList<Request>
        get() = _requests
        set(requests) {
            _requests = requests
        }
}
