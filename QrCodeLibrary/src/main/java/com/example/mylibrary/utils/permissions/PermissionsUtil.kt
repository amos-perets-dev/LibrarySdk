package com.example.mylibrary.utils.permissions

import android.app.Activity
import android.content.Context
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import io.reactivex.Single

class PermissionsUtil() : IPermissionsUtil{

    override fun isPermissionGranted(activity: Activity, vararg permissionsNames: String): Single<Boolean> {
        return Single.create<Boolean> { emitter ->
            Dexter.withActivity(activity)
                .withPermissions(permissionsNames.toList())
                .withListener(object : BaseMultiplePermissionsListener() {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        super.onPermissionsChecked(report)
                        if (report.areAllPermissionsGranted()) {
                            emitter.onSuccess(true)
                        } else {
                            emitter.onSuccess(false)
                        }
                    }
                })
                .check()
        }
    }

}