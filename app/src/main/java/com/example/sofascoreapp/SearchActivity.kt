package com.example.sofascoreapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.sofascoreapp.data.model.PlayerAutocomplete
import com.example.sofascoreapp.data.model.TeamAutocomplete
import com.example.sofascoreapp.databinding.ActivitySearchBinding
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.viewmodel.SearchActivityViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var sharedViewModel: SearchActivityViewModel
    private lateinit var autocompleteAdapter: ArrayAdapter<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel = ViewModelProvider(this)[SearchActivityViewModel::class.java]

        binding.autoComplete.threshold = 2
        binding.autoComplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(text: Editable?) {
                binding.autoComplete.setOnKeyListener { view, code, keyEvent ->
                    run {
                        if (code == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN && Utilities().isNetworkAvailable(
                                applicationContext
                            ) && !binding.autoComplete.text.equals(
                                ""
                            )
                        ) {
                            dismissKeyboard(view.windowToken)
                            //startCityDetailActivity()
                        }
                    }
                    false
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! == 3 && Utilities().isNetworkAvailable(applicationContext)) {
                    sharedViewModel.getAllAutocompletes(s.toString())
                } else if (s.isEmpty()) {
                    binding.autoComplete.setAdapter(null)
                }
            }
        })

        sharedViewModel.autocompleteList.observe(this) { list ->
            val autocompleteAdapter = object : ArrayAdapter<Any>(
                this,
                R.layout.autocomplete_result_item
            ) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = convertView ?: LayoutInflater.from(context)
                        .inflate(R.layout.autocomplete_result_item, parent, false)

                    val name = when (val item = getItem(position)) {
                        is TeamAutocomplete -> item.name
                        is PlayerAutocomplete -> item.name
                        else -> ""
                    }

                    val nameTextView: TextView = view.findViewById(R.id.textView)
                    nameTextView.text = name

                    return view
                }
            }

            autocompleteAdapter.addAll(list)
            binding.autoComplete.setAdapter(autocompleteAdapter)
            binding.autoComplete.showDropDown()
        }


        binding.clearIcon.setOnClickListener {
            binding.autoComplete.clear()
        }

        binding.autoComplete.setOnItemClickListener { _, _, position, _ ->
            if (sharedViewModel.autocompleteList.value?.get(position) is TeamAutocomplete) {
                TeamDetailsActivity.start(
                    this,
                    (sharedViewModel.autocompleteList.value!![position] as TeamAutocomplete).id
                )
            } else {
                PlayerDetailsActivity.start(
                    this,
                    (sharedViewModel.autocompleteList.value!![position] as PlayerAutocomplete).id
                )
            }
        }

    }


    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }


    fun AutoCompleteTextView.clear() {
        this.text.clear()
    }

}