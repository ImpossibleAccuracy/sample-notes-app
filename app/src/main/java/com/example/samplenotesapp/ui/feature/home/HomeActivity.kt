package com.example.samplenotesapp.ui.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.samplenotesapp.R
import com.example.samplenotesapp.databinding.ActivityMainBinding
import com.example.samplenotesapp.domain.model.SortType
import com.example.samplenotesapp.ui.feature.create.AddNoteActivity
import com.example.samplenotesapp.ui.feature.home.adapter.NotesAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<HomeViewModel>()

    private val notesAdapter by lazy {
        NotesAdapter(this) {
            Snackbar.make(binding.root, "Item ${it.title} clicked", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupNotesList()
        setupActionButton()

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.loader.isVisible = it.isLoading

                val isNotesEmpty = it.notes.isEmpty()
                binding.rvNotes.isVisible = !isNotesEmpty && !it.isLoading
                binding.tvNoNotes.isVisible = isNotesEmpty && !it.isLoading

                notesAdapter.items = it.notes
            }
            .launchIn(lifecycleScope)

        viewModel.state
            .flowWithLifecycle(lifecycle)
            .map { it.isLoading }
            .distinctUntilChanged()
    }

    private fun setupActionButton() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
    }

    private fun setupNotesList() {
        binding.rvNotes.adapter = notesAdapter
        binding.rvNotes.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.reload -> {
                viewModel.loadNotes()
                true
            }

            R.id.sort -> {
                openSortNotesDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    private fun openSortNotesDialog() {
        val sortVariants = SortType.entries
        val selectedSortType = viewModel.state.value.sortType

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.select_sort_type))
            .setSingleChoiceItems(
                /* items = */ sortVariants
                    .map { getString(it.title) }
                    .toTypedArray(),
                /* checkedItem = */ sortVariants.indexOf(selectedSortType),
                /* listener = */ null
            )
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()

                val resultIndex = (dialog as AlertDialog).listView.checkedItemPosition
                val result = sortVariants[resultIndex]
                viewModel.setSortType(result)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}