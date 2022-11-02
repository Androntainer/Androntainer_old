package io.androntainer.application.google

import android.os.Bundle
import android.os.PersistableBundle
import io.androntainer.application.google.FakeSignature

class Google : FakeSignature() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        main(1)
    }
}