package edu.itesm.gastos.database

import android.app.Application
import androidx.room.Room

class GastoApp : Application(){
    val roomDatabse = Room.databaseBuilder(this, GastosDB::class.java, "gastos").build()
}