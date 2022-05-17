package edu.itesm.gastos.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.itesm.gastos.databinding.ActivityCapturaGastoBinding
import edu.itesm.gastos.entities.Gasto

class CapturaGastoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCapturaGastoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCapturaGastoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.agregaGasto.setOnClickListener {
            val nombre  = binding.conceptoGastoCifra.text.toString()
            val universo = binding.montoGastoCifra.text.toString().toInt()
            val genero =binding.generoCameoCifra.text.toString()
            val gasto  = Gasto("",nombre, universo,genero)
            val intento = Intent()
            intento.putExtra("gasto", gasto)
            setResult(RESULT_OK, intento)
            finish()
        }






    }
}