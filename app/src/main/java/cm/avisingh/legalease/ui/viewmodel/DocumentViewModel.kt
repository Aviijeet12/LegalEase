package cm.avisingh.legalease.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cm.avisingh.legalease.data.model.FirebaseDocument
import cm.avisingh.legalease.data.repository.DocumentRepository
import cm.avisingh.legalease.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class DocumentViewModel(application: Application) : AndroidViewModel(application) {
    private val documentRepository = DocumentRepository(application)
    private val userRepository = UserRepository()

    private val _documents = MutableStateFlow<List<FirebaseDocument>>(emptyList())
    val documents: StateFlow<List<FirebaseDocument>> = _documents

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _uploadProgress = MutableLiveData<Int>()
    val uploadProgress: LiveData<Int> = _uploadProgress

    fun loadDocuments(category: String? = null) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val userId = userRepository.currentUser?.uid ?: return@launch
                val docs = documentRepository.getDocuments(userId, category)
                _documents.value = docs
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun uploadDocument(
        fileUri: Uri,
        fileName: String,
        category: String,
        description: String? = null
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _uploadProgress.value = 0
                val userId = userRepository.currentUser?.uid ?: return@launch

                val document = documentRepository.uploadDocument(
                    userId = userId,
                    fileUri = fileUri,
                    fileName = fileName,
                    category = category,
                    description = description
                )

                // Update documents list
                val currentDocs = _documents.value.toMutableList()
                currentDocs.add(0, document)
                _documents.value = currentDocs
                _error.value = null
                _uploadProgress.value = 100
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun downloadDocument(document: FirebaseDocument): LiveData<File?> {
        val result = MutableLiveData<File?>()
        viewModelScope.launch {
            try {
                _loading.value = true
                val file = documentRepository.downloadDocument(document)
                result.value = file
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                result.value = null
            } finally {
                _loading.value = false
            }
        }
        return result
    }

    fun updateDocument(document: FirebaseDocument) {
        viewModelScope.launch {
            try {
                _loading.value = true
                documentRepository.deleteDocument(document)
                
                // Update documents list
                val currentDocs = _documents.value.toMutableList()
                currentDocs.remove(document)
                _documents.value = currentDocs
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun moveDocument(document: FirebaseDocument, newCategory: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                documentRepository.moveDocument(document, newCategory)
                
                // Reload documents
                loadDocuments(document.category)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun searchDocuments(query: String, category: String? = null) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val userId = userRepository.currentUser?.uid ?: return@launch
                val results = documentRepository.searchDocuments(userId, query, category)
                _documents.value = results
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun getRecentDocuments(limit: Int = 10) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val userId = userRepository.currentUser?.uid ?: return@launch
                val docs = documentRepository.getRecentDocuments(userId, limit)
                _documents.value = docs
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}