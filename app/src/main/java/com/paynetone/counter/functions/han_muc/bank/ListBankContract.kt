package com.paynetone.counter.functions.han_muc.bank

import com.core.base.viper.interfaces.IInteractor
import com.core.base.viper.interfaces.IPresenter
import com.core.base.viper.interfaces.PresentView
import com.paynetone.counter.model.SimpleResult
import com.paynetone.counter.network.CommonCallback

class ListBankContract {

    interface Interactor : IInteractor<Presenter> {

    }

    interface View : PresentView<Presenter> {

    }

    interface Presenter : IPresenter<View, Interactor> {

    }
}