package com.adrenaline.ofathlet

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.adrenaline.ofathlet.data.DataManager
import com.adrenaline.ofathlet.presentation.GameViewModel
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Obfuscate
class BestActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_best)
        loadData()
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment_container).navigateUp()
    }

    private fun loadData() {
        lifecycleScope.launch {
            launch(Dispatchers.Main) {
                val balance =
                    async(Dispatchers.IO) {
                        DataManager.loadBalance(
                            applicationContext,
                            viewModel.balance.value
                        )
                    }
                viewModel.setBalance(balance.await(), applicationContext)

                val win =
                    async(Dispatchers.IO) {
                        DataManager.loadWin(
                            applicationContext,
                            viewModel.win.value
                        )
                    }
                viewModel.setWin(win.await(), applicationContext)

                val bet =
                    async(Dispatchers.IO) {
                        DataManager.loadBet(
                            applicationContext,
                            viewModel.bet.value
                        )
                    }
                viewModel.setBet(bet.await(), applicationContext)

                val login =
                    async(Dispatchers.IO) {
                        DataManager.loadLogin(applicationContext)
                    }
                viewModel.login = login.await()
                if (login.await().isNotEmpty()) viewModel.isUserAnonymous = false
            }
        }
    }
}