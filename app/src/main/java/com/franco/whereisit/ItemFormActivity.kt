package com.franco.whereisit

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.franco.whereisit.data.memory.MemoryDataRepository
import com.franco.whereisit.domain.model.CampusZone
import com.franco.whereisit.domain.model.Coordinates
import com.franco.whereisit.domain.model.ItemCategory
import com.franco.whereisit.domain.model.ItemStatus
import com.franco.whereisit.util.EXTRA_ITEM_ID
import com.franco.whereisit.util.Util
import com.franco.whereisit.viewmodel.ItemFormViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate

class ItemFormActivity : AppCompatActivity() {

    private lateinit var viewModel: ItemFormViewModel

    private lateinit var toolbar: MaterialToolbar
    private lateinit var etSearch: TextInputEditText
    private lateinit var toggleType: MaterialButtonToggleGroup
    private lateinit var etTitle: TextInputEditText
    private lateinit var actCategory: MaterialAutoCompleteTextView
    private lateinit var actZone: MaterialAutoCompleteTextView
    private lateinit var etDescription: TextInputEditText
    private lateinit var etLocation: TextInputEditText
    private lateinit var etDate: TextInputEditText
    private lateinit var btnSave: MaterialButton
    private lateinit var btnDelete: MaterialButton

    private var selectedCategory = ItemCategory.OTHER
    private var selectedZone = CampusZone.OTHER
    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ItemFormViewModel(MemoryDataRepository())

        toolbar = findViewById(R.id.toolbar_form)
        etSearch = findViewById(R.id.etSearch_form)
        toggleType = findViewById(R.id.toggleType_form)
        etTitle = findViewById(R.id.etTitle_form)
        actCategory = findViewById(R.id.actCategory_form)
        actZone = findViewById(R.id.actZone_form)
        etDescription = findViewById(R.id.etDescription_form)
        etLocation = findViewById(R.id.etLocation_form)
        etDate = findViewById(R.id.etDate_form)
        btnSave = findViewById(R.id.btnSave_form)
        btnDelete = findViewById(R.id.btnDelete_form)
        val btnSearch = findViewById<ImageButton>(R.id.btnSearch_form)

        setupDropdowns()

        toolbar.setNavigationOnClickListener { finish() }
        btnSearch.setOnClickListener { searchItem(etSearch.text.toString()) }
        etDate.setOnClickListener { showDatePickerDialog() }
        etLocation.setOnClickListener { pickLocation() }
        btnSave.setOnClickListener { onSaveClicked() }
        btnDelete.setOnClickListener { confirmDelete() }

        cleanScreen()

