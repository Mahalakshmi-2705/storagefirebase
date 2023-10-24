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
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ImageUploadFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var uploadImageButton: Button
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_upload2, container, false)

        imageView = view.findViewById(R.id.imageView)
        selectImageButton = view.findViewById(R.id.selectImageButton)
        uploadImageButton = view.findViewById(R.id.uploadImageButton)
        storageReference = FirebaseStorage.getInstance().reference.child("images")

        selectImageButton.setOnClickListener {
            openGallery()
        }

        uploadImageButton.setOnClickListener {
            uploadImage()
        }

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RC_IMAGE_PICK)
    }

    private fun uploadImage() {
        if (imageUri != null) {
            val imageRef = storageReference.child(imageUri!!.lastPathSegment!!)
            imageRef.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // Image uploaded successfully
                    Toast.makeText(
                        requireContext(),
                        "Image uploaded successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    // You can get the download URL if needed
                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val imageUrl = downloadUrl.toString()
                        // Now you can use imageUrl as needed (e.g., save it to a database).
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the error if the upload fails
                    Toast.makeText(
                        requireContext(),
                        "Image upload failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri)
            uploadImageButton.isEnabled = true
        }
    }

    companion object {
        private const val RC_IMAGE_PICK = 1
    }
}
