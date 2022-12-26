package example.win10.kozbookapp.model

class Location {
    private var locationId = 0
    var name: String? = null
    var extraInfo: String? = null

    constructor(locationId: Int, name: String?) {
        this.locationId = locationId
        this.name = name
    }

    constructor(locationId: Int, name: String?, extraInfo: String?) {
        this.locationId = locationId
        this.name = name
        this.extraInfo = extraInfo
    }
}