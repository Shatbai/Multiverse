package edu.itesm.gastos.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import edu.itesm.gastos.R
import edu.itesm.gastos.dao.GastoDao
import edu.itesm.gastos.database.GastosDB
import edu.itesm.gastos.databinding.ActivityMainBinding
import edu.itesm.gastos.entities.Gasto
import edu.itesm.gastos.entities.GastoFb
import edu.itesm.gastos.mvvm.MainActivityViewModel
import edu.itesm.gastos.mvvm.MainActivityViewModelFactory
import edu.itesm.perros.adapter.GastosAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var gastoDao: GastoDao
    private lateinit var  gastos: List<Gasto>
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GastosAdapter
    private lateinit var viewModel : MainActivityViewModel
    private val databaseReference = Firebase.database.getReference("gastos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(this@MainActivity, GastosDB::class.java, "gastos").build()
        gastoDao = db.gastoDao()

        initRecycler()
        initViewModel()
        fabAgregaDatos()
    }

    private fun initRecycler(){
        gastos = mutableListOf<Gasto>()
        adapter = GastosAdapter(gastos)
        binding.gastos.layoutManager = LinearLayoutManager(this)
        binding.gastos.adapter = adapter

        binding.gastos.setListener(object : SwipeLeftRightCallback.Listener{
            override fun onSwipedLeft(position: Int) {
                removeGasto(position)

            }

            override fun onSwipedRight(position: Int) {
                binding.gastos.adapter?.notifyDataSetChanged()

            }
        })
    }
    private fun removeGasto(position:Int){
        val gasto=adapter.getGasto(position)
        databaseReference.database.getReference("gastos").child(gasto.id.toString()).removeValue().addOnSuccessListener {
            Toast.makeText(baseContext,"Borrado de la DB",Toast.LENGTH_LONG).show()
            adapter.notifyDataSetChanged()
        }.addOnFailureListener{
            Toast.makeText(baseContext,"Falla DB",Toast.LENGTH_LONG).show()
        }
    private fun initViewModel(){
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var lista = mutableListOf<Gasto>()
                for (gastoObject in snapshot.children){
                    val objeto = gastoObject.getValue(GastoFb::class.java)
                    lista.add(Gasto(objeto!!.id.toString(),
                        objeto!!.description!!, objeto.monto!!))
                }
                adapter.setGastos(lista)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        /*
        val mainActivityViewModelFactory = MainActivityViewModelFactory(gastoDao)
        viewModel = ViewModelProvider(this,
            mainActivityViewModelFactory).get(MainActivityViewModel::class.java)
        lifecycle.coroutineScope.launch {
            viewModel.getGastosFlujo().collect(){ gastos->
                adapter.setGastos(gastos)
                adapter.notifyDataSetChanged()
            }
        }
         */
       /* viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getLiveDataObserver().observe(this, Observer {
            if(!it.isEmpty()){
                adapter.setGastos(it)
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.getGastos(gastoDao)*/
    }

    private val agregaDatosLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ resultado->
            if(resultado.resultCode == RESULT_OK){
                val gasto : Gasto= resultado.data?.getSerializableExtra("gasto") as Gasto
                Toast.makeText(baseContext, gasto.description, Toast.LENGTH_LONG ).show()
            }

    }
    private fun fabAgregaDatos(){
        binding.fab.setOnClickListener {
                /*val intento = Intent(baseContext,
                    CapturaGastoActivity::class.java)
                agregaDatosLauncher.launch(intento)
                 */
            GastoCapturaDialog(onSubmitClickListener = { gasto->
                Toast.makeText(baseContext, gasto.description, Toast.LENGTH_LONG).show()
                //gastoDao.insertGasto(gasto)
                /*CoroutineScope(Dispatchers.IO).launch {
                    gastoDao.insertGasto(gasto)
                }
                 */
                val id = databaseReference.push().key!!
                val gastoFb = GastoFb(id,gasto.description, gasto.monto)
                databaseReference.child(id).setValue(gastoFb)
                    .addOnSuccessListener {
                    Toast.makeText(baseContext, "Agregado", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                        Toast.makeText(baseContext, "Error", Toast.LENGTH_LONG).show()
                }
            }).show(supportFragmentManager, "")
        }

    }




}