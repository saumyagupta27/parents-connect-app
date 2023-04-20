package com.example.parentsconnect

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class AddFeeDetailsActivity : AppCompatActivity() {


    private var totalSum: Int = 0

    private lateinit var tuitionEditText: EditText
    private lateinit var developmentEditText: EditText
    private lateinit var miscEditText: EditText
    private lateinit var annualEditText: EditText
    private lateinit var insuranceEditText: EditText
    private lateinit var totalTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fee_details)

        val db = DatabaseHelper(this)

        tuitionEditText = findViewById(R.id.tuitionFeeEditText)
        developmentEditText = findViewById(R.id.developmentEditText)
        miscEditText = findViewById(R.id.miscellaneousEditText)
        annualEditText = findViewById(R.id.annualEditText)
        insuranceEditText = findViewById(R.id.insuranceEditText)
        totalTextView = findViewById(R.id.totalSumTextView)
        val submitBtn = findViewById<Button>(R.id.feeDetailsSubmitBtn)
        val backBtn = findViewById<ImageView>(R.id.backBtnFee)
        val spinner = findViewById<Spinner>(R.id.allClassesSpinner)
        val dropdownList = ArrayList<Int>()
        for(i in 1..12) {
            dropdownList.add(i)
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dropdownList)
        adapter.setDropDownViewResource(R.layout.class_spinner_item)
        spinner.adapter = adapter

        var selectedSpinnerItem: Int = 0
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedSpinnerItem = parent.getItemAtPosition(position) as Int
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        tuitionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(isValidFloat(s.toString())) {
                    tuitionEditText.setTextColor(resources.getColor(R.color.black))
                    if(allFieldsFilled() && allFieldsFloat()) {
                        updateTotalTextView()
                    }
                    else {
                        setTotalTextViewError()
                    }
                } else {
                    tuitionEditText.setTextColor(resources.getColor(R.color.red))
                    setTotalTextViewError()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        developmentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (isValidFloat(s.toString())) {
                    developmentEditText.setTextColor(resources.getColor(R.color.black))
                    if (allFieldsFilled() && allFieldsFloat()) {
                        updateTotalTextView()
                    } else {
                        setTotalTextViewError()
                    }
                } else {
                    developmentEditText.setTextColor(resources.getColor(R.color.red))
                    setTotalTextViewError()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        miscEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (isValidFloat(s.toString())) {
                    miscEditText.setTextColor(resources.getColor(R.color.black))
                    if (allFieldsFilled() && allFieldsFloat()) {
                        updateTotalTextView()
                    } else {
                        setTotalTextViewError()
                    }
                } else {
                    miscEditText.setTextColor(resources.getColor(R.color.red))
                    setTotalTextViewError()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        annualEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (isValidFloat(s.toString())) {
                    annualEditText.setTextColor(resources.getColor(R.color.black))
                    if (allFieldsFilled() && allFieldsFloat()) {
                        updateTotalTextView()
                    } else {
                        setTotalTextViewError()
                    }
                } else {
                    annualEditText.setTextColor(resources.getColor(R.color.red))
                    setTotalTextViewError()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        insuranceEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (isValidFloat(s.toString())) {
                    insuranceEditText.setTextColor(resources.getColor(R.color.black))
                    if (allFieldsFilled() && allFieldsFloat()) {
                        updateTotalTextView()
                    } else {
                        setTotalTextViewError()
                    }
                } else {
                    insuranceEditText.setTextColor(resources.getColor(R.color.red))
                    setTotalTextViewError()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        submitBtn.setOnClickListener {
            val tuitionText = tuitionEditText.text.toString()
            val developmentText = developmentEditText.text.toString()
            val miscellaneousText = miscEditText.text.toString()
            val annualChargesText = annualEditText.text.toString()
            val insuranceText = insuranceEditText.text.toString()
            if(!allFieldsFilled()) {
                Toast.makeText(this, "No field should be empty", Toast.LENGTH_SHORT).show()
            } else {
                if (allFieldsFloat()) {
                    val tuition = tuitionText.toFloat()
                    val developmentCharges = developmentText.toFloat()
                    val miscellaneous = miscellaneousText.toFloat()
                    val annualCharges = annualChargesText.toFloat()
                    val insurance = insuranceText.toFloat()
                    val targetClass = spinner.selectedItem as Int
                    if(db.checkClassInFee(targetClass)) {
                        Toast.makeText(this, "Fee details for this class already exist", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val result = db.addFeeDetails(targetClass, tuition, developmentCharges, miscellaneous, annualCharges, insurance)
                        if(result) {
                            Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show()
                            tuitionEditText.setText("")
                            developmentEditText.setText("")
                            miscEditText.setText("")
                            annualEditText.setText("")
                            insuranceEditText.setText("")
                        } else {
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "All values should be numerical", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun isValidFloat(value: String) : Boolean {
        return try {
            value.toFloat()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun allFieldsFloat() : Boolean {
        if(isValidFloat(tuitionEditText.text.toString())
            && isValidFloat(developmentEditText.text.toString())
            && isValidFloat(miscEditText.text.toString())
            && isValidFloat(annualEditText.text.toString())
            && isValidFloat(insuranceEditText.text.toString())) {
            return true
        }
        return false
    }

    private fun updateTotalTextView() {
        val tuition = tuitionEditText.text.toString().toFloat()
        val developmentCharges = developmentEditText.text.toString().toFloat()
        val miscellaneous = miscEditText.text.toString().toFloat()
        val annualCharges = annualEditText.text.toString().toFloat()
        val insurance = insuranceEditText.text.toString().toFloat()
        val total: Float = 12*(tuition) + 12*developmentCharges + 12*miscellaneous + annualCharges + insurance
        totalTextView.text = "Total: $total"
    }

    private fun allFieldsFilled() : Boolean {
        if(tuitionEditText.text.toString() == ""
            || developmentEditText.text.toString() == ""
            || miscEditText.text.toString() == ""
            || annualEditText.text.toString() == ""
            || insuranceEditText.text.toString() == "") {
            return false
        }
        return true
    }

    private fun setTotalTextViewError() {
        totalTextView.text = "Total: --"
    }
}