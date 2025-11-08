package cm.avisingh.legalease.ui.documents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cm.avisingh.legalease.R

class PeopleShareFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO: Implement people sharing UI
        return inflater.inflate(R.layout.activity_main, container, false)
    }
    
    companion object {
        fun newInstance(): PeopleShareFragment {
            return PeopleShareFragment()
        }
    }
}
