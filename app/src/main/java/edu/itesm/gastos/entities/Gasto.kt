package edu.itesm.gastos.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Gasto(
    @PrimaryKey(autoGenerate =false)
    val id: String,
    val description: String, val monto: Double
): Serializable

data class GastoFb(
    val id: String? ="",
    val description: String?="",
    val monto: Double?=0.0
): Serializable