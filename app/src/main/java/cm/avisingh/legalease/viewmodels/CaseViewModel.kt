package cm.avisingh.legalease.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cm.avisingh.legalease.services.FirestoreService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaseViewModel : ViewModel() {

    private val firestoreService = FirestoreService()

    private val _casesState = MutableStateFlow<CasesState>(CasesState.Loading)
    val casesState: StateFlow<CasesState> = _casesState

    fun loadCases(userId: String, userRole: String) {
        viewModelScope.launch {
            try {
                val cases = firestoreService.getCasesForUser(userId, userRole)
                _casesState.value = CasesState.Success(cases)
            } catch (e: Exception) {
                _casesState.value = CasesState.Error(e.message ?: "Failed to load cases")
            }
        }
    }

    fun createCase(caseData: HashMap<String, Any>) {
        viewModelScope.launch {
            try {
                val caseId = firestoreService.createCase(caseData)
                // Handle success
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    sealed class CasesState {
        object Loading : CasesState()
        data class Success(val cases: List<Map<String, Any>>) : CasesState()
        data class Error(val message: String) : CasesState()
    }
}