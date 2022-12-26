package example.win10.kozbookapp.model

class Location {
    var locationId: Int? = null
        private set
    var name: String? = null
    var extraInfo: String? = null

    constructor()
    constructor(location_id: Int, name: String?) {
        this.locationId = location_id
        this.name = name
    }

    constructor(name: String?, extra_info: String?) {
        this.name = name
        this.extraInfo = extra_info
    }

    constructor(location_id: Int, name: String?, extra_info: String?) {
        this.locationId = location_id
        this.name = name
        this.extraInfo = extra_info
    }

    fun setLocationId(location_id: Int) {
        this.locationId = location_id
    }

    override fun toString(): String {
        return if (extraInfo == null) name!! else "$name: $extraInfo"
    }
}