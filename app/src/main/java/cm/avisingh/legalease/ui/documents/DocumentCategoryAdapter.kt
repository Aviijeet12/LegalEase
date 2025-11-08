package cm.avisingh.legalease.ui.documents

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DocumentCategoryAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    private var currentFragment: DocumentCategoryFragment? = null

    override fun getItemCount() = 6

    override fun createFragment(position: Int): Fragment {
        return DocumentCategoryFragment.newInstance(
            when (position) {
                0 -> DocumentCategory.ALL
                1 -> DocumentCategory.PROPERTY
                2 -> DocumentCategory.LEGAL
                3 -> DocumentCategory.FINANCIAL
                4 -> DocumentCategory.PERSONAL
                else -> DocumentCategory.OTHER
            }
        ).also { fragment ->
            if (position == 0) {
                currentFragment = fragment
            }
        }
    }

    fun getCurrentFragment(): DocumentCategoryFragment? = currentFragment
}

enum class DocumentCategory {
    ALL,
    PROPERTY,
    LEGAL,
    FINANCIAL,
    PERSONAL,
    OTHER
}

enum class SortType {
    NAME,
    DATE,
    SIZE
}