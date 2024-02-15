package com.example.writecode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.example.writecode.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var checkboxes: List<CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonCalculate.setOnClickListener(this)
        binding.buttonCloseOrder.setOnClickListener(this)
        val buttonCloseOrder = binding.buttonCloseOrder
        buttonCloseOrder.isEnabled = false
        buttonCloseOrder.text = getString(R.string.button_close_order_desabilited)

        checkboxes = listOf(
            binding.checkboxEntries1,
            binding.checkboxEntries2,
            binding.checkboxEntries3,
            binding.checkboxPlate1,
            binding.checkboxPlate2,
            binding.checkboxPlate3,
            binding.checkboxPlate4,
            binding.checkboxDesserts1,
            binding.checkboxDesserts2,
            binding.checkboxDesserts3,
            binding.checkboxDrink1,
            binding.checkboxDrink2,
            binding.checkboxDrink3,
        )

        setupCheckboxes(buttonCloseOrder, checkboxes)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.button_calculate) {
            calculate(checkboxes)
        }
        if (view.id == R.id.button_close_order) {
            closeOrder(checkboxes)
        }
    }

    private fun setupCheckboxes(buttonCloseOrder: Button, checkbox: List<CheckBox>) {
        checkbox.forEach { check ->
            check.setOnCheckedChangeListener { _, _ ->
                buttonCloseOrder.isEnabled = !checkboxes.any { it.isChecked }
                buttonCloseOrder.text = getString(R.string.button_close_order_desabilited)
                buttonCloseOrder.isEnabled = false
            }
        }
    }

    private fun validate(checkbox: List<CheckBox>): Boolean {
        return !checkbox.any { it.isChecked }
    }

    private fun calculate(checkbox: List<CheckBox>) {
        lateinit var text: String
        var price = 0.0f
        var timing = 0
        lateinit var timingText: String
        val error: String = getString(R.string.message_error_no_products)


        if (validate(checkbox)) {
            binding.textTotalTimeWaiting.text = ""
            binding.textTotalPrice.text = getString(R.string.text_total_price)
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        } else {

            val prices = listOf(
                binding.textPriceEntries1.text.toString(),
                binding.textPriceEntries2.text.toString(),
                binding.textPriceEntries3.text.toString(),
                binding.textPricePlate1.text.toString(),
                binding.textPricePlate2.text.toString(),
                binding.textPricePlate3.text.toString(),
                binding.textPricePlate4.text.toString(),
                binding.textPriceDessert1.text.toString(),
                binding.textPriceDessert2.text.toString(),
                binding.textPriceDessert3.text.toString(),
                binding.textPriceDrink1.text.toString(),
                binding.textPriceDrink2.text.toString(),
                binding.textPriceDrink3.text.toString()
            )

            val times = listOf(
                binding.textTimeEntries1.text.toString(),
                binding.textTimeEntries2.text.toString(),
                binding.textTimeEntries3.text.toString(),
                binding.textTimePlate1.text.toString(),
                binding.textTimePlate2.text.toString(),
                binding.textTimePlate3.text.toString(),
                binding.textTimePlate4.text.toString()
            )

            checkbox.forEachIndexed { index, check ->
                if (check.isChecked) {
                    price += prices[index].replace("R$", "").toFloatOrNull() ?: 0.00f
                    if (index <= 6) {
                        val time = times[index].split(" ")
                        timing += time[time.size - 2].toIntOrNull() ?: 0
                    }
                }
            }
            text = "R$ ${"%.2f".format(price)}"

            if (timing == 0) {
                timingText = getString(R.string.time_zero)
            } else {
                timingText = getString(R.string.estimated_time, timing)
            }

            binding.textTotalTimeWaiting.text = timingText
            binding.textTotalPrice.text = text
            val buttonCloseOrder = binding.buttonCloseOrder
            buttonCloseOrder.isEnabled = true
            buttonCloseOrder.text = getString(R.string.button_close_order)
        }
    }

    private fun closeOrder(checkbox: List<CheckBox>) {
        val success = getString(R.string.message_success)
        binding.textTotalPrice.text = getString(R.string.text_total_price)
        binding.textTotalTimeWaiting.text = ""
        binding.editTextObservations.setText("")

        checkbox.forEach { check ->
            check.isChecked = false
        }
        binding.buttonCloseOrder.isEnabled = false
        Toast.makeText(
            this,
            success,
            Toast.LENGTH_LONG
        ).show()
    }
}
