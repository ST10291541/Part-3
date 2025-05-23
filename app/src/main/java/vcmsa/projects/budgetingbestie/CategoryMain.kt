package vcmsa.projects.budgetingbestie

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import vcmsa.projects.budgetingbestie.databinding.ActivityCategoryMainBinding


class CategoryMain : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryMainBinding
    private lateinit var allCategoriesAdapter: CategoryAdapter
    private val categoryRepository = CategoryRepository()
    private var currentUserId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // You need to implement your method of getting signed-in user ID string here:
        currentUserId = getCurrentUserId()
        if (currentUserId.isEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupUI()
        loadCategories()
    }

    private fun setupUI() {
        allCategoriesAdapter = CategoryAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = allCategoriesAdapter

        binding.btnCreate.setOnClickListener {
            startActivity(Intent(this, AddCategory::class.java))
        }

        binding.btnFilter.setOnClickListener {
            startActivity(Intent(this, CategoryTotals::class.java))
        }

        findViewById<FloatingActionButton>(R.id.fabDashboard).setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
        }

        /*findViewById<FloatingActionButton>(R.id.fabBudget).setOnClickListener {
            startActivity(Intent(this, BudgetMain::class.java))
        }*/

        findViewById<FloatingActionButton>(R.id.fabExpenses).setOnClickListener {
            startActivity(Intent(this, ExpensesMain::class.java))
        }
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            try {
                val categories = categoryRepository.getCategoriesForUser(currentUserId)
                allCategoriesAdapter.updateList(categories)
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private fun getCurrentUserId(): String {
        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        return user?.uid ?: ""
    }

}


