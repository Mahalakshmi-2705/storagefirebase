package com.example.image

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class VedioUploadFragment : Fragment() {

    private lateinit var videoView: VideoView
    private lateinit var selectVideoButton: Button
    private lateinit var uploadVideoButton: Button
    private lateinit var storageReference: StorageReference
    private var videoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vedio_upload2, container, false)

        videoView = view.findViewById(R.id.videoView)
        selectVideoButton = view.findViewById(R.id.selectVideoButton)
        uploadVideoButton = view.findViewById(R.id.uploadVideoButton)
        storageReference = FirebaseStorage.getInstance().reference.child("videos")

        selectVideoButton.setOnClickListener {
            openGallery()
        }

        uploadVideoButton.setOnClickListener {
            uploadVideo()
        }

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RC_VIDEO_PICK)
    }

    private fun uploadVideo() {
        if (videoUri != null) {
            val videoRef = storageReference.child(videoUri!!.lastPathSegment!!)
            videoRef.putFile(videoUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // Video uploaded successfully
                    Toast.makeText(
                        requireContext(),
                        "Video uploaded successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    // Handle the error if the upload fails
                    Toast.makeText(
                        requireContext(),
                        "Video upload failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(requireContext(), "Please select a video first", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_VIDEO_PICK && resultCode == Activity.RESULT_OK && data != null) {
            videoUri = data.data
            videoView.setVideoURI(videoUri)
            videoView.start()
            uploadVideoButton.isEnabled = true
        }
    }

    companion object {
        private const val RC_VIDEO_PICK = 1
    }
}
