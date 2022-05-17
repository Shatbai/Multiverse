package edu.itesm.gastos.activities

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import edu.itesm.gastos.databinding.ActivityCapturaGastoBinding
import edu.itesm.gastos.entities.Gasto

class GastoCapturaDialog ( private  val onSubmitClickListener: (Gasto) -> Unit):
    DialogFragment() {
        private lateinit var binding: ActivityCapturaGastoBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ActivityCapturaGastoBinding.inflate(LayoutInflater.from(context))
        val constructor = AlertDialog.Builder(requireActivity())
        constructor.setView(binding.root)
        binding.agregaGasto.setOnClickListener {
            val nombre  = binding.conceptoGastoCifra.text.toString()
            val universo = binding.montoGastoCifra.text.toString().toInt()
            val genero=binding.generoCameoCifra.text.toString()
            val  gasto  = Gasto("",nombre, universo,genero)
            onSubmitClickListener.invoke(gasto)
            dismiss()
        }
        val  dialog = constructor.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

}



