package com.bogiruapps.rdshapp.data.notice

import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result

interface NoticeDataSource {

    suspend fun createNotice(user: User, notice: Notice): Result<Void>

    suspend fun updateNotice(user: User, notice: Notice): Result<Void?>

    suspend fun deleteNotice(user: User, notice: Notice): Result<Void?>

}