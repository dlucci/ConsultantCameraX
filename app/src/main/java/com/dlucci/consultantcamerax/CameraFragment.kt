package com.dlucci.consultantcamerax

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraFragment : Fragment() {

    lateinit var mCamera: CameraManager

    lateinit var listener: FragmentListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return context?.inflate(container as ViewGroup, R.layout.fragment_camera)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preview.setOnClickListener {
            mCamera.stopCamera()
            it.findNavController().navigate(R.id.action_to_gallery)
        }

        mCamera = CameraManager(context)

        if (listener.allPermissionsGranted()) {
            viewFinder.post {
                startCamera()
                mCamera.populatePreview(preview)
            }
        } else {
            requestPermissions(
                listener.getRequiredPermissions(), listener.getCodePermissions())
        }

        mCamera.populatePreview(preview)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == listener.getCodePermissions()) {
            if (listener.allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Snackbar.make(viewFinder, "You did not grant all permissions",
                    Snackbar.LENGTH_LONG)
                    .setAction("Request", PermissionListener(this, listener))
                    .show()
            }
        }
    }

    private fun startCamera() {
        mCamera.startCamera(CameraData(listener.getLifecycleOwner(),
            capture, viewFinder, preview))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FragmentListener) {
            listener = context
        } else {
            throw IllegalCastException("Context is not of type FragmentListener")
        }
    }
}

class PermissionListener(
    var fragment: CameraFragment,
    var listener: FragmentListener
) : View.OnClickListener {
    override fun onClick(view: View?) {
        fragment.requestPermissions(
            listener.getRequiredPermissions(), listener.getCodePermissions())
    }
}