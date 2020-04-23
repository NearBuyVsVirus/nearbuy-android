package app.nexd.android.ui.helper.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import app.nexd.android.Api
import app.nexd.android.api.model.HelpList
import app.nexd.android.api.model.HelpListCreateDto
import app.nexd.android.api.model.HelpRequest
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers.io

class HelperDetailViewModel(private val api: Api) : ViewModel() {

    fun requestDetails(requestId: Long): LiveData<HelpRequest> {
        return LiveDataReactiveStreams.fromPublisher(
            api.helpRequestsControllerGetSingleRequest(requestId)
                .doOnError {
                    Log.e("Error", it.message.toString())
                }
                .onErrorReturnItem(HelpRequest()) // TODO: error handling
                .toFlowable(BackpressureStrategy.BUFFER)
        )
    }

    fun acceptRequest(requestId: Long) {
        api.helpListsControllerGetUserLists(null)
            .map { lists ->
                if (lists.any { it.status == HelpList.StatusEnum.ACTIVE })
                    lists.first { it.status == HelpList.StatusEnum.ACTIVE }
                else
                    HelpList()
            }
            .flatMap { shoppingList ->
                if (shoppingList.id == null) { // no help list found
                    api.helpListsControllerInsertNewHelpList(
                        HelpListCreateDto()
                            .addHelpRequestsIdsItem(requestId)
                    )
                } else {
                    api.helpListsControllerAddHelpRequestToList(
                        shoppingList.id,
                        requestId
                    )
                }
            }
            .subscribeOn(io())
            .doOnError {
                Log.e("Error", it.message.toString())
            }
            .blockingSubscribe()
    }

}