package com.dlucci.consultantcamerax

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import coil.api.load
import java.io.File
import kotlinx.android.synthetic.main.fragment_details.image
import kotlinx.android.synthetic.main.fragment_details.size
import kotlinx.android.synthetic.main.fragment_details.trash

class DetailsFragment : Fragment() {

    var path: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context?.inflate(container as ViewGroup, R.layout.fragment_details)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        path = arguments?.getString("path")

        var file = File(path)

        image.load(file)

        var fileSize = file.length().toKb()

        size.text = fileSize

        trash.setOnClickListener {
            file.delete()
            Navigation.findNavController(trash).navigateUp()
        }
    }
}