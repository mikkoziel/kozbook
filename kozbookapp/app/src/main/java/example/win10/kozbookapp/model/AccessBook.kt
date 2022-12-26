package example.win10.kozbookapp.model

import android.text.format.DateFormat
import java.util.*

class AccessBook {
    var book: Book? = null
    var user: User? = null
    private var startDate: Date? = null
    var endDate: Date? = null

    constructor(book: Book?, user: User?, start_year: Int, start_month: Int, start_day: Int) {
        this.book = book
        this.user = user
        val cal = Calendar.Builder().setCalendarType("gregorian")
                .setDate(start_year, start_month, start_day).build()
        startDate = cal.time
    }

    constructor(book: Book?, user: User?, startDate: Date?, endDate: Date?) {
        this.book = book
        this.user = user
        this.startDate = startDate
        this.endDate = endDate
    }

    val startDateString: CharSequence
        get() = DateFormat.format("dd-mm-yyyy", startDate)
    val endDateString: CharSequence
        get() = DateFormat.format("dd-mm-yyyy", endDate)
}