package app.nexd.android.ui.auth.register

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.nexd.android.Api
import app.nexd.android.Preferences
import app.nexd.android.R
import app.nexd.android.api.model.BackendErrorEntry.ErrorCodeEnum.*
import app.nexd.android.api.model.RegisterDto
import app.nexd.android.network.BackendError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class RegisterViewModel(
    private val api: Api,
    private val preferences: Preferences
) : ViewModel() {

    sealed class Progress {
        object Idle : Progress()
        object Loading : Progress()
        class Error(@StringRes val message: Int? = null) : Progress()
        class Finished(val token: String) : Progress()
    }

    val firstName = MutableLiveData("")
    val firstNameError = MutableLiveData(0)
    val firstNameIsEnabled = MutableLiveData(true)

    val lastName = MutableLiveData("")
    val lastNameError = MutableLiveData(0)
    val lastNameIsEnabled = MutableLiveData(true)

    val email = MutableLiveData("")
    val emailError = MutableLiveData(0)
    val emailIsEnabled = MutableLiveData(true)

    val password = MutableLiveData("")
    val passwordError = MutableLiveData(0)
    val passwordIsEnabled = MutableLiveData(true)

    val passwordConfirmation = MutableLiveData("")
    val passwordConfirmationError = MutableLiveData(0)
    val passwordConfirmationIsEnabled = MutableLiveData(true)

    val dataProtection = MutableLiveData(false)
    val dataProtectionError = MutableLiveData(0)
    val dataProtectionIsEnabled = MutableLiveData(true)

    val buttonRegisterIsEnabled = MutableLiveData(true)
    val buttonDataProtectionIsEnabled = MutableLiveData(true)

    val progress = MutableLiveData<Progress>(Progress.Idle)

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }

    fun register() {
        switchUiIsEnabled(false)
        var success = true

        if (firstName.value.isNullOrEmpty()) {
            firstNameError.value = R.string.error_message_user_detail_field_missing
            success = false
        }

        if (lastName.value.isNullOrEmpty()) {
            lastNameError.value = R.string.error_message_user_detail_field_missing
            success = false
        }

        if (email.value.isNullOrEmpty()) {
            emailError.value = R.string.error_message_user_detail_field_missing
            success = false
        }

        if (password.value.isNullOrEmpty()) {
            passwordError.value = R.string.error_message_user_detail_field_missing
            success = false
        }

        if (passwordConfirmation.value.isNullOrEmpty()) {
            passwordConfirmationError.value = R.string.error_message_user_detail_field_missing
            success = false
        } else if (password.value != passwordConfirmation.value) {
            passwordError.value = R.string.error_message_registration_password_match
            passwordConfirmationError.value = R.string.error_message_registration_password_match
            success = false
        }

        if (dataProtection.value == false) {
            dataProtectionError.value = R.string.error_message_registration_field_missing
            success = false
        } else {
            dataProtectionError.value = 0
        }

        if (success) {
            progress.value = Progress.Loading
            with(api) {
                authControllerRegister(
                    RegisterDto()
                        .firstName(firstName.value)
                        .lastName(lastName.value)
                        .email(email.value)
                        .password(password.value)
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            switchUiIsEnabled(true)
                            progress.value = Progress.Finished(token = it.accessToken)
                        },
                        {
                            switchUiIsEnabled(true)
                            handleErrors(it)
                        }
                    )
            }
        } else {
            switchUiIsEnabled(true)
            progress.value = Progress.Error()
        }
    }

    private fun handleErrors(throwable: Throwable) {
        if (throwable is BackendError) {
            throwable.errorCodes.forEach {
                when (it) {
                    USERS_USER_EXISTS -> {
                        emailError.value =
                            R.string.error_message_registration_user_already_exists
                    }
                    VALIDATION_PASSWORD_TOO_SHORT -> {
                        passwordError.value = R.string.error_message_registration_password_too_short
                        passwordConfirmationError.value =
                            R.string.error_message_registration_password_too_short
                    }
                    VALIDATION_EMAIL_INVALID -> {
                        emailError.value = R.string.error_message_registration_invalid_email
                    }
                    else -> {
                        Log.e(
                            RegisterViewModel::class.simpleName,
                            "Unknown error $it",
                            throwable
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
    }

    private fun switchUiIsEnabled(enable: Boolean) {
        firstNameIsEnabled.value = enable
        lastNameIsEnabled.value = enable
        emailIsEnabled.value = enable
        passwordIsEnabled.value = enable
        passwordConfirmationIsEnabled.value = enable
        dataProtectionIsEnabled.value = enable
        buttonRegisterIsEnabled.value = enable
        buttonDataProtectionIsEnabled.value = enable
    }

}