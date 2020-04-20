package app.nexd.android.ui.auth.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import app.nexd.android.R
import app.nexd.android.api
import app.nexd.android.api.model.UpdateUserDto
import app.nexd.android.ui.utils.ErrorUtil
import io.reactivex.android.schedulers.AndroidSchedulers

class RegisterDetailedViewModel(application: Application) : AndroidViewModel(application) {

    sealed class Progress {
        object Idle : Progress()
        object Loading : Progress()
        class Error(val message: String) : Progress()
        object Finished : Progress()
    }

    val phoneNumber = MutableLiveData("")

    val phoneNumberError = MutableLiveData(0)

    val street = MutableLiveData("")

    val streetError = MutableLiveData(0)

    val houseNumber = MutableLiveData("")

    val houseNumberError = MutableLiveData(0)

    val zipCode = MutableLiveData("")

    val zipCodeError = MutableLiveData(0)

    val locality = MutableLiveData("")

    val localityError = MutableLiveData(0)

    val dataProtection = MutableLiveData(false)

    val dataProtectionError = MutableLiveData(0)

    val progress = MutableLiveData<Progress>(Progress.Idle)

    fun setUserDetails() {
        var success = true

        if (phoneNumber.value.isNullOrEmpty()) {
            phoneNumberError.value = R.string.error_message_user_detail_field_missing
            success = false
        } else {
            phoneNumberError.value = 0
        }

        if (street.value.isNullOrEmpty()) {
            streetError.value = R.string.error_message_user_detail_field_missing
            success = false
        } else
            streetError.value = 0

        if (houseNumber.value.isNullOrEmpty()) {
            houseNumberError.value = R.string.error_message_user_detail_field_missing
            success = false
        } else
            houseNumberError.value = 0

        if (zipCode.value.isNullOrEmpty()) {
            zipCodeError.value = R.string.error_message_user_detail_field_missing
            success = false
        } else
            zipCodeError.value = 0

        if (locality.value.isNullOrEmpty()) {
            localityError.value = R.string.error_message_user_detail_field_missing
            success = false
        } else
            localityError.value = 0

        if (dataProtection.value == false) {
            dataProtectionError.value = R.string.error_message_registration_field_missing
            success = false
        } else
            dataProtectionError.value = 0

        if (success) {
            progress.value = Progress.Loading
            with(api) {
                userControllerUpdateMyself(
                    UpdateUserDto()
                        .phoneNumber(phoneNumber.value)
                        .street(street.value)
                        .number(houseNumber.value)
                        .zipCode(zipCode.value)
                        .city(locality.value)
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        progress.value = Progress.Finished
                    }, {
                        progress.value = Progress.Error(ErrorUtil.parseError(it).firstMessage)
                    })
            }
        }
    }

}