package com.opeteer.watervolumeconv

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MainActivity : Activity() {

    private val volumeUnits = listOf(
        Pair("Liter (L)", 1.0),
        Pair("Mililiter (mL)", 0.001),
        Pair("Meter Kubik (m³)", 1000.0),
        Pair("Sentimeter Kubik (cm³ / cc)", 0.001),
        Pair("Galon AS (gal)", 3.785411784)
    )

    private val massUnits = listOf(
        Pair("Kilogram (kg)", 1.0),
        Pair("Gram (g)", 0.001),
        Pair("Miligram (mg)", 0.000001),
        Pair("Ton Metrik (t)", 1000.0),
        Pair("Pound / Pon (lbs)", 0.45359237),
        Pair("Ounce / Ons (oz)", 0.028349523125)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etVolume = findViewById<EditText>(R.id.etVolume)
        val spVolumeUnit = findViewById<Spinner>(R.id.spVolumeUnit)
        val spMassUnit = findViewById<Spinner>(R.id.spMassUnit)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvFormula = findViewById<TextView>(R.id.tvFormula)

        val volumeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            volumeUnits.map { it.first }
        )
        spVolumeUnit.adapter = volumeAdapter

        val massAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            massUnits.map { it.first }
        )
        spMassUnit.adapter = massAdapter

        fun calculateMass() {
            val inputStr = etVolume.text.toString().trim().replace(',', '.')
            if (inputStr.isEmpty()) {
                Toast.makeText(this, "Masukkan nilai volume terlebih dahulu", Toast.LENGTH_SHORT).show()
                return
            }

            val inputVolume = inputStr.toDoubleOrNull()
            if (inputVolume == null || inputVolume < 0) {
                Toast.makeText(this, "Nilai volume tidak valid", Toast.LENGTH_SHORT).show()
                return
            }

            val selectedVol = volumeUnits[spVolumeUnit.selectedItemPosition]
            val selectedMass = massUnits[spMassUnit.selectedItemPosition]

            // Perhitungan:
            val volumeInLiters = inputVolume * selectedVol.second
            val massInKg = volumeInLiters * 1.0
            val resultMass = massInKg / selectedMass.second

            val formattedResult = formatNumber(resultMass)
            val formattedInput = formatNumber(inputVolume)

            tvResult.text = "$formattedResult ${selectedMass.first.substringAfter("(").substringBefore(")")}"
            tvFormula.text = "Perhitungan: $formattedInput ${selectedVol.first} = $formattedResult ${selectedMass.first}"
        }

        btnCalculate.setOnClickListener {
            calculateMass()
        }

        calculateMass()
    }

    private fun formatNumber(value: Double): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        val df = if (value % 1.0 == 0.0) {
            DecimalFormat("#,##0", symbols)
        } else {
            DecimalFormat("#,##0.######", symbols)
        }
        return df.format(value)
    }
}