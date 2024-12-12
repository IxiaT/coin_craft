package com.example.coincraft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment

class AddNeedCategoryDialogFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_add_need_category_dialog, container, false)
        rootView.findViewById<ImageButton>(R.id.closebtn).setOnClickListener {
            dismiss()
        }
        /*
        rootView.findViewById<ImageButton>(R.id.add_button).setOnClickListener {
            //unya na
            dismiss()
        }
        */
        return rootView
    }

}