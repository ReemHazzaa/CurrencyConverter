package com.reem.currencyconverter.app.utils

import org.junit.Assert
import org.junit.Test

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
        val yesterday = "2023-07-24"

        Assert.assertEquals(
            getCalculatedDate(dateFormat, noOfDaysBefore),
            yesterday
        )
    }

    @Test
    fun getCalculatedDate_validDateFormatAndPositiveDaysValue_success() {
        val noOfDaysAfter = 1
        val dateFormat = "yyyy-MM-dd"
        val tomorrow = "2023-07-26"

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
        val expected = listOf("2023-07-24", "2023-07-23", "2023-07-22")
        Assert.assertEquals(expected, getLast3daysDates())
    }

}