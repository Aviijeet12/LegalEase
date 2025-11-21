package com.example.lawclientauth

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.adapters.ItemAdapter
import com.example.lawclientauth.models.ItemModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    private val PAGE_SIZE = 20L
    private var lastVisible: DocumentSnapshot? = null
    private var isLoading = false
    private var isLastPage = false

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var emptyView: LinearLayout
    private lateinit var etSearch: EditText

    private lateinit var greeting: TextView
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid ?: ""

        greeting = findViewById(R.id.tvGreeting)
        greeting.text = "Hi, ${auth.currentUser?.email?.substringBefore("@")} 👋"

        emptyView = findViewById(R.id.emptyView)
        etSearch = findViewById(R.id.etSearch)
        recycler = findViewById(R.id.recyclerHome)

        adapter = ItemAdapter(
            mutableListOf(),
            onItemClick = { item -> showEditItemDialog(item) },
            onItemLongClick = { item, pos -> showItemPopup(item, pos) }
        )

        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter

        loadFirstPage()

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)

                if (dy <= 0) return
                val total = layoutManager.itemCount
                val visible = layoutManager.childCount
                val first = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage && visible + first >= total - 3) {
                    loadNextPage()
                }
            }
        })

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {
                adapter.filter(s.toString())
                emptyView.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
            }
        })

        val swipeHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) =
                    false

                override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
                    val pos = vh.adapterPosition
                    val item = adapter.getItem(pos)
                    showDeleteConfirmation(item)
                }

                override fun onChildDraw(
                    c: Canvas, rv: RecyclerView, vh: RecyclerView.ViewHolder,
                    dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(c, rv, vh, dX, dY, actionState, isCurrentlyActive)
                }
            })

        swipeHelper.attachToRecyclerView(recycler)

        findViewById<FloatingActionButton>(R.id.fabAddItem).setOnClickListener {
            showAddItemDialog()
        }

        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {}
        findViewById<LinearLayout>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<LinearLayout>(R.id.navSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }

    // -------------------------------------------------------------------------
    // Pagination
    // -------------------------------------------------------------------------
    private fun loadFirstPage() {
        isLoading = true
        isLastPage = false
        lastVisible = null

        adapter.clearAll()

        db.collection("items")
            .whereEqualTo("ownerId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(PAGE_SIZE)
            .get()
            .addOnSuccessListener { snap ->
                val items = snap.toObjects(ItemModel::class.java)
                adapter.setList(items)

                if (snap.documents.isNotEmpty()) {
                    lastVisible = snap.documents.last()
                }

                emptyView.visibility =
                    if (adapter.itemCount == 0) View.VISIBLE else View.GONE

                isLoading = false
                isLastPage = snap.size() < PAGE_SIZE
            }
            .addOnFailureListener {
                isLoading = false
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadNextPage() {
        if (isLoading || isLastPage) return

        val last = lastVisible ?: return

        isLoading = true

        db.collection("items")
            .whereEqualTo("ownerId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .startAfter(last)
            .limit(PAGE_SIZE)
            .get()
            .addOnSuccessListener { snap ->
                val items = snap.toObjects(ItemModel::class.java)
                adapter.addMore(items)

                if (snap.documents.isNotEmpty()) {
                    lastVisible = snap.documents.last()
                }

                isLoading = false
                if (snap.size() < PAGE_SIZE) isLastPage = true
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    // -------------------------------------------------------------------------
    // Add Item
    // -------------------------------------------------------------------------
    private fun showAddItemDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_add_item, null)
        val etTitle = view.findViewById<EditText>(R.id.etItemTitle)
        val etDesc = view.findViewById<EditText>(R.id.etItemDesc)

        AlertDialog.Builder(this)
            .setTitle("Add New Item")
            .setView(view)
            .setPositiveButton("Add") { _, _ ->
                val t = etTitle.text.toString()
                val d = etDesc.text.toString()

                if (t.isEmpty()) {
                    Toast.makeText(this, "Title required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val map = mapOf(
                    "title" to t,
                    "description" to d,
                    "createdAt" to System.currentTimeMillis(),
                    "ownerId" to userId
                )

                db.collection("items")
                    .add(map)
                    .addOnSuccessListener { loadFirstPage() }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // -------------------------------------------------------------------------
    // Edit Item
    // -------------------------------------------------------------------------
    private fun showEditItemDialog(item: ItemModel) {
        val view = layoutInflater.inflate(R.layout.dialog_add_item, null)
        val etTitle = view.findViewById<EditText>(R.id.etItemTitle)
        val etDesc = view.findViewById<EditText>(R.id.etItemDesc)

        etTitle.setText(item.title)
        etDesc.setText(item.description)

        AlertDialog.Builder(this)
            .setTitle("Edit Item")
            .setView(view)
            .setPositiveButton("Update") { _, _ ->
                val newT = etTitle.text.toString()
                val newD = etDesc.text.toString()

                if (newT.isEmpty()) {
                    Toast.makeText(this, "Title required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                db.collection("items")
                    .whereEqualTo("createdAt", item.createdAt)
                    .whereEqualTo("ownerId", userId)
                    .get()
                    .addOnSuccessListener { docs ->
                        for (d in docs) {
                            d.reference.update(
                                mapOf(
                                    "title" to newT,
                                    "description" to newD
                                )
                            )
                        }
                        loadFirstPage()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // -------------------------------------------------------------------------
    // Long Press Popup
    // -------------------------------------------------------------------------
    private fun showItemPopup(item: ItemModel, position: Int) {
        val vh = recycler.findViewHolderForAdapterPosition(position) ?: return
        val popup = androidx.appcompat.widget.PopupMenu(this, vh.itemView)
        popup.menu.add("Edit")
        popup.menu.add("Delete")

        popup.setOnMenuItemClickListener { mi ->
            when (mi.title) {
                "Edit" -> showEditItemDialog(item)
                "Delete" -> showDeleteConfirmation(item)
            }
            true
        }

        popup.show()
    }

    // -------------------------------------------------------------------------
    // Delete Confirmation
    // -------------------------------------------------------------------------
    private fun showDeleteConfirmation(item: ItemModel) {
        AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure?")
            .setPositiveButton("Delete") { _, _ ->
                db.collection("items")
                    .whereEqualTo("createdAt", item.createdAt)
                    .whereEqualTo("ownerId", userId)
                    .get()
                    .addOnSuccessListener { docs ->
                        for (d in docs) d.reference.delete()
                        loadFirstPage()
                    }
            }
            .setNegativeButton("Cancel") { _, _ ->
                loadFirstPage()
            }
            .show()
    }

    // -------------------------------------------------------------------------
    // Export PDF
    // -------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.export_pdf) {
            exportToPdf()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exportToPdf() {
        val items = adapter.getAllLoadedItems()
        if (items.isEmpty()) {
            Toast.makeText(this, "No items to export", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val pageWidth = 595
            val pageHeight = 842
            val pdf = PdfDocument()
            val paint = Paint()
            val titlePaint = Paint().apply {
                textSize = 16f
                isFakeBoldText = true
            }

            var y = 40
            var pageNum = 1
            var page = pdf.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create())
            var canvas = page.canvas

            items.forEachIndexed { index, it ->
                if (y > pageHeight - 80) {
                    pdf.finishPage(page)
                    pageNum++
                    page = pdf.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create())
                    canvas = page.canvas
                    y = 40
                }

                canvas.drawText("${index + 1}. ${it.title}", 20f, y.toFloat(), titlePaint)
                y += 20
                paint.textSize = 12f
                canvas.drawText(it.description ?: "", 24f, y.toFloat(), paint)
                y += 28
            }

            pdf.finishPage(page)

            val fileName =
                "items_export_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.pdf"
            val file = File(cacheDir, fileName)
            pdf.writeTo(FileOutputStream(file))
            pdf.close()

            val uri = FileProvider.getUriForFile(
                this,
                "com.example.lawclientauth.fileprovider",
                file
            )

            val share = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(share, "Share PDF"))

        } catch (e: Exception) {
            Toast.makeText(this, "PDF export failed", Toast.LENGTH_LONG).show()
        }
    }
}
