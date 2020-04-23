package app.nexd.android.ui.seeker.call

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import app.nexd.android.Api
import io.reactivex.BackpressureStrategy

class PhoneCallViewModel(private val api: Api) : ViewModel() {

    fun getPhoneNumber(): LiveData<String> {
        return LiveDataReactiveStreams.fromPublisher(
            api.phoneControllerGetNumbers()
                .map {
                    it.first().number
                }
                .toFlowable(BackpressureStrategy.LATEST)
        )
    }

}