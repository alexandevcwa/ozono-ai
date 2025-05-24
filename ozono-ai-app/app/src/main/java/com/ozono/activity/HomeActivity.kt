package com.ozono.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ozono.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.ozono.service.PhotoAnalyzerService
import com.ozono.util.KDialog
import com.ozono.util.KIcon
import com.ozono.util.KProperties
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var photoAnalyzerService: PhotoAnalyzerService

    private val REQUEST_IMAGE_CAPTURE = 1
    private val CAMERA_PERMISSION_CODE = 100

    private var photoUri: Uri? = null
    private var photoFile: File? = null // ← archivo real

    private lateinit var codeTextView: AppCompatTextView
    private lateinit var materialTextView: AppCompatTextView
    private lateinit var descriptionTextView: AppCompatTextView
    private lateinit var difficultyTextView: AppCompatTextView
    private lateinit var disintegrationTextView: AppCompatTextView
    private lateinit var contaminationTextView: AppCompatTextView
    private lateinit var fileNameTextView: AppCompatTextView
    private lateinit var uuidTextView: AppCompatTextView
    private lateinit var analyzerButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        codeTextView = findViewById(R.id.code_text_view)
        materialTextView = findViewById(R.id.material_text_view)
        descriptionTextView = findViewById(R.id.description_text_view)
        difficultyTextView = findViewById(R.id.difficulty_text_view)
        disintegrationTextView = findViewById(R.id.disintegration_text_view)
        contaminationTextView = findViewById(R.id.contamination_text_view)
        fileNameTextView = findViewById(R.id.file_name_text_view)
        uuidTextView = findViewById(R.id.uuid_text_view)
        analyzerButton = findViewById(R.id.analyzer_button)
    }

    private fun initListeners() {
        analyzerButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            } else {
                takePhoto()
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile!!)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("IMG_", ".jpg", storageDir)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            photoFile?.let { file ->
                if (file.exists()) {
                    var compressedFile = compressImage(file)
                    uploadImage(compressedFile)
                } else {
                    Toast.makeText(this, "Archivo de imagen no encontrado", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Archivo de imagen no creado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage(file: File) {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        lifecycleScope.launch {
            val result = photoAnalyzerService.analyzePhoto(body)
            if (result.isSuccess) {
                val analysis = result.getOrNull()
                // Aquí puedes mostrar los resultados si quieres
            } else {
                runOnUiThread {
                    KDialog(this@HomeActivity).inflate()
                        .show(
                            KProperties.Builder()
                                .title("Error de análisis")
                                .description(result.exceptionOrNull()?.message ?: "Error desconocido")
                                .icon(KIcon.ERROR)
                                .textButton("Ok")
                                .build()
                        )
                }
            }
        }
    }

    private fun compressImage(file: File, maxWidth: Int = 1024, maxHeight: Int = 1024, quality: Int = 80): File {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))

        // Redimensionar manteniendo proporciones
        val ratioBitmap = if (bitmap.width > bitmap.height) {
            val scale = maxWidth.toFloat() / bitmap.width
            Bitmap.createScaledBitmap(bitmap, maxWidth, (bitmap.height * scale).toInt(), true)
        } else {
            val scale = maxHeight.toFloat() / bitmap.height
            Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt(), maxHeight, true)
        }

        // Comprimir a JPEG
        val compressedFile = File.createTempFile("COMPRESSED_", ".jpg", cacheDir)
        compressedFile.outputStream().use { out ->
            ratioBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }

        return compressedFile
    }

}
