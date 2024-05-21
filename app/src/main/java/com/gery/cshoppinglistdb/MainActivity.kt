package com.gery.cshoppinglistdb

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gery.cshoppinglistdb.adapter.ShoppingListAdapter
import com.gery.cshoppinglistdb.data.ShoppingItem
import com.gery.cshoppinglistdb.data.ShoppingItemDatabase
import com.gery.cshoppinglistdb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ShoppingItemDialog.ShoppingItemDialogHandler {
    private lateinit var binding: ActivityMainBinding

    private lateinit var shoppingListAdapter: ShoppingListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.tbMain)

        shoppingListAdapter = ShoppingListAdapter(this)
        binding.rvMain.adapter = shoppingListAdapter

        binding.fabAddItem.setOnClickListener {
            ShoppingItemDialog(null, -1).show(supportFragmentManager, "ShoppingItemDialog")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete_all) {
            shoppingListAdapter.deleteAll()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun shoppingItemCreated(item: ShoppingItem) {
        val dbThread = Thread {
            ShoppingItemDatabase.getInstance(this).shoppingItemDao().insertItem(item)
            runOnUiThread {
                shoppingListAdapter.addItem(item)
            }
        }
        dbThread.start()
    }

    override fun shoppingItemModified(item: ShoppingItem, position: Int) {
        val dbThread = Thread {
            ShoppingItemDatabase.getInstance(this).shoppingItemDao().updateItem(item)
            runOnUiThread {
                shoppingListAdapter.editItem(item, position)
            }
        }
        dbThread.start()
    }
}