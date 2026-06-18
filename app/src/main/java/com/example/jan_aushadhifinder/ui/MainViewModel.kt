package com.example.jan_aushadhifinder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jan_aushadhifinder.data.Medicine
import com.example.jan_aushadhifinder.data.Reminder
import com.example.jan_aushadhifinder.repository.MedicineRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MainViewModel(private val repository: MedicineRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Medicine>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    val reminders: StateFlow<List<Reminder>> = repository.getAllReminders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Hardcoded fallback data for immediate visibility
    private val fallbackMedicines = listOf(
        Medicine(brandName = "Dolo 650", genericName = "Paracetamol", brandedPrice = 30.0, genericPrice = 10.0, category = "Analgesics", dosageForm = "Tablet", saltComposition = "Paracetamol 650mg"),
        Medicine(brandName = "Augmentin 625", genericName = "Amoxicillin + Clavulanic Acid", brandedPrice = 200.0, genericPrice = 50.0, category = "Antibiotics", dosageForm = "Tablet", saltComposition = "Amoxicillin 500mg + Clavulanic Acid 125mg"),
        Medicine(brandName = "Telma 40", genericName = "Telmisartan", brandedPrice = 150.0, genericPrice = 40.0, category = "Antihypertensives", dosageForm = "Tablet", saltComposition = "Telmisartan 40mg"),
        Medicine(brandName = "Pan 40", genericName = "Pantoprazole", brandedPrice = 120.0, genericPrice = 30.0, category = "Gastrointestinal", dosageForm = "Tablet", saltComposition = "Pantoprazole 40mg"),
        Medicine(brandName = "Voveran SR 100", genericName = "Diclofenac", brandedPrice = 80.0, genericPrice = 20.0, category = "Analgesics", dosageForm = "Tablet", saltComposition = "Diclofenac 100mg"),
        Medicine(brandName = "Calpol 500", genericName = "Paracetamol", brandedPrice = 15.0, genericPrice = 5.0, category = "Analgesics", dosageForm = "Tablet", saltComposition = "Paracetamol 500mg")
    )

    init {
        // Automatically load and filter medicines based on query
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .flatMapLatest { query ->
                    repository.searchMedicines(query)
                }
                .collect { results ->
                    // If DB returns empty, show fallback data to ensure visibility
                    if (results.isEmpty() && _searchQuery.value.isEmpty()) {
                        _searchResults.value = fallbackMedicines
                    } else if (results.isEmpty() && _searchQuery.value.isNotEmpty()) {
                        // If user is searching and no match in DB, also check fallback
                        val filteredFallback = fallbackMedicines.filter { 
                            it.brandName.contains(_searchQuery.value, ignoreCase = true) ||
                            it.genericName.contains(_searchQuery.value, ignoreCase = true)
                        }
                        _searchResults.value = filteredFallback
                    } else {
                        _searchResults.value = results
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun addReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.insertReminder(reminder)
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
        }
    }
}
