package com.reem.currencyconverter.app.utils

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateUtilsKtTest {

    @Test
    fun formatDate_validDateFormat_success() {
        val unFormattedDate = "2023-07-22"
        val formattedDate = "Jul 22, 2023"
        Assert.assertEquals(formattedDate, formatDate(unFormattedDate))
    }

    @Test
    fun formatDate_invalidDateFormat_failure() {
        val unFormattedDate = "22-07-2023"
        val formattedDate = "Jul 22, 2023"
        Assert.assertNotEquals(formattedDate, formatDate(unFormattedDate))
    }

    @Test
    fun formatDate_emptyDate_failure() {
        val unFormattedDate = "22-07-2023"
        val formattedDate = "Jul 22, 2023"
        Assert.assertNotEquals(formattedDate, formatDate(unFormattedDate))
    }

    @Test
    fun getCalculatedDate_validDateFormatAndNegativeDaysValue_success() {
        val noOfDaysBefore = -1
        val dateFormat = "yyyy-MM-dd"
        val yesterday = getCalculatedDate( -1)

        Assert.assertEquals(
            getCalculatedDate(dateFormat, noOfDaysBefore),
            yesterday
        )
    }

    @Test
    fun getCalculatedDate_validDateFormatAndPositiveDaysValue_success() {
        val noOfDaysAfter = 1
        val dateFormat = "yyyy-MM-dd"
        val tomorrow = getCalculatedDate( 1)

        Assert.assertEquals(
            getCalculatedDate(dateFormat, noOfDaysAfter),
            tomorrow
        )
    }

    @Test
    fun getCalculatedDate_invalidDateFormat_failure() {
        val noOfDaysBefore = -1
        val dateFormat = "mmbb"

        Assert.assertThrows(IllegalArgumentException::class.java) {
            getCalculatedDate(dateFormat, noOfDaysBefore)
        }
    }

    @Test
    fun getLast3daysDates_success() {
        val expected = listOf(getCalculatedDate(-1), getCalculatedDate(-2), getCalculatedDate(-3))
        Assert.assertEquals(expected, getLast3daysDates())
    }

    private fun getCalculatedDate( days: Int): String? {
        val cal: Calendar = Calendar.getInstance()
        val s = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

}