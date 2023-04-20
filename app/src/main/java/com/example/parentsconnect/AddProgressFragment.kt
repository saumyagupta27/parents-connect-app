package com.example.parentsconnect

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class AddProgressFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_progress, container, false)

        val sharedPref = requireActivity().getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", 0)

        // all initializations
        val db = DatabaseHelper(requireContext())
        val classSpinner = view.findViewById<Spinner>(R.id.class_spinner)
        val postTextView = view.findViewById<EditText>(R.id.postEditTextMultiLine)
        val backBtn = view.findViewById<ImageView>(R.id.backBtnPP)

        // retrieve the classes that are taught by the logged in faculty
        val dropDownList2 : ArrayList<String> = db.getTeacherClasses(userId)

        // set the list of classes to the spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dropDownList2)
        adapter.setDropDownViewResource(R.layout.class_spinner_item)
        classSpinner.adapter = adapter

        // put a limit of 500 character on the post edit text
        val maxLength = 500
        val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        postTextView.filters = filters
        postTextView.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

        var selectedSpinnerItem: String = ""
        classSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedSpinnerItem = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // set a listener to the 'Post' button
        val postBtn = view.findViewById<Button>(R.id.add_progress_post_btn)
        postBtn.setOnClickListener {
            val selectedItem = classSpinner.selectedItem as String
            val postMsg = postTextView.text.toString()
            val result = db.addProgressPost(userId, selectedItem, postMsg)
            if(result) {
                Toast.makeText(requireContext(), "POSTED", Toast.LENGTH_SHORT).show()
                postTextView.setText("")
            } else {
                Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
            }
        }

        backBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        return view
    }

}