package com.example.parentsconnect

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.paypal.android.sdk.payments.*
import java.math.BigDecimal

class PayFeeFragment : Fragment() {

    private var totalSum: Float = 0.toFloat()
    private var currentTuition: Float = 0.toFloat()
    private var currentDevelopment: Float = 0.toFloat()
    private var currentMiscellaneous: Float = 0.toFloat()
    private lateinit var totalSumTextView: TextView
    private lateinit var tuitionTextView: TextView
    private lateinit var developmentTextView: TextView
    private lateinit var miscTextView: TextView
    private lateinit var annualTextView: TextView
    private lateinit var insuranceTextView: TextView

    private lateinit var allCheckBoxes: Array<CheckBox>
    private lateinit var months: Array<String>

    private var isPaymentSuccessful: Boolean = false

    private val clientId = "<your-client-id>"
    val PAYPAL_REQUEST_CODE = 123

    val config = PayPalConfiguration()
        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
        .clientId(clientId)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pay_fee, container, false)
        // Inflate the layout for this fragment
        val db = DatabaseHelper(requireContext())

        val sharedPref = requireActivity().getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", 0)

        val studentNameTextView = view.findViewById<TextView>(R.id.studentNameTextView)
        val studentClassTextView = view.findViewById<TextView>(R.id.studentClassTextView)
        val aprilCheckBox = view.findViewById<CheckBox>(R.id.aprilCheckbox)
        val mayCheckBox = view.findViewById<CheckBox>(R.id.mayCheckbox)
        val juneCheckBox = view.findViewById<CheckBox>(R.id.juneCheckbox)
        val julyCheckBox = view.findViewById<CheckBox>(R.id.julyCheckbox)
        val augustCheckBox = view.findViewById<CheckBox>(R.id.augustCheckbox)
        val septemberCheckBox = view.findViewById<CheckBox>(R.id.septemberCheckbox)
        val octoberCheckBox = view.findViewById<CheckBox>(R.id.octoberCheckbox)
        val novemberCheckBox = view.findViewById<CheckBox>(R.id.novemberCheckbox)
        val decemberCheckBox = view.findViewById<CheckBox>(R.id.decemberCheckbox)
        val janCheckBox = view.findViewById<CheckBox>(R.id.januaryCheckbox)
        val febCheckBox = view.findViewById<CheckBox>(R.id.februaryCheckbox)
        val marchCheckBox = view.findViewById<CheckBox>(R.id.marchCheckbox)
        val payBtn = view.findViewById<Button>(R.id.payWithPaypalBtn)
        val backBtn = view.findViewById<ImageView>(R.id.backBtnPayFee)

        allCheckBoxes = arrayOf(aprilCheckBox, mayCheckBox, juneCheckBox, julyCheckBox,
            augustCheckBox, septemberCheckBox, octoberCheckBox, novemberCheckBox, decemberCheckBox,
            janCheckBox, febCheckBox, marchCheckBox)
        months = arrayOf("APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER",
            "NOVEMBER", "DECEMBER", "JANUARY", "FEBRUARY", "MARCH")

        totalSumTextView = view.findViewById(R.id.totalTextView)
        tuitionTextView = view.findViewById(R.id.tuitionFeeTextView)
        developmentTextView = view.findViewById(R.id.developmentTextView)
        miscTextView = view.findViewById(R.id.miscTextView)
        annualTextView = view.findViewById(R.id.annualTextView)
        insuranceTextView = view.findViewById(R.id.insuranceTextView)

        val studentName = db.getStudentName(userId)
        val studentClass = db.getStudentClass(userId)
        val targetClass = studentClass.substring(0, studentClass.length - 1).toInt()


        studentNameTextView.text = "Student name: $studentName"
        studentClassTextView.text = "Student class: $studentClass"


        if(!db.checkClassInFee(targetClass)) {
            for(i in 0..11) {
                allCheckBoxes[i].isEnabled = false
                payBtn.isEnabled = false
            }
        }
        else {
            val monthsTrue = db.getMonthsTrue(userId)
            for (month in monthsTrue) {
                val resourceName = "${month.toLowerCase()}Checkbox"
                val checkBox = view.findViewById<CheckBox>(
                    resources.getIdentifier(resourceName, "id", "com.example.parentsconnect")
                )
                checkBox.isChecked = true
                checkBox.isEnabled = false
            }

            if(monthsTrue.size == 12) {
                payBtn.isEnabled = false
            }

            if(aprilCheckBox.isEnabled) {
                for (i in 1..11) {
                    allCheckBoxes[i].isEnabled = false
                }
            }
            for (i in 1..11) {
                if (allCheckBoxes[i].isEnabled && !allCheckBoxes[i - 1].isEnabled) {
                    for (j in i + 1..11) {
                        allCheckBoxes[j].isEnabled = false
                    }
                    break
                }
            }

            val cursorFeeDetails = db.getFeeDetailsForClass(targetClass)

            cursorFeeDetails.moveToNext()
            val tuition = cursorFeeDetails.getFloat(cursorFeeDetails.getColumnIndexOrThrow("TUITION"))
            val development = cursorFeeDetails.getFloat(cursorFeeDetails.getColumnIndexOrThrow("DEVELOPMENT"))
            val miscellaneous = cursorFeeDetails.getFloat(cursorFeeDetails.getColumnIndexOrThrow("MISCELLANEOUS"))
            val annual = cursorFeeDetails.getFloat(cursorFeeDetails.getColumnIndexOrThrow("ANNUAL"))
            val insurance = cursorFeeDetails.getFloat(cursorFeeDetails.getColumnIndexOrThrow("INSURANCE"))


            aprilCheckBox.setOnClickListener {
                mayCheckBox.isEnabled = aprilCheckBox.isChecked
                mayCheckBox.isChecked = false
                if (!aprilCheckBox.isChecked) {
                    for(i in 2..11) {
                        allCheckBoxes[i].isEnabled = false
                        allCheckBoxes[i].isChecked = false
                    }
                }
                calculateTotal(tuition, development, miscellaneous, annual, insurance)
            }

            mayCheckBox.setOnClickListener {
                juneCheckBox.isEnabled = mayCheckBox.isChecked
                juneCheckBox.isChecked = false
                if (!mayCheckBox.isChecked) {
                    for(i in 3..11) {
                        allCheckBoxes[i].isEnabled = false
                        allCheckBoxes[i].isChecked = false
                    }
                }
                calculateTotal(tuition, development, miscellaneous, annual, insurance)
            }

            juneCheckBox.setOnClickListener {
                julyCheckBox.isEnabled = juneCheckBox.isChecked
                julyCheckBox.isChecked = false
                if(!juneCheckBox.isChecked) {
                    for(i in 4..11) {
                        allCheckBoxes[i].isEnabled = false
                        allCheckBoxes[i].isChecked = false
                    }
                }
                calculateTotal(tuition, development, miscellaneous, annual, insurance)
            }

            julyCheckBox.setOnClickListener {
                augustCheckBox.isEnabled = julyCheckBox.isChecked
                augustCheckBox.isChecked = false
                if(!julyCheckBox.isChecked) {
                    for(i in 5..11) {
                        allCheckBoxes[i].isEnabled = false
                        allCheckBoxes[i].isChecked = false
                    }
                }
                calculateTotal(tuition, development, miscellaneous, annual, insurance)
            }

            augustCheckBox.setOnClickListener {
                septemberCheckBox.isEnabled = augustCheckBox.isChecked
                septemberCheckBox.isChecked = false
                if(!augustCheckBox.isChecked) {
                    for(i in 6..11) {
                        allCheckBoxes[i].isEnabled = false
                        allCheckBoxes[i].isChecked = false
                    }
                }
                calculateTotal(tuition, development, miscellaneous, annual, insurance)
            }

            septemberCheckBox.setOnClickListener {
                octoberCheckBox.isEnabled = septemberCheckBox.isChecked
                octoberCheckBox.isChecked = false
                if(!septemberCheckBox.isChecked) {
                    for(i in 7..11) {
                        allCheckBoxes[i].isEnabled = false
                        allCheckBoxes[i].isChecked = false
                    }
                }
                calculateTotal(tuition, development, miscellaneous, annual, insurance)
            }

            octoberCheckBox.setOnClickListener {
                novemberCheckBox.isEnabled = octoberCheckBox.isChecked
                novemberCheckBox.isChecked = false
                if(!octoberCheckBox.isChecked) {
                    for(i in 8..11) {
                        allCheckBoxes[i].isEnabled = false
                        allCheckBoxes[i].isChecked = false
                    }
                }
                calculateTotal(tuition, development, miscellaneous, annual, insurance)
            }

            novemberCheckBox.setOnClickListener {
                decemberCheckBox.isEnabled = novemberCheckBox.isChecked
                decemberCheckBox.isChecked = false
                if(!novemberCheckBox.isChecked) {
                    for(i in 9..11) {
                        allCheckBoxes[i].isEnabled = false
                        allCheckBoxes[i].isChecked = false
                    }
                }
                calculateTotal(tuition, development, miscellaneous, annual, insurance)
            }

            decemberCheckBox.setOnClickListener {
                janCheckBox.isEnabled = decemberCheckBox.isChecked
                janCheckBox.isChecked = false
                if(!decemberCheckBox.isChecked) {
                    for(i in 10..11) {
                        allCheckBoxes[i].isEnabled = false
                        allCheckBoxes[i].isChecked = false
                    }
                }
                calculateTotal(tuition, development, miscellaneous, annual, insurance)
            }

            janCheckBox.setOnClickListener {
                febCheckBox.isEnabled = janCheckBox.isChecked
                febCheckBox.isChecked = false
                if(!janCheckBox.isChecked) {
                    for(i in 11..11) {
                        allCheckBoxes[i].isEnabled = false
                        allCheckBoxes[i].isChecked = false
                    }
                }
                calculateTotal(tuition, development, miscellaneous, annual, insurance)
            }

            febCheckBox.setOnClickListener {
                marchCheckBox.isEnabled = febCheckBox.isChecked
                marchCheckBox.isChecked = false
                calculateTotal(tuition, development, miscellaneous, annual, insurance)
            }

            marchCheckBox.setOnClickListener {
                calculateTotal(tuition, development, miscellaneous, annual, insurance)
            }

            payBtn.setOnClickListener {
                val monthsSelected = ArrayList<String>()
                for(i in 0..11) {
                    if(allCheckBoxes[i].isEnabled && allCheckBoxes[i].isChecked) {
                        monthsSelected.add(months[i])
                    }
                }
                if(monthsSelected.size == 0) {
                    Toast.makeText(requireContext(), "No month selected", Toast.LENGTH_SHORT).show()
                } else {
                    getPayment()
                    if(isPaymentSuccessful) {
                        val res = db.paymentMade(monthsSelected, userId)
                        if (res) {
                            Toast.makeText(requireContext(), "Payment successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Payment unsuccessful", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        backBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        return view
    }

    private fun updateMonthlyTextViews() {
        tuitionTextView.text = "Tuition Fee: $currentTuition"
        developmentTextView.text = "Development Fund: $currentDevelopment"
        miscTextView.text = "Miscellaneous Fee: $currentMiscellaneous"
    }

    private fun updateTotalTextView() {
        totalSumTextView.text = "Total: $totalSum"
    }

    private fun calculateTotal(tuition: Float, development: Float, miscellaneous: Float
                               , annual: Float, insurance: Float) {
        currentTuition = 0.toFloat()
        currentDevelopment = 0.toFloat()
        currentMiscellaneous = 0.toFloat()
        totalSum = 0.toFloat()
        if(allCheckBoxes[0].isEnabled && allCheckBoxes[0].isChecked) {
            totalSum += annual + insurance
            annualTextView.text = "Annual Fee: $annual"
            insuranceTextView.text = "Insurance: $insurance"
        } else {
            annualTextView.text = "Annual Fee: --"
            insuranceTextView.text = "Insurance: --"
        }
        var count = 0
        for(i in 0..11) {
            if(allCheckBoxes[i].isEnabled && allCheckBoxes[i].isChecked) {
                count += 1
            }
        }
        currentTuition = tuition * count
        currentDevelopment = development * count
        currentMiscellaneous = miscellaneous * count
        totalSum += currentTuition + currentDevelopment + currentMiscellaneous
        updateMonthlyTextViews()
        updateTotalTextView()
    }

    private fun getPayment() {
        val amount = totalSum
        val payPalPayment = PayPalPayment(
            BigDecimal(amount.toString()),
            "USD",
            "Fee",
            PayPalPayment.PAYMENT_INTENT_SALE
        )
        val intent = Intent(requireContext(), PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment)
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Payment was successful
                val confirmation = data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
//                val paymentId = confirmation?.proofOfPayment?.paymentId
                Toast.makeText(requireContext(), "Payment Successful", Toast.LENGTH_SHORT).show()
                isPaymentSuccessful = true
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Payment was canceled by user
                Toast.makeText(requireContext(), "Payment Canceled", Toast.LENGTH_SHORT).show()
                isPaymentSuccessful = false
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // Payment failed due to invalid configuration
                Toast.makeText(requireContext(), "Payment Failed", Toast.LENGTH_SHORT).show()
                isPaymentSuccessful = false
            }
//            sb-uf5qd25442386@personal.example.com
//            w'|UZt1^
            //saumyagupta9650@gmail.com
            // testingpay

            //sb-f4gvw25441028@personal.example.com
            //l[4@W|bD
            // new: personalsandbox
//            4032 0348 6102 5925
        }
    }
}