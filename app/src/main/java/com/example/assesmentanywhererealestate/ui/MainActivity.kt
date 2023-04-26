package com.example.assesmentanywhererealestate.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.assesmentanywhererealestate.R
import com.example.assesmentanywhererealestate.databinding.ActivityMainBinding
import com.example.assesmentanywhererealestate.ui.viewmodel.CharacterUiState
import com.example.assesmentanywhererealestate.ui.viewmodel.CharactersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var charactersAdapter: CharactersAdapter
    val characterViewModel: CharactersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //filter
                characterViewModel.doSearch(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        charactersAdapter = CharactersAdapter() { position -> // on item clicked
            characterViewModel.getDetails(position)
            binding.slidingPaneLayout.openPane()
        }
        binding.recyclerView.apply {
            adapter = charactersAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
        onBackPressedDispatcher.addCallback(this, CharacterDetailsOnBackPressedCallback(binding.slidingPaneLayout, binding.searchEditText))
        characterViewModel.getDetails(characterViewModel.curPosition)
        characterViewModel.charactersLiveData.observe(this) {
            when(it) {
                is CharacterUiState.Loading -> {
                    //show progress spinner
                }
                is CharacterUiState.Success -> {
                    charactersAdapter.data = it.relatedTopics
                    charactersAdapter.notifyDataSetChanged()
                }
                is CharacterUiState.Error -> {
                    charactersAdapter.data = emptyList()
                }
            }
        }
        characterViewModel.filteredCharacterLiveData.observe(this) {
            when(it) {
                is CharacterUiState.Success -> {
                    charactersAdapter.data = it.relatedTopics
                    charactersAdapter.notifyDataSetChanged()
                }
                is CharacterUiState.Error -> {
                    //handle error state
                }
                CharacterUiState.Loading -> {
                    //show loading progress
                }
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    //submitSearchIntent(createInt)

                    return true
                }
            })
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            //setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }

        return true
    }

    private fun submitSearchIntent(query: String?) {
        val searchIntent = Intent(this, this::class.java).apply {
            putExtra(SearchManager.QUERY, query)
        }
        if (searchIntent.resolveActivity(packageManager) != null) {
            startActivity(searchIntent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}

class CharacterDetailsOnBackPressedCallback(private val slidingPaneLayout: SlidingPaneLayout, private val searchEditText: EditText): //https://www.youtube.com/watch?v=2rtLdF9UFqg
    OnBackPressedCallback(true),
    SlidingPaneLayout.PanelSlideListener {
    init {
        slidingPaneLayout.addPanelSlideListener(this)
    }
    override fun handleOnBackPressed() {
        slidingPaneLayout.closePane()
        searchEditText.setText("")
        searchEditText.clearFocus()
    }

    override fun onPanelSlide(panel: View, slideOffset: Float) {

    }

    override fun onPanelOpened(panel: View) {
        isEnabled = true
    }

    override fun onPanelClosed(panel: View) {
        isEnabled = false
    }


}