        val itemId = intent.getStringExtra(EXTRA_ITEM_ID)
        if (!itemId.isNullOrBlank()) {
            loadItem(itemId)
        }
    }

    private fun categoryLabel(category: ItemCategory): String {
        return getString(when (category) {
                ItemCategory.BACKPACK -> R.string.category_backpack
                ItemCategory.KEYS -> R.string.category_keys
                ItemCategory.PHONE -> R.string.category_phone
                ItemCategory.DOCUMENTS -> R.string.category_documents
                ItemCategory.WALLET -> R.string.category_wallet
                ItemCategory.CLOTHES -> R.string.category_clothing
                ItemCategory.ELECTRONICS -> R.string.category_electronics
                ItemCategory.OTHER -> R.string.category_other
                ItemCategory.BOTTLES -> R.string.category_bottles
        }
        )
    }

    private fun zoneLabel(zone: CampusZone): String {
        return getString(when (zone) {
            CampusZone.LIBRARY -> R.string.zone_library
            CampusZone.CAFETERIA -> R.string.zone_cafeteria
            CampusZone.BUILDING_A -> R.string.zone_building_a
            CampusZone.BUILDING_B -> R.string.zone_building_b
            CampusZone.GYM -> R.string.zone_gym
            CampusZone.PARKING -> R.string.zone_parking
            CampusZone.OTHER -> R.string.zone_other
            }
        )
    }

    private fun setupDropdowns() {
        val categories = ItemCategory.entries
        actCategory.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_list_item_1,
                categories.map { categoryLabel(it) })
        )
        actCategory.setOnItemClickListener { _, _, position, _ ->
            selectedCategory = categories[position]
        }

        val zones = CampusZone.entries
        actZone.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_list_item_1,
                zones.map { zoneLabel(it) })
        )
        actZone.setOnItemClickListener { _, _, position, _ ->
            selectedZone = zones[position]
        }
    }

    private fun showDatePickerDialog() {
        val dialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                etDate.setText(Util.formatDate(selectedDate))
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
        dialog.setTitle(R.string.form_date_picker_title)
        dialog.show()
    }

    // Placeholder until the map task: uses the default campus coordinates
    private fun showLocation(coordinates: Coordinates) {
        etLocation.setText(
            getString(
                R.string.form_location_value,
                coordinates.latitude.toString(),
                coordinates.longitude.toString()
            )
        )
    }

    private fun pickLocation() {
        val coordinates = Coordinates(Util.DEFAULT_LATITUDE, Util.DEFAULT_LONGITUDE)
        viewModel.coordinates = coordinates
        showLocation(coordinates)
    }

    // ---------- Search / Read ----------

    private fun searchItem(query: String) {
        if (query.isBlank()) return
        val item = viewModel.searchByTitle(query)
        if (item == null) {
            Toast.makeText(this, getString(R.string.detail_not_found), Toast.LENGTH_LONG).show()
        } else {
            loadItem(item.id)
        }
    }

    private fun loadItem(id: String) {
        if (!viewModel.loadItem(id)) {
            Toast.makeText(this, getString(R.string.detail_not_found), Toast.LENGTH_LONG).show()
            return
        }
        val item = viewModel.editingItem ?: return

        etTitle.setText(item.title)
        etDescription.setText(item.description)

        selectedCategory = item.category
        actCategory.setText(categoryLabel(item.category), false)
        selectedZone = item.zone
        actZone.setText(zoneLabel(item.zone), false)

        selectedDate = item.eventDate
        etDate.setText(Util.formatDate(selectedDate))
        showLocation(item.coordinates)

        toggleType.check(
            if (item.status == ItemStatus.FOUND) R.id.btnTypeFound_form
            else R.id.btnTypeLost_form
        )
        setEditMode(true)
    }

    // ---------- Create / Update ----------

    private fun currentStatus(): ItemStatus {
        val existing = viewModel.editingItem
        if (existing != null && existing.status == ItemStatus.DELIVERED) {
            return ItemStatus.DELIVERED
        }
        return if (toggleType.checkedButtonId == R.id.btnTypeFound_form) ItemStatus.FOUND
        else ItemStatus.LOST
    }

    private fun onSaveClicked() {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()

        val error = viewModel.validate(title, description)
        if (error != null) {
            Toast.makeText(this, getString(error), Toast.LENGTH_LONG).show()
            return
        }

        if (viewModel.editingItem != null) {
            confirmUpdate(title, description)
        } else {
            saveItem(title, description)
        }
    }

    private fun confirmUpdate(title: String, description: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_update_title)
            .setMessage(R.string.dialog_update_message)
            .setPositiveButton(R.string.action_confirm) { _, _ -> saveItem(title, description) }
            .setNegativeButton(R.string.action_cancel, null)
            .show()
    }

    private fun saveItem(title: String, description: String) {
        val isEditing = viewModel.editingItem != null
        val saved = viewModel.saveItem(
            title, description, selectedCategory, currentStatus(), selectedZone, selectedDate
        )
        if (!saved) {
            // There is no current user yet
            Toast.makeText(this, getString(R.string.error_name_required), Toast.LENGTH_LONG).show()
            return
        }
        val message = if (isEditing) R.string.message_item_updated else R.string.message_item_saved
        Toast.makeText(this, getString(message), Toast.LENGTH_LONG).show()
        cleanScreen()
    }

    // ---------- Delete ----------

    private fun confirmDelete() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setMessage(R.string.dialog_delete_message)
            .setPositiveButton(R.string.action_delete) { _, _ ->
                viewModel.deleteItem()
                Toast.makeText(this, getString(R.string.message_item_deleted), Toast.LENGTH_LONG).show()
                cleanScreen()
            }
            .setNegativeButton(R.string.action_cancel, null)
            .show()
    }

    // ---------- Screen state ----------

    private fun setEditMode(isEditMode: Boolean) {
        toolbar.title = getString(if (isEditMode) R.string.form_title_edit else R.string.form_title_new)
        btnSave.text = getString(if (isEditMode) R.string.action_save else R.string.action_publish)
        btnDelete.visibility = if (isEditMode) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun cleanScreen() {
        viewModel.reset()
        selectedCategory = ItemCategory.OTHER
        selectedZone = CampusZone.OTHER
        selectedDate = LocalDate.now()

        etSearch.setText("")
        etTitle.setText("")
        etDescription.setText("")
        etLocation.setText("")
        actCategory.setText(categoryLabel(selectedCategory), false)
        actZone.setText(zoneLabel(selectedZone), false)
        etDate.setText(Util.formatDate(selectedDate))
        toggleType.check(R.id.btnTypeLost_form)
        setEditMode(false)
    }
}