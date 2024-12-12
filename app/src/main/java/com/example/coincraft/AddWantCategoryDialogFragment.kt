package com.example.coincraft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment

class AddWantCategoryDialogFragment: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_add_want_category_dialog, container, false)
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