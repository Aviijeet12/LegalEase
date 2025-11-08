package cm.avisingh.legalease.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.DialogAdvancedSearchBinding
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class AdvancedSearchDialog : DialogFragment() {
    private var _binding: DialogAdvancedSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by activityViewModels()
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_LegalEase_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAdvancedSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupDateRangePicker()
        setupCategoryChips()
        setupDocumentTypeChips()
        setupSortingOptions()
        setupSizeFilter()
        setupApplyButton()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setNavigationOnClickListener { dismiss() }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_reset -> {
                        resetFilters()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupDateRangePicker() {
        binding.dateRangeLayout.setOnClickListener {
            showDateRangePicker()
        }
    }

    private fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select date range")
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = Date(selection.first)
            val endDate = Date(selection.second)
            viewModel.setDateRange(startDate, endDate)
            
            binding.dateRangeText.text = getString(
                R.string.date_range_format,
                dateFormat.format(startDate),
                dateFormat.format(endDate)
            )
        }

        dateRangePicker.show(parentFragmentManager, "DATE_RANGE_PICKER")
    }

    private fun setupCategoryChips() {
        DocumentCategory.values().forEach { category ->
            val chip = layoutInflater.inflate(
                R.layout.item_filter_chip,
                binding.categoryChipGroup,
                false
            ) as Chip
            
            chip.apply {
                text = category.displayName
                tag = category
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        viewModel.addCategory(category)
                    } else {
                        viewModel.removeCategory(category)
                    }
                }
            }

            binding.categoryChipGroup.addView(chip)
        }
    }

    private fun setupDocumentTypeChips() {
        DocumentType.values().forEach { type ->
            val chip = layoutInflater.inflate(
                R.layout.item_filter_chip,
                binding.typeChipGroup,
                false
            ) as Chip
            
            chip.apply {
                text = type.displayName
                tag = type
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        viewModel.addDocumentType(type)
                    } else {
                        viewModel.removeDocumentType(type)
                    }
                }
            }

            binding.typeChipGroup.addView(chip)
        }
    }

    private fun setupSortingOptions() {
        binding.sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val sortOption = when (checkedId) {
                R.id.radioRelevance -> SortOption.RELEVANCE
                R.id.radioDateNewest -> SortOption.DATE_DESC
                R.id.radioDateOldest -> SortOption.DATE_ASC
                R.id.radioNameAZ -> SortOption.NAME_ASC
                R.id.radioNameZA -> SortOption.NAME_DESC
                R.id.radioSizeLargest -> SortOption.SIZE_DESC
                R.id.radioSizeSmallest -> SortOption.SIZE_ASC
                else -> SortOption.RELEVANCE
            }
            viewModel.setSortOption(sortOption)
        }
    }

    private fun setupSizeFilter() {
        binding.sizeRangeSlider.addOnChangeListener { _, _, _ ->
            val range = binding.sizeRangeSlider.values
            viewModel.setSizeRange(range[0].toInt(), range[1].toInt())
        }
    }

    private fun setupApplyButton() {
        binding.applyButton.setOnClickListener {
            viewModel.applyAdvancedSearch()
            dismiss()
        }
    }

    private fun resetFilters() {
        viewModel.resetFilters()
        binding.apply {
            categoryChipGroup.clearCheck()
            typeChipGroup.clearCheck()
            sortRadioGroup.check(R.id.radioRelevance)
            dateRangeText.text = getString(R.string.any_date)
            sizeRangeSlider.values = listOf(0f, 100f)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}