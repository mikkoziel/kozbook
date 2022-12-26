package example.win10.kozbookapp.model.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import example.win10.kozbookapp.model.Book

@SuppressLint("ViewConstructor")
class BookLayout(context: Context?, var book: Book) : LinearLayout(context)