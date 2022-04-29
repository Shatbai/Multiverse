package edu.itesm.gastos.dao

import androidx.room.*
import edu.itesm.gastos.entities.Gasto
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao{
    @Query("SELECT * from Gasto")
    fun getAllGastos(): Flow<List<Gasto>>

    //"SELECT SUM(monto) from Gasto"
    @Insert
    suspend fun insertGasto(gasto: Gasto)
}
