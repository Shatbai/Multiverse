package edu.itesm.gastos.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Gasto(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val nombre: String,
    val universo: Int,
    val genero: String,
    //val foto: Bitmap
): Serializable

data class GastoFb(
    val id: String? ="",
    val nombre: String?="",
    val universo: Int?=0,
    val genero: String?="",
    //val foto: Bitmap
): Serializable