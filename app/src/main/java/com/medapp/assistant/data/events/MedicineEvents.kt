package com.medapp.assistant.data.events

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicineEvents @Inject constructor() {
    private val _refreshPersonalMedicines = MutableSharedFlow<Unit>()
    val refreshPersonalMedicines: SharedFlow<Unit> = _refreshPersonalMedicines.asSharedFlow()

    suspend fun triggerRefreshPersonalMedicines() {
        _refreshPersonalMedicines.emit(Unit)
    }
} 