package com.medapp.assistant.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medapp.assistant.data.local.entities.InventoryItemEntity
import com.medapp.assistant.data.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InventoryUiState(
    val homeItems: List<InventoryItemEntity> = emptyList(),
    val portableItems: List<InventoryItemEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        loadInventoryItems()
    }

    private fun loadInventoryItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                combine(
                    inventoryRepository.getHomeInventoryItems(),
                    inventoryRepository.getPortableInventoryItems()
                ) { homeItems, portableItems ->
                    _uiState.update { 
                        it.copy(
                            homeItems = homeItems,
                            portableItems = portableItems,
                            isLoading = false,
                            error = null
                        )
                    }
                }.collect()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    fun addInventoryItem(item: InventoryItemEntity) {
        viewModelScope.launch {
            try {
                inventoryRepository.addInventoryItem(item)
                loadInventoryItems() // Перезагружаем список после добавления
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to add item")
                }
            }
        }
    }

    fun updateInventoryItem(item: InventoryItemEntity) {
        viewModelScope.launch {
            try {
                inventoryRepository.updateInventoryItem(item)
                loadInventoryItems() // Перезагружаем список после обновления
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to update item")
                }
            }
        }
    }

    fun deleteInventoryItem(id: Long) {
        viewModelScope.launch {
            try {
                inventoryRepository.deleteInventoryItem(id)
                loadInventoryItems() // Перезагружаем список после удаления
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to delete item")
                }
            }
        }
    }

    fun toggleItemLocation(item: InventoryItemEntity) {
        viewModelScope.launch {
            try {
                val updatedItem = item.copy(atHome = !item.atHome)
                inventoryRepository.updateInventoryItem(updatedItem)
                loadInventoryItems() // Перезагружаем список после обновления
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to update item location")
                }
            }
        }
    }
} 