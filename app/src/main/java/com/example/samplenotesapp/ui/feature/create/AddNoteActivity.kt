package com.example.samplenotesapp.ui.feature.create

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.samplenotesapp.R
import com.example.samplenotesapp.databinding.ActivityAddNoteBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding

    private val viewModel by viewModels<AddNoteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupUi()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach {
                if (binding.etTitle.text?.toString() != it.titleValue) {
                    binding.etTitle.setText(it.titleValue)
                }
                if (binding.etDescription.text?.toString() != it.descriptionValue) {
                    binding.etDescription.setText(it.descriptionValue)
                }

                binding.tlTitle.error = if (it.isTitleInvalid) " " else null
                binding.tlDescription.error = if (it.isDescriptionInvalid) " " else null
            }
            .launchIn(lifecycleScope)

        viewModel.actions
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    AddNoteUiAction.NavigateSuccess -> finish()

                    is AddNoteUiAction.ShowError -> {
                        Snackbar.make(binding.root, getString(it.message), Snackbar.LENGTH_LONG)
                            .setAnchorView(R.id.fab).show()
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setupUi() {
        binding.etTitle.addTextChangedListener { editable ->
            editable?.toString()?.let {
                viewModel.updateTitle(it)
            }
        }

        binding.etDescription.addTextChangedListener { editable ->
            editable?.toString()?.let {
                viewModel.updateDescription(it)
            }
        }

        binding.fab.setOnClickListener {
            viewModel.saveNote()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
}
