package app.nexd.android.ui.helper.checkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import app.nexd.android.Api
import app.nexd.android.api.model.HelpList
import io.reactivex.BackpressureStrategy

class CheckoutViewModel(private val api: Api) : ViewModel() {

    fun getShoppingList(): LiveData<HelpList> {
        return LiveDataReactiveStreams.fromPublisher(
            api.helpListsControllerGetUserLists(null)
                .map { lists -> lists.first { it.status == HelpList.StatusEnum.ACTIVE } }
                .doOnError {
                    Log.e("Error", it.message.toString())
                }
                .onErrorReturnItem(HelpList())
                .toFlowable(BackpressureStrategy.BUFFER))
    }

}