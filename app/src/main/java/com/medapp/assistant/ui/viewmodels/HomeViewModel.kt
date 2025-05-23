package com.medapp.assistant.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medapp.assistant.data.repository.MedicineRepository
import com.medapp.assistant.data.repository.FirstAidGuidesRepository
import com.medapp.assistant.data.repository.QuizRepository
import com.medapp.assistant.data.local.JsonStorage
import com.medapp.assistant.data.model.InventoryItem
import com.medapp.assistant.data.model.ChatMessage
import com.medapp.assistant.data.events.MedicineEvents
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.medapp.assistant.data.local.entities.MedicineEntity

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val medicineRepository: MedicineRepository,
    private val firstAidGuidesRepository: FirstAidGuidesRepository,
    private val quizRepository: QuizRepository,
    private val medicineEvents: MedicineEvents
) : AndroidViewModel(application) {

    private val _medicineCount = MutableStateFlow(0)
    val medicineCount: StateFlow<Int> = _medicineCount.asStateFlow()

    private val _firstAidCount = MutableStateFlow(0)
    val firstAidCount: StateFlow<Int> = _firstAidCount.asStateFlow()

    private val _quizCount = MutableStateFlow(0)
    val quizCount: StateFlow<Int> = _quizCount.asStateFlow()

    private val _trackingCount = MutableStateFlow(0)
    val trackingCount: StateFlow<Int> = _trackingCount.asStateFlow()

    private val _expiringMedicines = MutableStateFlow<List<MedicineEntity>>(emptyList())
    val expiringMedicines: StateFlow<List<MedicineEntity>> = _expiringMedicines.asStateFlow()

    private val _personalMedicines = MutableStateFlow<List<MedicineEntity>>(emptyList())
    val personalMedicines: StateFlow<List<MedicineEntity>> = _personalMedicines.asStateFlow()

    private val _inventory = MutableStateFlow<List<InventoryItem>>(emptyList())
    val inventory: StateFlow<List<InventoryItem>> = _inventory.asStateFlow()
    private val inventoryFile = "inventory.json"

    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory.asStateFlow()
    private val chatFile = "chat_history.json"

    init {
        loadCounts()
        loadExpiringMedicines()
        loadPersonalMedicines()
        loadInventory()
        loadChatHistory()
        
        // Подписываемся на события обновления списка личных медикаментов
        viewModelScope.launch {
            medicineEvents.refreshPersonalMedicines.collect {
                refreshPersonalMedicines()
            }
        }
    }

    private fun loadCounts() {
        viewModelScope.launch {
            try {
                medicineRepository.getAllMedicinesFlow().collect { medicines ->
                    _medicineCount.value = medicines.size
                }
                
                firstAidGuidesRepository.getAllGuides().let { guides ->
                    _firstAidCount.value = guides.size
                }
                
                quizRepository.getAllQuizzes().let { quizzes ->
                    _quizCount.value = quizzes.size
                }
                
                medicineRepository.getPersonalMedicinesFlow().collect { (medicines, _) ->
                    _trackingCount.value = medicines.size
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading counts", e)
            }
        }
    }

    private fun loadExpiringMedicines() {
        viewModelScope.launch {
            try {
                val medicines = medicineRepository.getExpiringMedicines()
                _expiringMedicines.value = medicines
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun loadPersonalMedicines() {
        viewModelScope.launch {
            try {
                val medicines = medicineRepository.getPersonalMedicines()
                _personalMedicines.value = medicines
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun loadInventory() {
        _inventory.value = JsonStorage.loadList(getApplication(), inventoryFile)
    }

    fun addInventoryItem(item: InventoryItem) {
        val list = _inventory.value.toMutableList()
        val idx = list.indexOfFirst { it.id == item.id }
        if (idx >= 0) {
            list[idx] = item
        } else {
            // При добавлении нового элемента, по умолчанию он добавляется в "С собой"
            list.add(item.copy(atHome = false))
        }
        _inventory.value = list
        JsonStorage.saveList(getApplication(), inventoryFile, list)
    }

    fun deleteInventoryItem(item: InventoryItem) {
        val list = _inventory.value.toMutableList().apply { removeAll { it.id == item.id } }
        _inventory.value = list
        JsonStorage.saveList(getApplication(), inventoryFile, list)
    }

    fun refresh() {
        loadCounts()
    }

    fun loadChatHistory() {
        val loaded = JsonStorage.loadList<ChatMessage>(getApplication(), chatFile)
        Log.d("ChatHistory", "Загружено: $loaded")
        _chatHistory.value = loaded
    }

    fun addChatMessage(msg: ChatMessage) {
        val list = _chatHistory.value.toMutableList().apply { add(msg) }
        _chatHistory.value = list
        JsonStorage.saveList(getApplication(), chatFile, list)
        Log.d("ChatHistory", "Сохранено: $list")
    }

    fun refreshPersonalMedicines() {
        viewModelScope.launch {
            try {
                loadPersonalMedicines()
                loadCounts() // Refresh counts as well
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error refreshing personal medicines", e)
            }
        }
    }
} 