package com.bogiruapps.rdshapp.data.notice

import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.utils.Result

interface NoticeDataSource {

    suspend fun createNotice(region: Region, city: City, school: School, notice: Notice): Result<Void>

    suspend fun updateNotice(region: Region, city: City, school: School, notice: Notice): Result<Void?>

    suspend fun deleteNotice(region: Region, city: City, school: School, notice: Notice): Result<Void?>

}