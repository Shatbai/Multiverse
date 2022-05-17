package edu.itesm.gastos.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import edu.itesm.gastos.databinding.ActivityCapturaGastoBinding
import edu.itesm.gastos.entities.Gasto

class CapturaGastoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCapturaGastoBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var currentImagePath: String? = null
    private lateinit var fotov: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCapturaGastoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                fotov = resultado.data?.extras?.get("data") as Bitmap
                //fotom.setImageBitmap(fotov)
            }

            binding.agregaFoto.setOnClickListener {
                val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultLauncher.launch(camaraIntent)
            }
            binding.agregaGasto.setOnClickListener {
                val nombre = binding.conceptoGastoCifra.text.toString()
                val universo = binding.montoGastoCifra.text.toString().toInt()
                val genero = binding.generoCameoCifra.text.toString()
                val gasto = Gasto("", nombre, universo, genero)
                val intento = Intent()
                intento.putExtra("gasto", gasto)
                setResult(RESULT_OK, intento)
                finish()
            }
        }
    }
}