package cm.avisingh.legalease.data.cache

import android.content.Context
import androidx.room.*
import cm.avisingh.legalease.data.model.FirebaseDocument
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

@Entity(tableName = "documents")
data class CachedDocument(
    @PrimaryKey val id: String,
    val name: String,
    val url: String,
    val category: String,
    val description: String?,
    val uploadedBy: String,
    val createdAt: Long,
    val updatedAt: Long,
    val size: Long,
    val mimeType: String?,
    val version: Int,
    val tags: String, // JSON string
    val sharedWith: String, // JSON string
    val isPublic: Boolean,
    val metadata: String, // JSON string
    val localPath: String?, // Path to cached file
    val lastSyncTime: Long, // Last time synced with server
    val syncStatus: SyncStatus,
    val pendingChanges: String? // JSON string of pending changes
)

enum class SyncStatus {
    SYNCED,
    PENDING_UPLOAD,
    PENDING_UPDATE,
    PENDING_DELETE,
    CONFLICT
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(Date(it)) }
    }

    @TypeConverter
    fun toTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.seconds?.times(1000)
    }
}

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents WHERE category = :category")
    fun getDocumentsByCategory(category: String): Flow<List<CachedDocument>>

    @Query("SELECT * FROM documents WHERE uploadedBy = :userId")
    fun getDocumentsByUser(userId: String): Flow<List<CachedDocument>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: CachedDocument)

    @Update
    suspend fun updateDocument(document: CachedDocument)

    @Delete
    suspend fun deleteDocument(document: CachedDocument)

    @Query("SELECT * FROM documents WHERE syncStatus != 'SYNCED'")
    fun getPendingChanges(): Flow<List<CachedDocument>>

    @Query("SELECT * FROM documents WHERE id = :id")
    suspend fun getDocument(id: String): CachedDocument?

    @Query("DELETE FROM documents WHERE id = :id")
    suspend fun deleteDocumentById(id: String)
}

@Database(entities = [CachedDocument::class], version = 1)
@TypeConverters(Converters::class)
abstract class DocumentDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao

    companion object {
        @Volatile
        private var INSTANCE: DocumentDatabase? = null

        fun getDatabase(context: Context): DocumentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DocumentDatabase::class.java,
                    "document_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class DocumentCache(context: Context) {
    private val dao = DocumentDatabase.getDatabase(context).documentDao()
    private val gson = Gson()

    fun observeDocuments(userId: String): Flow<List<FirebaseDocument>> {
        return dao.getDocumentsByUser(userId).map { cachedDocs ->
            cachedDocs.map { it.toFirebaseDocument() }
        }
    }

    fun observeDocumentsByCategory(category: String): Flow<List<FirebaseDocument>> {
        return dao.getDocumentsByCategory(category).map { cachedDocs ->
            cachedDocs.map { it.toFirebaseDocument() }
        }
    }

    suspend fun cacheDocument(document: FirebaseDocument, localPath: String? = null) {
        dao.insertDocument(document.toCachedDocument(localPath))
    }

    suspend fun updateDocument(document: FirebaseDocument, syncStatus: SyncStatus = SyncStatus.PENDING_UPDATE) {
        val cached = dao.getDocument(document.id)
        if (cached != null) {
            dao.updateDocument(cached.copy(
                name = document.name,
                category = document.category,
                description = document.description,
                updatedAt = document.updatedAt.seconds * 1000,
                syncStatus = syncStatus,
                pendingChanges = if (syncStatus != SyncStatus.SYNCED) {
                    gson.toJson(document)
                } else null
            ))
        }
    }

    suspend fun deleteDocument(document: FirebaseDocument) {
        dao.deleteDocumentById(document.id)
    }

    fun observePendingChanges(): Flow<List<FirebaseDocument>> {
        return dao.getPendingChanges().map { cachedDocs ->
            cachedDocs.map { it.toFirebaseDocument() }
        }
    }

    fun observePendingCachedDocuments(): Flow<List<CachedDocument>> {
        return dao.getPendingChanges()
    }

    private fun CachedDocument.toFirebaseDocument(): FirebaseDocument {
        return FirebaseDocument(
            id = id,
            name = name,
            url = url,
            category = category,
            description = description,
            uploadedBy = uploadedBy,
            createdAt = Timestamp(Date(createdAt)),
            updatedAt = Timestamp(Date(updatedAt)),
            size = size,
            mimeType = mimeType,
            version = version,
            tags = gson.fromJson(tags, object : TypeToken<List<String>>() {}.type),
            sharedWith = gson.fromJson(sharedWith, object : TypeToken<List<String>>() {}.type),
            isPublic = isPublic,
            metadata = gson.fromJson(metadata, object : TypeToken<Map<String, String>>() {}.type)
        )
    }

    private fun FirebaseDocument.toCachedDocument(localPath: String? = null): CachedDocument {
        return CachedDocument(
            id = id,
            name = name,
            url = url,
            category = category,
            description = description,
            uploadedBy = uploadedBy,
            createdAt = createdAt.seconds * 1000,
            updatedAt = updatedAt.seconds * 1000,
            size = size,
            mimeType = mimeType,
            version = version,
            tags = gson.toJson(tags),
            sharedWith = gson.toJson(sharedWith),
            isPublic = isPublic,
            metadata = gson.toJson(metadata),
            localPath = localPath,
            lastSyncTime = System.currentTimeMillis(),
            syncStatus = SyncStatus.SYNCED,
            pendingChanges = null
        )
    }
}