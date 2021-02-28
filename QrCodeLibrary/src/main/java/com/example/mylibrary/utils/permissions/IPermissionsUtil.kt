package com.example.mylibrary.utils.permissions

import android.app.Activity
import io.reactivex.Single

interface IPermissionsUtil {
    /**
     * Call to check if the permission is granted or not
     */
    fun isPermissionGranted(activity: Activity, vararg permissionsNames: String): Single<Boolean>
}