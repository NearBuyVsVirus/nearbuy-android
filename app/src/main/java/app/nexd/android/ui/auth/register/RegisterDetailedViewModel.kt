package app.nexd.android.ui.auth.register

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.nexd.android.Api
import app.nexd.android.Preferences
import app.nexd.android.R
import app.nexd.android.api.model.BackendErrorEntry.ErrorCodeEnum.VALIDATION_PHONENUMBER_INVALID
import app.nexd.android.api.model.UpdateUserDto
import app.nexd.android.network.BackendError
import io.reactivex.android.schedulers.AndroidSchedulers

class RegisterDetailedViewModel(
    private val api: Api,
    private val preferences: Preferences
) : ViewModel() {

    sealed class Progress {
        object Idle : Progress()
        object Loading : Progress()
        class Error(@StringRes val message: Int? = null) : Progress()
        object Finished : Progress()
    }

    val phoneNumber = MutableLiveData("")
    val phoneNumberError = MutableLiveData(0)
    val phoneNumberIsEnabled = MutableLiveData(true)

    val street = MutableLiveData("")
    val streetError = MutableLiveData(0)
    val streetIsEnabled = MutableLiveData(true)

    val houseNumber = MutableLiveData("")
    val houseNumberError = MutableLiveData(0)
    val houseNumberIsEnabled = MutableLiveData(true)

    val zipCode = MutableLiveData("")
    val zipCodeError = MutableLiveData(0)
    val zipCodeIsEnabled = MutableLiveData(true)

    val locality = MutableLiveData("")
    val localityError = MutableLiveData(0)
    val localityIsEnabled = MutableLiveData(true)

    val buttonRegisterIsEnabled = MutableLiveData(true)
    val buttonDataProtectionIsEnabled = MutableLiveData(true)

    val progress = MutableLiveData<Progress>(Progress.Idle)

    fun setUserDetails() {
        switchUiIsEnabled(false)

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
                        switchUiIsEnabled(true)
                        progress.value = Progress.Finished
                    }, { error ->
                        switchUiIsEnabled(true)
                        if (error is BackendError) {
                            error.errorCodes.forEach {
                                when (it) {
                                    VALIDATION_PHONENUMBER_INVALID -> {
                                        phoneNumberError.value =
                                            R.string.error_message_input_validation_phone_number_invalid
                                    }
                                    else -> {
                                        Log.e(
                                            RegisterDetailedViewModel::class.simpleName,
                                            "Unknown error $it",
                                            error
                                        )
                                        progress.value = Progress.Error()
                                    }
                                }
                            }

                            progress.value = Progress.Error()
                        }

                        if (progress.value !is Progress.Error) {
                            progress.value = Progress.Error(R.string.error_message_unknown)
                        }
                    })
            }
        } else {
            switchUiIsEnabled(true)
            progress.value = Progress.Error()
        }
    }

    private fun switchUiIsEnabled(enable: Boolean) {
        phoneNumberIsEnabled.value = enable
        streetIsEnabled.value = enable
        houseNumberIsEnabled.value = enable
        zipCodeIsEnabled.value = enable
        localityIsEnabled.value = enable
        buttonRegisterIsEnabled.value = enable
        buttonDataProtectionIsEnabled.value = enable
    }

}